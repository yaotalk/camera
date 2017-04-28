//package com.minivision.camaraplat.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.StandardPasswordEncoder;
//
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//  @Autowired
//  private DataSource dataSource;
//
//  @Autowired
//  private StandardPasswordEncoder passwordEncoder;
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
////      http.csrf().disable()
////          .rememberMe().key("camera")
////              .and().authorizeRequests()
////              .antMatchers("/assets/**", "/login").permitAll()          //TODO 静态资源过滤
////              .antMatchers("/admin/**").hasRole("ADMIN")
////              .anyRequest().fullyAuthenticated()
////              .and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
////              .and().logout().permitAll();
//  }
//
//  @Override
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//      //TODO auth.userDetailsService()?
//
////      auth.jdbcAuthentication().passwordEncoder(passwordEncoder)
////          .dataSource(this.dataSource);
//          //.withUser("admin").password(passwordEncoder.encode("admin")).roles("USER", "ADMIN");
//  }
//
//  @Bean
//  public StandardPasswordEncoder passwordEncoder(){
//    return new StandardPasswordEncoder();
//  }
//
//}
