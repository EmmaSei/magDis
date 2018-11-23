package com.seiranyan.jsonpostgres.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HHClient {


    private static final String baseUrl = "https://api.hh.ru";
    private static final String userAgent = "AppV/0.1 (semmg@mail.ru)";
    private static final String clientID = "LI274TOFGU27TDCDL2VG6C4BV928R2L630R7S14PSFQKAPS7LHI3EUCL65P9K1J3";
    private static final String clientSecret = "SPHJBKGG0S168PE58O1H57T6VI23R2PGB3RRB4HPOFAEDVABJDVHCSH53PHQFVPH";
    private static final String userLogin = "semmg@mail.ru";
    private static final String password = "VB0RUc";
    private static HttpHeaders headers;

    public HHClient() {
//        String code = getAuthCode();
//        String accessToken = null;
//        try {
//            accessToken = getAccessToken(code);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //String accessToken = "JFQRG1IGMDR14E4QH11EGO1VTSDJBALSB2GBJ21PDFH7RLS7BFKHVOJ3CV9VH4HL";
        headers = new HttpHeaders();
        headers.setUserAgent(userAgent);
        headers.setAcceptEncoding("json");
        headers.setAccept("*/*");
  //      headers.setAuthorization("Bearer " + accessToken);
        System.setProperty("https.proxyHost", "proxy.corp.tele2.ru");
        System.setProperty("https.proxyPort", "8080");
    }

    public HttpResponse getVacancy(String id)throws IOException {
        return executeRequest("/vacancies/"+id);
    }

    public HttpResponse getVacancies(String idArea, int page)throws IOException {
        return executeRequest("/vacancies?area="+idArea+"&page="+page);
    }

    public HttpResponse getVacancies(String idArea)throws IOException {
        return getVacancies(idArea, 0);
    }

    public HttpResponse getStatByIdVacancy(String id)throws IOException {
        return executeRequest("/vacancies/"+id+"/stats");
    }

    public String getAreaById(Long id)throws IOException {
        return convertStreamToString(executeRequest("/areas/"+id).getContent());
    }

    public HttpResponse getAreas()throws IOException {
        return executeRequest("/areas/");
    }

    private HttpResponse executeRequest (String pathUrl) throws IOException {

        HttpTransport transport = new NetHttpTransport();
        HttpRequest request = transport.createRequestFactory().buildGetRequest(new GenericUrl(baseUrl + pathUrl));
        System.out.println("request to URL "+request.getUrl());
        request.setHeaders(headers);

        return request.execute();
    }

    //используя временный код авторизации, выполняем POST-запрос к /oauth/token для получения access_token;
    private static String getAccessToken(String code) throws Exception {
        HttpTransport transport = new NetHttpTransport();
        HttpRequest request = transport.createRequestFactory().
                buildPostRequest(new GenericUrl("https://m.hh.ru/oauth/token?grant_type=authorization_code&client_id="
                        + clientID + "&client_secret=" + clientSecret + "&code=" + code), new EmptyContent());
        HttpHeaders headers = new HttpHeaders();
        headers.setUserAgent(userAgent);
        request.setHeaders(headers);
        HttpResponse response = request.execute();

        JSONObject json = new JSONObject(response.parseAsString());
        transport.shutdown();

        return json.get("access_token").toString();
    }

    //открываем страницу авторизации пользователя, вводим логин и пароль, подтверждаем;
    //из полученного URL вырезаем временный код авторизации;
    private String getAuthCode(){
        //если хотим наблюдать визуально выбираем FirefoxDriver() или ChromeDriver(), например;;
        System.setProperty("webdriver.chrome.driver", "D:\\lib\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //WebDriver driver = new HtmlUnitDriver();

        driver.get("https://m.hh.ru/oauth/authorize?response_type=code&client_id=" + clientID);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        //System.out.println(driver.);
        driver.findElement(By.name("username")).clear();
        driver.findElement(By.name("username")).sendKeys(userLogin);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.xpath("//input[@value='Войти в личный кабинет']")).click();
        String currentUrl = driver.getCurrentUrl();
        driver.close();

        return currentUrl.substring(currentUrl.indexOf("?code=")).substring(6);
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
