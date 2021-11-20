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

public class Publisher extends Immutable {

    private long publisherId;
    private String name;

    private Publisher() {}

    public long getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Publisher bean) {
        Builder builder = new Builder();
        builder.publisherId = bean.publisherId;
        builder.name = bean.name;
        return builder;
    }

    public static class Builder {

        private long publisherId;
        private String name;

        private Builder() {}

        public Publisher build() {
            Publisher bean = new Publisher();
            bean.publisherId = this.publisherId;
            bean.name = this.name;
            return bean;
        }

        public Builder publisherId(long publisherId) {
            this.publisherId = publisherId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

    }

    @Override
    public String toString() {
        return "Publisher [publisherId=" + publisherId + ", name=" + name + "]";
    }

}
