package com.utils;

import com.google.gson.Gson;
import com.models.Purchase;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Component
public class JsonToModel {
    public Purchase jsonToEntity(HttpServletRequest req) {

        StringBuilder stb = new StringBuilder();
        Gson gson = new Gson();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null)
                stb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(stb.toString(), Purchase.class);
    }
}
