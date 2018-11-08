package com.seiranyan.jsonpostgres.utils;


import com.seiranyan.jsonpostgres.entities.Vacancy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonManipulation {

    //private JSONParser parser = new JSONParser();

    public void transformFile(String fileName){
        File fnew = new File(fileName);

        String outputFile = "";
        try {
            FileReader fr = new FileReader(fileName);
            //fr.re
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONArray jsARR = new JSONArray(); //мой массив с данными JSON из файла, который ранее считал
        for (int i = 0; i < jsARR.length(); i++) { //Прохожу циклом по массиву
            JSONObject jsonObj = jsARR.getJSONObject(i); //Вытягиваю по одному объекту
            JSONObject jsObj2 = new JSONObject(); //создаю новый JSON объект
            jsonObj.remove("content"); //Из основного удаляю нужные данные
//            for (int k = 0; k<edits.size(); k++) { //Заполняю новый объект нужными елементами
//                jsObj2.put(edits_name.get(k).getText().toString(),edits.get(k).getText().toString());
//            }
            jsonObj.put("id", jsObj2); //Добавляю jsObj2 в jsonObj
            jsARR.put(jsObj2);
            outputFile += jsARR.getJSONObject(i).toString()+",";//Формирую выходной файл, который потом перезаписываю
            System.out.println(outputFile);
            try {
                FileWriter fw = new FileWriter(fnew, false);
                fw.write(outputFile);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public Vacancy toVacancy (String stringVacancy){
        Vacancy vacancy = new Vacancy();

        JSONObject objVacancy = new JSONObject(stringVacancy);

        vacancy.setId(objVacancy.getLong("id"));
        vacancy.setPremium(objVacancy.getBoolean("premium"));
        vacancy.setCreated_at(objVacancy.getString("created_at"));
        vacancy.setName(objVacancy.getString("name"));
        vacancy.setArea(new JSONObject(objVacancy.get("area").toString()).getString("name"));

        JSONObject objSalary = (objVacancy.get("salary")!=null ? objVacancy.getJSONObject("salary") : new JSONObject("{\"to\": 0, \"from\": 0}"));

        vacancy.setSalary_from(objSalary.get("from").equals(null) ? 0 :objSalary.getFloat("from"));
        vacancy.setSalary_to(objSalary.get("to").equals(null) ? 0 : objSalary.getFloat("to"));
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
}

