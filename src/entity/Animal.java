package entity;

import island.Cell;
import island.Coordinate;
import island.Island;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import setting.AnimalFactory;
import setting.PredatorPreyProbability;
import setting.Setting;
import setting.YamlReader;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@ToString
@Data
public abstract class Animal {
    private double initWeight;
    private double actualWeight;
    private int maxSpeed;
    private double maxPerCell;
    private double maxSatiety;
    private double actualSatiety;
    private boolean virginity;
    private String ration;

    public Animal() {
        String className = this.getClass().getSimpleName();
        String parentName = this.getClass().getSuperclass().getSimpleName().toLowerCase();
        Map<String, Object> animalChar = AnimalFactory.getANIMALCHARTABLE().get(className);
        this.initWeight = YamlReader.getDouble(animalChar, "weight");
        this.actualWeight = initWeight;
        this.maxSpeed = YamlReader.getInt(animalChar, "maxSpeed");
        this.maxPerCell = YamlReader.getDouble(animalChar, "maxPerCell");
        this.maxSatiety = YamlReader.getDouble(animalChar, "maxSatiety");
        this.actualSatiety = maxSatiety;
        this.virginity = true;
        this.ration = parentName;
    }

    public void tryToReproduce(Cell cell) {
            Double birthProbability = ThreadLocalRandom.current().nextDouble();
            if (!(cell.countSameTypeOnCell(this.getClass()) >= this.getMaxPerCell())) {
                if (birthProbability < Setting.REPRODUCTION_PROBABILITY) {
                    List<Animal> animals = cell.getAnimalsOnCell();
                    if (animals.size() > 1) {
                        Optional<Animal> findVirginAnimal = animals.stream().filter(this::canReproduce).findAny();
                        if (findVirginAnimal.isPresent()) {
                            Animal animal = findVirginAnimal.get();
                            this.virginity = false;
                            animal.virginity = false;
                            checkForDie(cell);
                            try {
                                Animal newAnimal = this.getClass().getConstructor().newInstance();
                                cell.addAnimalOnCell(newAnimal);

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
        return (this.getClass() == other.getClass()) && (other.virginity && this.virginity);
    }

    public abstract void eat(Cell cell);


    public Double getProbabilityOfEat(Animal predator, Animal prey) {
            return PredatorPreyProbability.getPredatorPreyMatrix().getOrDefault(predator.getClass().getSimpleName(), new HashMap<>()).
                    getOrDefault(prey.getClass().getSimpleName(), 0.0);
        }


    public void satietyFromHunting(Animal prey) {
        this.setActualSatiety(Math.min(this.maxSatiety, this.actualSatiety + prey.actualWeight));
    }

    public boolean isPredator() {
        return this.ration.equals("predators");
    }

    public boolean isOmnivore() {
        return this.ration.equals("omnivore");
    }

    public void move(Cell cell) {
            List<Coordinate> moveDirections = chooseDirectionForMovement((cell));
            if (!(moveDirections.isEmpty())) {
                Coordinate newCoordinate = moveDirections.get(ThreadLocalRandom.current().nextInt(moveDirections.size()));
                Cell newCell = Island.getISLAND_MAP().get(newCoordinate);
                if (newCoordinate != null && newCell != null &&
                        !(newCell.countSameTypeOnCell(this.getClass()) >= this.getMaxPerCell())) {
                    newCell.addAnimalOnCell(this);
                    cell.removeAnimalFromCell(this);
                    this.actualSatiety = actualSatiety - satietyReduceFromMovement();
                }
            }
    }

    public double satietyReduceFromMovement() {
        return this.maxSatiety*0.01;
    }

    public List<Coordinate> chooseDirectionForMovement(Cell cell) {
        List<Coordinate> moveDirections = new ArrayList<>();
        if (cell != null && !cell.getAnimalsOnCell().isEmpty()) {
            for (int i = -maxSpeed; i <= maxSpeed; i++) {
                for (int j = -maxSpeed; j <= maxSpeed; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    Coordinate coordinate = new Coordinate(cell.getXcoordynate() + i, cell.getYcoordynate() + j);
                    if (isValidCoordinateForMovement(coordinate) && (!(cell.countSameTypeOnCell(this.getClass()) >= this.getMaxPerCell()))) {
                        moveDirections.add(coordinate);
                    }
                }
            }
        }
        return moveDirections;
    }

    public boolean isValidCoordinateForMovement(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < Setting.NUMBER_OF_ROWS &&
                coordinate.getY() >= 0 && coordinate.getY() < Setting.NUMBER_OF_COLUMNS;
    }

    public boolean checkForDie(Cell cell) {
        if (this.actualSatiety<this.maxSatiety*0.6){
            this.actualWeight-=this.initWeight*0.15;
        }
        if (this.actualWeight < this.initWeight * 0.5 || this.actualSatiety < this.maxSatiety * 0.3) {
            return true;
        } else return false;
    }

    public void die(Cell cell) {
//        Island.getIslandInstance().islandLock.lock();
        cell.removeAnimalFromCell(this);
//        Island.getIslandInstance().islandLock.unlock();
    }

}
