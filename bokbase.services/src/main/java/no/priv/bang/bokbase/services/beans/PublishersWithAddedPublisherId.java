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

public class PublishersWithAddedPublisherId {

    private List<Publisher> publishers;
    private Long addedPublisherId;

    private PublishersWithAddedPublisherId() {}

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public Long getAddedPublisherId() {
        return addedPublisherId;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private List<Publisher> publishers;
        private Long addedPublisherId;

        private Builder() {}

        public PublishersWithAddedPublisherId build() {
            PublishersWithAddedPublisherId bean = new PublishersWithAddedPublisherId();
            bean.publishers = publishers;
            bean.addedPublisherId = addedPublisherId;
            return bean;
        }

        public Builder publishers(List<Publisher> publishers) {
            this.publishers = publishers;
            return this;
        }

        public Builder addedPublisherId(Long addedPublisherId) {
            this.addedPublisherId = addedPublisherId;
            return this;
        }

    }

}
