package com.alipay.sofa.koupleless.integration;

import com.alipay.sofa.koupleless.test.suite.spring.model.BaseSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.BizSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.MultiSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.multi.TestMultiSpringApplication;
import com.alipay.sofa.web.base.BaseApplication;
import com.alipay.sofa.web.biz1.Biz1Application;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogbackIntegrationTest {

    @BeforeClass
    public static void setUp() {
        TestMultiSpringApplication multiApp = new TestMultiSpringApplication(
                MultiSpringTestConfig
                        .builder()
                        .baseConfig(BaseSpringTestConfig
                                .builder()
                                .mainClass(BaseApplication.class)
                                .build())
                        .bizConfigs(Lists.newArrayList(
                                BizSpringTestConfig
                                        .builder()
                                        .bizName("biz1")
                                        .mainClass(Biz1Application.class)
                                        .build()
                        ))
                        .build()
        );

        multiApp.run();
    }

    @Test
    public void test() {
        System.out.println("hello world");
    }
}
