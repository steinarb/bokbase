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
package no.priv.bang.bokbase.db.liquibase;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

class BokbaseLiquibaseTest {
    DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
    private int accountid1;
    private int authorid1;
    private int publisherid1;
    private int seriesid1;
    private int bookid1;
    private int bookshelfid1;
    private int ratingid1;

    @Test
    void testCreateSchema() throws Exception {
        try(Connection connection = createConnection()) {
            BokbaseLiquibase bokbaseLiquibase = new BokbaseLiquibase();
            bokbaseLiquibase.createInitialSchema(connection);
            addAccounts(connection);
            assertAccounts(connection);
            addAuthors(connection);
            assertAuthors(connection);
            addPublishers(connection);
            assertPublishers(connection);
            addSeries(connection);
            assertSeries(connection);
            addBooks(connection);
            assertBooks(connection);
            addBookshelves(connection);
            assertBookshelves(connection);
            addRatings(connection);
            assertRatings(connection);
            bokbaseLiquibase.updateSchema(connection);
        }
    }

    @Test
    void testForceReleaseLocks() throws Exception {
        Connection connection = createConnection();
        BokbaseLiquibase bokbaseLiquibase = new BokbaseLiquibase();
        assertDoesNotThrow(() -> bokbaseLiquibase.forceReleaseLocks(connection));
    }

    private void addAccounts(Connection connection) throws Exception {
        accountid1 = addAccount(connection, "admin");
    }

