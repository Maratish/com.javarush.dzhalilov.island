package entity.ration.predator;


    import entity.Animal;
    import island.Cell;

    import java.util.List;
    import java.util.concurrent.ThreadLocalRandom;

    public class Predators extends Animal {
        public Predators() {
            super();
        }

        @Override
        public void eat(Cell cell) {
            List<Animal> animalsOnCellCopy = cell.getAnimalsOnCell();
            if (!(animalsOnCellCopy.isEmpty())) {
                if (this.isPredator()) {
                    int randomPrey = ThreadLocalRandom.current().nextInt(animalsOnCellCopy.size());
                    Animal prey = animalsOnCellCopy.get(randomPrey);
                    double probabilityOfEat = getProbabilityOfEat(this, prey);
                    Double random = ThreadLocalRandom.current().nextDouble();
                    if (random < probabilityOfEat) {
                            satietyFromHunting(prey);
                            cell.removeAnimalFromCell(prey);
                        }
                    } else {
                        this.setActualSatiety(this.getActualSatiety() - this.getInitWeight()*0.01);
                    }
                }
            }
        }
