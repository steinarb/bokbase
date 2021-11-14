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
package no.priv.bang.bokbase.backend;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import no.priv.bang.bokbase.db.liquibase.test.BokbaseTestDbLiquibaseRunner;
import no.priv.bang.bokbase.services.beans.Account;
import no.priv.bang.bokbase.services.beans.LocaleBean;
import no.priv.bang.osgi.service.mocks.logservice.MockLogService;
import no.priv.bang.osgiservice.users.UserManagementService;

class BokbaseServiceProviderTest {
    private final static Locale NB_NO = Locale.forLanguageTag("nb-no");

    private static DataSource datasource;

    @BeforeAll
    static void commonSetupForAllTests() throws Exception {
        DataSourceFactory derbyDataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:bokbase;create=true");
        datasource = derbyDataSourceFactory.createDataSource(properties);
        MockLogService logservice = new MockLogService();
        BokbaseTestDbLiquibaseRunner runner = new BokbaseTestDbLiquibaseRunner();
        runner.setLogService(logservice);
        runner.activate();
        runner.prepare(datasource);
    }

    @Test
    void testGetAccounts() {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        BokbaseServiceProvider provider = new BokbaseServiceProvider();
        provider.setLogservice(logservice);
        provider.setDatasource(datasource);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        List<Account> accountsBefore = provider.getAccounts();
        assertThat(accountsBefore).isEmpty();
        boolean accountCreated = provider.lazilyCreateAccount("jad");
        assertTrue(accountCreated);
        List<Account> accountsAfter = provider.getAccounts();
        assertThat(accountsAfter).isNotEmpty();
        boolean secondAccountCreate = provider.lazilyCreateAccount("jad");
        assertFalse(secondAccountCreate);
        List<Account> accountsAfterSecondCreate = provider.getAccounts();
        assertThat(accountsAfterSecondCreate).isEqualTo(accountsAfter);
    }

    @Test
    void testLazilyCreateAccountWithSQLException() throws Exception {
        MockLogService logservice = new MockLogService();
        UserManagementService useradmin = mock(UserManagementService.class);
        BokbaseServiceProvider provider = new BokbaseServiceProvider();
        DataSource datasourceThrowsException = mock(DataSource.class);
        when(datasourceThrowsException.getConnection()).thenThrow(SQLException.class);
        provider.setLogservice(logservice);
        provider.setDatasource(datasourceThrowsException);
        provider.setUseradmin(useradmin);
        provider.activate(Collections.singletonMap("defaultlocale", "nb_NO"));

        assertThat(logservice.getLogmessages()).isEmpty();
        boolean accountCreated = provider.lazilyCreateAccount("jad");
        assertFalse(accountCreated);
        assertThat(logservice.getLogmessages()).isNotEmpty();
    }

    @Test
    void testDefaultLocale() {
        BokbaseServiceProvider ukelonn = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        assertEquals(NB_NO, ukelonn.defaultLocale());
    }

    @Test
    void testAvailableLocales() {
        BokbaseServiceProvider ukelonn = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        List<LocaleBean> locales = ukelonn.availableLocales();
        assertThat(locales).isNotEmpty().contains(LocaleBean.with().locale(ukelonn.defaultLocale()).build());
    }

    @Test
    void testDisplayTextsForDefaultLocale() {
        BokbaseServiceProvider ukelonn = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        Map<String, String> displayTexts = ukelonn.displayTexts(ukelonn.defaultLocale());
        assertThat(displayTexts).isNotEmpty();
    }

    @Test
    void testDisplayText() {
        BokbaseServiceProvider ukelonn = new BokbaseServiceProvider();
        UserManagementService useradmin = mock(UserManagementService.class);
        ukelonn.setUseradmin(useradmin);
        ukelonn.activate(Collections.singletonMap("defaultlocale", "nb_NO"));
        String text1 = ukelonn.displayText("hi", "nb_NO");
        assertEquals("Hei", text1);
        String text2 = ukelonn.displayText("hi", "en_GB");
        assertEquals("Hi", text2);
        String text3 = ukelonn.displayText("hi", "");
        assertEquals("Hei", text3);
        String text4 = ukelonn.displayText("hi", null);
        assertEquals("Hei", text4);
    }

}
