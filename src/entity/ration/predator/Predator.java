package entity.ration.predator;


import entity.Animal;
import island.Cell;
import setting.PredatorPreyProbability;
import setting.Setting;

import java.util.Map;

public class Predator extends Animal {
    public Predator(){
        super();
    }

    @Override
    public void eat(Cell cell){
        //TODO предаторпрей матрикс нормально инициализируется. теперь надо взять всех жиовтных из клетки. взять рандомного. использовать метод гетпробабилити. если успешно, то поднять сатиети, если не успешно, то отнять х2.
        //TODO жертву удалить из мапы. создать метод, который будет у всех животных отнимать сатиети 0.1 каждый ход. если сатиети ==0, то смерть
    }

}
