import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FileReader {
    static final String FILE_NAME = "files_urls.txt";

    static final Map<Integer, String> mapOfUrls = new HashMap<>();

    static Map<Integer, String> getUrlsFromFile() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(FILE_NAME, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split("\\s");
                mapOfUrls.put(Integer.parseInt(words[0]), words[1]);
            }
        }
        return mapOfUrls;
    }

    static void DeliteFiles() throws IOException {
        for (var pair : mapOfUrls.entrySet())
            Files.deleteIfExists(Paths.get(pair.getValue().split("/")[pair.getValue().split("/").length - 1]).toAbsolutePath());
    }

}
