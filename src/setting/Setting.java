package setting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Setting {


    public static final double PLANT_MAX_WEIGHT_PER_UNIT = 1.0;
    public static final int MAX_PLANTS_PER_CELL = 100;
    public static final double PLANT_GROWTH_PER_CYCLE = 0.25;
    public static final int MAX_SATIETY = 1;
    public static final double REPRODUCTION_PROBABILITY=0.17;
    public static final int PROCCESORS_CORE=Runtime.getRuntime().availableProcessors();
    public static final int NUMBER_OF_ROWS = 10;
    public static final int NUMBER_OF_COLUMNS=10;
    public static final String PATH_OF_ANIMAL_CHAR="src/resource/charactericOfAnimal.yaml";
    public static final String PATH_OF_ANIMAL_PROBABILITY="src/resource/preyProbability.yaml";
}
