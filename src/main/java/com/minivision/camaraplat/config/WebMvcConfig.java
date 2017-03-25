package com.minivision.camaraplat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
  
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
      configurer.mediaType("json", MediaType.APPLICATION_JSON);
      configurer.mediaType("text", MediaType.APPLICATION_JSON);
      configurer.mediaType("xml", MediaType.APPLICATION_XML);
      configurer.defaultContentType(MediaType.TEXT_HTML);
      configurer.ignoreAcceptHeader(false);
  }
  
  @Autowired
  private ThymeleafViewResolver thymeleafViewResolver;
  
  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
      registry.enableContentNegotiation(new MappingJackson2JsonView());
      registry.viewResolver(thymeleafViewResolver);
  }
  
}
