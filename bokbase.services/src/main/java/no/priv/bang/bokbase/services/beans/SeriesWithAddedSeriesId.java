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

public class SeriesWithAddedSeriesId {

    private List<Series> series;
    private Long addedSeriesId;

    private SeriesWithAddedSeriesId() { }

    public List<Series> getSeries() {
        return series;
    }

    public Long getAddedSeriesId() {
        return addedSeriesId;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private List<Series> series;
        private Long addedSeriesId;

        private Builder() { }

        public SeriesWithAddedSeriesId build() {
            SeriesWithAddedSeriesId bean = new SeriesWithAddedSeriesId();
            bean.series = series;
            bean.addedSeriesId = addedSeriesId;
            return bean;
        }

        public Builder series(List<Series> series) {
            this.series = series;
            return this;
        }

        public Builder addedSeriesId(Long addedSeriesId) {
            this.addedSeriesId = addedSeriesId;
            return this;
        }

    }

}
