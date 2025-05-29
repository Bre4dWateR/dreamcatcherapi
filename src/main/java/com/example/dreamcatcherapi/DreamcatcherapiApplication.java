package com.example.dreamcatcherapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // <--- ОБЯЗАТЕЛЬНО ИМПОРТИРУЙТЕ ЭТО

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) // <--- ДОБАВЬТЕ ЭТО
public class DreamcatcherapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DreamcatcherapiApplication.class, args);
	}

}
