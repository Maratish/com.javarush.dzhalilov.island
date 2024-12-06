import island.Cell;
import island.Island;
import setting.AnimalFactory;
import setting.PredatorPreyProbability;
import setting.Setting;
import setting.YamlReader;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args)  {
        AnimalFactory.initializeAnimalCharTable();
        AnimalFactory.createAnimal(YamlReader.animalCharInit("src/resource/charactericOfAnimal.yaml"));
            Island island = new Island();
        //TODO сделать eat() и визуал






    }
}

