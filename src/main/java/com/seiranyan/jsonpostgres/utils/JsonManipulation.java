package com.seiranyan.jsonpostgres.utils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class JsonManipulation {

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
}
