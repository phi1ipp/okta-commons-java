/*
 * Copyright 2021-Present Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.commons.http.config;

public interface ClientConfiguration {

    String MAX_CONNECTIONS_PER_ROUTE_PROPERTY_NAME = "okta.client.httpclient.maxPerRoute";
    String MAX_CONNECTIONS_TOTAL_PROPERTY_NAME = "okta.client.httpclient.maxTotal";
    String CONNECTION_VALIDATION_INACTIVITY_PROPERTY_NAME = "okta.client.httpclient.validateAfterInactivity";
    String CONNECTION_TIME_TO_LIVE_PROPERTY_NAME = "okta.client.httpclient.timeToLive";

    int MAX_CONNECTIONS_PER_ROUTE_PROPERTY_VALUE_DEFAULT = Integer.MAX_VALUE/2;
    int MAX_CONNECTIONS_TOTAL_PROPERTY_VALUE_DEFAULT = Integer.MAX_VALUE;
    int CONNECTION_VALIDATION_INACTIVITY_PROPERTY_VALUE_DEFAULT = 2000; // 2sec
    int CONNECTION_TIME_TO_LIVE_PROPERTY_VALUE_DEFAULT = 5 * 1000 * 60; // 5 minutes

    String MAX_CONNECTIONS_PER_ROUTE_WARNING_MESSAGE = "Bad max connection per route value";
    String MAX_CONNECTIONS_TOTAL_WARNING_MESSAGE = "Bad max connection total value";
    String CONNECTION_VALIDATION_INACTIVITY_WARNING_MESSAGE = "Invalid max connection inactivity validation value";
    String CONNECTION_TIME_TO_LIVE_WARNING_MESSAGE = "Invalid connection time to live value";
}
