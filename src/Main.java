import setting.AnimalFactory;
import setting.YamlReader;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Map<String, Map<String,Map<String,Object>>> animalCharacteristic = YamlReader.animalCharInit("src/resource/charactericOfAnimal.yaml");
//        for (String animalType:animalCharacteristic.keySet()){
//            for (String animalName:animalCharacteristic.get(animalType).keySet()){
//                for (Object animalChar:animalCharacteristic.get(animalType).values())
//                System.out.println(animalType+" "+animalName+" "+animalChar);
        AnimalFactory.createAnimal(animalCharacteristic);
            }
        }

