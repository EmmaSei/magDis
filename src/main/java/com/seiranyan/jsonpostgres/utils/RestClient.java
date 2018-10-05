package com.seiranyan.jsonpostgres.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seiranyan.jsonpostgres.entities.Vacancy;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class RestClient {
    private URL url;

    public RestClient(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Vacancy getConnection() throws IOException {
        Vacancy vacancy = new Vacancy();
        StringBuffer response = null;
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.corp.tele2.ru", 8080));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        HttpURLConnection.setFollowRedirects(false);
        connection.setRequestProperty("User-Agent", "Mozilla/0729)");
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        OutputStream os = connection.getOutputStream();
        System.out.println(os);
        os.flush();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            response = new StringBuffer();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode obj = mapper.readTree(response.toString());
        //JSONObject obj = new JSONObject(response);
        vacancy.setId(obj.get("id").asLong());
        vacancy.setArea(obj.get("area").asText());
        vacancy.setName(obj.get("name").asText());
        vacancy.setCreated_at(obj.get("created_at").asText());
        vacancy.setPremium(obj.get("premium").asBoolean());

        connection.getResponseCode();
        connection.disconnect();
        return vacancy;
    }
}
