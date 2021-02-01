/*
 * Copyright 2019-Present Okta, Inc.
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
package com.okta.commons.http.httpclient

import com.okta.commons.http.config.HttpClientConfiguration
import org.apache.http.HttpEntity
import org.testng.Assert
import org.testng.IHookCallBack
import org.testng.IHookable
import org.testng.ITestResult
import org.testng.SkipException
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.is

class HttpClientRequestExecutorStaticConfigTest implements IHookable {

    @Test(dataProvider = "validateAfterInactivity")
    void validateAfterInactivityIsEmpty(String configValue, int expectedValue) {
        def prop = "com.okta.sdk.impl.http.httpclient.HttpClientRequestExecutor.connPoolControl.validateAfterInactivity"
        if (configValue != null) {
            System.properties.setProperty(prop, configValue)
        }
        def requestExecutor = loadHttpClientRequestExecutor()
        assertThat requestExecutor.getMaxConnectionInactivity(), is(expectedValue)
    }

    @Test(dataProvider = "timeToLive")
    void timeToLive(String configValue, int expectedValue) {
        def prop = "com.okta.sdk.impl.http.httpclient.HttpClientRequestExecutor.connPoolControl.timeToLive"
        if (configValue != null) {
            System.properties.setProperty(prop, configValue)
        }
        def requestExecutor = loadHttpClientRequestExecutor()
        assertThat requestExecutor.getConnectionTimeToLive(), is(expectedValue)
    }

    @Test(dataProvider = "maxConnections")
    void maxConnections(String configValue, int expectedValue) {
        def prop = "com.okta.sdk.impl.http.httpclient.HttpClientRequestExecutor.connPoolControl.maxTotal"
        if (configValue != null) {
            System.properties.setProperty(prop, configValue)
        }
        def requestExecutor = loadHttpClientRequestExecutor()
        assertThat requestExecutor.getMaxConnectionTotal(), is(expectedValue)
    }

    @Test(dataProvider = "maxConnectionsPerRoute")
    void maxConnectionsPerRoute(String configValue, int expectedValue) {
        def prop = "com.okta.sdk.impl.http.httpclient.HttpClientRequestExecutor.connPoolControl.maxPerRoute"
        if (configValue != null) {
            System.properties.setProperty(prop, configValue)
        }
        if(configValue == "0" || configValue == "-1") {
            def exception = expect(IllegalArgumentException, {loadHttpClientRequestExecutor()})
            assertThat exception.getMessage(), is("Max per route value may not be negative or zero")
        } else {
            def requestExecutor = loadHttpClientRequestExecutor()
            assertThat requestExecutor.getMaxConnectionPerRoute(), is(expectedValue)
        }
    }

    HttpClientRequestExecutor loadHttpClientRequestExecutor() {
        def e = new HttpClientRequestExecutor(new HttpClientConfiguration()) {
            @Override
            protected byte[] toBytes(HttpEntity he) throws IOException {
                return null
            }
        }
        return e
    }

    @DataProvider
    Object[][] validateAfterInactivity() {
        return standardConfigTests(2000)
    }

    @DataProvider
    Object[][] maxConnectionsPerRoute() {
        return standardConfigTests(Integer.MAX_VALUE/2 as int)
    }

    @DataProvider
    Object[][] maxConnections() {
        return standardConfigTests(Integer.MAX_VALUE)
    }

    @DataProvider
    Object[][] timeToLive() {
        return standardConfigTests(300000)
    }

    static Object[][] standardConfigTests(int defaultValue) {
        return [
               ["", defaultValue],
               ["0", 0],
               ["-1", -1],
               ["😊", defaultValue],
               ["O'Doyle Rules!", defaultValue],
               ["12", 12],
               [null, defaultValue]
        ]
    }

    static <T extends Throwable> T expect(Class<T> catchMe, Closure closure) {
        try {
            closure.call()
            Assert.fail("Expected ${catchMe.getName()} to be thrown.")
        } catch(e) {
            if (!e.class.isAssignableFrom(catchMe)) {
                throw e
            }
            return e
        }
    }

    @Override
    void run(IHookCallBack callBack, ITestResult testResult) {
        def originalProperties = System.getProperties()
        Properties copy = new Properties()
        copy.putAll(originalProperties)
        System.setProperties(copy)
        try {
            // run the tests
            if (!System.getProperty("java.version").startsWith("1.8")) {
                throw new SkipException("Test test only supported on JDK 8, see https://github.com/okta/okta-sdk-java/issues/276")
            }
            callBack.runTestMethod(testResult)

        } finally {
            System.setProperties(originalProperties)
        }
    }
}