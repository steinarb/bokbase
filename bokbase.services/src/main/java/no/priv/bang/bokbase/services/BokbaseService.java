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

import java.util.List;
import java.util.Locale;
import java.util.Map;

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

public interface BokbaseService {

    public List<Account> getAccounts();

    Locale defaultLocale();

    List<LocaleBean> availableLocales();

    public Map<String, String> displayTexts(Locale locale);

    public String displayText(String key, String locale);

    public boolean lazilyCreateAccount(String username);

    public List<Book> listBooks(String username);

    public BooksWithAddedBookId addBook(String username, Book book);

    public List<Book> updateBook(String username, Book book);

    public List<Book> removeBook(String username, Book book);

    public List<Author> listAuthors();

    public AuthorsWithAddedAuthorId addAuthor(Author author);

    public List<Author> updateAuthor(Author author);

    public List<Author> removeAuthor(Author author);

    public List<Publisher> listPublishers();

    public PublishersWithAddedPublisherId addPublisher(Publisher publisher);

    public List<Publisher> updatePublisher(Publisher publisher);

    public List<Publisher> removePublisher(Publisher publisher);

    public List<Series> listSeries();

    public SeriesWithAddedSeriesId addSeries(Series newSeries);

    public List<Series> updateSeries(Series updatedSeries);

    public List<Series> removeSeries(Series seriesToRemove);

}
