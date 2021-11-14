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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ServerProperties;
import org.junit.jupiter.api.Test;
import org.osgi.service.log.LogService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletOutputStream;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.Credentials;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.bokbase.web.api.resources.ErrorMessage;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class BokbaseWebApiTest extends ShiroTestBase {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");
    private final static Locale EN_UK = Locale.forLanguageTag("en-uk");

    public static final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();

    @Test
    void testLogin() throws Exception {
        String username = "jd";
        String password = "johnnyBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());

    }

    @Test
    void testLoginWrongPassword() throws Exception {
        String username = "jd";
        String password = "johnniBoi";
        Credentials credentials = Credentials.with().username(username).password(password).build();
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        createSubjectAndBindItToThread();
        MockHttpServletRequest request = buildPostUrl("/login");
        String postBody = mapper.writeValueAsString(credentials);
        request.setBodyContent(postBody);
        MockHttpServletResponse response = new MockHttpServletResponse();

        servlet.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetAccounts() throws Exception {
        MockLogService logservice = new MockLogService();
        BokbaseService bokbase = mock(BokbaseService.class);
        Account account = Account.with().accountId(123).build();
        when(bokbase.getAccounts()).thenReturn(Collections.singletonList(account));
        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);
        MockHttpServletRequest request = buildGetUrl("/accounts");
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginUser(request, response, "jd", "johnnyBoi");
        servlet.service(request, response);
        assertEquals(200, response.getStatus());
        List<Account> accounts = mapper.readValue(getBinaryContent(response), new TypeReference<List<Account>>() {});
        assertThat(accounts).isNotEmpty();
    }
    @Test
    void testDefaultLocale() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.defaultLocale()).thenReturn(NB_NO);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/defaultlocale");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Locale defaultLocale = mapper.readValue(getBinaryContent(response), Locale.class);
        assertEquals(NB_NO, defaultLocale);
    }
    @Test
    void testAvailableLocales() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.availableLocales()).thenReturn(Collections.singletonList(Locale.forLanguageTag("nb-NO")).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList()));
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/availablelocales");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        List<LocaleBean> availableLocales = mapper.readValue(getBinaryContent(response), new TypeReference<List<LocaleBean>>() {});
        assertThat(availableLocales).isNotEmpty().contains(LocaleBean.with().locale(Locale.forLanguageTag("nb-NO")).build());
    }

    @Test
    void testDisplayTexts() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(bokbase.displayTexts(NB_NO)).thenReturn(texts);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=nb_NO");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        Map<String, String> displayTexts = mapper.readValue(getBinaryContent(response), new TypeReference<Map<String, String>>() {});
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayTextsWithUnknownLocale() throws Exception {
        // Set up REST API servlet with mocked services
        BokbaseService bokbase = mock(BokbaseService.class);
        Map<String, String> texts = new HashMap<>();
        texts.put("date", "Dato");
        when(bokbase.displayTexts(EN_UK)).thenThrow(MissingResourceException.class);
        MockLogService logservice = new MockLogService();

        BokbaseWebApi servlet = simulateDSComponentActivationAndWebWhiteboardConfiguration(bokbase , logservice);

        // Create the request and response
        MockHttpServletRequest request = buildGetUrl("/displaytexts");
        request.setQueryString("locale=en_UK");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Run the method under test
        servlet.service(request, response);

        // Check the response
        assertEquals(500, response.getStatus());
        assertEquals("application/json", response.getContentType());
        ErrorMessage errorMessage = mapper.readValue(getBinaryContent(response), ErrorMessage.class);
        assertEquals(500, errorMessage.getStatus());
        assertThat(errorMessage.getMessage()).startsWith("Unknown locale");
    }

    private byte[] getBinaryContent(MockHttpServletResponse response) throws IOException {
        MockServletOutputStream outputstream = (MockServletOutputStream) response.getOutputStream();
        return outputstream.getBinaryContent();
    }

    private MockHttpServletRequest buildGetUrl(String resource) {
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("GET");
        return request;
    }

    private MockHttpServletRequest buildPostUrl(String resource) {
        String contenttype = MediaType.APPLICATION_JSON;
        MockHttpServletRequest request = buildRequest(resource);
        request.setMethod("POST");
        request.setContentType(contenttype);
        request.addHeader("Content-Type", contenttype);
        return request;
    }

    private MockHttpServletRequest buildRequest(String resource) {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setProtocol("HTTP/1.1");
        request.setRequestURL("http://localhost:8181/bokbase/api" + resource);
        request.setRequestURI("/bokbase/api" + resource);
        request.setContextPath("/bokbase");
        request.setServletPath("/api");
        request.setSession(session);
        return request;
    }

    private BokbaseWebApi simulateDSComponentActivationAndWebWhiteboardConfiguration(BokbaseService bokbase, LogService logservice) throws Exception {
        BokbaseWebApi servlet = new BokbaseWebApi();
        servlet.setLogService(logservice);
        servlet.setSampleappService(bokbase);
        servlet.activate();
        ServletConfig config = createServletConfigWithApplicationAndPackagenameForJerseyResources();
        servlet.init(config);
        return servlet;
    }

    private ServletConfig createServletConfigWithApplicationAndPackagenameForJerseyResources() {
        ServletConfig config = mock(ServletConfig.class);
        when(config.getInitParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(ServerProperties.PROVIDER_PACKAGES)));
        when(config.getInitParameter(eq(ServerProperties.PROVIDER_PACKAGES))).thenReturn("no.priv.bang.bokbase.web.api.resources");
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getContextPath()).thenReturn("/bokbase");
        when(config.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttributeNames()).thenReturn(Collections.emptyEnumeration());
        return config;
    }
}
