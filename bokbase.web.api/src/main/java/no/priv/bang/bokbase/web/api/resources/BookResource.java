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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Book;
import no.priv.bang.bokbase.services.beans.BooksWithAddedBookId;

@Path("book")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    public BokbaseService bokbase;

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public BooksWithAddedBookId addBook(Book newBook) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        return bokbase.addBook(username, newBook);
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Book> updateBook(Book updatedBook) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        return bokbase.updateBook(username, updatedBook);
    }

    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Book> removeBook(Book bookToRemove) {
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        return bokbase.removeBook(username, bookToRemove);
    }

}
