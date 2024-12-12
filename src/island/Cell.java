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
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Cell implements Runnable {
    Coordinate coordinate;
    @Getter
    private final List<Animal> animalsOnCell = new ArrayList<>();
    @Getter
    ReentrantLock lock = new ReentrantLock();
    private final Plant plantOnCell = new Plant();
    private final List<Animal> animalsToRemove = new ArrayList<>();
    private final List<Animal> animalsToAdd = new ArrayList<>();


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

    public void addAnimal(Animal animal) {
        lock.lock();
        try {
            animalsToAdd.add(animal);
        } catch (Exception e) {
            throw new RuntimeException("ошибка добавления животного " + e);
        } finally {
            lock.unlock();
        }
    }


    public int countOfAllAnimalsOnCell() {
        try {
            return animalsOnCell.size();
        } catch (Exception e) {
            throw new RuntimeException("ошибка подсчета животных в клетке " + e);
        }
    }


    public boolean removeAnimalFromCell(Animal animal) {
        lock.lock();
        try {
            return animalsToRemove.add(animal);
        } finally {
            lock.unlock();
        }
    }

    public void populatePlantsOnCell() {
        plantOnCell.setWeight(Plant.MAX_PLANTS_PER_CELL * 0.1);
    }

    public long countSameTypeOnCell(Class<? extends Animal> animal) {
        try {
            return animalsOnCell.stream()
                    .filter(e -> animal.isInstance(e))
                    .count();
        } finally {
        }
    }

    public void removeDiedAnimal() {
        lock.lock();
        for (Animal a : animalsToRemove) {
            if (animalsOnCell.contains(a)) {
                animalsOnCell.remove(a);
            }
        }
        lock.unlock();
    }

    public void addBornedAnimal() {
        lock.lock();
        for (Animal a : animalsToAdd) {
            animalsOnCell.add(a);
        }
        lock.unlock();
    }

@Override
public void run() {
    for (Animal animal : animalsOnCell) {
        animal.tryToSex(this);
        animal.move(this);
        animal.eat(this);
        plantOnCell.growthPlants();
        reduceWeightPerDay(animal);
        animal.checkForDie(this);
        removeDiedAnimal();
    }

}

public int getXcoordynate() {
    return this.getCoordinate().getX();
}

public int getYcoordynate() {
    return this.getCoordinate().getY();
}

public void reduceWeightPerDay(Animal animal) {;
    animal.setActualSatiety(animal.getActualSatiety() - animal.getMaxSatiety() * 0.9);
    if (animal.getActualSatiety()<0){
        animal.die(this);
    }
}

}