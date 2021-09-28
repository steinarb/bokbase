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

import static no.priv.bang.bokbase.services.BokbaseConstants.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.log.Logger;

import no.priv.bang.bokbase.services.Account;
import no.priv.bang.bokbase.services.LocaleBean;
import no.priv.bang.bokbase.services.BokbaseService;
import no.priv.bang.osgiservice.users.Role;
import no.priv.bang.osgiservice.users.User;
import no.priv.bang.osgiservice.users.UserManagementService;

@Component(service=BokbaseService.class, immediate=true, property= { "defaultlocale=nb_NO" })
public class BokbaseServiceProvider implements BokbaseService {

    private static final String DISPLAY_TEXT_RESOURCES = "i18n.Texts";
    private Logger logger;
    private DataSource datasource;
    private UserManagementService useradmin;
    private Locale defaultLocale;

    @Reference
    public void setLogservice(LogService logservice) {
        this.logger = logservice.getLogger(BokbaseServiceProvider.class);
    }

    @Reference(target = "(osgi.jndi.service.name=jdbc/bokbase)")
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Reference
    public void setUseradmin(UserManagementService useradmin) {
        this.useradmin = useradmin;
    }

    @Activate
    public void activate(Map<String, Object> config) {
        defaultLocale = Locale.forLanguageTag(((String) config.get("defaultlocale")).replace('_', '-'));
        addRolesIfNotpresent();
    }

    @Override
    public boolean lazilyCreateAccount(String username) {
        boolean exists = false;
        try(Connection connection = datasource.getConnection()) {
            try(PreparedStatement findAccount = connection.prepareStatement("select * from bokbase_accounts where username=?")) {
                findAccount.setString(1, username);
                try(ResultSet results = findAccount.executeQuery()) {
                    while (results.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                return false;
            }

            try(PreparedStatement createAccount = connection.prepareStatement("insert into bokbase_accounts (username) values (?)")) {
                createAccount.setString(1, username);
                createAccount.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            logger.warn("Failed to create bokbase account for username \"{}\"", username, e);
        }

        return false;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = datasource.getConnection()) {
            try(Statement statement = connection.createStatement()) {
                try(ResultSet results = statement.executeQuery("select * from bokbase_accounts")) {
                    while(results.next()) {
                        int accountId = results.getInt(1);
                        String username = results.getString(2);
                        User user = useradmin.getUser(username);
                        Account account = Account.with().accountId(accountId).user(user).build();
                        accounts.add(account);
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Ingen bokbase");
        }

        return accounts;
    }

    @Override
    public Locale defaultLocale() {
        return defaultLocale;
    }

    @Override
    public List<LocaleBean> availableLocales() {
        return Arrays.asList(Locale.forLanguageTag("nb-NO"), Locale.UK).stream().map(l -> LocaleBean.with().locale(l).build()).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> displayTexts(Locale locale) {
        return transformResourceBundleToMap(locale);
    }

    @Override
    public String displayText(String key, String locale) {
        Locale active = locale == null || locale.isEmpty() ? defaultLocale : Locale.forLanguageTag(locale.replace('_', '-'));
        ResourceBundle bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, active);
        return bundle.getString(key);
    }

    private void addRolesIfNotpresent() {
        String bokbaseuser = SAMPLEAPPUSER_ROLE;
        List<Role> roles = useradmin.getRoles();
        Optional<Role> existingRole = roles.stream().filter(r -> bokbaseuser.equals(r.getRolename())).findFirst();
        if (!existingRole.isPresent()) {
            useradmin.addRole(Role.with().id(-1).rolename(bokbaseuser).description("Bruker av applikasjonen bokbase").build());
        }
    }

    Map<String, String> transformResourceBundleToMap(Locale locale) {
        Map<String, String> map = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle(DISPLAY_TEXT_RESOURCES, locale);
        Enumeration<String> keys = bundle.getKeys();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, bundle.getString(key));
        }

        return map;
    }

}
