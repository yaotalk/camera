package com.minivision.camaraplat.config;

import com.minivision.camaraplat.config.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
  
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
      configurer.mediaType("json", MediaType.APPLICATION_JSON);
      configurer.mediaType("text", MediaType.APPLICATION_JSON);
      configurer.mediaType("xml", MediaType.APPLICATION_XML);
      configurer.defaultContentType(MediaType.APPLICATION_JSON);
      configurer.ignoreAcceptHeader(false);
  }
  
  @Autowired
  private ThymeleafViewResolver thymeleafViewResolver;

  @Autowired
  private LoginInterceptor loginInterceptor;

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
      registry.enableContentNegotiation(new MappingJackson2JsonView());
      registry.viewResolver(thymeleafViewResolver);
  }

    @Override public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/user/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/api/v1/**").excludePathPatterns("/swagger*").excludePathPatterns("/v2/**");
    }
}
