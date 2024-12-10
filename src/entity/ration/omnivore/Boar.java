package entity.ration.omnivore;

import entity.Animal;
import island.Cell;

import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Omnivore {
    public Boar() {
        super();
    }
    public void eat(Cell cell) {
        if (this.isOmnivore()) {
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

