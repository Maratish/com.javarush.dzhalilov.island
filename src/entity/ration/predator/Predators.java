package entity.ration.predator;


import entity.Animal;
import island.Cell;

import java.util.concurrent.ThreadLocalRandom;

public class Predators extends Animal {
    public Predators() {
        super();
    }

    public void eat(Cell cell) {
        if (this.isPredator()) {
            int randomPrey = ThreadLocalRandom.current().nextInt(cell.getAnimalsOnCell().size());
            Animal prey = cell.getAnimalsOnCell().get(randomPrey);
            double probabilityOfEat = getProbability(this, prey);
            Double random = ThreadLocalRandom.current().nextDouble();
            if (random < probabilityOfEat) {
                if (this.getActualSatiety() > huntingCost()) {
                    cell.removeAnimalFromCell(prey);
                    this.setActualSatiety(this.getActualSatiety() + this.satietyFromHunting(prey) - huntingCost());
                    checkForDie(cell);
                } else {
                    die(cell);
                }
            } else {
                this.setActualSatiety(this.getActualSatiety() - this.huntingCost());
                checkForDie(cell);
            }
        }
    }
}
