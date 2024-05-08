package com.alipay.sofa.koupleless.integration;

import com.alipay.sofa.config.nacos.BaseNacosApplication;
import com.alipay.sofa.config.nacos.Biz1NacosApplication;
import com.alipay.sofa.config.nacos.Biz2NacosApplication;
import com.alipay.sofa.koupleless.test.suite.spring.model.BaseSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.BizSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.model.MultiSpringTestConfig;
import com.alipay.sofa.koupleless.test.suite.spring.multi.TestMultiSpringApplication;
import com.google.common.collect.Lists;
import org.junit.BeforeClass;
import org.junit.Test;

public class NacosIntegrationTest {
    @BeforeClass
    public static void setUp() {
        TestMultiSpringApplication multiApp = new TestMultiSpringApplication(
                MultiSpringTestConfig
                        .builder()
                        .baseConfig(BaseSpringTestConfig
                                .builder()
                                .mainClass(BaseNacosApplication.class)
                                .build())
                        .bizConfigs(Lists.newArrayList(
                                BizSpringTestConfig.builder()
                                        .bizName("biz1")
                                        .mainClass(Biz1NacosApplication.class)
                                        .includeArtifactIds(Lists.newArrayList("nacos-config-spring-boot-starter"))
                                        .build(),

                                BizSpringTestConfig.builder()
                                        .bizName("biz2")
                                        .mainClass(Biz2NacosApplication.class)
                                        .includeArtifactIds(Lists.newArrayList("nacos-config-spring-boot-starter"))
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
