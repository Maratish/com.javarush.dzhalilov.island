package island;

import lombok.Data;
import lombok.Getter;
import setting.Setting;

import java.util.concurrent.*;

@Data
public class Island {
    @Getter
    final public static ConcurrentHashMap<Coordinate, Cell> ISLAND_MAP = new ConcurrentHashMap<>();

    public Island() {
        islandInit();

    }

    public int getTotalAnimalCount() {
        return ISLAND_MAP.values().stream()
                .mapToInt(Cell::countOfAllAnimalsOnCell)
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
                initLatch.countDown();
            }
        }
        try {
            initLatch.await(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
