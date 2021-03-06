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
package no.priv.bang.bokbase.services;

public class BokbaseConstants {
    public static final String BOKBASE_JDBC_URL = "bokbase.db.jdbc.url";
    public static final String BOKBASE_JDBC_USER = "bokbase.db.jdbc.user";
    public static final String BOKBASE_JDBC_PASSWORD = "bokbase.db.jdbc.password"; // NOSONAR hard to write code to handle passwords without saying the word "password"

    public static final String BOKBASEUSER_ROLE = "bokbaseuser";

    private BokbaseConstants() {}
}
