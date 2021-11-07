package Utils.Parser;

import Models.WeatherForecast;
import com.google.gson.Gson;

public class MapJsonToClass {
    public static WeatherForecast[] getJSONAsObject() throws Exception {
        String jsonFile = ReadJsonAsString.getJsonDataAsString();
        Gson gsonUtil = new Gson();

        WeatherForecast[] citiesForecast = gsonUtil.fromJson(jsonFile, WeatherForecast[].class);

        return citiesForecast;
    }
}