    private void assertAccounts(Connection connection) throws Exception {
        String sql = "select count(*) from bokbase_accounts";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    int count = results.getInt(1);
                    assertEquals(1, count);
                }
            }
        }
    }

    private void addAuthors(Connection connection) throws Exception {
        authorid1 = addAuthor(connection, "Marko", "Kloos");
    }

    private void assertAuthors(Connection connection) throws Exception {
        assertAuthor(connection, authorid1, "Marko", "Kloos");
    }

    private void addPublishers(Connection connection) throws Exception {
        publisherid1 = addPublisher(connection, "47North");
    }

    private void assertPublishers(Connection connection) throws Exception {
        assertPublisher(connection, publisherid1, "47North");
    }

    private void addSeries(Connection connection) throws Exception {
        seriesid1 = addSeries(connection, "Vorkosigan");
    }

    private void assertSeries(Connection connection) throws Exception {
        assertSeries(connection, seriesid1, "Vorkosigan");
    }

    private void addBooks(Connection connection) throws Exception {
        bookid1 = addBook(connection, "Lord Vorpatrils Alliance", "A romance", authorid1, seriesid1, 14.0, 4, publisherid1, "Hardcover", 413, 2013);
    }

    private void assertBooks(Connection connection) throws Exception {
        assertBook(connection, bookid1, "Lord Vorpatrils Alliance", "A romance", authorid1, seriesid1, 14.0, 4, publisherid1, "Hardcover", 413, 2013);
    }

    private void addBookshelves(Connection connection) throws Exception {
        bookshelfid1 = addBookshelf(connection, accountid1, "to-read", bookid1);
    }

    private void assertBookshelves(Connection connection) throws Exception {
        assertBookshelf(connection, bookshelfid1, accountid1, "to-read", bookid1);
    }

    private void addRatings(Connection connection) throws Exception {
        ratingid1 = addRating(connection, accountid1, bookid1, 5);
    }

    private void assertRatings(Connection connection) throws Exception {
        assertRating(connection, ratingid1, accountid1, bookid1, 5);
    }

    private int addAccount(Connection connection, String username) throws Exception {
        String sql = "insert into bokbase_accounts (username) values (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }

        return findAccountId(connection, username);
    }

    private int findAccountId(Connection connection, String username) throws Exception {
        String sql = "select account_id from bokbase_accounts where username=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private int addAuthor(Connection connection, String firstname, String lastname) throws Exception {
        String sql = "insert into authors (author_firstname, author_lastname) values (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.executeUpdate();
        }

        return findAuthorId(connection, firstname, lastname);
    }

    private int findAuthorId(Connection connection, String firstname, String lastname) throws Exception {
        String sql = "select author_id from authors where author_firstname=? and author_lastname=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertAuthor(Connection connection, int authorid, String firstname, String lastname) throws Exception {
        String sql = "select * from authors where author_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, authorid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(authorid, results.getInt(1));
                    assertEquals(firstname, results.getString(2));
                    assertEquals(lastname, results.getString(3));
                }
            }
        }
    }

    private int addPublisher(Connection connection, String publisherName) throws Exception {
        String sql = "insert into publishers (publisher_name) values (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publisherName);
            statement.executeUpdate();
        }

        return findPublisherId(connection, publisherName);
    }

    private int findPublisherId(Connection connection, String publisherName) throws Exception {
        String sql = "select publisher_id from publishers where publisher_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, publisherName);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertPublisher(Connection connection, int publisherid, String publisherName) throws Exception {
        String sql = "select * from publishers where publisher_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, publisherid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(publisherid, results.getInt(1));
                    assertEquals(publisherName, results.getString(2));
                }
            }
        }
    }

    private int addSeries(Connection connection, String seriesName) throws Exception {
        String sql = "insert into series (series_name) values (?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seriesName);
            statement.executeUpdate();
        }

        return findSeriesId(connection, seriesName);
    }

    private int findSeriesId(Connection connection, String seriesName) throws Exception {
        String sql = "select series_id from series where series_name=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, seriesName);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertSeries(Connection connection, int seriesid, String seriesName) throws Exception {
        String sql = "select * from series where series_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, seriesid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(seriesid, results.getInt(1));
                    assertEquals(seriesName, results.getString(2));
                }
            }
        }
    }

    private int addBook(Connection connection, String title, String subtitle, int authorid, int seriesid, Double seriesNumber, int averageRating, int publisherid, String binding, int pages, int yearPublished) throws Exception {
        String sql = "insert into books (book_title, book_subtitle, series_id, series_number, author_id, average_rating, publisher_id, binding, pages, year_published) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, subtitle);
            statement.setLong(3, seriesid);
            statement.setDouble(4, seriesNumber);
            statement.setLong(5, authorid);
            statement.setInt(6, averageRating);
            statement.setLong(7, publisherid);
            statement.setString(8, binding);
            statement.setInt(9, pages);
            statement.setInt(10, yearPublished);
            statement.executeUpdate();
        }

        return findBookId(connection, title);
    }

    private int findBookId(Connection connection, String title) throws Exception {
        String sql = "select book_id from books where book_title=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertBook(Connection connection, int bookid, String title, String subtitle, int authorid, int seriesid, Double seriesNumber, int averageRating, int publisherid, String binding, int pages, int yearPublished) throws Exception {
        String sql = "select * from books where book_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(bookid, results.getLong("book_id"));
                    assertEquals(title, results.getString("book_title"));
                    assertEquals(subtitle, results.getString("book_subtitle"));
                    assertEquals(authorid, results.getLong("author_id"));
                    assertEquals(seriesid, results.getLong("series_id"));
                    assertEquals(seriesNumber, results.getDouble("series_number"));
                    assertEquals(averageRating, results.getInt("average_rating"));
                    assertEquals(publisherid, results.getLong("publisher_id"));
                    assertEquals(binding, results.getString("binding"));
                    assertEquals(pages, results.getInt("pages"));
                    assertEquals(yearPublished, results.getInt("year_published"));
                }
            }
        }
    }

    private int addBookshelf(Connection connection, int accountid, String bookshelf, int bookid) throws Exception {
        String sql = "insert into bookshelves (account_id, bookshelf, book_id) values (?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountid);
            statement.setString(2, bookshelf);
            statement.setLong(3, bookid);
            statement.executeUpdate();
        }

        return findBookshelfId(connection, accountid, bookshelf, bookid);
    }

    private int findBookshelfId(Connection connection, int accountid, String bookshelf, int bookid) throws Exception {
        String sql = "select bookshelf_id from bookshelves where account_id=? and bookshelf=? and book_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountid);
            statement.setString(2, bookshelf);
            statement.setLong(3, bookid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertBookshelf(Connection connection, int bookshelfid, int accountid, String bookshelf, int bookid) throws Exception {
        String sql = "select * from bookshelves where bookshelf_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(accountid, results.getLong("account_id"));
                    assertEquals(bookshelf, results.getString("bookshelf"));
                    assertEquals(bookid, results.getLong("book_id"));
                }
            }
        }
    }

    private int addRating(Connection connection, int accountid, int bookid, int rating) throws Exception {
        String sql = "insert into book_ratings (account_id, book_id, rating) values (?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountid);
            statement.setLong(2, bookid);
            statement.setInt(3, rating);
            statement.executeUpdate();
        }

        return findRatingId(connection, accountid, bookid);
    }

    private int findRatingId(Connection connection, int accountid, int bookid) throws Exception {
        String sql = "select book_rating_id from book_ratings where account_id=? and book_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountid);
            statement.setLong(2, bookid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return results.getInt(1);
                }
            }
        }

        return -1;
    }

    private void assertRating(Connection connection, int ratingid, int accountid, int bookid, int rating) throws Exception {
        String sql = "select * from book_ratings where book_rating_id=?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ratingid);
            try(ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    assertEquals(accountid, results.getLong("account_id"));
                    assertEquals(bookid, results.getLong("book_id"));
                    assertEquals(rating, results.getInt("rating"));
                }
            }
        }
    }

    private Connection createConnection() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:bokbase;create=true");
        DataSource dataSource = derbyDataSourceFactory.createDataSource(properties);
        return dataSource.getConnection();
    }

}
