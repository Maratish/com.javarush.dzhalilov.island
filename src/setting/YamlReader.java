package setting;

import entity.Animal;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Data
public class YamlReader {

    public static Map<String,Map<String, Map<String, Object>>> animalCharInit(String yamlPath) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(yamlPath)) {
            return yaml.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            int newValue = ((Number) value).intValue();
            return newValue;
        } else {
            System.err.println("НЕВЕРНЫЙ");
        }
        return 0;
    }

    public static double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            double newValue = ((Number) value).doubleValue();
            return newValue;
        } else {
            System.err.println("НЕВЕРНЫЙ");
            return 0;
        }
    }

    public static PredatorPreyProbability probabilityPrey(String yamlPath) {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(yamlPath)) {
            Map<String, Map<String, Double>> matrix = yaml.load(is);
            return new PredatorPreyProbability(matrix);
        } catch (IOException | ClassCastException e) {
            throw new RuntimeException("Ошибка загрузки или обработки YAML файла: " + e.getMessage(), e);
        }
    }
            }
