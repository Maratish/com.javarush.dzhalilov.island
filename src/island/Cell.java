package island;

import entity.Animal;
import entity.Plant;
import lombok.Data;
import lombok.Getter;
import setting.AnimalFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Cell implements Runnable {
    Coordinate coordinate;
    @Getter
    private final List<Animal> animalsOnCell = new ArrayList<>();
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

    public List<Animal> getAnimalsOnCell() {
        return new ArrayList<>(animalsOnCell);
    }

    public void addAnimal(Animal animal) {
        try {
            Island.getIslandInstance().getIslandLock().lock();
            animalsToAdd.add(animal);
        } catch (Exception e) {
            throw new RuntimeException("ошибка добавления животного " + e);
        } finally {
            Island.getIslandInstance().getIslandLock().unlock();
        }
    }


    public int countOfAllAnimalsOnCell() {
        Island.getIslandInstance().getIslandLock().lock();
        try {
            return animalsOnCell.size();
        } catch (Exception e) {
            throw new RuntimeException("ошибка подсчета животных в клетке " + e);
        } finally {
            Island.getIslandInstance().getIslandLock().unlock();
        }
    }


    public boolean removeAnimalFromCell(Animal animal) {
        Island.getIslandInstance().getIslandLock().lock();
        try {
            return animalsToRemove.add(animal);
        } finally {
            Island.getIslandInstance().getIslandLock().unlock();
        }
    }

    public void populatePlantsOnCell() {
        plantOnCell.setWeight(Plant.MAX_PLANTS_PER_CELL * 0.1);
    }

    public long countSameTypeOnCell(Class<? extends Animal> animal) {
        Island.getIslandInstance().getIslandLock().lock();
        try {
            return animalsOnCell.stream()
                    .filter(e -> animal.isInstance(e))
                    .count();
        } finally {
            Island.getIslandInstance().getIslandLock().unlock();
        }
    }


    public void removeDiedAnimal() {
        Island.getIslandInstance().getIslandLock().lock();
        for (Animal a : animalsToRemove) {
            if (animalsOnCell.contains(a)) {
                animalsOnCell.remove(a);
            }
        }
        Island.getIslandInstance().getIslandLock().unlock();
    }

    public void addBornedAnimal() {
        Island.getIslandInstance().getIslandLock().lock();
        for (Animal a : animalsToAdd) {
            animalsOnCell.add(a);
        }
        Island.getIslandInstance().getIslandLock().unlock();
    }


    @Override
    public void run() {
        List<Animal> currentAnimals = new ArrayList<>(animalsOnCell);
        for (Animal animal : currentAnimals) {
            animal.tryToSex(this);
            animal.move(this);
            animal.eat(this);
            reduceWeightPerDay(animal);
            animal.fatigueMovement();
            animal.checkForDie(this);
        }
        addBornedAnimal();
        animalsToAdd.clear();
        removeDiedAnimal();
        animalsToRemove.clear();
        plantOnCell.growthPlants();
    }

    public int getXcoordynate() {
        return this.getCoordinate().getX();
    }

    public int getYcoordynate() {
        return this.getCoordinate().getY();
    }

    public void reduceWeightPerDay(Animal animal) {
        animal.setActualSatiety(animal.getActualSatiety() - animal.getMaxSatiety() * 0.05);
        if (animal.getActualSatiety() < 0) {
            animal.die(this);
        }
    }

    public double getTotalPlantWeight() {
        return plantOnCell.getWeight();
    }

}