package island;

import setting.Setting;

import java.util.concurrent.*;

public class Island {
    final int ROWS;
    final int COLUMNS;
    final Cell[][] ISLAND_MAP;

    public Island(int rows, int columns) {
        this.ROWS = rows;
        this.COLUMNS = columns;
        ISLAND_MAP = new Cell[rows][columns];
        CountDownLatch reproduceLatch = new CountDownLatch(ROWS * COLUMNS);
        islandInit();
    }

    private void islandInit() {
        ExecutorService executorService = Executors.newFixedThreadPool(Setting.PROCCESORS_CORE);
        CountDownLatch initLatch = new CountDownLatch(ROWS * COLUMNS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                final int row = i;
                final int column = j;
                executorService.execute(() -> {
                    ISLAND_MAP[row][column] = new Cell(row, column);
                    initLatch.countDown();
                });
            }
        }
        try {
            initLatch.await();
            System.out.println("Все клетки инициализированы");
            for (Cell[] row : ISLAND_MAP) {
                for (Cell cell : row) {
                    executorService.execute(cell);
                }
            }

            System.out.println("Спаривание завершено");
            executorService.shutdown();
            if (!executorService.awaitTermination(50, TimeUnit.MILLISECONDS)) {
                System.err.println("Превышен срок ожидания завершения потоков");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}