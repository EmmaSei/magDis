package com.seiranyan.jsonpostgres;

import com.seiranyan.jsonpostgres.entities.Vacancy;
import com.seiranyan.jsonpostgres.repositories.AreaRepository;
import com.seiranyan.jsonpostgres.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonPostgresApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonPostgresApplication.class, args);
	}
}
