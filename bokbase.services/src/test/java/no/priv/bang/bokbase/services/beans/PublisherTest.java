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

class PublisherTest {

    @Test
    void testCreate() {
        long publisherId = 456L;
        String name = "Baen Books";
        Publisher bean = Publisher.with()
            .publisherId(publisherId)
            .name(name)
            .build();
        assertEquals(publisherId, bean.getPublisherId());
        assertEquals(name, bean.getName());
    }

    @Test
    void testCopy() {
        long publisherId = 456L;
        String name = "Baen Books";
        Publisher bean = Publisher.with()
            .publisherId(publisherId)
            .name(name)
            .build();
        Publisher copyOfBean = Publisher.with(bean).build();
        assertThat(copyOfBean).usingRecursiveComparison().isEqualTo(bean);
        assertEquals(bean, copyOfBean);
    }

}
