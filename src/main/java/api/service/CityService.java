package api.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@Service
public class CityService {

    @Value("${application.viber-bot.api-key}")
    private String API_KEY;

    public String getCityKey(String name) throws IOException {
        URL url = new URL("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + name);
        // http://dataservice.accuweather.com/locations/v1/cities/search?apikey=iOgCoivoZGiWzlY5mkyXlhgpVmEzbthi&q=lviv"

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";

        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONArray object = new JSONArray(result);
        return object.getJSONObject(0).getString("Key");
    }

    public String getCountryByCity(String name) throws IOException {
        URL url = new URL("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + name);

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";

        while (in.hasNext()) {
            result += in.nextLine();
        }

        String countryName = "";
        JSONArray object = new JSONArray(result);
        for (int i = 0; i < object.length(); i++) {
            JSONObject country = object.getJSONObject(i).getJSONObject("Country");
            countryName = country.getString("EnglishName");
        }
        return countryName;
    }
}
