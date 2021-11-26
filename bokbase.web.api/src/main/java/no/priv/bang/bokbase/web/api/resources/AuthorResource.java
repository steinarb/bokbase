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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Author;
import no.priv.bang.bokbase.services.beans.AuthorsWithAddedAuthorId;

@Path("author")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @Inject
    BokbaseService bokbase;

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public AuthorsWithAddedAuthorId addAuthor(Author author) {
        return bokbase.addAuthor(author);
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Author> updateAuthor(Author modifiedAuthor) {
        return bokbase.updateAuthor(modifiedAuthor);
    }

    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Author> removeAuthor(Author deletedAuthor) {
        return bokbase.removeAuthor(deletedAuthor);
    }

}
