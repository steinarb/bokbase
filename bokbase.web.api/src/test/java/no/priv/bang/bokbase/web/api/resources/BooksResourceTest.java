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

class BooksResourceTest {

    @Test
    void testGetBooks() {
        BooksResource resource = new BooksResource();
        BokbaseService bokbase = mock(BokbaseService.class);
        String title = "Memory";
        when(bokbase.listBooks(anyString())).thenReturn(Collections.singletonList(Book.with().title(title).build()));
        resource.bokbase = bokbase;
        String username = "jad";
        List<Book> books = resource.getBooks(username);
        assertThat(books).isNotEmpty();
        assertEquals(title, books.get(0).getTitle());
    }

}
