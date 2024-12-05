package island;

import entity.Animal;
import entity.Plant;
import setting.AnimalFactory;

import java.lang.reflect.InvocationTargetException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Cell implements Runnable {
    int xCoord;
    int yCoord;
    private final CopyOnWriteArrayList<Animal> animalsOnCell = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Plant> plants = new CopyOnWriteArrayList<>();
    public final ConcurrentHashMap<Class<? extends Animal>, Integer> typeOfAnimalOnCell = new ConcurrentHashMap<>();

    public Cell(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        populateAnimalOnCell();
        populatePlantsOnCell();
        calculateAnimalsCount();

    }

    public Cell getCoordinate(){
        return this;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void populateAnimalOnCell() {

        for (Animal animal : AnimalFactory.getAllAnimalList()) {
            double maxOnCell = animal.getMaxPerCell();
            for (int i = 0; i < maxOnCell; i++) {
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

    public void populatePlantsOnCell() {
        for (int i = 0; i < Plant.MAX_PLANTS_PER_CELL; i++) {
            plants.add(new Plant());
        }
    }

    public CopyOnWriteArrayList<Animal> getAnimalsOnCell() {
        return animalsOnCell;

    }

    public void calculateAnimalsCount() {
        for (Animal animal : animalsOnCell) {
            Class<? extends Animal> animalClass = animal.getClass();
            if (typeOfAnimalOnCell.containsKey(animalClass)) {
                AtomicInteger count = new AtomicInteger(typeOfAnimalOnCell.get(animalClass));
                typeOfAnimalOnCell.put(animalClass, count.incrementAndGet());
            } else {
                typeOfAnimalOnCell.put(animalClass, 1);
            }
        }
    }

    public void addAnimal(Animal animal) {
        animalsOnCell.add(animal);
    }

    @Override
    public void run() {
          for (Animal animal:animalsOnCell){
              animal.tryToSex(this);
          }
        }
    }




