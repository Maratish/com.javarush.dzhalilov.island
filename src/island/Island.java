package island;

import lombok.Data;
import lombok.Getter;
import setting.Setting;

import java.util.Map;
import java.util.concurrent.*;

@Data
public class Island {
    public static int ROWS=0;
    public static int COLUMNS=0;
    @Getter
    final private ConcurrentHashMap<Coordinate, Cell> ISLAND_MAP = new ConcurrentHashMap<>();



    public Island(int rows, int columns) {
        ROWS = rows;
        COLUMNS = columns;
        islandInit();
    }

    private void islandInit() {
        ExecutorService executorService = Executors.newFixedThreadPool(Setting.PROCCESORS_CORE);
        CountDownLatch initLatch = new CountDownLatch(ROWS * COLUMNS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                    final int row=i;
                    final int column=j;
                executorService.execute(() -> {
                    Coordinate coordinate = new Coordinate(row, column);
                    ISLAND_MAP.put(coordinate, new Cell(coordinate));
                    initLatch.countDown();
                });
            }
        }
        try {
            initLatch.await();
            System.out.println("Все клетки инициализированы");
            for (Cell cell : ISLAND_MAP.values()) {
                executorService.execute(cell);
            }
            System.out.println("Спаривание завершено");
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Превышен срок ожидания завершения потоков");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //TODO заставить многопоточно двигаться животных
    }
}