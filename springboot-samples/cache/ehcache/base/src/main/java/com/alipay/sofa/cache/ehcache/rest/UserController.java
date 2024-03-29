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
package com.alipay.sofa.cache.ehcache.rest;

import net.sf.ehcache.management.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@CacheConfig(cacheNames = "base")
@RestController
public class UserController {

    private Logger             LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Cacheable(key = "#id")
    @PostMapping("/getUserById")
    public String getUserById(@RequestParam String id) {
        String value = "user_" + applicationContext.getId() + "_" + id;
        LOGGER.info("add user into cache: {\"id\": {}, \"value\": {}}", id, value);
        return value;
    }

    @PostConstruct
    public void registerMBean() {
        EhCacheCacheManager ehCacheManager = (EhCacheCacheManager) applicationContext
            .getBean("baseEhcacheCacheManager");
        LOGGER.info("got ehCacheManager: {}", ehCacheManager);
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ManagementService.registerMBeans(ehCacheManager.getCacheManager(), mBeanServer, true, true,
            true, true);
    }
}
