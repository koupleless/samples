/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.web.webflux.integration.test;

import com.alipay.sofa.koupleless.test.suite.spring.model.BaseSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.BizSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.MultiSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.multi.TestMultiSpringApplication;
import com.alipay.sofa.web.biz1.Biz1Application;
import com.alipay.sofa.web.biz2.Biz2Application;
import com.alipay.sofa.web.base.BaseApplication;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author CodeNoobKing
 * @date 2024/3/11
 */
@Slf4j
public class WebSingleHostTest {

    private static TestMultiSpringApplication multiApp;

    private static final OkHttpClient         client = new OkHttpClient();

    @SneakyThrows
    @BeforeClass
    public static void setUpMultiApplication() {
        multiApp = new TestMultiSpringApplication(MultiSpringTestConfig
            .builder()
            .baseConfig(BaseSpringTestConfig.builder().mainClass(BaseApplication.class).build())
            .bizConfigs(
                Lists.newArrayList(
                    BizSpringTestConfig.builder().bizName("biz1").mainClass(Biz1Application.class)
                        .build(),
                    BizSpringTestConfig.builder().bizName("biz2").mainClass(Biz2Application.class)
                        .build())).build());
        multiApp.run();
        Thread.sleep(1000);
    }

    @Test
    public void testContextWebhookPathPrefixIsAdded() throws Throwable {
        Response resp = client.newCall(
            new Request.Builder().url("http://localhost:8080/").get().build()).execute();
        String result = resp.body().string();
        log.info("result: {}", result);
        Assert.assertTrue(result.contains("hello to base deploy"));

        resp = client.newCall(
            new Request.Builder().url("http://localhost:8080/biz1/").get().build()).execute();
        result = resp.body().string();
        log.info("result: {}", result);
        Assert.assertTrue(result.contains("hello to biz1 deploy"));

        //        resp = client.newCall(
        //            new Request.Builder().url("http://localhost:8080/biz2/").get().build()).execute();
        //        result = resp.body().string();
        //        log.info("result: {}", result);
        //        Assert.assertTrue(result.contains("hello to biz2 deploy"));
    }
}
