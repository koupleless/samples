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
package com.alipay.sofa.biz3;

import com.alipay.sofa.biz3.service.Biz3AppServiceImpl;
import com.alipay.sofa.biz3.service.Biz3OtherAppServiceImpl;
import com.alipay.sofa.koupleless.common.MainApplication;

/**
 * @author lianglipeng.llp@alibaba-inc.com
 * @version $Id: Biz3Application.java, v 0.1 2024年09月11日 16:01 立蓬 Exp $
 */
public class Biz3Application {
    public static void main(String[] args) {
        // 初始化模块的实例容器
        MainApplication.init();

        // 注册实例到模块容器中
        MainApplication.register("biz3AppServiceImpl", new Biz3AppServiceImpl());
        MainApplication.register("biz3OtherAppServiceImpl", new Biz3OtherAppServiceImpl());

        System.out.println("Biz3 start!");
        System.out.println("Biz classLoader: " + Biz3Application.class.getClassLoader());
    }
}