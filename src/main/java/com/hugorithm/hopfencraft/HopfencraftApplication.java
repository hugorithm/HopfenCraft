package com.hugorithm.hopfencraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication()
public class HopfencraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(HopfencraftApplication.class, args);
	}
}
