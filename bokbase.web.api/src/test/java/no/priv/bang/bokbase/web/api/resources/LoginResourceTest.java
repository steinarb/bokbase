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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.util.WebUtils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.mockrunner.mock.web.MockHttpServletRequest;

import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.bokbase.services.beans.Credentials;
import no.priv.bang.bokbase.services.beans.Loginresult;
import no.priv.bang.bokbase.web.api.ShiroTestBase;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class LoginResourceTest extends ShiroTestBase {

    @Test
    void testLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BokbaseService bokbase = mock(BokbaseService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.bokbase = bokbase;
        String username = "jd";
        String password = "johnnyBoi";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuksess());
        assertTrue(result.isAuthorized());
        assertNull(result.getOriginalRequestUrl());
    }

    @Test
    void testLoginByUserWithoutRole() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BokbaseService bokbase = mock(BokbaseService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.bokbase = bokbase;
        String username = "jad";
        String password = "1ad";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuksess());
        assertFalse(result.isAuthorized());
    }

    @Test
    void testLoginWithOriginalRequestUrl() {
        MockHttpServletRequest request = new MockHttpServletRequest()
            .setContextPath("/bokbase");
        BokbaseService bokbase = mock(BokbaseService.class);
        LoginResource resource = new LoginResource();
        resource.request = request;
        resource.bokbase = bokbase;
        String username = "jd";
        String password = "johnnyBoi";
        MockHttpServletRequest originalRequest = new MockHttpServletRequest();
        originalRequest.setRequestURI("/bokbase/");
        createSubjectFromOriginalRequestAndBindItToThread(originalRequest);
        WebUtils.saveRequest(originalRequest);
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult result = resource.login(locale, credentials);
        assertTrue(result.getSuksess());
        assertTrue(result.isAuthorized());
        assertEquals("/", result.getOriginalRequestUrl());
    }

    @Test
    void testLoginFeilPassord() {
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Feil passord");
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        resource.setLogservice(logservice);
        String username = "jd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult resultat = resource.login(locale, credentials);
        assertFalse(resultat.getSuksess());
        assertThat(resultat.getFeilmelding()).startsWith("Feil passord");
    }

    @Test
    void testLoginUkjentBrukernavn() {
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Ukjent konto");
        MockLogService logservice = new MockLogService();
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        resource.setLogservice(logservice);
        String username = "jdd";
        String password = "feil";
        createSubjectAndBindItToThread();
        Credentials credentials = Credentials.with().username(username).password(password).build();
        String locale = "nb_NO";
        Loginresult resultat = resource.login(locale, credentials);
        assertThat(resultat.getFeilmelding()).startsWith("Ukjent konto");
    }

    @Test
    void testLogout() {
        String locale = "nb_NO";
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Logget ut");
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);
        assertTrue(subject.isAuthenticated()); // Verify precondition user logged in

        Loginresult loginresultat = resource.logout(locale);
        assertFalse(loginresultat.getSuksess());
        assertEquals("Logget ut", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
        assertFalse(subject.isAuthenticated()); // Verify user has been logged out
    }

    @Test
    void testGetLoginstateWhenLoggedIn() {
        String locale = "nb_NO";
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Bruker er logget inn og har tilgang");
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        String username = "jd";
        String password = "johnnyBoi";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresultat = resource.loginstate(locale);
        assertTrue(loginresultat.getSuksess());
        assertEquals("Bruker er logget inn og har tilgang", loginresultat.getFeilmelding());
        assertTrue(loginresultat.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenLoggedInButUserDoesntHaveRoleBokbaseuser() {
        String locale = "nb_NO";
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Bruker er logget inn men mangler tilgang");
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        String username = "jad";
        String password = "1ad";
        WebSubject subject = createSubjectAndBindItToThread();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), true);
        subject.login(token);

        Loginresult loginresultat = resource.loginstate(locale);
        assertTrue(loginresultat.getSuksess());
        assertEquals("Bruker er logget inn men mangler tilgang", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
    }

    @Test
    void testGetLoginstateWhenNotLoggedIn() {
        String locale = "nb_NO";
        BokbaseService bokbase = mock(BokbaseService.class);
        when(bokbase.displayText(anyString(), anyString())).thenReturn("Bruker er ikke logget inn");
        LoginResource resource = new LoginResource();
        resource.bokbase = bokbase;
        createSubjectAndBindItToThread();

        Loginresult loginresultat = resource.loginstate(locale);
        assertFalse(loginresultat.getSuksess());
        assertEquals("Bruker er ikke logget inn", loginresultat.getFeilmelding());
        assertFalse(loginresultat.isAuthorized());
    }

}
