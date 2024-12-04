package setting;

import entity.Animal;

import java.lang.reflect.Constructor;
import java.util.Map;

public class AnimalFactory {
    public static Animal createAnimal(Map<String, Map<String,Map<String,Object>>> animalChars) throws ClassNotFoundException, NoSuchMethodException {
        for (String animalClassName: animalChars.keySet()){
            for (String animalName:animalChars.get(animalClassName).keySet()){
                Class className = Object.class;
                if (animalClassName.toLowerCase().equals("predator")){
                    className = Class.forName("entity.ration.predator."+animalName);
                } else if (animalClassName.toLowerCase().equals("herbivorou")){
                    className = Class.forName("entity.ration.herbivorou."+animalName);
                }
                Constructor<? extends Animal> constructor = className.getConstructor(Map.class);
                Map <String, Object> animalsChars =
            }
        }
    }

}
