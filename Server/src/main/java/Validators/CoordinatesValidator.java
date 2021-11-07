package Validators;

public class CoordinatesValidator {
    public static Boolean validateLatitude(String userLatitudeInput) {
        double preciseLatitude = Double.parseDouble(userLatitudeInput);
        return (preciseLatitude >= -90) && (preciseLatitude <= 90);
    }

    public static Boolean validateLongitude(String userLongitudeInput) {
        double preciseLongitude = Double.parseDouble(userLongitudeInput);
        return (preciseLongitude >= -180) && (preciseLongitude <= 180);
    }
}
