package com.seiranyan.jsonpostgres.controllers;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.seiranyan.jsonpostgres.entities.Area;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import com.seiranyan.jsonpostgres.repositories.AreaRepository;
import com.seiranyan.jsonpostgres.repositories.VacancyRepository;
import com.seiranyan.jsonpostgres.utils.HHClient;
import com.seiranyan.jsonpostgres.utils.JsonManipulation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xpath.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @RequestMapping("go")
    @ResponseBody
    public Boolean go(){
        List<Area> areas = AreaController.areaRepository.findAll();
        for (Area area: areas) {
            if(area.getParent_id()!= null){
                List<Long> vacIds = getVacancyIds(area.getId().toString());
//                if (!vacancyRepository.findById(vacIds.get(0)).isPresent()){
//                    continue;
//                }
                for (Long vacId: vacIds) {
                    if (!vacancyRepository.findById(vacId).isPresent()) {
                        Vacancy vacancy;
                        if ((vacancy = getVacancy(vacId.toString()) )!= null) {
                            vacancyRepository.save(vacancy);
                        }
                    }
                }
            }
            //AreaController.areaRepository.findById(area.getParent_id()).get().getParent_id()
        }
        return true;
    }


    @RequestMapping("vacancies")
    @ResponseBody
    public Vacancy getVacancy(@RequestParam("id") String id){
        Vacancy vacancy = null;
            try {
                HttpResponse resp =  hhClient.getVacancy(id);
                if(resp.getStatusCode()==200){
                    vacancy = vacancyRepository.save(jsonManipulation.toVacancy(convertStreamToString(resp.getContent())));
                    return vacancy;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return vacancy;
    }

    @RequestMapping("vacanciesByArea")
    @ResponseBody
    public List<Long> getVacancyIds(@RequestParam("idArea") String idArea){

        ArrayList<Long> listIds = null;
        HttpResponse resp = null;
        try {
            resp = hhClient.getVacancies(idArea);
            if(resp.getStatusCode()==200){
                String r = convertStreamToString(resp.getContent());
                listIds = jsonManipulation.getIds(r);
                if (!listIds.isEmpty() && !vacancyRepository.findById(listIds.get(0)).isPresent()){
                    for (int i = 1; i < jsonManipulation.getPagesNum(r); i++) {
                        resp = hhClient.getVacancies(idArea, i);
                        ArrayList<Long> arr = jsonManipulation.getIds(convertStreamToString(resp.getContent()));
                        if (vacancyRepository.findById(arr.get(0)).isPresent()){
                            return listIds;
                        }
                        listIds.addAll(arr);
                    }
                }


            }else{
                logger.error("url is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listIds;
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
