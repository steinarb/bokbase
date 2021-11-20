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

public class Series extends Immutable {

    private long seriesId;
    private String name;

    private Series() { }

    public long getSeriesId() {
        return seriesId;
    }

    public String getName() {
        return name;
    }

    public static Builder with() {
        return new Builder();
    }

    public static Builder with(Series bean) {
        Builder builder = new Builder();
        builder.seriesId = bean.seriesId;
        builder.name = bean.name;
        return builder;
    }

    public static class Builder {

        private long seriesId;
        private String name;

        private Builder() { }

        public Series build() {
            Series bean = new Series();
            bean.seriesId = seriesId;
            bean.name = name;
            return bean;
        }

        public Builder seriesId(long seriesId) {
            this.seriesId = seriesId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

    }

    @Override
    public String toString() {
        return "Series [seriesId=" + seriesId + ", name=" + name + "]";
    }

}
