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

import no.priv.bang.beans.immutable.Immutable;

public class Author extends Immutable {

    private long authorId;
    private String firstname;
    private String lastname;

    private Author() {}

    public long getAuthorId() {
        return authorId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Author author) {
        Builder builder = new Builder();
        builder.authorId = author.authorId;
        builder.firstname = author.firstname;
        builder.lastname = author.lastname;
        return builder;
    }

    public static class Builder {

        private Builder() {}

        private long authorId;
        private String firstname;
        private String lastname;

        public Author build() {
            Author author = new Author();
            author.authorId = this.authorId;
            author.firstname = this.firstname;
            author.lastname = this.lastname;
            return author;
        }

        public Builder authorId(long authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

    }

    @Override
    public String toString() {
        return "Author [authorId=" + authorId + ", firstname=" + firstname + ", lastname=" + lastname + "]";
    }

}
