package com.seiranyan.jsonpostgres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonPostgresApplication {


	public static void main(String[] args) {
		SpringApplication.run(JsonPostgresApplication.class, args);
	}

}
