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
package com.alipay.sofa.aop.base.common.conf;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

@Slf4j
@Aspect
@Service
public class LogAroundInterceptor implements MethodInterceptor {

    // 需要配合 advisor 使用
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("开始执行 in method invocation.");
        LogAround anno = methodInvocation.getMethod().getAnnotation(LogAround.class);
        if (anno != null) {
            log.info("注解值为:{}", anno.value());
        }
        Object o = methodInvocation.proceed();
        log.info("执行结束 in method invocation.");
        return o;
    }

    @Pointcut("@annotation(com.alipay.sofa.aop.base.common.conf.LogAround)")
    public void pointcut() {
    }

    // 模块扫描到 bean 即可执行
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("开始执行 in around");
        Object o = joinPoint.proceed();
        log.info("执行结束 in around");
        return o;
    }
}
