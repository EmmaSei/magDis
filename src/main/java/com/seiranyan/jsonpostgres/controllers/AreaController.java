package com.seiranyan.jsonpostgres.controllers;

import com.google.api.client.http.HttpResponse;
import com.seiranyan.jsonpostgres.entities.Area;
import com.seiranyan.jsonpostgres.repositories.AreaRepository;
import com.seiranyan.jsonpostgres.repositories.VacancyRepository;
import com.seiranyan.jsonpostgres.utils.HHClient;
import com.seiranyan.jsonpostgres.utils.JsonManipulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AreaController {


    private static final HHClient hhClient = new HHClient();
    private static final JsonManipulation jsonManipulation = new JsonManipulation();
    private final static Logger logger = LoggerFactory.getLogger(AreaController.class);


    public static AreaRepository areaRepository;

    @Autowired
    public AreaController(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }


    @RequestMapping("areas")
    @ResponseBody
    public List<Area> getAreas(){

        ArrayList<Area> list = null;
        HttpResponse resp = null;
        try {
            resp = hhClient.getAreas();
            if(resp.getStatusCode()==200){
                list = jsonManipulation.getAreas(convertStreamToString(resp.getContent()));
            }else{
                logger.error("url is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @RequestMapping("areasFromBD")
    @ResponseBody
    public List<Area> getAreasBD(){
        return areaRepository.findAll();
    }


    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
