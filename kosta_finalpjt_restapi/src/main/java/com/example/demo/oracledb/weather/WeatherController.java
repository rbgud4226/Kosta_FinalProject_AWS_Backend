package com.example.demo.oracledb.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class WeatherController {

    @GetMapping("/auth/getWeather")
    @ResponseBody
    public JsonNode getWeather(@RequestParam("lat") String lat, @RequestParam("lon") String lon) {
        String key = "d8837c0fc8f973c07fdd90ac194e7d7a";
        String path = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + key + "&lang=kr";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode result = objectMapper.createArrayNode();

        try {
            URL url = new URL(path);
            InputStream is = url.openStream();
            JsonNode root = objectMapper.readTree(is);
            JsonNode list = root.get("list");

            for (JsonNode item : list) {
                JsonNode weatherItem = item;
                JsonNode weatherLists = objectMapper.createObjectNode();
                JsonNode weatherArray = weatherItem.get("weather");
                JsonNode weather = weatherArray.get(0);
                JsonNode main = weatherItem.get("main");
                String dt_txt = weatherItem.get("dt_txt").asText();
                SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = dateForm.parse(dt_txt);
                SimpleDateFormat newDateForm = new SimpleDateFormat("M월d일 E HH시");
                String finalDateForm = newDateForm.format(date);
                String icon = weather.get("icon").asText();
                double temp = main.get("temp").asDouble() - 273.15;

                if (finalDateForm.endsWith("00시")) {
                    ((ObjectNode) weatherLists).put("dateTime", finalDateForm);
                    ((ObjectNode) weatherLists).put("icon", icon);
                    ((ObjectNode) weatherLists).put("temp", String.format("%.1f", temp));
                    ((ArrayNode) result).add(weatherLists);
                }
            }

        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}