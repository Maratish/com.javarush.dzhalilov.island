package island;

import entity.Animal;
import entity.Plant;
import lombok.Data;
import lombok.Getter;
import setting.AnimalFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Cell implements Runnable {
    Coordinate coordinate;
    @Getter
    private final List<Animal> animalsOnCell = new ArrayList<>();
    private final Plant plantOnCell = new Plant();

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        populateAnimalOnCell();
        populatePlantsOnCell();

    }

    public void populateAnimalOnCell() {
        for (Animal animal : AnimalFactory.getAllAnimalList()) {
            double maxOnCell = animal.getMaxPerCell();
            for (int i = 0; i < maxOnCell * ThreadLocalRandom.current().nextDouble(0.6); i++) {
                Animal newAnimal = null;
                try {
                    newAnimal = (animal.getClass().getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("Ошибка заселения клетки" + e);
                }
                animalsOnCell.add(newAnimal);
            }
        }
    }

    public List<Animal> getAnimalsOnCell() {
        return new ArrayList<>(animalsOnCell);
    }

    public void addAnimalOnCell(Animal animal) {
        Island.getIslandInstance().islandLock.lock();
        try {
            animalsOnCell.add(animal);
        } finally {
            Island.getIslandInstance().islandLock.unlock();
        }
    }


    public boolean removeAnimalFromCell(Animal animal) {
        Island.getIslandInstance().islandLock.lock();
        try {
            return animalsOnCell.remove(animal);
        } finally {
            Island.getIslandInstance().islandLock.unlock();
        }
    }

    public void populatePlantsOnCell() {
        plantOnCell.setWeight(Plant.MAX_PLANTS_PER_CELL * 0.1);
    }

    public long countSameTypeOnCell(Class<? extends Animal> animal) {
        Island.getIslandInstance().islandLock.lock();
        try {
          return  animalsOnCell.stream()
                    .filter(e -> animal.isInstance(e))
                    .count();
        }finally {
            Island.getIslandInstance().islandLock.unlock();
        }
    }

    public int countOfAllAnimalsOnCell() {
        Island.getIslandInstance().islandLock.lock();
        try {
            return animalsOnCell.size();
        } finally {
            Island.getIslandInstance().islandLock.unlock();
        }
    }


    @Override
    public void run() {

            List<Animal> animalsOnCellCopy = new ArrayList<>(animalsOnCell);
            for (Animal animal : animalsOnCellCopy) {
                if (animal.checkForDie(this)){
                    System.out.println(animal+" "+animal.getActualWeight());
//                    System.out.println(animal+" "+animal.getActualWeight()+" вес "+animal.getActualSatiety()+" насыщение");
                    this.removeAnimalFromCell(animal);
                    continue;
                }
                animal.tryToReproduce(this);
                animal.move(this);
                animal.eat(this);
                reduceWeightPerDay(animal);
                animal.satietyReduceFromMovement();
            }
            plantOnCell.growthPlants();

    }

    public int getXcoordynate() {
        return this.getCoordinate().getX();
    }

    public int getYcoordynate() {
        return this.getCoordinate().getY();
    }

    public void reduceWeightPerDay(Animal animal) {
        animal.setActualSatiety(animal.getActualSatiety() - animal.getMaxSatiety() * 0.01);
        if (animal.getActualSatiety() < 0) {
            animal.die(this);
        }
    }

    public double getTotalPlantWeight() {
        return plantOnCell.getWeight();
    }

}