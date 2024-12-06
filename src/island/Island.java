package island;

import lombok.Data;
import lombok.Getter;
import setting.Setting;

import java.util.concurrent.*;

@Data
public class Island {
    @Getter
    final public static ConcurrentHashMap<Coordinate, Cell> ISLAND_MAP = new ConcurrentHashMap<>();
    private final ScheduledExecutorService schedul = Executors.newSingleThreadScheduledExecutor();
    private final CountDownLatch simulationStepLatch = new CountDownLatch(0);

    public Island() {
        islandInit();

    }

    public int getTotalAnimalCount() {
        return ISLAND_MAP.values().stream()
                .mapToInt(Cell::countOfAnimalsOnCell)
                .sum();
    }

    private void islandInit() {
        ExecutorService islandInitExecutor = Executors.newFixedThreadPool(6);
        CountDownLatch initLatch = new CountDownLatch(Setting.NUMBER_OF_ROWS * Setting.NUMBER_OF_COLUMNS);
        for (int i = 0; i < Setting.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < Setting.NUMBER_OF_COLUMNS; j++) {
                final int row = i;
                final int column = j;
                islandInitExecutor.execute(() -> {
                    Coordinate coordinate = new Coordinate(row, column);
                    ISLAND_MAP.put(coordinate, new Cell(coordinate));
                    initLatch.countDown();
                });
            }
        }
        try {
            initLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Все клетки инициализированы");

        schedul.scheduleAtFixedRate(this::runSimulation, 1, 1, TimeUnit.SECONDS);

    }

    private void runSimulation() {
        ExecutorService animalLifeExecutor = Executors.newFixedThreadPool(6);
        CountDownLatch animalMove = new CountDownLatch(ISLAND_MAP.size());
        for (Cell cell : ISLAND_MAP.values()) {
            animalLifeExecutor.execute(() -> {
                cell.run();
                animalMove.countDown();
            });
        }
                System.out.println(getTotalAnimalCount());
        System.out.println("Спаривание завершено и перемещение завершено ");
        animalLifeExecutor.shutdown();
        try {
            if (!animalLifeExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Превышен срок ожидания завершения потоков");
                animalLifeExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
