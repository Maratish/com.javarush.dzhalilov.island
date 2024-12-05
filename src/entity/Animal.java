package entity;

import island.Cell;
import setting.AnimalFactory;

import setting.Setting;

import java.lang.reflect.InvocationTargetException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal {
    double weight;
    int maxSpeed;
    double maxPerCell;
    double maxSatiety;
    double actualSatiety;
    boolean virginity;
    static AtomicInteger animalCount = new AtomicInteger();

    public double getMaxPerCell() {
        return maxPerCell;
    }

    public Animal() {
        String className = this.getClass().getSimpleName();
        String parentName = this.getClass().getSuperclass().getSimpleName().toLowerCase();
        Map<String, Object> animalChar = AnimalFactory.getANIMALCHARTABLE().get(className);
        this.weight = getDouble(animalChar, "weight");
        this.maxSpeed = getInt(animalChar, "maxSpeed");
        this.maxPerCell = getDouble(animalChar, "maxPerCell");
        this.maxSatiety = getDouble(animalChar, "foodNeeded");
        this.actualSatiety = maxSatiety;
        this.virginity = true;


    }

    private double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing value for key: " + key);
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Invalid type for key: " + key);
        }
        return ((Number) value).doubleValue();
    }

    private int getInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException(key);
        }
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(key);
        }
        return ((Number) value).intValue();
    }


    public boolean isVirginity() {
        return virginity;
    }

    public void tryToSex(Cell cell) {
        if (cell.getAnimalsOnCell().size() > 2) {
            for (Animal animal : cell.getAnimalsOnCell()) {
                if (this.canReproduce(animal)) {
                    ThreadLocalRandom localRandom = ThreadLocalRandom.current();
                    this.virginity = false;
                    animal.virginity = false;
                    if (localRandom.nextDouble() > Setting.REPRODUCTION_PROBABILITY) {
                        try {
                            cell.addAnimal(this.getClass().getConstructor().newInstance());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                 NoSuchMethodException e) {
                            throw new RuntimeException("ошибка спаривания " + e);
                        }
                    }
                }
            }
        }
    }

    public boolean canReproduce(Animal other) {
        return (this.getClass() == other.getClass()) && (this.virginity == true && other.virginity == true);
    }

    @Override
    public String toString() {
        return STR."\{this.getClass().getSimpleName()}{weight=\{weight}, maxSpeed=\{maxSpeed}, maxPerCell=\{maxPerCell}, maxSatiety=\{maxSatiety}, actualSatiety=\{actualSatiety}}";
    }
}
