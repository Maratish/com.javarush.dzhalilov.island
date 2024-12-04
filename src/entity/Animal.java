package entity;

import lombok.Data;
import lombok.ToString;
import setting.PredatorPreyProbability;
import setting.YamlReader;

import java.util.Map;

@ToString
@Data
public abstract class Animal {
    final double weight;
    final int maxSpeed;
    final double maxPerCell;
    final double foodNeeded;
    public void eat() {
    }//кушать

    public void move() {
    }//движение

    public void chooseDirection() {
    }

    public void reproduce() {
    }// секс

    public void worker() {
    }// уменьшает сытость

    public void die() {
    }

    public Animal(Map<String, Object> animalChar) {
        this.weight = YamlReader.getDouble(animalChar, "weight");
        this.maxSpeed = YamlReader.getInt(animalChar, "maxSpeed");
        this.maxPerCell = YamlReader.getDouble(animalChar, "maxPerCell");
        this.foodNeeded = YamlReader.getDouble(animalChar, "foodNeeded");
    }
}
