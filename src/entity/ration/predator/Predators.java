package entity.ration.predator;


import entity.Animal;
import island.Cell;
import island.Island;

import java.util.concurrent.ThreadLocalRandom;

public class Predators extends Animal {
    public Predators() {
        super();
    }

    @Override
    public void eat(Cell cell) {
        Island.getIslandInstance().getIslandLock().lock();
        this.checkForDie(cell);
        if (this.isPredator()) {
            int randomPrey = ThreadLocalRandom.current().nextInt(cell.getAnimalsOnCell().size());
            Animal prey = cell.getAnimalsOnCell().get(randomPrey);
            double probabilityOfEat = getProbability(this, prey);
            Double random = ThreadLocalRandom.current().nextDouble();
            if (random < probabilityOfEat) {
                if (this.getActualSatiety() > huntingCost()) {
                    cell.removeAnimalFromCell(prey);
                    cell.getAnimalsOnCell().contains(prey);
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
        Island.getIslandInstance().getIslandLock().unlock();
    }
}
