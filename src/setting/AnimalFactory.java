package setting;

import entity.Animal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimalFactory {


    public static Animal createAnimal(Map<String, Map<String, Map<String, Object>>> animalCharTable) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Animal animal = null;
        for (String animalRatio : animalCharTable.keySet()) {
            for (String animalName : animalCharTable.get(animalRatio).keySet()) {
                Class animalClassName = Class.forName(getFullName(animalRatio, animalName));
                Constructor<? extends Animal> constructor = animalClassName.getConstructor(Map.class);

                Map<String, Object> characteristics = animalCharTable.get(animalRatio).get(animalName);
                animal = constructor.newInstance(characteristics);
            }
        }
        return animal;
    }

    private static String getFullName(String animalRatio, String animalName) {
        return "entity.ration." + animalRatio.toLowerCase() + "." + animalName;
    }


}

