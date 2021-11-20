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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import no.priv.bang.bokbase.db.liquibase.test.BokbaseTestDbLiquibaseRunner;
import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;
import no.priv.bang.bokbase.services.beans.Binding;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.bokbase.services.beans.Publisher;
import no.priv.bang.bokbase.services.beans.PublishersWithAddedPublisherId;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.UserManagementService;

class BokbaseServiceProviderTest {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");

    private static DataSource datasource;

    @BeforeAll
    static void commonSetupForAllTests() throws Exception {
        DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:bokbase;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        MockLogService logservice = new MockLogService();
        BokbaseTestDbLiquibaseRunner runner = new BokbaseTestDbLiquibaseRunner();
        runner.setLogService(logservice);
        runner.activate();
        runner.prepare(datasource);
    }

    @Test
    void testGetAccounts() {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        BokbaseServiceProvider provider = new BokbaseServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        List<Account> accountsBefore = provider.getAccounts();
        assertThat(accountsBefore).isNotEmpty();
        boolean accountCreated = provider.lazilyCreateAccount("jod");
        assertTrue(accountCreated);
        List<Account> accountsAfter = provider.getAccounts();
        assertThat(accountsAfter).isNotEmpty();
        boolean secondAccountCreate = provider.lazilyCreateAccount("jod");
        assertFalse(secondAccountCreate);
        List<Account> accountsAfterSecondCreate = provider.getAccounts();
        assertThat(accountsAfterSecondCreate).isEqualTo(accountsAfter);
    }

    @Test
    void testGetAccountsWithDatabaseError() throws Exception {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        BokbaseServiceProvider provider = new BokbaseServiceProvider();
        provider.setLogservice(logservice);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        provider.setDatasource(datasourceWithFailure);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        List<Account> accountsBefore = provider.getAccounts();
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(accountsBefore).isEmpty();
    }

    @Test
    void testLazilyCreateAccountWithSQLException() throws Exception {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        BokbaseServiceProvider provider = new BokbaseServiceProvider();
        DataSource datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        boolean accountCreated = provider.lazilyCreateAccount("jod");
        assertFalse(accountCreated);
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDefaultLocale() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertEquals(NB_NO, bokbase.defaultLocale());
    }

