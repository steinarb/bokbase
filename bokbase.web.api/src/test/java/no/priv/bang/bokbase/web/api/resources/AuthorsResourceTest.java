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
import no.priv.bang.bokbase.services.beans.Author;

class AuthorsResourceTest {

    @Test
    void testGetAuthors() {
        long authorId = 456L;
        Author author = Author.with()
            .authorId(authorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.listAuthors()).thenReturn(Collections.singletonList(author));
        AuthorsResource resource = new AuthorsResource();
        resource.bokbase = bokbase;

        List<Author> authors = resource.getAuthors();
        assertThat(authors).contains(author);
        assertEquals(authorId, authors.get(0).getAuthorId());
    }

}
