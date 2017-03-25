package com.minivision.faceplat;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class App {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(App.class, args);
	}
}
