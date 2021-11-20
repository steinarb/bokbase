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
package no.priv.bang.bokbase.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.bokbase.services.beans.Publisher;
import no.priv.bang.bokbase.services.beans.PublishersWithAddedPublisherId;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;


class BokbaseServiceTest {

    @Test
    void testOfAllOfTheMethods() {
        BokbaseService service = mock(BokbaseService.class);
        String username = "jad";
        boolean created = service.lazilyCreateAccount(username);
        assertFalse(created);
        List<Account> accounts = service.getAccounts();
        assertThat(accounts).isEmpty();
        Locale defaultLocale = service.defaultLocale();
        assertNull(defaultLocale);
        List<LocaleBean> availableLocales = service.availableLocales();
        assertThat(availableLocales).isEmpty();
        Locale locale = Locale.UK;
        Map<String, String> texts = service.displayTexts(locale);
        assertThat(texts).isEmpty();
        String key = "loggedout";
        String text = service.displayText(key, locale.toString());
        assertNull(text);
        List<Book> books = service.listBooks(username);
        assertThat(books).isEmpty();
        Book book = Book.with().build();
        BooksWithAddedBookId booksWithAddedBook = service.addBook(username, book);
        assertNull(booksWithAddedBook);
        List<Book> booksWithUpdatedBook = service.updateBook(username, book);
        assertThat(booksWithUpdatedBook).isEmpty();
        List<Book> booksWithDeletedBook = service.removeBook(username, book);
        assertThat(booksWithDeletedBook).isEmpty();
        List<Author> authors = service.listAuthors();
        assertThat(authors).isEmpty();
        Author author = Author.with().build();
        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = service.addAuthor(author);
        assertNull(authorsWithAddedAuthorId);
        List<Author> updatedAuthors = service.updateAuthor(author);
        assertThat(updatedAuthors).isEmpty();
        List<Author> authorsWithDeleted = service.removeAuthor(author);
        assertThat(authorsWithDeleted).isEmpty();
        List<Publisher> publishers = service.listPublishers();
        assertThat(publishers).isEmpty();
        Publisher publisher = Publisher.with().build();
        PublishersWithAddedPublisherId publishersWithAddedPublisherId = service.addPublisher(publisher);
        assertNull(publishersWithAddedPublisherId);
        List<Publisher> updatedPublishers = service.updatePublisher(publisher);
        assertThat(updatedPublishers).isEmpty();
        List<Publisher> publishersWithRemoved = service.removePublisher(publisher);
        assertThat(publishersWithRemoved).isEmpty();
        List<Series> series = service.listSeries();
        assertThat(series).isEmpty();
        Series newSeries = Series.with().name("Expanse").build();
        SeriesWithAddedSeriesId seriesWithAddedSeriesId = service.addSeries(newSeries);
        assertNull(seriesWithAddedSeriesId);
        Series updatedSeries = Series.with().name("Expanse").build();
        List<Series> seriesWithUpdated = service.updateSeries(updatedSeries);
        assertThat(seriesWithUpdated).isEmpty();
        Series seriesToRemove = Series.with().name("Expanse").build();
        List<Series> seriesWithoutRemoved = service.removeSeries(seriesToRemove);
        assertThat(seriesWithoutRemoved).isEmpty();
    }

}
