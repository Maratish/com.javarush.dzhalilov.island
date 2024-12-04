package entity.ration.predator;

import lombok.AllArgsConstructor;


public class Bear extends Predator {
    public Bear(double weight, int maxSpeed, double maxPerCell, double maxSatiety, double foodNeeded){
        super(weight, maxSpeed, maxPerCell, maxSatiety, foodNeeded);
    }
}
