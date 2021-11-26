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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Publisher;
import no.priv.bang.bokbase.services.beans.PublishersWithAddedPublisherId;

class PublisherResourceTest {

    @Test
    void testAddPublisher() {
        long addedPublisherId = 678L;
        Publisher addedPublisher = Publisher.with().publisherId(addedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addPublisher(any())).thenReturn(PublishersWithAddedPublisherId.with().addedPublisherId(addedPublisherId).publishers(Collections.singletonList(addedPublisher)).build());
        PublisherResource resource = new PublisherResource();
        resource.bokbase = bokbase;

        PublishersWithAddedPublisherId publishersWithAddedPublisherId = resource.addPublisher(addedPublisher);
        assertThat(publishersWithAddedPublisherId.getPublishers()).isNotEmpty();
        assertEquals(addedPublisherId, publishersWithAddedPublisherId.getAddedPublisherId());
    }

    @Test
    void testUpdatePublisher() {
        long modifiedPublisherId = 678L;
        Publisher modifiedPublisher = Publisher.with().publisherId(modifiedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updatePublisher(any())).thenReturn(Collections.singletonList(modifiedPublisher));
        PublisherResource resource = new PublisherResource();
        resource.bokbase = bokbase;

        List<Publisher> publishersWithUpdatedPublisher = resource.updatePublisher(modifiedPublisher);
        assertThat(publishersWithUpdatedPublisher).isNotEmpty();
        assertEquals(modifiedPublisherId, publishersWithUpdatedPublisher.get(0).getPublisherId());
    }

    @Test
    void testRemovePublisher() {
        long removedPublisherId = 678L;
        Publisher publisherToRemove = Publisher.with().publisherId(removedPublisherId).name("Baen Books").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removePublisher(any())).thenReturn(Collections.singletonList(Publisher.with().name("Orbit Books").build()));
        PublisherResource resource = new PublisherResource();
        resource.bokbase = bokbase;

        List<Publisher> publishersWithRemovedPublisher = resource.removePublisher(publisherToRemove);
        assertThat(publishersWithRemovedPublisher)
            .isNotEmpty()
            .doesNotContain(publisherToRemove);
    }

}
