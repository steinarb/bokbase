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
package no.priv.bang.bokbase.services.beans;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void testCreate() {
        Long authorId = 5L;
        String firstname = "Lois McMaster";
        String lastname = "Bujold";
        Author bean = Author.with()
            .authorId(authorId)
            .firstname(firstname)
            .lastname(lastname)
            .build();
        assertEquals(authorId, bean.getAuthorId());
        assertEquals(firstname, bean.getFirstname());
        assertEquals(lastname, bean.getLastname());
    }

    @Test
    void testCopy() {
        Long authorId = 5L;
        String firstname = "Lois McMaster";
        String lastname = "Bujold";
        Author bean = Author.with()
            .authorId(authorId)
            .firstname(firstname)
            .lastname(lastname)
            .build();
        Author copyOfBean = Author.with(bean).build();
        assertThat(copyOfBean).usingRecursiveComparison().isEqualTo(bean);
    }

}
