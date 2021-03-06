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
package no.priv.bang.bokbase.db.liquibase.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.ops4j.pax.jdbc.derby.impl.DerbyDataSourceFactory;
import org.osgi.service.jdbc.DataSourceFactory;

import no.priv.bang.osgi.service.mocks.logservice.MockLogService;

class BokbaseTestDbLiquibaseRunnerTest {

    @Test
    void testCreateAndVerifySomeDataInSomeTables() throws Exception {
        DataSourceFactory dataSourceFactory = new DerbyDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty(DataSourceFactory.JDBC_URL, "jdbc:derby:memory:bokbase;create=true");
        DataSource datasource = dataSourceFactory.createDataSource(properties);

        MockLogService logservice = new MockLogService();
        BokbaseTestDbLiquibaseRunner runner = new BokbaseTestDbLiquibaseRunner();
        runner.setLogService(logservice);
        runner.activate();
        runner.prepare(datasource);
        assertAccounts(datasource);
        assertBooks(datasource);
        assertBookRatings(datasource);
        assertBooksWithRatings(datasource, "jad");
    }

    private void assertAccounts(DataSource datasource) throws Exception {
        int resultcount = 0;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from bokbase_accounts")) {
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        ++resultcount;
                    }
                }
            }
        }
        assertNotEquals(0, resultcount);
        assertThat(resultcount).isPositive();
    }

    private void assertBooks(DataSource datasource) throws Exception {
        int resultcount = 0;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from books")) {
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        ++resultcount;
                    }
                }
            }
        }
        assertThat(resultcount).isPositive();
    }

    private void assertBookRatings(DataSource datasource) throws Exception {
        int resultcount = 0;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from book_ratings")) {
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        ++resultcount;
                    }
                }
            }
        }
        assertThat(resultcount).isPositive();
    }

    private void assertBooksWithRatings(DataSource datasource, String username) throws Exception {
        int resultcount = 0;
        try (Connection connection = datasource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("select * from books b left join series e on b.series_id=e.series_id left join authors a on b.author_id=a.author_id left join publishers p on p.publisher_id=b.publisher_id join bookshelves s on b.book_id=s.book_id join bokbase_accounts c on s.account_id=c.account_id join book_ratings r on b.book_id=r.book_id and c.account_id=r.account_id where c.username=?")) {
                statement.setString(1, username);
                try (ResultSet results = statement.executeQuery()) {
                    while (results.next()) {
                        ++resultcount;
                    }
                }
            }
        }
        assertThat(resultcount).isPositive();
    }

}
