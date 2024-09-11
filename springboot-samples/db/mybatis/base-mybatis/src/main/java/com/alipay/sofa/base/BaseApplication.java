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
package com.alipay.sofa.base;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource({ "classpath*:META-INF/spring/service.xml" })
@SpringBootApplication
@MapperScan(basePackages = "com.alipay.sofa.base.mapper")
public class BaseApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(BaseApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
        LOGGER.info("BaseApplication start!");
        LOGGER.info("Spring Boot Version: "
                    + SpringApplication.class.getPackage().getImplementationVersion());
        LOGGER.info("BaseApplication classLoader: " + BaseApplication.class.getClassLoader());
    }
}
