package com.seiranyan.jsonpostgres.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import com.seiranyan.jsonpostgres.repositories.VacancyRepository;
import com.seiranyan.jsonpostgres.utils.HHClient;
import com.seiranyan.jsonpostgres.utils.JsonManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VacancyController {

    private final static Logger logger = LoggerFactory.getLogger(VacancyController.class);


    private VacancyRepository vacancyRepository;
    private static final HHClient hhClient = new HHClient();
    private static final JsonManipulation jsonManipulation = new JsonManipulation();

    @Autowired
    public VacancyController(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }


    @RequestMapping("json")
    @ResponseBody
    public Vacancy json(){
        Vacancy vacancy = new Vacancy();
            try {
                HttpResponse resp =  hhClient.getVacancy("28713828");
                if(resp.getStatusCode()==200){
                    return jsonManipulation.toVacancy(convertStreamToString(resp.getContent()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return vacancy;
    }

    @RequestMapping("vacancies")
    @ResponseBody
    public List<Long> getVacancyIds(){

        ArrayList<Long> list = null;
        HttpResponse resp = null;
        try {
            resp = hhClient.getVacancies("1");
            if(resp.getStatusCode()==200){
                list = jsonManipulation.getIds(convertStreamToString(resp.getContent()));
            }else{
                logger.error("url is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("vacancyStats")
    @ResponseBody
    public String getVacancyStats(){

        ArrayList<Long> list = null;
        HttpResponse resp = null;
        try {
            resp = hhClient.getStatByIdVacancy("28713828");
            if(resp.getStatusCode()==200){
                //list = jsonManipulation.getIds(convertStreamToString(resp.getContent()));
                return "good";
            }else{
                logger.error("url is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "bad";
    }



    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
