package entity;

import lombok.Data;
import lombok.ToString;
import setting.YamlReader;

import java.beans.ConstructorProperties;
import java.util.Map;
import java.util.Objects;
@ToString
@Data
public abstract class Animal {
    final double weight;
    final int maxSpeed;
    final double maxPerCell;
    final double maxSatiety;
    final double foodNeeded;
    public void eat() {}//кушать
    public void move() {}//движение
    public void chooseDirection() {}
    public void reproduce() {}// секс
    public void worker(){}// уменьшает сытость
    public void die() {}

    public Animal(Map<String,Object> animalChar){
        this.weight= YamlReader.getDouble(animalChar, "weight");
        this.maxSpeed=YamlReader.getInt(animalChar, "maxSpeed");;
        this.maxPerCell=YamlReader.getDouble(animalChar, "maxPerCell");;
        this.maxSatiety= YamlReader.getDouble(animalChar, "maxSatiety");;
        this.foodNeeded=YamlReader.getDouble(animalChar, "foodNeeded");;
    }
}
