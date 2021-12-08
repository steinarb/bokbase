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
package no.priv.bang.bokbase.services.beans;

public class ErrorMessage {

    private String message;

    private ErrorMessage() { }

    public String getMessage() {
        return message;
    }

    public static Builder with() {
        return new Builder();
    }

    public static class Builder {

        private String message;

        private Builder() { }

        public ErrorMessage build() {
            ErrorMessage bean = new ErrorMessage();
            bean.message = this.message;
            return bean;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

    }

}
