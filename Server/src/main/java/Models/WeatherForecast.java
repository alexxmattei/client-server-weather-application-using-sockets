package Models;

public class WeatherForecast {
    private String City;
    private Double Latitude;
    private Double Longitude;
    private Double Temperature;

    public WeatherForecast() {
        this.City = "";
        this.Latitude = -1.0;
        this.Longitude = -1.0;
        this.Temperature = 0.0;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double temperature) {
        Temperature = temperature;
    }
}
