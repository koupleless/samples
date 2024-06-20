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
package com.alipay.sofa.rpc.dubbo3.base.controller;

import com.alipay.sofa.rpc.dubbo3.model.CommonRequest;
import com.alipay.sofa.rpc.dubbo3.model.CommonResponse;
import com.alipay.sofa.rpc.dubbo3.model.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.Constants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author syd
 * @version BaseController.java, v 0.1 2023年11月05日 16:06 syd
 */
@RestController
@RequestMapping("/base")
@Slf4j
public class BaseController {

    /**
     * 通过远程rpc调用模块triplebiz的CommonService服务
     * check = false是因为启动时还没安装模块
     */
    @DubboReference(group = "triplebiz", scope = Constants.SCOPE_LOCAL, check = false, filter = "baseFilter")
    private CommonService commonServiceLocal;

    /**
     * 通过远程rpc调用模块triplebiz的CommonService服务
     * check = false是因为启动时还没安装模块
     */
    @DubboReference(group = "triplebiz", scope = Constants.SCOPE_REMOTE, check = false, filter = "baseFilter")
    private CommonService commonServiceRemote;

    @RequestMapping(value = "/triplebiz/injvm", method = RequestMethod.GET)
    public String localHello() {
        try {
            CommonRequest commonRequest = new CommonRequest();
            commonRequest.setName("base");
            CommonResponse response = commonServiceLocal.sayHello(commonRequest);
            log.info(response.getMessage());
            return response.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/triplebiz/remote", method = RequestMethod.GET)
    public String remoteHello() {
        try {
            CommonRequest commonRequest = new CommonRequest();
            commonRequest.setName("base");
            CommonResponse response = commonServiceRemote.sayHello(commonRequest);
            log.info(response.getMessage());
            return response.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}