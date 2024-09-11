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

import com.alipay.sofa.base.mapper.UserMapper;
import com.alipay.sofa.base.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

/**
 * @author: yuanyuan
 * @date: 2023/10/18 5:34 下午
 */
@SpringBootTest
@Slf4j
@Sql(scripts = "/create-table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MybatisTest {

    static {
        System.setProperty("sofa.ark.embed.enable", "true");
        System.setProperty("sofa.ark.plugin.export.class.enable", "true");
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        userMapper.insert(User.builder().age(18).name("沉默王二").password("123456").build());
        userMapper.insert(User.builder().age(18).name("沉默王三").password("123456").build());
        userMapper.insert(User.builder().age(18).name("沉默王四").password("123456").build());
        log.info("查询所有：{}", userMapper.getAll());
    }

    @Test
    public void testQuery() {
        List<User> all = userMapper.getAll();
        log.info("查询所有：{}", all);
    }

    @Test
    public void testUpdate() {
        List<User> all = userMapper.getAll();
        if (all == null || all.isEmpty()) {
            return;
        }
        User one = userMapper.getOne(all.get(0).getId());
        log.info("更新前{}", one);
        one.setPassword("654321");
        userMapper.update(one);
        log.info("更新后{}", userMapper.getOne(all.get(0).getId()));
    }

    @Test
    public void testDelete() {
        List<User> all = userMapper.getAll();
        if (all == null || all.isEmpty()) {
            return;
        }
        log.info("删除前{}", userMapper.getAll().toArray());
        userMapper.delete(all.get(0).getId());
        log.info("删除后{}", userMapper.getAll().toArray());
    }
}
