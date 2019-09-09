package api.service;

import api.model.WeatherApiModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@Service
public class WeatherService {

    @Value("${application.viber-bot.api-key}")
    private String API_KEY;

    private WeatherApiModel model;
    private CityService cityService;

    @Autowired
    public WeatherService(WeatherApiModel model, CityService cityService) {
        this.model = model;
        this.cityService = cityService;
    }

    public String getWeather(String name) throws IOException {

        String cityKey = cityService.getCityKey(name);

        URL url = new URL("http://dataservice.accuweather.com/currentconditions/v1/" + cityKey + "?apikey=" + API_KEY);
//        http://dataservice.accuweather.com/currentconditions/v1/324561?apikey=iOgCoivoZGiWzlY5mkyXlhgpVmEzbthi

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";

        while (in.hasNext()) {
            result += in.nextLine();
        }
        JSONArray object = new JSONArray(result);
        for (int i = 0; i < object.length(); i++) {
            JSONObject type = object.getJSONObject(i);
            String weatherText = type.getString("WeatherText");
            model.setName(weatherText);
        }
        for (int i = 0; i < object.length(); i++) {
            JSONObject type = object.getJSONObject(i);
            String weatherText = type.getString("WeatherText");
            model.setName(weatherText);

            JSONObject temp = type.getJSONObject("Temperature");
            JSONObject metric = temp.getJSONObject("Metric");
            Double temperature = metric.getDouble("Value");
            model.setTemp(temperature);
        }

        return "Weather description: " + model.getName() + "\n" + "Temperature: " + model.getTemp() + "C";
    }

}

