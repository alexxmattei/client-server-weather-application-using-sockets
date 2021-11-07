package Utils.Parser;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadJsonAsString {

    public static String getJsonDataAsString() throws Exception {
        String filePath = "src/main/java/Persistence/weatherForecast.json";
        return readJSONFileAsString(filePath);
    }

    public static String readJSONFileAsString(String filePath) throws Exception {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
