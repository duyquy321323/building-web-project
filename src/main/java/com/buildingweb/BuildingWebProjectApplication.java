package com.buildingweb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.buildingweb.entity.Building;

@SpringBootApplication
public class BuildingWebProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildingWebProjectApplication.class, args);
		Building building = new Building();
		building.setId(12);
		System.out.println(building.getId());
	}

}
