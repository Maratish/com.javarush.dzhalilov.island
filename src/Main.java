import setting.AnimalFactory;
import setting.PredatorPreyProbability;
import setting.YamlReader;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Map<String, Map<String,Map<String,Object>>> animalCharacteristic = YamlReader.animalCharInit("src/resource/charactericOfAnimal.yaml");

        PredatorPreyProbability predatorPreyProbability = YamlReader.probabilityPrey("src/resource/preyProbability.yaml");
        Map<String,Map<String,Double>> e = predatorPreyProbability.getPredatorPreyMatrix();
        System.out.println(predatorPreyProbability.getProbability("wolf", "sheep"));

        // СДЕЛАЛ ВЕРОЯТНОСТИ И ХАРАКТЕРИСТИКИ. ТЕПЕРЬ НАДО СДЕЛАТЬ ЯЧЕЙКИ, ЗАТЕМ МЕТОД ИТ, МУВ И ТД. НЕ ЗАБЫТЬ ПРО МАКС САТИЕТИ

    }
        }

