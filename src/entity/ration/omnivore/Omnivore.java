package entity.ration.omnivore;

import entity.Animal;
import island.Cell;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Omnivore extends Animal {

    public Omnivore() {
        super();
    }

    @Override
    public void eat(Cell cell) {
        List<Animal> animalsOnCellCopy = cell.getAnimalsOnCell();
        if (!(animalsOnCellCopy.isEmpty())) {
            if (this.isOmnivore()) {
                int randomPrey = ThreadLocalRandom.current().nextInt(animalsOnCellCopy.size());
                Animal prey = animalsOnCellCopy.get(randomPrey);
                double probabilityOfEat = getProbabilityOfEat(this, prey);
                Double random = ThreadLocalRandom.current().nextDouble();
                if (random < probabilityOfEat) {
                        satietyFromHunting(prey);
                        prey.die(cell);
                } else {
                    this.setActualSatiety(this.getActualSatiety()*0.9 );
                }
            }
        }
    }
}