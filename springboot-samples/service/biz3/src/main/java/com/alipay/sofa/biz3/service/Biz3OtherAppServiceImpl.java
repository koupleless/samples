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
package com.alipay.sofa.biz3.service;

import com.alipay.sofa.base.facade.AppService;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;

/**
 * @author lianglipeng.llp@alibaba-inc.com
 * @version $Id: Biz3OtherAppServiceImpl.java, v 0.1 2024年09月11日 16:25 立蓬 Exp $
 */
public class Biz3OtherAppServiceImpl implements AppService {
    // 获取基座的bean
    private AppService baseAppService = SpringServiceFinder.getBaseService(AppService.class);

    @Override
    public String getAppName() {
        return "biz3OtherAppServiceImpl in the base: " + baseAppService.getAppName();
    }
}