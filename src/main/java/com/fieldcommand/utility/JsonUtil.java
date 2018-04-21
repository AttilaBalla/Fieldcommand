package com.fieldcommand.utility;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class JsonUtil {


    public static JSONObject getJson(String url) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String jsonText = restTemplate.getForEntity(url, String.class).getBody();
        return new JSONObject(jsonText);
    }

    public static String toJson(Object object) {

        return new Gson().toJson(object);
    }
}