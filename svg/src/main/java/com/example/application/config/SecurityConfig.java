package com.example.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
          .authorizeRequests()
          .antMatchers(HttpMethod.GET, "/api/v1/test**").permitAll()
          .antMatchers(HttpMethod.POST, "/api/v1/read**").permitAll()
          .and()
          .httpBasic();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
