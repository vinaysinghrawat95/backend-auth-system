package com.vinay.auth_system;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthSystemApplication {

	@PostConstruct
	public void init() {
		System.out.println("APP STARTED");
	}
	public static void main(String[] args) {
		SpringApplication.run(AuthSystemApplication.class, args);
	}

}
