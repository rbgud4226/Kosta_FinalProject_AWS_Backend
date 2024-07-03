package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KostaFinalpjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(KostaFinalpjtApplication.class, args);
	}

}
