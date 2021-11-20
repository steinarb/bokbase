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

class SeriesTest {

    @Test
    void testCreate() {
        long seriesId = 579L;
        String name = "Vorkosigan";
        Series bean = Series.with()
            .seriesId(seriesId)
            .name(name)
            .build();
        assertEquals(seriesId, bean.getSeriesId());
        assertEquals(name, bean.getName());
        assertThat(bean.toString()).startsWith("Series ");
    }

    @Test
    void testCopy() {
        Series bean = Series.with()
            .seriesId(579L)
            .name("Vorkosigan")
            .build();
        Series copyOfBean = Series.with(bean).build();
        assertThat(copyOfBean).usingRecursiveComparison().isEqualTo(bean);
        assertEquals(bean, copyOfBean);
    }

}
