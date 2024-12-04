package entity.ration.herbivorou;

import entity.Herbivorous;
import lombok.ToString;


public class Boar extends Herbivorous {
    public Boar(double weight, int maxSpeed, double maxPerCell, double maxSatiety, double foodNeeded){
        super(weight, maxSpeed, maxPerCell, maxSatiety, foodNeeded);
    }
}
