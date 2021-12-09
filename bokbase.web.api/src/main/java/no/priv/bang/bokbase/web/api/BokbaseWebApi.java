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
package no.priv.bang.bokbase.web.api;

import static org.osgi.service.http.whiteboard.HttpWhiteboardConstants.*;

import javax.servlet.Servlet;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardContextSelect;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletName;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;
import org.osgi.service.log.LogService;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.osgiservice.users.UserManagementService;
import no.priv.bang.servlet.jersey.JerseyServlet;


@Component(service=Servlet.class, immediate=true)
@HttpWhiteboardContextSelect("(" + HTTP_WHITEBOARD_CONTEXT_NAME + "=bokbase)")
@HttpWhiteboardServletName("bokbaseapi")
@HttpWhiteboardServletPattern("/api/*")
public class BokbaseWebApi extends JerseyServlet {
    private static final long serialVersionUID = 3391345571152153990L; // NOSONAR

    @Override
    @Reference
    public void setLogService(LogService logService) {
        super.setLogService(logService);
    }

    @Reference
    public void setBokbaseService(BokbaseService bokbase) {
        addInjectedOsgiService(BokbaseService.class, bokbase);
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        addInjectedOsgiService(UserManagementService.class, useradmin);
    }

    @Activate
    public void activate() {
        // This method is called after all injections have been satisfied
    }
}
