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
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;

class SeriesResourceTest {

    @Test
    void testGetSeries() {
        long seriesId = 978L;
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.listSeries()).thenReturn(Collections.singletonList(Series.with().seriesId(seriesId).build()));
        SeriesResource resource = new SeriesResource();
        resource.bokbase = bokbase;

        List<Series> series = resource.getSeries();
        assertThat(series).isNotEmpty();
        assertEquals(seriesId, series.get(0).getSeriesId());
    }

    @Test
    void testAddSeries() {
        long seriesId = 978L;
        Series seriesToAdd = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.addSeries(any())).thenReturn(SeriesWithAddedSeriesId.with().series(Collections.singletonList(seriesToAdd)).addedSeriesId(seriesId).build());
        SeriesResource resource = new SeriesResource();
        resource.bokbase = bokbase;

        SeriesWithAddedSeriesId seriesWithAddedSeriesId = resource.addSeries(seriesToAdd);
        assertThat(seriesWithAddedSeriesId.getSeries()).contains(seriesToAdd);
    }

    @Test
    void testUpdateSeries() {
        long seriesId = 978L;
        Series seriesToModify = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.updateSeries(any())).thenReturn(Collections.singletonList(seriesToModify));
        SeriesResource resource = new SeriesResource();
        resource.bokbase = bokbase;

        List<Series> seriesWithModifiedSeries = resource.updateSeries(seriesToModify);
        assertThat(seriesWithModifiedSeries).contains(seriesToModify);
    }

    @Test
    void testRemoveSeries() {
        long seriesId = 978L;
        Series seriesToRemove = Series.with().seriesId(seriesId).name("Palladium Wars").build();
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.removeSeries(any())).thenReturn(Collections.singletonList(Series.with().build()));
        SeriesResource resource = new SeriesResource();
        resource.bokbase = bokbase;

        List<Series> seriesWithoutRemovedSeries = resource.removeSeries(seriesToRemove);
        assertThat(seriesWithoutRemovedSeries)
            .isNotEmpty()
            .doesNotContain(seriesToRemove);
    }

}
