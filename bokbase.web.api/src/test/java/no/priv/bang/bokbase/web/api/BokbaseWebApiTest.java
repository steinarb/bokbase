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
package no.priv.bang.bokbase.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.jupiter.api.Test;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletOutputStream;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;
import no.priv.bang.bokbase.services.beans.Credentials;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.bokbase.services.beans.Publisher;
import no.priv.bang.bokbase.services.beans.PublishersWithAddedPublisherId;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;
import no.priv.bang.bokbase.web.api.resources.ErrorMessage;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class BokbaseWebApiTest extends ShiroTestBase {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");
    private final static Locale EN_UK = Locale.forLanguageTag("en-uk");

    public static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();

    @Test
    void testLogin() throws Exception {
        String username = "jd";
        String password = "johnnyBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());

    }

    @Test
    void testLoginWrongPassword() throws Exception {
        String username = "jd";
        String password = "johnniBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetAccounts() throws Exception {
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        Account account = Account.with().accountId(123).build();
        when(bokbase.getAccounts()).thenReturn(Collections.singletonList(account));
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        MockHttpServletRequest request = buildGetUrl("/accounts");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jd", "johnnyBoi");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        List<Account> accounts = mapper.readValue(getBinaryContent(response), new TypeReference<List<Account>>() {});
        assertThat(accounts).isNotEmpty();
    }
    @Test
    void testDefaultLocale() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.defaultLocale()).thenReturn(NB_NO);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/defaultlocale");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Locale defaultLocale = mapper.readValue(getBinaryContent(response), Locale.class);
        assertEquals(NB_NO, defaultLocale);
    }
    @Test
    void testAvailableLocales() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.availableLocales()).thenReturn(Collections.singletonList(Locale.forLanguageTag("nb-NO")).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/availablelocales");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<LocaleBean> availableLocales = mapper.readValue(getBinaryContent(response), new TypeReference<List<LocaleBean>>() {});
        assertThat(availableLocales).isNotEmpty().contains(LocaleBean.with().locale(Locale.forLanguageTag("nb-NO")).build());
    }

    @Test
    void testDisplayTexts() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(bokbase.displayTexts(NB_NO)).thenReturn(texts);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=nb_NO");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Map<String, String> displayTexts = mapper.readValue(getBinaryContent(response), new TypeReference<Map<String, String>>() {});
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayTextsWithUnknownLocale() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(bokbase.displayTexts(EN_UK)).thenThrow(MissingResourceException.class);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=en_UK");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(500, response.getStatus());
        assertEquals("application/json", response.getContentType());
        ErrorMessage errorMessage = mapper.readValue(getBinaryContent(response), ErrorMessage.class);
        assertEquals(500, errorMessage.getStatus());
        assertThat(errorMessage.getMessage()).startsWith("Unknown locale");
    }

    @Test
    void testGetBooks() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        String title = "Memory";
        when(bokbase.listBooks(anyString())).thenReturn(Arrays.asList(Book.with().title(title).build()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/books/jad");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Book> books = mapper.readValue(getBinaryContent(response), new TypeReference<List<Book>>() {});
        assertThat(books).isNotEmpty();
    }

    @Test
    void testAddBook() throws Exception {
        // Set up REST API servlet with mocked services
        Long newBookId = 456L;
        Book newBook = Book.with().bookId(newBookId).title("Shards of Honor").build();
        List<Book> books = Collections.singletonList(newBook);
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addBook(anyString(), any())).thenReturn(BooksWithAddedBookId.with().addedBookId(newBookId).books(books).build());
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/book/add");
        String postBody = mapper.writeValueAsString(newBook);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        BooksWithAddedBookId booksWithAddedBookId = mapper.readValue(getBinaryContent(response), BooksWithAddedBookId.class);
        assertThat(booksWithAddedBookId.getBooks()).contains(newBook);
        assertEquals(newBookId, booksWithAddedBookId.getAddedBookId());
    }

    @Test
    void testUpdateBook() throws Exception {
        // Set up REST API servlet with mocked services
        Long newBookId = 456L;
        Book updatedBook = Book.with().bookId(newBookId).title("Shards of Honor").build();
        List<Book> books = Collections.singletonList(updatedBook);
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updateBook(anyString(), any())).thenReturn(books);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/book/update");
        String postBody = mapper.writeValueAsString(updatedBook);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Book> updatedBooks = mapper.readValue(getBinaryContent(response), new TypeReference<List<Book>>() {});
        assertThat(updatedBooks).contains(updatedBook);
    }

    @Test
    void testRemoveBook() throws Exception {
        // Set up REST API servlet with mocked services
        Long bookToRemoveId = 456L;
        Book bookToRemove = Book.with().bookId(bookToRemoveId).title("Shards of Honor").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removeBook(anyString(), any())).thenReturn(Collections.emptyList());
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/book/update");
        String postBody = mapper.writeValueAsString(bookToRemove);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Book> updatedBooks = mapper.readValue(getBinaryContent(response), new TypeReference<List<Book>>() {});
        assertThat(updatedBooks).doesNotContain(bookToRemove);
    }

    @Test
    void testGetAuthors() throws Exception {
        // Set up REST API servlet with mocked services
        long authorId = 456L;
        Author author = Author.with()
            .authorId(authorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.listAuthors()).thenReturn(Collections.singletonList(author));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/authors");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Author> authors = mapper.readValue(getBinaryContent(response), new TypeReference<List<Author>>() {});
        assertThat(authors).contains(author);
    }

    @Test
    void testAddAuthor() throws Exception {
        // Set up REST API servlet with mocked services
        long addedAuthorId = 456L;
        Author addedAuthor = Author.with()
            .authorId(addedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(addedAuthor);
        when(bokbase.addAuthor(any())).thenReturn(AuthorsWithAddedAuthorId.with().addedAuthorId(addedAuthorId).authors(authors).build());
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/author/add");
        String postBody = mapper.writeValueAsString(addedAuthor);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = mapper.readValue(getBinaryContent(response), AuthorsWithAddedAuthorId.class);
        assertThat(authorsWithAddedAuthorId.getAuthors()).contains(addedAuthor);
        assertEquals(addedAuthorId, authorsWithAddedAuthorId.getAddedAuthorId());
    }

    @Test
    void testUpdateAuthor() throws Exception {
        // Set up REST API servlet with mocked services
        long modifiedAuthorId = 456L;
        Author modifiedAuthor = Author.with()
            .authorId(modifiedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(modifiedAuthor);
        when(bokbase.updateAuthor(any())).thenReturn(authors);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/author/update");
        String postBody = mapper.writeValueAsString(modifiedAuthor);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Author> authorsWithUpdatedAuthor = mapper.readValue(getBinaryContent(response), new TypeReference<List<Author>>() {});
        assertThat(authorsWithUpdatedAuthor).contains(modifiedAuthor);
        assertEquals(modifiedAuthorId, authorsWithUpdatedAuthor.get(0).getAuthorId());
    }

    @Test
    void testRemoveAuthor() throws Exception {
        // Set up REST API servlet with mocked services
        long deletedAuthorId = 456L;
        Author deletedAuthor = Author.with()
            .authorId(deletedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(Author.with().firstname("Other").lastname("Author").build());
        when(bokbase.removeAuthor(any())).thenReturn(authors);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/author/remove");
        String postBody = mapper.writeValueAsString(deletedAuthor);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Author> authorsWithoutRemovedAuthor = mapper.readValue(getBinaryContent(response), new TypeReference<List<Author>>() {});
        assertThat(authorsWithoutRemovedAuthor)
            .isNotEmpty()
            .doesNotContain(deletedAuthor);
    }

    @Test
    void testGetPublishers() throws Exception {
        // Set up REST API servlet with mocked services
        long publisherId = 678L;
        Publisher publisher = Publisher.with().publisherId(publisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.listPublishers()).thenReturn(Collections.singletonList(publisher));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/publishers");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Publisher> publishers = mapper.readValue(getBinaryContent(response), new TypeReference<List<Publisher>>() {});
        assertThat(publishers).isNotEmpty();
        assertEquals(publisherId, publishers.get(0).getPublisherId());
    }

    @Test
    void testAddPublisher() throws Exception {
        // Set up REST API servlet with mocked services
        long addedPublisherId = 678L;
        Publisher addedPublisher = Publisher.with().publisherId(addedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addPublisher(any())).thenReturn(PublishersWithAddedPublisherId.with().addedPublisherId(addedPublisherId).publishers(Collections.singletonList(addedPublisher)).build());
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/publisher/add");
        String postBody = mapper.writeValueAsString(addedPublisher);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        PublishersWithAddedPublisherId publishersWithAddedPublisherId = mapper.readValue(getBinaryContent(response), PublishersWithAddedPublisherId.class);
        assertThat(publishersWithAddedPublisherId.getPublishers()).isNotEmpty();
        assertEquals(addedPublisherId, publishersWithAddedPublisherId.getAddedPublisherId());
    }

    @Test
    void testUpdatePublisher() throws Exception {
        // Set up REST API servlet with mocked services
        long modifiedPublisherId = 678L;
        Publisher modifiedPublisher = Publisher.with().publisherId(modifiedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updatePublisher(any())).thenReturn(Collections.singletonList(modifiedPublisher));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/publisher/update");
        String postBody = mapper.writeValueAsString(modifiedPublisher);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Publisher> publishersWithUpdatedPublisher = mapper.readValue(getBinaryContent(response), new TypeReference<List<Publisher>>() {});
        assertThat(publishersWithUpdatedPublisher).isNotEmpty();
        assertEquals(modifiedPublisherId, publishersWithUpdatedPublisher.get(0).getPublisherId());
    }

    @Test
    void testRemovePublisher() throws Exception {
        // Set up REST API servlet with mocked services
        long removedPublisherId = 678L;
        Publisher publisherToRemove = Publisher.with().publisherId(removedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removePublisher(any())).thenReturn(Collections.singletonList(Publisher.with().name("Orbit Books").build()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/publisher/remove");
        String postBody = mapper.writeValueAsString(publisherToRemove);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Publisher> publishersWithRemovedPublisher = mapper.readValue(getBinaryContent(response), new TypeReference<List<Publisher>>() {});
        assertThat(publishersWithRemovedPublisher)
            .isNotEmpty()
            .doesNotContain(publisherToRemove);
    }

    @Test
    void testGetSeries() throws Exception {
        // Set up REST API servlet with mocked services
        long seriesId = 978L;
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.listSeries()).thenReturn(Collections.singletonList(Series.with().seriesId(seriesId).build()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/series");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Series> series = mapper.readValue(getBinaryContent(response), new TypeReference<List<Series>>() {});
        assertThat(series).isNotEmpty();
        assertEquals(seriesId, series.get(0).getSeriesId());
    }

    @Test
    void testAddSeries() throws Exception {
        // Set up REST API servlet with mocked services
        long seriesId = 978L;
        Series seriesToAdd = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addSeries(any())).thenReturn(SeriesWithAddedSeriesId.with().series(Collections.singletonList(seriesToAdd)).addedSeriesId(seriesId).build());
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/series/add");
        String postBody = mapper.writeValueAsString(seriesToAdd);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        SeriesWithAddedSeriesId seriesWithAddedSeriesId = mapper.readValue(getBinaryContent(response), SeriesWithAddedSeriesId.class);
        assertThat(seriesWithAddedSeriesId.getSeries()).contains(seriesToAdd);
    }

    @Test
    void testUpdateSeries() throws Exception {
        // Set up REST API servlet with mocked services
        long seriesId = 978L;
        Series seriesToModify = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updateSeries(any())).thenReturn(Collections.singletonList(seriesToModify));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/series/update");
        String postBody = mapper.writeValueAsString(seriesToModify);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Series> seriesWithModifiedSeries = mapper.readValue(getBinaryContent(response), new TypeReference<List<Series>>() {});
        assertThat(seriesWithModifiedSeries).contains(seriesToModify);
    }

    @Test
    void testRemoveSeries() throws Exception {
        // Set up REST API servlet with mocked services
        long seriesId = 978L;
        Series seriesToRemove = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removeSeries(any())).thenReturn(Collections.singletonList(Series.with().build()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildPostUrl("/series/remove");
        String postBody = mapper.writeValueAsString(seriesToRemove);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<Series> seriesWithoutRemovedSeries = mapper.readValue(getBinaryContent(response), new TypeReference<List<Series>>() {});
        assertThat(seriesWithoutRemovedSeries)
            .isNotEmpty()
            .doesNotContain(seriesToRemove);
    }

    private byte[] getBinaryContent(MockHttpServletResponse response) throws IOException {
        MockServletOutputStream outputstream = (MockServletOutputStream) response.getOutputStream();
        return outputstream.getBinaryContent();
    }

    private MockHttpServletRequest buildGetUrl(String resource) {
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletRequest buildPostUrl(String resource) {
        String contenttype = MediaType.APPLICATION_JSON;
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("POST");
        request.setContentType(contenttype);
        request.addHeader("Content-Type", contenttype);
        return request;
    }

    private MockHttpServletRequest buildRequest(String resource) {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setProtocol("HTTP/1.1");
        request.setRequestURL("http://localhost:8181/bokbase/api" + resource);
        request.setRequestURI("/bokbase/api" + resource);
        request.setContextPath("/bokbase");
        request.setServletPath("/api");
        request.setSession(session);
        return request;
    }

    private BokbaseWebApi simulateDSComponentActivationAndWebWhiteboardConfiguration(BokbaseService bokbase, LogService logservice) throws Exception {
        BokbaseWebApi servlet = new BokbaseWebApi();
        servlet.setLogService(logservice);
        servlet.setSampleappService(bokbase);
        servlet.activate();
        ServletConfig config = createServletConfigWithApplicationAndPackagenameForJerseyResources();
        servlet.init(config);
        return servlet;
    }

    private ServletConfig createServletConfigWithApplicationAndPackagenameForJerseyResources() {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(ServerProperties.PROVIDER_PACKAGES)));
        when(config.getInitParameter(eq(ServerProperties.PROVIDER_PACKAGES))).thenReturn("no.priv.bang.bokbase.web.api.resources");
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("/bokbase");
        when(config.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
        return config;
    }
}