    @Test
    void testAvailableLocales() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<LocaleBean> locales = bokbase.availableLocales();
        assertThat(locales).isNotEmpty().contains(LocaleBean.with().locale(bokbase.defaultLocale()).build());
    }

    @Test
    void testDisplayTextsForDefaultLocale() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Map<String, String> displayTexts = bokbase.displayTexts(bokbase.defaultLocale());
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayText() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        String text1 = bokbase.displayText("hi", "nb_NO");
        assertEquals("Hei", text1);
        String text2 = bokbase.displayText("hi", "en_GB");
        assertEquals("Hi", text2);
        String text3 = bokbase.displayText("hi", "");
        assertEquals("Hei", text3);
        String text4 = bokbase.displayText("hi", null);
        assertEquals("Hei", text4);
    }

    @Test
    void testGetAuthorNameWithOnlyLastname() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        ResultSet results = mock(ResultSet.class);
        String authorLastname = "Moon";
        when(results.getString("author_lastname")).thenReturn(authorLastname);
        assertEquals(authorLastname, bokbase.getAuthorName(results));
    }

    @Test
    void testGetAuthorNameWithOnlyFirsrname() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        ResultSet results = mock(ResultSet.class);
        String authorFirstname = "Elizabeth";
        when(results.getString("author_firstname")).thenReturn(authorFirstname);
        assertNull(bokbase.getAuthorName(results));
    }

    @Test
    void testGetAuthorNameWithNullLastnameAndNullFirstname() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        ResultSet results = mock(ResultSet.class);
        assertNull(bokbase.getAuthorName(results));
    }

    @Test
    void testListBooks() {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<Book> books = bokbase.listBooks(username);
        assertThat(books).isNotEmpty();
    }

    @Test
    void testListBooksWithDatabaseError() throws Exception {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Book> books = bokbase.listBooks(username);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(books).isEmpty();
    }

    @Test
    void testAddBook() {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        int beforeCount = bokbase.listBooks(username).size();
        Book newBook = Book.with()
            .title("Memory")
            .authorId(1L)
            .publisherId(1L)
            .yearPublished(1997)
            .binding(Binding.Paperback)
            .pages(419)
            .yearRead(1997)
            .monthRead(8)
            .seriesId(1L)
            .seriesNumber(10.0)
            .averageRating(5)
            .rating(5)
            .build();
        BooksWithAddedBookId booksAfterAdd = bokbase.addBook(username, newBook);
        assertThat(booksAfterAdd.getBooks().size()).isGreaterThan(beforeCount);
        assertNotNull(booksAfterAdd.getAddedBookId());
        Book addedBook = booksAfterAdd.getBooks().stream()
            .filter(b -> b.getBookId() == booksAfterAdd.getAddedBookId())
            .findFirst()
            .orElse(null);
        assertEquals(newBook.getTitle(), addedBook.getTitle());
    }

    @Test
    void testAddBookWithDatabaseError() throws Exception {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Book newBook = Book.with().build();
        assertThat(logservice.getLogmessages()).isEmpty();
        BooksWithAddedBookId booksAfterAdd = bokbase.addBook(username, newBook);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(booksAfterAdd.getBooks()).isEmpty();
        assertNull(booksAfterAdd.getAddedBookId());
    }

    @Test
    void testUpdateBook() {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Book originalBook = bokbase.listBooks(username).stream()
            .filter(b -> b.getBookId() == 1L)
            .findFirst()
            .orElse(null);
        Book modifiedBook = Book.with(originalBook)
            .title("Barrayar")
            .yearPublished(1991)
            .binding(Binding.Hardcover)
            .pages(386)
            .yearRead(1992)
            .monthRead(7)
            .seriesNumber(2.0)
            .averageRating(3)
            .rating(4)
            .build();
        Book updatedBook = bokbase.updateBook(username, modifiedBook).stream()
            .filter(b -> b.getBookId() == 1L)
            .findFirst()
            .orElse(null);
        assertThat(updatedBook).usingRecursiveComparison().isEqualTo(modifiedBook);
        assertNotEquals(originalBook, updatedBook);
    }

    @Test
    void testUpdateBookWithNullFieldValues() {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        String titleOfBookToDelete = "This book should be deleted";
        BooksWithAddedBookId added = bokbase.addBook(username, Book.with().title(titleOfBookToDelete).build());
        List<Book> booksBeforeDelete = added.getBooks();
        Book updateCandidate = booksBeforeDelete
            .stream()
            .filter(b -> b.getBookId() == added.getAddedBookId())
            .findFirst()
            .orElse(null);
        Book bookWithNullFields = Book.with().bookId(updateCandidate.getBookId()).build();
        List<Book> updatedBooks = bokbase.updateBook(username, bookWithNullFields);
        assertThat(updatedBooks).doesNotContain(updateCandidate);
        assertThat(updatedBooks).contains(bookWithNullFields);
    }

    @Test
    void testUpdateBookWithDatabaseError() throws Exception {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Book modifiedBook = Book.with().build();
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Book> updatedBooks = bokbase.updateBook(username, modifiedBook);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(updatedBooks).isEmpty();
    }

    @Test
    void testRemoveBook() {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        String titleOfBookToDelete = "This book should be deleted";
        BooksWithAddedBookId added = bokbase.addBook(username, Book.with().title(titleOfBookToDelete).build());
        List<Book> booksBeforeDelete = added.getBooks();
        Book deleteCandidate = booksBeforeDelete
            .stream()
            .filter(b -> b.getBookId() == added.getAddedBookId())
            .findFirst()
            .orElse(null);
        List<Book> booksAfterDelete = bokbase.removeBook(username, deleteCandidate);
        assertThat(booksBeforeDelete).contains(deleteCandidate);
        assertThat(booksAfterDelete).doesNotContain(deleteCandidate);
    }

    @Test
    void testRemoveBookWithDatabaseErrors() throws Exception {
        String username = "jad";
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Book deleteCandidate = Book.with().build();
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Book> booksAfterDelete = bokbase.removeBook(username, deleteCandidate);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(booksAfterDelete).isEmpty();
    }

    @Test
    void testListAuthors() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<Author> authors = bokbase.listAuthors();
        assertThat(authors).isNotEmpty();
        Author author = authors.stream().filter(a -> a.getAuthorId() == 1L).findFirst().orElse(null);
        assertEquals("Lois McMaster", author.getFirstname());
        assertEquals("Bujold", author.getLastname());
    }

    @Test
    void testListAuthorsWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Author> authors = bokbase.listAuthors();
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(authors).isEmpty();
    }

    @Test
    void testAddAuthor() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        int authorsCountBeforeAdd = bokbase.listAuthors().size();
        Author newAuthor = Author.with().firstname("Michael").lastname("Moorcock").build();
        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = bokbase.addAuthor(newAuthor);
        assertThat(authorsWithAddedAuthorId.getAuthors().size()).isGreaterThan(authorsCountBeforeAdd);
        Author addedAuthor = authorsWithAddedAuthorId.getAuthors().stream()
            .filter(a -> a.getAuthorId() == authorsWithAddedAuthorId.getAddedAuthorId())
            .findFirst()
            .orElse(null);
        assertEquals(newAuthor.getFirstname(), addedAuthor.getFirstname());
        assertEquals(newAuthor.getLastname(), addedAuthor.getLastname());
    }

    @Test
    void testAddAuthorWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Author newAuthor = Author.with().firstname("Michael").lastname("Moorcock").build();
        assertThat(logservice.getLogmessages()).isEmpty();
        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = bokbase.addAuthor(newAuthor);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(authorsWithAddedAuthorId.getAuthors()).isEmpty();
    }

    @Test
    void testUpdateAuthor() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = bokbase.addAuthor(Author.with().build());
        List<Author> authorsBeforeRemove = authorsWithAddedAuthorId.getAuthors();
        Author authorToUpdate = authorsBeforeRemove.stream()
            .filter(a -> a.getAuthorId() == authorsWithAddedAuthorId.getAddedAuthorId())
            .findFirst()
            .orElse(null);
        Author modifiedAuthor = Author.with(authorToUpdate)
            .firstname("Elizabeth")
            .lastname("Moon")
            .build();
        Author updatedAuthor = bokbase.updateAuthor(modifiedAuthor).stream()
            .filter(a -> a.getAuthorId() == modifiedAuthor.getAuthorId())
            .findFirst()
            .orElse(null);
        assertThat(updatedAuthor).usingRecursiveComparison().isEqualTo(modifiedAuthor);
        assertEquals(modifiedAuthor, updatedAuthor);
    }

    @Test
    void testUpdateAuthorWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        Author modifiedAuthor = Author.with().build();
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Author> updatedAuthors = bokbase.updateAuthor(modifiedAuthor);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(updatedAuthors).isEmpty();
    }

    @Test
    void testRemoveAuthor() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = bokbase.addAuthor(Author.with().firstname("to").lastname("remove").build());
        List<Author> authorsBeforeRemove = authorsWithAddedAuthorId.getAuthors();
        Author authorToRemove = authorsBeforeRemove.stream()
            .filter(a -> a.getAuthorId() == authorsWithAddedAuthorId.getAddedAuthorId())
            .findFirst()
            .orElse(null);
        List<Author> authorsAfterRemove = bokbase.removeAuthor(authorToRemove);
        assertThat(authorsBeforeRemove).contains(authorToRemove);
        assertThat(authorsAfterRemove).doesNotContain(authorToRemove);
    }

    @Test
    void testRemoveAuthorWhenBookUsesAuthor() {
        // Database operation fails because of database foreign key constraints
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        Author authorUsedInBook = bokbase.listAuthors().stream()
            .filter(a -> a.getAuthorId() == 1L)
            .findFirst()
            .orElse(null);
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Author> authorsAfterRemove = bokbase.removeAuthor(authorUsedInBook);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(authorsAfterRemove).contains(authorUsedInBook);
    }

    @Test
    void testListPublishers() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<Publisher> publishers = bokbase.listPublishers();
        assertThat(publishers).isNotEmpty();
        Publisher publisher = publishers.stream().filter(a -> a.getPublisherId() == 1L).findFirst().orElse(null);
        assertEquals("Baen Books", publisher.getName());
    }

    @Test
    void testListPublishersWithDatabaseError() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Publisher> publishers = bokbase.listPublishers();
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(publishers).isEmpty();
    }

    @Test
    void testAddPublisher() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        int publishersCountBeforeAdd = bokbase.listAuthors().size();
        Publisher newPublisher = Publisher.with().name("TOR Books").build();
        PublishersWithAddedPublisherId publishersWithAddedPublisherId = bokbase.addPublisher(newPublisher);
        assertThat(publishersWithAddedPublisherId.getPublishers().size()).isGreaterThan(publishersCountBeforeAdd);
        Publisher addedPublisher = publishersWithAddedPublisherId.getPublishers().stream()
            .filter(a -> a.getPublisherId() == publishersWithAddedPublisherId.getAddedPublisherId())
            .findFirst()
            .orElse(null);
        assertEquals(newPublisher.getName(), addedPublisher.getName());
    }

    @Test
    void testAddPublisherWithDatabaseError() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Publisher newPublisher = Publisher.with().name("TOR Books").build();
        assertThat(logservice.getLogmessages()).isEmpty();
        PublishersWithAddedPublisherId publishersWithAddedPublisherId = bokbase.addPublisher(newPublisher);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(publishersWithAddedPublisherId.getPublishers()).isEmpty();
    }

    @Test
    void testUpdatePublisher() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        PublishersWithAddedPublisherId publishersWithAddedAuthorId = bokbase.addPublisher(Publisher.with().build());
        List<Publisher> publishersBeforeRemove = publishersWithAddedAuthorId.getPublishers();
        Publisher publisherToUpdate = publishersBeforeRemove.stream()
            .filter(a -> a.getPublisherId() == publishersWithAddedAuthorId.getAddedPublisherId())
            .findFirst()
            .orElse(null);
        Publisher modifiedPublisher = Publisher.with(publisherToUpdate)
            .name("DelRey Books")
            .build();
        Publisher updatedPublisher = bokbase.updatePublisher(modifiedPublisher).stream()
            .filter(a -> a.getPublisherId() == modifiedPublisher.getPublisherId())
            .findFirst()
            .orElse(null);
        assertThat(updatedPublisher).usingRecursiveComparison().isEqualTo(modifiedPublisher);
        assertEquals(modifiedPublisher, updatedPublisher);
    }

    @Test
    void testUpdatePublisherWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        Publisher modifiedPublisher = Publisher.with().build();
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Publisher> updatedPublishers = bokbase.updatePublisher(modifiedPublisher);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(updatedPublishers).isEmpty();
    }

    @Test
    void testRemovePublisher() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        PublishersWithAddedPublisherId publishersWithAddedPublisherId = bokbase.addPublisher(Publisher.with().name("to remove").build());
        List<Publisher> publishersBeforeRemove = publishersWithAddedPublisherId.getPublishers();
        Publisher publisherToRemove = publishersBeforeRemove.stream()
            .filter(a -> a.getPublisherId() == publishersWithAddedPublisherId.getAddedPublisherId())
            .findFirst()
            .orElse(null);
        List<Publisher> publishersAfterRemove = bokbase.removePublisher(publisherToRemove);
        assertThat(publishersBeforeRemove).contains(publisherToRemove);
        assertThat(publishersAfterRemove).doesNotContain(publisherToRemove);
    }

    @Test
    void testRemovePublisherWhenBookUsesPublisher() {
        // Database operation fails because of database foreign key constraints
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        Publisher publisherUsedInBook = bokbase.listPublishers().stream()
            .filter(a -> a.getPublisherId() == 1)
            .findFirst()
            .orElse(null);
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Publisher> publishersAfterRemove = bokbase.removePublisher(publisherUsedInBook);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(publishersAfterRemove).contains(publisherUsedInBook);
    }

    @Test
    void testGetAccountIdNoResults() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        ResultSet results = mock(ResultSet.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(results);
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        Long account = bokbase.getAccountId(connection, "jad");
        assertNull(account);
    }

    @Test
    void testGetAccountIdNullResult() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        ResultSet results = mock(ResultSet.class);
        when(results.next()).thenReturn(true).thenReturn(false);
        when(results.wasNull()).thenReturn(true);
        PreparedStatement statement = mock(PreparedStatement.class);
        when(statement.executeQuery()).thenReturn(results);
        Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        Long account = bokbase.getAccountId(connection, "jad");
        assertNull(account);
    }

    @Test
    void testListSeries() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<Series> serieslist = bokbase.listSeries();
        assertThat(serieslist).isNotEmpty();
        Series series = serieslist.stream().filter(a -> a.getSeriesId() == 1L).findFirst().orElse(null);
        assertEquals("Vorkosigan", series.getName());
    }

    @Test
    void testListSeriesWithDatabaseError() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Series> serieslist = bokbase.listSeries();
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(serieslist).isEmpty();
    }

    @Test
    void testAddSeries() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        int seriesCountBeforeAdd = bokbase.listSeries().size();
        Series newSeries = Series.with().name("Expanse").build();
        SeriesWithAddedSeriesId seriesWithAddedPublisherId = bokbase.addSeries(newSeries);
        assertThat(seriesWithAddedPublisherId.getSeries().size()).isGreaterThan(seriesCountBeforeAdd);
        Series addedSeries = seriesWithAddedPublisherId.getSeries().stream()
            .filter(a -> a.getSeriesId() == seriesWithAddedPublisherId.getAddedSeriesId())
            .findFirst()
            .orElse(null);
        assertEquals(newSeries.getName(), addedSeries.getName());
    }

    @Test
    void testAddSeriesWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Series newSeries = Series.with().name("Expanse").build();
        assertThat(logservice.getLogmessages()).isEmpty();
        SeriesWithAddedSeriesId seriesWithAddedPublisherId = bokbase.addSeries(newSeries);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(seriesWithAddedPublisherId.getSeries()).isEmpty();
    }

    @Test
    void testUpdateSeries() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        SeriesWithAddedSeriesId seriesWithAddedSeriesId = bokbase.addSeries(Series.with().build());
        List<Series> seriesBeforeRemove = seriesWithAddedSeriesId.getSeries();
        Series seriesToUpdate = seriesBeforeRemove.stream()
            .filter(a -> a.getSeriesId() == seriesWithAddedSeriesId.getAddedSeriesId())
            .findFirst()
            .orElse(null);
        Series modifiedSeries = Series.with(seriesToUpdate)
            .name("Time of Heroes")
            .build();
        Series updatedSeries = bokbase.updateSeries(modifiedSeries).stream()
            .filter(a -> a.getSeriesId() == modifiedSeries.getSeriesId())
            .findFirst()
            .orElse(null);
        assertThat(updatedSeries).usingRecursiveComparison().isEqualTo(modifiedSeries);
        assertEquals(modifiedSeries, updatedSeries);
    }

    @Test
    void testUpdateSeriesWithDatabaseErrors() throws Exception {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        DataSource datasourceWithFailure = mock(DataSource.class);
        when(datasourceWithFailure.getConnection()).thenThrow(SQLException.class);
        bokbase.setDatasource(datasourceWithFailure);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        Series modifiedSeries = Series.with().name("Time of Heroes").build();
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Series> seriesWithupdatedSeries = bokbase.updateSeries(modifiedSeries);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(seriesWithupdatedSeries).isEmpty();
    }

    @Test
    void testRemoveSeries() {
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        SeriesWithAddedSeriesId seriesWithAddedPublisherId = bokbase.addSeries(Series.with().name("to remove").build());
        List<Series> seriesBeforeRemove = seriesWithAddedPublisherId.getSeries();
        Series seriesToRemove = seriesBeforeRemove.stream()
            .filter(a -> a.getSeriesId() == seriesWithAddedPublisherId.getAddedSeriesId())
            .findFirst()
            .orElse(null);
        List<Series> seriesAfterRemove = bokbase.removeSeries(seriesToRemove);
        assertThat(seriesBeforeRemove).contains(seriesToRemove);
        assertThat(seriesAfterRemove).doesNotContain(seriesToRemove);
    }

    @Test
    void testRemoveSeriesWhenBookUsesSeries() {
        // Database operation fails because of database foreign key constraints
        BokbaseServiceProvider bokbase = new BokbaseServiceProvider();
        MockLogService logservice = new MockLogService();
        bokbase.setLogservice(logservice);
        UserManagementService useradmin = mock(UserManagementService.class);
        bokbase.setUseradmin(useradmin);
        bokbase.setDatasource(datasource);
        bokbase.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        List<Series> seriesBeforeRemove = bokbase.listSeries();
        Series seriesToRemove = seriesBeforeRemove.stream()
            .filter(a -> a.getSeriesId() == 1L)
            .findFirst()
            .orElse(null);
        assertThat(logservice.getLogmessages()).isEmpty();
        List<Series> seriesAfterRemove = bokbase.removeSeries(seriesToRemove);
        assertThat(logservice.getLogmessages()).isNotEmpty();
        assertThat(seriesBeforeRemove).contains(seriesToRemove);
        assertThat(seriesAfterRemove).contains(seriesToRemove);
    }

}
