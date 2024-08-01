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
package com.alipay.sofa.security.biz1.service;

import com.alipay.sofa.koupleless.common.api.SpringBeanFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return SpringBeanFinder.getBaseBean(SecurityFilterChain.class);
        //        http.authorizeHttpRequests((requests) -> requests.requestMatchers().permitAll()
        //                        .anyRequest().authenticated())
        //                .formLogin((form) -> form.loginPage("/login").permitAll())
        //                .logout((logout) -> logout.permitAll());
        //
        //        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //        UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password")
        //            .roles("USER").build();
        //
        //        return new InMemoryUserDetailsManager(user);
        return SpringBeanFinder.getBaseBean(UserDetailsService.class);
    }
}
