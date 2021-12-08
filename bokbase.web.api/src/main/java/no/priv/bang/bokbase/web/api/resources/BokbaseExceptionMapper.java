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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import no.priv.bang.bokbase.services.BokbaseException;
import no.priv.bang.bokbase.services.beans.ErrorMessage;

@Provider
public class BokbaseExceptionMapper implements ExceptionMapper<BokbaseException> {
    @Override
    public Response toResponse(BokbaseException exception) {
        return Response
            .status(500)
            .entity(ErrorMessage.with().message(exception.getMessage()).build())
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

}
