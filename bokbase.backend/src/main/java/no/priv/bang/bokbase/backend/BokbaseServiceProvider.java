/*
 * Copyright 2021 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.bokbase.backend;

import static no.priv.bang.bokbase.services.BokbaseConstants.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import no.priv.bang.bokbase.services.BokbaseException;
import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;
import no.priv.bang.bokbase.services.beans.Binding;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;
import no.priv.bang.bokbase.services.beans.Bookshelf;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.bokbase.services.beans.Publisher;
import no.priv.bang.bokbase.services.beans.PublishersWithAddedPublisherId;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.User;
import no.priv.bang.osgiservice.users.UserManagementService;

@Component(service=BokbaseService.class, immediate=true, property= { "defaultlocale=nb_NO" })
public class BokbaseServiceProvider implements BokbaseService {

    private static final String DISPLAY_TEXT_RESOURCES = "i18n.Texts";
    private Logger logger;
    private DataSource datasource;
    private UserManagementService useradmin;
    private Locale defaultLocale;

    @Reference
    public void setLogservice(LogService logservice) {
        this.logger = logservice.getLogger(BokbaseServiceProvider.class);
    }

    @Reference(target = "(osgi.jndi.service.name=jdbc/bokbase)")
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        this.useradmin = useradmin;
    }

    @Activate
    public void activate(Map<String, Object> config) {
        defaultLocale = Locale.forLanguageTag(((String) config.get("defaultlocale")).replace('_', '-'));
        addRolesIfNotpresent();
    }

    @Override
    public boolean lazilyCreateAccount(String username) {
        boolean exists = false;
        try(Connection connection = datasource.getConnection()) {
            try(PreparedStatement findAccount = connection.prepareStatement("select * from bokbase_accounts where username=?")) {
                findAccount.setString(1, username);
                try(ResultSet results = findAccount.executeQuery()) {
                    while (results.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                return false;
            }

            try(PreparedStatement createAccount = connection.prepareStatement("insert into bokbase_accounts (username) values (?)")) {
                createAccount.setString(1, username);
                createAccount.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            logger.warn("Failed to create bokbase account for username \"{}\"", username, e);
        }

        return false;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet results = statement.executeQuery("select * from bokbase_accounts")) {
                    while(results.next()) {
                        accounts.add(unpackAccount(results));
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Ingen bokbase");
        }

        return accounts;
    }

    @Override
    public Locale defaultLocale() {
        return defaultLocale;
    }

    @Override
    public List<LocaleBean> availableLocales() {
        return Arrays.asList(Locale.forLanguageTag("nb-NO"), Locale.UK).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> displayTexts(Locale locale) {
        return transformResourceBundleToMap(locale);
    }

    @Override
    public String displayText(String key, String locale) {
        Locale active = locale == null || locale.isEmpty() ? defaultLocale : Locale.forLanguageTag(locale.replace('_', '-'));
        ResourceBundle bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, active);
        return bundle.getString(key);
    }

    @Override
    public List<Book> listBooks(String username) {
        List<Book> books = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            String sql = "select * from books b left join series e on b.series_id=e.series_id left join authors a on b.author_id=a.author_id left join publishers p on p.publisher_id=b.publisher_id join bookshelves s on b.book_id=s.book_id join bokbase_accounts c on s.account_id=c.account_id join book_ratings r on b.book_id=r.book_id and c.account_id=r.account_id where c.username=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try(ResultSet results = statement.executeQuery()) {
                    while(results.next()) {
                        books.add(unpackBook(results));
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException(String.format("Unable to list books for user %s", username), e);
        }

        return books;
    }

    @Override
    public BooksWithAddedBookId addBook(String username, Book book) {
        Long bookId = null;
        try(Connection connection = datasource.getConnection()) {
            String booksInsert = "insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, published_date, isbn13) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(booksInsert, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getSubtitle());
                setLongOrNull(statement, 3, book.getSeriesId());
                setDoubleOrNull(statement, 4, book.getSeriesNumber());
                setLongOrNull(statement, 5, book.getAuthorId());
                setIntOrNull(statement, 6, book.getAverageRating());
                setLongOrNull(statement, 7, book.getPublisherId());
                statement.setString(8, book.getBinding() != null ? book.getBinding().toString() : null);
                setIntOrNull(statement, 9, book.getPages());
                setDateOrNull(statement, 10, book.getPublishedDate());
                statement.setString(11, book.getIsbn13());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        bookId = keys.getLong(1);
                    }
                }
            }

            Long accountId = getAccountId(connection, username);
            String bookshelvesInsert = "insert into bookshelves (account_id, bookshelf, book_id) values (?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(bookshelvesInsert)) {
                setLongOrNull(statement, 1, accountId);
                statement.setString(2, book.getBinding() != null ? book.getBinding().toString() : null);
                setLongOrNull(statement, 3, bookId);
                statement.executeUpdate();
            }

            String ratingsInsert = "insert into book_ratings (account_id, book_id, rating, finished_read_date) values (?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(ratingsInsert)) {
                statement.setLong(1, accountId);
                statement.setLong(2, bookId);
                setIntOrNull(statement, 3, book.getRating());
                setDateOrNull(statement, 4, book.getFinishedReadDate());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException(String.format("Unable to add book for user %s", username), e);
        }

        return BooksWithAddedBookId.with()
            .books(listBooks(username))
            .addedBookId(bookId)
            .build();
    }

    @Override
    public List<Book> updateBook(String username, Book book) {
        try(Connection connection = datasource.getConnection()) {
            String booksInsert = "update books set book_title=?, book_subtitle=?, series_id=?, series_number=?, author_id=?, average_rating=?, publisher_id=?, binding=?, pages=?, published_date=?, isbn13=? where book_id=?";
            try(PreparedStatement statement = connection.prepareStatement(booksInsert)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getSubtitle());
                setLongOrNull(statement, 3, book.getSeriesId());
                setDoubleOrNull(statement, 4, book.getSeriesNumber());
                setLongOrNull(statement, 5, book.getAuthorId());
                setIntOrNull(statement, 6, book.getAverageRating());
                setLongOrNull(statement, 7, book.getPublisherId());
                statement.setString(8, book.getBinding() != null ? book.getBinding().toString() : null);
                setIntOrNull(statement, 9, book.getPages());
                setDateOrNull(statement, 10, book.getPublishedDate());
                statement.setString(11, book.getIsbn13());
                setLongOrNull(statement, 12, book.getBookId());
                statement.executeUpdate();
            }

            Long accountId = getAccountId(connection, username);
            String bookshelvesInsert = "update bookshelves set bookshelf=? where book_id=? and account_id=?";
            try(PreparedStatement statement = connection.prepareStatement(bookshelvesInsert)) {
                statement.setString(1, book.getBookshelf() != null ? book.getBookshelf().getValue() : null);
                setLongOrNull(statement, 2, book.getBookId());
                setLongOrNull(statement, 3, accountId);
                statement.executeUpdate();
            }

            String ratingsInsert = "update book_ratings set rating=?, finished_read_date=? where book_id=? and account_id=?";
            try(PreparedStatement statement = connection.prepareStatement(ratingsInsert)) {
                setIntOrNull(statement, 1, book.getRating());
                setDateOrNull(statement, 2, book.getFinishedReadDate());
                setLongOrNull(statement, 3, book.getBookId());
                setLongOrNull(statement, 4, accountId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException(String.format("Unable to modify book for user %s", username), e);
        }

        return listBooks(username);
    }

    @Override
    public List<Book> removeBook(String username, Book book) {
        try(Connection connection = datasource.getConnection()) {
            Long accountId = getAccountId(connection, username);
            String bookshelvesInsert = "delete from bookshelves where book_id=? and account_id=?";
            try(PreparedStatement statement = connection.prepareStatement(bookshelvesInsert)) {
                statement.setLong(1, book.getBookId());
                statement.setLong(2, accountId);
                statement.executeUpdate();
            }

            String ratingsInsert = "delete from book_ratings where book_id=? and account_id=?";
            try(PreparedStatement statement = connection.prepareStatement(ratingsInsert)) {
                statement.setLong(1, book.getBookId());
                statement.setLong(2, accountId);
                statement.executeUpdate();
            }

            String booksInsert = "delete from books where book_id=?";
            try(PreparedStatement statement = connection.prepareStatement(booksInsert)) {
                statement.setLong(1, book.getBookId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException(String.format("Unable to remove book for user %s", username), e);
        }

        return listBooks(username);
    }

    @Override
    public List<Author> listAuthors() {
        List<Author> authors = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            String sql = "select * from authors";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet results = statement.executeQuery()) {
                    while(results.next()) {
                        authors.add(unpackAuthor(results));
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to list authors", e);
        }

        return authors;
    }

    @Override
    public AuthorsWithAddedAuthorId addAuthor(Author author) {
        Long authorId = null;
        try(Connection connection = datasource.getConnection()) {
            String sql = "insert into authors (author_firstname, author_lastname) values (?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, author.getFirstname());
                statement.setString(2, author.getLastname());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        authorId = keys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to add author", e);
        }

        return AuthorsWithAddedAuthorId.with()
            .authors(listAuthors())
            .addedAuthorId(authorId)
            .build();
    }

    @Override
    public List<Author> updateAuthor(Author author) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "update authors set author_firstname=?, author_lastname=? where author_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, author.getFirstname());
                statement.setString(2, author.getLastname());
                statement.setLong(3, author.getAuthorId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to update author", e);
        }

        return listAuthors();
    }

    @Override
    public List<Author> removeAuthor(Author author) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "delete from authors where author_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, author.getAuthorId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to remove author", e);
        }

        return listAuthors();
    }

    @Override
    public List<Publisher> listPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            String sql = "select * from publishers";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet results = statement.executeQuery()) {
                    while(results.next()) {
                        publishers.add(unpackPublisher(results));
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to list publishers", e);
        }

        return publishers;
    }

    @Override
    public PublishersWithAddedPublisherId addPublisher(Publisher publisher) {
        Long publisherId = null;
        try(Connection connection = datasource.getConnection()) {
            String sql = "insert into publishers (publisher_name) values (?)";
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, publisher.getName());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        publisherId = keys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to add publisher", e);
        }

        return PublishersWithAddedPublisherId.with()
            .publishers(listPublishers())
            .addedPublisherId(publisherId)
            .build();
    }

    @Override
    public List<Publisher> updatePublisher(Publisher publisher) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "update publishers set publisher_name=? where publisher_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, publisher.getName());
                statement.setLong(2, publisher.getPublisherId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to update publisher", e);
        }

        return listPublishers();
    }

    @Override
    public List<Publisher> removePublisher(Publisher publisher) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "delete from publishers where publisher_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, publisher.getPublisherId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to remove publisher", e);
        }

        return listPublishers();
    }

    @Override
    public List<Series> listSeries() {
        List<Series> series = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            String sql = "select * from series";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet results = statement.executeQuery()) {
                    while(results.next()) {
                        series.add(unpackSeries(results));
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to list series", e);
        }

        return series;
    }

    @Override
    public SeriesWithAddedSeriesId addSeries(Series newSeries) {
        Long seriesId = null;
        try(Connection connection = datasource.getConnection()) {
            String sql = "insert into series (series_name) values (?)";
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, newSeries.getName());
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        seriesId = keys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to add series", e);
        }

        return SeriesWithAddedSeriesId.with()
            .series(listSeries())
            .addedSeriesId(seriesId)
            .build();
    }

    @Override
    public List<Series> updateSeries(Series updatedSeries) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "update series set series_name=? where series_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, updatedSeries.getName());
                statement.setLong(2, updatedSeries.getSeriesId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to update series", e);
        }

        return listSeries();
    }

    @Override
    public List<Series> removeSeries(Series seriesToRemove) {
        try(Connection connection = datasource.getConnection()) {
            String sql = "delete from series where series_id=?";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, seriesToRemove.getSeriesId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new BokbaseException("Unable to remove series", e);
        }

        return listSeries();
    }

    Long getAccountId(Connection connection, String username) throws SQLException {
        String sql = "select * from bokbase_accounts where username=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try(ResultSet results = statement.executeQuery()) {
                while(results.next()) {
                    long accountId = results.getLong("account_id");
                    if (!results.wasNull()) {
                        return accountId;
                    }
                }
            }
        }

        return null;
    }

    private void addRolesIfNotpresent() {
        String bokbaseuser = BOKBASEUSER_ROLE;
        List<Role> roles = useradmin.getRoles();
        Optional<Role> existingRole = roles.stream().filter(r -> bokbaseuser.equals(r.getRolename())).findFirst();
        if (!existingRole.isPresent()) {
            useradmin.addRole(Role.with().id(-1).rolename(bokbaseuser).description("Bruker av applikasjonen bokbase").build());
        }
    }

    private Account unpackAccount(ResultSet results) throws SQLException {
        int accountId = results.getInt(1);
        String username = results.getString(2);
        User user = useradmin.getUser(username);
        return Account.with().accountId(accountId).user(user).build();
    }

    Book unpackBook(ResultSet results) throws SQLException {
        String binding = results.getString("binding");
        return Book.with()
            .bookId(results.getLong("book_id"))
            .title(results.getString("book_title"))
            .subtitle(results.getString("book_subtitle"))
            .seriesId(getNullableLong(results, "series_id"))
            .series(results.getString("series_name"))
            .seriesNumber(getNullableDouble(results, "series_number"))
            .authorId(getNullableLong(results, "author_id"))
            .authorName(getAuthorName(results))
            .rating(getNullableInteger(results, "rating"))
            .averageRating(getNullableInteger(results, "average_rating"))
            .publisherId(getNullableLong(results, "publisher_id"))
            .publisherName(results.getString("publisher_name"))
            .binding(binding != null ? Binding.valueOf(binding) : null)
            .pages(getNullableInteger(results, "pages"))
            .publishedDate(getNullableDate(results, "published_date"))
            .finishedReadDate(getNullableDate(results, "finished_read_date"))
            .bookshelf(Bookshelf.fromValue(results.getString("bookshelf")))
            .isbn13(results.getString("isbn13"))
            .build();
    }

    private Author unpackAuthor(ResultSet results) throws SQLException {
        return Author.with()
            .authorId(results.getLong("author_id"))
            .firstname(results.getString("author_firstname"))
            .lastname(results.getString("author_lastname"))
            .build();
    }

    private Publisher unpackPublisher(ResultSet results) throws SQLException {
        return Publisher.with()
            .publisherId(results.getLong("publisher_id"))
            .name(results.getString("publisher_name"))
            .build();
    }

    private Series unpackSeries(ResultSet results) throws SQLException {
        return Series.with()
            .seriesId(results.getLong("series_id"))
            .name(results.getString("series_name"))
            .build();
    }

    private void setIntOrNull(PreparedStatement statement, int index, Integer value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setInt(index, value);
        }
    }

    private void setLongOrNull(PreparedStatement statement, int index, Long value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.INTEGER);
        } else {
            statement.setLong(index, value);
        }
    }

    private void setDoubleOrNull(PreparedStatement statement, int index, Double value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.FLOAT);
        } else {
            statement.setDouble(index, value);
        }
    }

    private void setDateOrNull(PreparedStatement statement, int index, LocalDate value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.DATE);
        } else {
            statement.setDate(index, Date.valueOf(value));
        }
    }

    private Integer getNullableInteger(ResultSet results, String column) throws SQLException {
        int value = results.getInt(column);
        if (results.wasNull()) {
            return null;
        }

        return value;
    }

    private Long getNullableLong(ResultSet results, String column) throws SQLException {
        long value = results.getLong(column);
        if (results.wasNull()) {
            return null;
        }

        return value;
    }

    private Double getNullableDouble(ResultSet results, String column) throws SQLException {
        double value = results.getDouble(column);
        if (results.wasNull()) {
            return null;
        }

        return value;
    }

    private LocalDate getNullableDate(ResultSet results, String column) throws SQLException {
        Date value = results.getDate(column);
        if (results.wasNull()) {
            return null;
        }

        return value.toLocalDate();
    }

    String getAuthorName(ResultSet results) throws SQLException {
        String firstname = results.getString("author_firstname");
        String lastname = results.getString("author_lastname");
        if (firstname != null && lastname != null) {
            return firstname + " " + lastname;
        } else if (firstname == null) {
            return lastname;
        }

        return null;
    }

    Map<String, String> transformResourceBundleToMap(Locale locale) {
        Map<String, String> map = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, locale);
        Enumeration<String> keys = bundle.getKeys();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, bundle.getString(key));
        }

        return map;
    }

}
