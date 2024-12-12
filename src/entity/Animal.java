package entity;

import island.Cell;
import island.Coordinate;
import island.Island;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    double initWeight;
    double actualWeight;
    int maxSpeed;
    @Getter
    double maxPerCell;
    double maxSatiety;
    double actualSatiety;
    boolean virginity;
    String ration;
    @Setter
    @Getter
    Coordinate coordinate;

    public Animal() {
        String className = this.getClass().getSimpleName();
        String parentName = this.getClass().getSuperclass().getSimpleName().toLowerCase();
        Map<String, Object> animalChar = AnimalFactory.getANIMALCHARTABLE().get(className);
        this.initWeight = YamlReader.getDouble(animalChar, "weight");
        this.actualWeight = initWeight;
        this.maxSpeed = YamlReader.getInt(animalChar, "maxSpeed");
        this.maxPerCell = YamlReader.getDouble(animalChar, "maxPerCell");
        this.maxSatiety = YamlReader.getDouble(animalChar, "foodNeeded");
        this.actualSatiety = maxSatiety;
        this.virginity = true;
        this.ration = parentName;
    }

    public synchronized void tryToSex(Cell cell) {
        Double birthProbability = ThreadLocalRandom.current().nextDouble();
        if (birthProbability < Setting.REPRODUCTION_PROBABILITY) {
            List<Animal> animals = cell.getAnimalsOnCell();
            if (animals.size() > 1) {
                Optional<Animal> findVirginAnimal = animals.stream().filter(this::canReproduce).findFirst();
                if (findVirginAnimal.isPresent()) {
                    Animal animal = findVirginAnimal.get();
                    this.virginity = false;
                    animal.virginity = false;
                    sexCostAndCheckDeath(this, cell);
                    sexCostAndCheckDeath(animal, cell);
                    checkForDie(cell);
                    try {
                        Animal newAnimal =this.getClass().getConstructor().newInstance();
                        cell.addAnimal(newAnimal);
//            System.out.println("родился "+newAnimal.getClass().getSimpleName());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException("ошибка спаривания " + e);
                    }
                }
            }
        }
    }

    public void sexCostAndCheckDeath(Animal animal, Cell cell) {
        animal.actualSatiety = animal.actualSatiety - animal.actualWeight * 0.05;
        animal.checkForDie(cell);
    }

    public boolean canReproduce(Animal other) {
        return (this.getClass() == other.getClass()) && (other.virginity && this.virginity);
    }

    public abstract void eat(Cell cell);
    public double huntingCost() {
        return this.actualWeight * 0.01;
    }

    public Double getProbability(Animal predator, Animal prey) {
        return PredatorPreyProbability.getPredatorPreyMatrix().getOrDefault(predator.getClass().getSimpleName(), new HashMap<>()).getOrDefault(prey.getClass().getSimpleName(), 0.0);
    }

    public double satietyFromHunting(Animal prey) {
        return prey.actualWeight;
    }

    public boolean isPredator() {
        return this.ration.equals("predators");
    }

    public boolean isOmnivore() {
        return this.ration.equals("omnivore");
    }

    public void move(Cell cell) {

            List<Coordinate> moveDirections = chooseDirection((cell));
            if (!(moveDirections.isEmpty())) {
                Coordinate newCoordinate = moveDirections.get(ThreadLocalRandom.current().nextInt(moveDirections.size()));
                Cell newCell = Island.getISLAND_MAP().get(newCoordinate);
                if (newCoordinate != null && newCell.addAnimal(this)) {
                    cell.removeAnimalFromCell(this);
                    this.setCoordinate(newCoordinate);
                    actualSatiety = actualSatiety - fatigueMovement();
                    this.checkForDie(cell);
                }
            }
            System.out.println(this.getClass().getSimpleName()+" переместился из "+cell.getCoordinate()+" в "+this.getCoordinate());
        }

    public double fatigueMovement() {
        if (this.actualSatiety<this.maxSatiety*0.5){
            this.actualWeight-=this.initWeight*0.2;
        }
        return this.initWeight;
    }

    public List<Coordinate> chooseDirection(Cell cell) {
        List<Coordinate> moveDirections = new ArrayList<>();
        for (int i = -maxSpeed; i <= maxSpeed; i++) {
            for (int j = -maxSpeed; j <= maxSpeed; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Coordinate coordinate = new Coordinate(cell.getXcoordynate() + i, cell.getYcoordynate() + j);
                if (isValidCoordinate(coordinate)) {
                    moveDirections.add(coordinate);
                }
            }
        }
        return moveDirections;
    }

    public boolean isValidCoordinate(Coordinate coordinate) {
        return coordinate.getX() >= 0 && coordinate.getX() < Setting.NUMBER_OF_ROWS && coordinate.getY() >= 0 && coordinate.getY() < Setting.NUMBER_OF_COLUMNS;
    }

    public void checkForDie(Cell cell) {
        if (this.actualWeight < this.initWeight * 0.5 || this.actualSatiety < this.maxSatiety * 0.3) {
            this.die(cell);
        }
    }

    public void die(Cell cell) {
//        System.out.println(this.getClass().getSimpleName()+" умер");
        cell.removeAnimalFromCell(this);
    }

}

