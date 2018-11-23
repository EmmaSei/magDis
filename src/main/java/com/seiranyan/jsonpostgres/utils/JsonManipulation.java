package com.seiranyan.jsonpostgres.utils;



import com.seiranyan.jsonpostgres.controllers.AreaController;
import com.seiranyan.jsonpostgres.controllers.SpecializationController;
import com.seiranyan.jsonpostgres.entities.Area;
import com.seiranyan.jsonpostgres.entities.Specialization;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class JsonManipulation {

    private final static Logger logger = LoggerFactory.getLogger(JsonManipulation.class);


    public Vacancy toVacancy (String stringVacancy){
        Vacancy vacancy = new Vacancy();

        JSONObject objVacancy = new JSONObject(stringVacancy);

        vacancy.setId(objVacancy.getLong("id"));
        vacancy.setCreated_at(objVacancy.getString("created_at"));
        vacancy.setName(objVacancy.getString("name"));
        JSONObject areaJson = objVacancy.getJSONObject("area");
        vacancy.setCity(areaJson.getString("name"));

        //Area area = new Area();

        try {
            vacancy.setArea(getAreaById(areaJson.getLong("id")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        vacancy.setExperience(new JSONObject(objVacancy.get("experience").toString()).getString("id"));
        vacancy.setDescription(objVacancy.getString("description"));

        JSONArray specializations = objVacancy.getJSONArray("specializations");
        Set<Specialization> set = new HashSet<>();
        //logger.info("array "+specializations.toString());
        for (int i = 0 ; i<specializations.length(); i++) {
            JSONObject spec = new JSONObject(specializations.get(i).toString());
            Specialization specialization = new Specialization();
            specialization.setId(spec.getString("id"));
            specialization.setName(spec.getString("name"));
            //logger.info("obj "+spec.toString());
            set.add(specialization);
            SpecializationController.specializationRepository.save(specialization);
        }

        vacancy.setSpecializations(set);

       // logger.info(objVacancy.get("salary")+" salary");
        JSONObject objSalary = (!objVacancy.get("salary").equals(null) ? objVacancy.getJSONObject("salary") : new JSONObject("{\"to\": 0, \"from\": 0}"));


        vacancy.setSalary_from(objSalary.get("from").equals(null) ? 0 :objSalary.getFloat("from"));
        vacancy.setSalary_to(objSalary.get("to").equals(null) ? 0 : objSalary.getFloat("to"));
        //logger.info(vacancy.toString());
        return vacancy;
    }

    public ArrayList<Long> getIds (String stringVacancies){

        JSONObject objVacancy = new JSONObject(stringVacancies);
        ArrayList<Long> ids = new ArrayList();
        JSONArray arrayVacancy = objVacancy.getJSONArray("items");
        //JSONObject objVacancy = new JSONObject(stringVacancy);

        for (int i = 0; i < arrayVacancy.length(); i++) {
            ids.add(arrayVacancy.getJSONObject(i).getLong("id"));
        }
        return ids;
    }

    public Area getAreaById (Long id) throws IOException {;
        if (!AreaController.areaRepository.findById(id).isPresent()){
            Area area = new Area();
            JSONObject areaJson = new JSONObject(new HHClient().getAreaById(id));
            //     logger.error("area "+areaJson.toString());
            area.setId(areaJson.getLong("id"));
            area.setName(areaJson.getString("name"));
            area.setParent_id(areaJson.getLong("parent_id"));
            return AreaController.areaRepository.save(area);
        }
        return AreaController.areaRepository.findById(id).get();
    }

    public int getPagesNum (String str) throws IOException {
        JSONObject json = new JSONObject(str);
        return json.getInt("pages");
    }

    public ArrayList<Area> getAreas(String json) throws IOException {

        ArrayList<Area> areas = new ArrayList<>();
        JSONArray areasAll = new JSONArray(json);
        for (int i = 0; i < areasAll.length(); i++) {
            JSONObject areasCountry = areasAll.getJSONObject(i);
            areas.add(getAreaFromJson(areasCountry));
            JSONArray areasRegion = areasCountry.getJSONArray("areas");
            for (int j = 0; j < areasRegion.length(); j++) {
                JSONObject areaRegion = areasRegion.getJSONObject(j);
                areas.add(getAreaFromJson(areaRegion));
                JSONArray areasCity = areaRegion.getJSONArray("areas");
                for (int k = 0; k < areasCity.length(); k++) {
                    JSONObject areaCity = areasCity.getJSONObject(k);
                    areas.add(getAreaFromJson(areaCity));
                }
            }

        }
        AreaController.areaRepository.saveAll(areas);
        return areas;
    }

    private Area getAreaFromJson(JSONObject areaJson){
        Area area = new Area();
        area.setId(areaJson.getLong("id"));
        area.setName(areaJson.getString("name"));

        area.setParent_id(areaJson.get("parent_id").equals(null)? null: areaJson.getLong("parent_id"));
        return area;
    }
}

