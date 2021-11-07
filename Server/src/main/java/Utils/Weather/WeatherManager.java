package Utils.Weather;

import Models.WeatherForecast;
import Utils.Parser.MapJsonToClass;

import java.lang.Math;


public class WeatherManager {
    public static WeatherForecast calculateClosestCity(Double latitude, Double longitude) throws Exception {
        WeatherForecast[] cities = MapJsonToClass.getJSONAsObject();
        WeatherForecast result = new WeatherForecast();
        Double currentDistance = 2147483647.0;
        for (WeatherForecast city : cities) {
            Double auxDistance = Math.sqrt((latitude - city.getLatitude()) * (latitude - city.getLatitude()) + (longitude - city.getLongitude()) * (longitude - city.getLongitude()));
            if(auxDistance < currentDistance) {
                currentDistance = auxDistance;
                result = city;
            }
        }
        return result;
    }

    public static WeatherForecast[] getAllCities() throws Exception {
        WeatherForecast[] cities = MapJsonToClass.getJSONAsObject();
        return cities;
    }
}
