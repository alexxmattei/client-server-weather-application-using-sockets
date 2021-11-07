package Utils.Parser;

import java.io.FileWriter;

public class MapJsonToFile {
    public static Boolean mapJsonToFile(String data) throws Exception {
        try {
            FileWriter fileWriter = new FileWriter("src/main/java/Persistence/weatherForecast.json");
            fileWriter.write(data);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
