/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
 */
package com.onbelay.dagnabit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/graphNodes/**").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/api/graphNodes/**").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/api/graphNodes/**").hasRole("USER")
        .antMatchers(HttpMethod.PATCH, "/api/graphNodes/").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/api/graphNodes/").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/api/graphLinks/**").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/api/graphLinks/**").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/api/graphLinks/**").hasRole("USER")
        .antMatchers(HttpMethod.PATCH, "/api/graphLinks/").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/api/graphLinks/").hasRole("USER")
        .and()
        .csrf().disable()
        .formLogin().disable();
    }
  
    @Override
    public void configure(AuthenticationManagerBuilder auth) 
            throws Exception 
    {
        auth.inMemoryAuthentication()
            .withUser("michael")
            .password("{noop}michael")
            .roles("USER");
    }
}