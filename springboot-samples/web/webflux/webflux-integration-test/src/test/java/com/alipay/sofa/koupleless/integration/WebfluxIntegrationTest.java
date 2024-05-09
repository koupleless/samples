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
package com.alipay.sofa.koupleless.integration;

import com.alipay.sofa.koupleless.test.suite.spring.model.BaseSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.BizSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.MultiSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.multi.TestMultiSpringApplication;
import com.example.yuan.bizwebflux.BizwebfluxApplication;
import com.example.yuan.demowebflux.DemoWebfluxApplication;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebfluxIntegrationTest {

    @BeforeClass
    public static void setUp() {
        TestMultiSpringApplication multiApp = new TestMultiSpringApplication(MultiSpringTestConfig
            .builder()
            .baseConfig(
                BaseSpringTestConfig.builder().mainClass(DemoWebfluxApplication.class).build())
            .bizConfigs(
                Lists.newArrayList(BizSpringTestConfig.builder().bizName("biz")
                    .mainClass(BizwebfluxApplication.class).build())).build());
        multiApp.run();
    }

    @Test
    public void test() {
        System.out.println("hello world!");
    }
}
