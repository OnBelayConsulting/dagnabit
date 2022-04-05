package com.onbelay.testapp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Order(2) //Load this rule after the api rule
@Configuration
public class UISecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
                .antMatchers("/actuator/health/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2Login().userInfoEndpoint();
    }
}