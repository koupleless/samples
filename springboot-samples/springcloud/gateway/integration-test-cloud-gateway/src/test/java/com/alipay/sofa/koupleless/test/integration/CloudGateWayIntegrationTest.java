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
package com.alipay.sofa.koupleless.test.integration;

import com.alipay.sofa.koupleless.test.suite.spring.model.BaseSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.BizSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.MultiSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.multi.TestMultiSpringApplication;
import com.alipay.sofa.springcloud.gateway.GatewayApplication;
import com.alipay.sofa.springcloud.gateway.biz1.Biz1Application;
import com.alipay.sofa.springcloud.gateway.biz2.Biz2Application;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

public class CloudGateWayIntegrationTest {
    @BeforeClass
    public static void setUp() {
        TestMultiSpringApplication multiApp = new TestMultiSpringApplication(MultiSpringTestConfig
            .builder()
            .baseConfig(BaseSpringTestConfig.builder().mainClass(GatewayApplication.class).build())
            .bizConfigs(
                Lists.newArrayList(
                    BizSpringTestConfig.builder().bizName("biz1").mainClass(Biz1Application.class)
                        .build(),
                    BizSpringTestConfig.builder().bizName("biz2").mainClass(Biz2Application.class)
                        .build())).build());

        multiApp.run();
    }

    @Test
    public void testHelloWorld() {

    }
}
