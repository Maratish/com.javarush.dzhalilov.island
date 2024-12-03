package setting;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Yaml_Initializer {

    private static Map<String, Map<String, Object>> animalCharacteristic(String yamlPath) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(yamlPath)) {
            return yaml.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
