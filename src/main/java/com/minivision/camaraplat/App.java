package com.minivision.camaraplat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.minivision.camaraplat.mqtt.MqttStartup;

@SpringBootApplication
@EnableScheduling
@EnableAsync
//@PropertySource(value={"file:config/application.yml"})
public class App {
  
  public static void main(String[] args) throws Exception {
	ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);
	
	MqttStartup startup = ctx.getBean(MqttStartup.class);
	
	startup.start();
  }
}
