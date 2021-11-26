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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;

class AuthorResourceTest {

    @Test
    void testAddAuthor() {
        long addedAuthorId = 456L;
        Author addedAuthor = Author.with()
            .authorId(addedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(addedAuthor);
        when(bokbase.addAuthor(any())).thenReturn(AuthorsWithAddedAuthorId.with().addedAuthorId(addedAuthorId).authors(authors).build());
        AuthorResource resource = new AuthorResource();
        resource.bokbase = bokbase;

        AuthorsWithAddedAuthorId authorsWithAddedAuthorId = resource.addAuthor(addedAuthor);
        assertThat(authorsWithAddedAuthorId.getAuthors()).contains(addedAuthor);
        assertEquals(addedAuthorId, authorsWithAddedAuthorId.getAddedAuthorId());
    }

    @Test
    void testUpdateAuthor() {
        long modifiedAuthorId = 456L;
        Author modifiedAuthor = Author.with()
            .authorId(modifiedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(modifiedAuthor);
        when(bokbase.updateAuthor(any())).thenReturn(authors);
        AuthorResource resource = new AuthorResource();
        resource.bokbase = bokbase;

        List<Author> authorsWithUpdatedAuthor = resource.updateAuthor(modifiedAuthor);
        assertThat(authorsWithUpdatedAuthor).contains(modifiedAuthor);
        assertEquals(modifiedAuthorId, authorsWithUpdatedAuthor.get(0).getAuthorId());
    }

    @Test
    void testRemoveAuthor() {
        long deletedAuthorId = 456L;
        Author deletedAuthor = Author.with()
            .authorId(deletedAuthorId)
            .firstname("Lois McMaster")
            .lastname("Bujold")
            .build();
        BokbaseService bokbase = mock(BokbaseService.class);
        List<Author> authors = Collections.singletonList(Author.with().firstname("Other").lastname("Author").build());
        when(bokbase.removeAuthor(any())).thenReturn(authors);
        AuthorResource resource = new AuthorResource();
        resource.bokbase = bokbase;

        List<Author> authorsWithoutRemovedAuthor = resource.removeAuthor(deletedAuthor);
        assertThat(authorsWithoutRemovedAuthor)
            .isNotEmpty()
            .doesNotContain(deletedAuthor);
    }

}
