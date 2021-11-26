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
package no.priv.bang.bokbase.web.api.resources;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;

class BookResourceTest {

    @Test
    void testAddBook() {
        BookResource resource = new BookResource();
        Long newBookId = 456L;
        Book newBook = Book.with().bookId(newBookId).title("Shards of Honor").build();
        List<Book> books = Collections.singletonList(newBook);
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addBook(anyString(), any())).thenReturn(BooksWithAddedBookId.with().addedBookId(newBookId).books(books).build());
        resource.bokbase = bokbase;

        BooksWithAddedBookId booksWithAddedBookId = resource.addBook(newBook);
        assertThat(booksWithAddedBookId.getBooks()).contains(newBook);
        assertEquals(newBookId, booksWithAddedBookId.getAddedBookId());
    }

    @Test
    void testUpdateBook() {
        BookResource resource = new BookResource();
        Long newBookId = 456L;
        Book updatedBook = Book.with().bookId(newBookId).title("Shards of Honor").build();
        List<Book> books = Collections.singletonList(updatedBook);
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updateBook(anyString(), any())).thenReturn(books);
        resource.bokbase = bokbase;

        List<Book> updatedBooks = resource.updateBook(updatedBook);
        assertThat(updatedBooks).contains(updatedBook);
    }

    @Test
    void testRemoveBook() {
        BookResource resource = new BookResource();
        Long bookToRemoveId = 456L;
        Book bookToRemove = Book.with().bookId(bookToRemoveId).title("Shards of Honor").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removeBook(anyString(), any())).thenReturn(Collections.emptyList());
        resource.bokbase = bokbase;

        List<Book> updatedBooks = resource.removeBook(bookToRemove);
        assertThat(updatedBooks).doesNotContain(bookToRemove);
    }

}
