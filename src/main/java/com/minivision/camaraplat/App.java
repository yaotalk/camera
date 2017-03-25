package com.minivision.camaraplat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.mqtt.MqttStartup;
import com.minivision.camaraplat.repository.FaceSetRepository;

@SpringBootApplication
@EnableScheduling
public class App {
  
  @Autowired
  private FaceSetRepository faceSetRepository;
  

  @Bean
  public Converter<String, FaceSet> faceSetConverter() {
      return new Converter<String, FaceSet>() {
          @Override
          public FaceSet convert(String token) {
              return faceSetRepository.findOne(token);
          }
      };
  }
  
  public static void main(String[] args) throws Exception {
	ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
	
	MqttStartup startup = ctx.getBean(MqttStartup.class);
	
	startup.start();
  }
}
