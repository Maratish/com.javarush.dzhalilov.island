package island;

import entity.Animal;
import lombok.Data;
import lombok.Getter;
import setting.Setting;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Island {
    @Getter
    final public static ConcurrentHashMap<Coordinate, Cell> ISLAND_MAP = new ConcurrentHashMap<>();
    @Getter
    private static Island islandInstance;

    public ReentrantLock islandLock = new ReentrantLock();

    public Island() {

    }

    public static synchronized Island getIslandInstance() {
        if (islandInstance == null) {
            islandInstance = new Island();
            islandInstance.islandInit();
        }
        return islandInstance;
    }

    public int getTotalAnimalCount() {
        return ISLAND_MAP.values().stream()
                .mapToInt(Cell::countOfAllAnimalsOnCell)
                .sum();
    }

    public Map<Class<? extends Animal>, Integer> getAnimalCountsOnCell() {
        Map<Class<? extends Animal>, Integer> counts = new ConcurrentHashMap<>();
        for (Cell cell : ISLAND_MAP.values()) {
            for (Animal animal : cell.getAnimalsOnCell()) {
                counts.put(animal.getClass(), counts.getOrDefault(animal.getClass(), 0) + 1);
            }
        }
        return counts;
    }

    public int getTotalCountOfPredators() {
        int predatorCount = 0;
        for (Cell cell : Island.getISLAND_MAP().values()) {
            for (Animal animal : cell.getAnimalsOnCell()) {
                if (animal.isPredator()) {
                    predatorCount++;
                }
            }
        }
        return predatorCount;
    }

    public int getTotalCountOfHerbivorous() {
        int herbivoreCount = 0;
        for (Cell cell : Island.getISLAND_MAP().values()) {
            for (Animal animal : cell.getAnimalsOnCell()) {
                if (!animal.isPredator()) {
                    herbivoreCount++;
                }
            }
        }
        return herbivoreCount;
    }

    public double getTotalPlantWeight() {
        return ISLAND_MAP.values().stream()
                .mapToDouble(Cell::getTotalPlantWeight)
                .sum();
    }

    private void islandInit() {
        CountDownLatch initLatch = new CountDownLatch(Setting.NUMBER_OF_ROWS * Setting.NUMBER_OF_COLUMNS);
        for (int i = 0; i < Setting.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < Setting.NUMBER_OF_COLUMNS; j++) {
                final int row = i;
                final int column = j;
                Coordinate coordinate = new Coordinate(row, column);
                ISLAND_MAP.put(coordinate, new Cell(coordinate));
                System.out.println("Создана клетка с координатами: " + coordinate);
                initLatch.countDown();
            }
        }
        try {
            initLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

