package setting;

import entity.Animal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimalFactory {

    public static CopyOnWriteArrayList<Animal> getAllAnimalList() {
        return allAnimalList;
    }

    static CopyOnWriteArrayList<Animal> allAnimalList = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<Animal> createAnimal(Map<String, Map<String, Map<String, Object>>> animalCharTable) {
        for (String animalRatio : animalCharTable.keySet()) {
            for (String animalName : animalCharTable.get(animalRatio).keySet()) {
                Class animalClassName = null;
                try {
                    animalClassName = Class.forName(getFullName(animalRatio, animalName));
                    Constructor<? extends Animal> constructor = animalClassName.getConstructor();
                    allAnimalList.add(constructor.newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         ClassNotFoundException | NoSuchMethodException e) {
                    throw new RuntimeException("Ошибка создания экземпляров животных");
                }
            }
        }
        return allAnimalList;
    }

    private static String getFullName(String animalRatio, String animalName) {
        return "entity.ration." + animalRatio.toLowerCase() + "." + animalName;
    }

    static ConcurrentHashMap<String, Map<String, Object>> ANIMALCHARTABLE = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Map<String, Object>> getANIMALCHARTABLE() {
        return ANIMALCHARTABLE;
    }


    public static void initializeAnimalCharTable() {
        Map<String, Map<String, Map<String, Object>>> charsMap = YamlReader.animalCharInit("src/resource/charactericOfAnimal.yaml");
        for (Map.Entry<String, Map<String, Map<String, Object>>> animalRatioEntry : charsMap.entrySet()) {
            Map<String, Map<String, Object>> animalTypes = animalRatioEntry.getValue();
            for (Map.Entry<String, Map<String, Object>> animalEntry : animalTypes.entrySet()) {
                String animalName = animalEntry.getKey();
                Map<String, Object> animalCharacteristics = animalEntry.getValue();
                ANIMALCHARTABLE.put(animalName, Map.copyOf(animalCharacteristics));
            }
        }
    }
}


