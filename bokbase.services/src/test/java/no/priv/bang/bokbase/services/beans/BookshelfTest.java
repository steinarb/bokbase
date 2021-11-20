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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BookshelfTest {

    @Test
    void testCreateFromValue() {
        assertEquals(Bookshelf.toRead, Bookshelf.fromValue("to-read"));
        assertEquals(Bookshelf.currentlyReading, Bookshelf.fromValue("currently-reading"));
        assertEquals(Bookshelf.read, Bookshelf.fromValue("read"));
        assertEquals(Bookshelf.quitReading, Bookshelf.fromValue("quit-reading"));
        assertNull(Bookshelf.fromValue("no-match"));
        assertNull(Bookshelf.fromValue(""));
        assertNull(Bookshelf.fromValue(null));
    }

}
