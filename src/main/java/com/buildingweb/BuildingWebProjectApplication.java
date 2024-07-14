package com.buildingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.buildingweb")
public class BuildingWebProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildingWebProjectApplication.class, args);
	}

}
