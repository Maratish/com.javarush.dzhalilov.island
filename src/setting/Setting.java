package setting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Setting {


    public static final double REPRODUCTION_PROBABILITY=0.000000000000000000000000000000000000000000000000001;
    public static final int PROCCESORS_CORE=Runtime.getRuntime().availableProcessors();
    public static final int NUMBER_OF_ROWS = 10;
    public static final int NUMBER_OF_COLUMNS=10;
    public static final int SATIETY_FROM_HUNTING=10;
    public static final String PATH_OF_ANIMAL_CHAR="src/resource/charactericOfAnimal.yaml";
    public static final String PATH_OF_ANIMAL_PROBABILITY="src/resource/preyProbability.yaml";
}
