package setting;

import lombok.Getter;
import lombok.ToString;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
public class PredatorPreyProbability {
    @Getter
    private static Map<String, Map<String, Double>> predatorPreyMatrix;

    static {
            Yaml yaml = new Yaml();
            try (InputStream is = new FileInputStream(Setting.PATH_OF_ANIMAL_PROBABILITY)) {
                Map<String, Map<String, Double>> matrix = yaml.load(is);
     predatorPreyMatrix=Collections.unmodifiableMap(new HashMap<>(matrix));
            } catch (IOException | ClassCastException e) {
                predatorPreyMatrix=new HashMap<>();
                throw new RuntimeException(e);
            }
        }
    }
