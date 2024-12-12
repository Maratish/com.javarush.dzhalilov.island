package entity.ration.omnivore;

import entity.Animal;
import island.Cell;

import java.util.concurrent.ThreadLocalRandom;

public class Omnivore extends Animal {

    public Omnivore() {
        super();
    }
    @Override
    public void eat(Cell cell) {
        this.checkForDie(cell);
        if (this.isOmnivore()) {
            int randomPrey = ThreadLocalRandom.current().nextInt(cell.getAnimalsOnCell().size());
            Animal prey = cell.getAnimalsOnCell().get(randomPrey);
            double probabilityOfEat = getProbability(this, prey);
            Double random = ThreadLocalRandom.current().nextDouble();
            if (random < probabilityOfEat) {
                if (this.getActualSatiety() > huntingCost()) {
                    prey.die(cell);
                    satietyFromHunting(prey);
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