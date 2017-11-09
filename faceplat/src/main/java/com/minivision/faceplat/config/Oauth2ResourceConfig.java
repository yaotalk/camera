package com.minivision.faceplat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {
  
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
    .authorizeRequests()
    .antMatchers("/api/v1/*").access("#oauth2.hasScope('face')")
    .antMatchers("/api/v1/faceset/**").access("#oauth2.hasScope('faceset')");
//        .antMatchers("/api/v1/*").permitAll()
//        .antMatchers("/api/v1/faceset/**").permitAll();
  }
}
