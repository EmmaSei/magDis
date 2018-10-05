package com.seiranyan.jsonpostgres.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import com.seiranyan.jsonpostgres.repositories.VacancyRepository;
import com.seiranyan.jsonpostgres.utils.JsonManipulation;
import com.seiranyan.jsonpostgres.utils.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RestController
public class VacancyController {

    private final static Logger logger = LoggerFactory.getLogger(VacancyController.class);

    private VacancyRepository vacancyRepository;

    @Autowired
    public VacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }


    @RequestMapping("json")
    @ResponseBody
    public Vacancy json(){
        Vacancy vacancy = null;
        URL url = this.getClass().getClassLoader().getResource("vacancies.json");
        if(url!=null){
            File jsonFile = new File(url.getFile());

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                RestClient client = new RestClient("https://api.hh.ru/vacancies/27853577");
                vacancy = client.getConnection();
                //JsonManipulation jsonManipulation = new JsonManipulation();
                //jsonManipulation.transformFile("vacancies.json");
                List<Vacancy> vacancies = objectMapper.readValue(jsonFile, new TypeReference<List<Vacancy>>(){

                });
                vacancyRepository.saveAll(vacancies);
                logger.info("All records saved.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            logger.warn("url is null");
        }
        return vacancy;
    }
}
