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

public class AuthorsWithAddedAuthorId extends Immutable {

    private List<Author> authors;
    private Long addedAuthorId;

    private AuthorsWithAddedAuthorId() {}

    public List<Author> getAuthors() {
        return authors;
    }

    public Long getAddedAuthorId() {
        return addedAuthorId;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private List<Author> authors;
        private Long addedAuthorId;

        private Builder() {}

        public AuthorsWithAddedAuthorId build() {
            AuthorsWithAddedAuthorId bean = new AuthorsWithAddedAuthorId();
            bean.authors = this.authors;
            bean.addedAuthorId = this.addedAuthorId;
            return bean;
        }

        public Builder authors(List<Author> authors) {
            this.authors = authors;
            return this;
        }

        public Builder addedAuthorId(Long addedAuthorId) {
            this.addedAuthorId = addedAuthorId;
            return this;
        }

    }

}
