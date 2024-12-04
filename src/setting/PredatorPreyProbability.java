package setting;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
public class PredatorPreyProbability {
    private static Map<String, Map<String, Double>> predatorPreyMatrix;

    public PredatorPreyProbability(Map<String, Map<String, Double>> predatorPreyMatrix) {
        this.predatorPreyMatrix = predatorPreyMatrix != null ? predatorPreyMatrix : new HashMap<>();
    }

    public Double getProbability(String predator, String prey) {
        return predatorPreyMatrix.getOrDefault(predator, new HashMap<>()).getOrDefault(prey, 0.0);
    }
    public Map<String, Map<String, Double>> getPredatorPreyMatrix(){
        return predatorPreyMatrix;
    }
}
