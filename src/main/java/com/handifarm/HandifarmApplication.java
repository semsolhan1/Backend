package com.handifarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
public class HandifarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandifarmApplication.class, args);
	}

}
