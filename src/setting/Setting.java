package setting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Setting {


    public static final double REPRODUCTION_PROBABILITY=0.1;
    public static final int PROCCESORS_CORE=Runtime.getRuntime().availableProcessors();
    public static final int NUMBER_OF_ROWS = 2;
    public static final int NUMBER_OF_COLUMNS=2;
    public static final int SATIETY_FROM_HUNTING=10;
    public static final String PATH_OF_ANIMAL_CHAR="src/resource/charactericOfAnimal.yaml";
    public static final String PATH_OF_ANIMAL_PROBABILITY="src/resource/preyProbability.yaml";
}
