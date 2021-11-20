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

import java.util.List;

import no.priv.bang.beans.immutable.Immutable;

public class BooksWithAddedBookId extends Immutable {

    private List<Book> books;
    private Long addedBookId;

    private BooksWithAddedBookId() {}

    public List<Book> getBooks() {
        return books;
    }

    public Long getAddedBookId() {
        return addedBookId;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder extends BooksWithAddedBookId {
        private List<Book> books;
        private Long addedBookId;
        private Builder() {}

        public BooksWithAddedBookId build() {
            BooksWithAddedBookId bean = new BooksWithAddedBookId();
            bean.addedBookId = this.addedBookId;
            bean.books = this.books;
            return bean;
        }

        public Builder books(List<Book> books) {
            this.books = books;
            return this;
        }

        public Builder addedBookId(Long addedBookId) {
            this.addedBookId = addedBookId;
            return this;
        }

    }


}
