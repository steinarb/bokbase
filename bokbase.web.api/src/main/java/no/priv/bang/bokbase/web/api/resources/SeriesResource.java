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

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Series;
import no.priv.bang.bokbase.services.beans.SeriesWithAddedSeriesId;

@Path("series")
@Produces(MediaType.APPLICATION_JSON)
public class SeriesResource {

    @Inject
    BokbaseService bokbase;

    @GET
    public List<Series> getSeries() {
        return bokbase.listSeries();
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public SeriesWithAddedSeriesId addSeries(Series seriesToAdd) {
        return bokbase.addSeries(seriesToAdd);
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Series> updateSeries(Series seriesToModify) {
        return bokbase.updateSeries(seriesToModify);
    }

    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Series> removeSeries(Series seriesToModify) {
        return bokbase.removeSeries(seriesToModify);
    }

}
