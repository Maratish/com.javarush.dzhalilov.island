package entity;

import island.Cell;
import island.Coordinate;
import island.Island;
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
public abstract class Animal {
    double weight;
    int maxSpeed;
    @Getter
    double maxPerCell;
    double maxSatiety;
    double actualSatiety;
    boolean virginity;
    @Getter
    @Setter
    Coordinate coordinate;

    public Animal() {
        String className = this.getClass().getSimpleName();
        String parentName = this.getClass().getSuperclass().getSimpleName().toLowerCase();
        Map<String, Object> animalChar = AnimalFactory.getANIMALCHARTABLE().get(className);
        this.weight = YamlReader.getDouble(animalChar, "weight");
        this.maxSpeed = YamlReader.getInt(animalChar, "maxSpeed");
        this.maxPerCell = YamlReader.getDouble(animalChar, "maxPerCell");
        this.maxSatiety = YamlReader.getDouble(animalChar, "foodNeeded");
        this.actualSatiety = maxSatiety;
        this.virginity = true;
    }


    public synchronized void tryToSex(Cell cell) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        if (localRandom.nextDouble(1) < Setting.REPRODUCTION_PROBABILITY) {
            List<Animal> animals = cell.getAnimalsOnCell();
            if (animals.size() > 1) {
                Optional<Animal> findVirginAnimal = animals.stream().filter(this::canReproduce).findFirst();
                if (findVirginAnimal.isPresent()) {
                    Animal animal = findVirginAnimal.get();
                    this.virginity = false;
                    animal.virginity = false;
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

    public boolean canReproduce(Animal other) {
        return (this.getClass() == other.getClass()) && (other.virginity && this.virginity);
    }

    public synchronized void eat(Cell cell) {}

    public synchronized void move(Cell cell) {
        if (this.actualSatiety == this.maxSatiety) {
            List<Coordinate> moveDirections = chooseDirection((cell));
            if (!(moveDirections.isEmpty())) {
                Coordinate newCoordinate = moveDirections.get(ThreadLocalRandom.current().nextInt(moveDirections.size()));
                Cell newCell = Island.getISLAND_MAP().get(newCoordinate);
                if (newCoordinate != null && newCell.addAnimal(this)) ;
                cell.removeAnimalFromCell(this);
                this.setCoordinate(newCoordinate);
            }

        }
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
        return coordinate.getX() >= 0 && coordinate.getX() < Setting.NUMBER_OF_ROWS &&
                coordinate.getY() >= 0 && coordinate.getY() < Setting.NUMBER_OF_COLUMNS;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
    public void die(Island islandMap){
        Cell cell = Island.ISLAND_MAP.get(this.getCoordinate());
        cell.removeAnimalFromCell(this);
    }

    public Double getProbability(String predator, String prey) {
        return PredatorPreyProbability.getPredatorPreyMatrix().getOrDefault(predator, new HashMap<>()).getOrDefault(prey, 0.0);
    }

}
