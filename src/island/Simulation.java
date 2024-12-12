package island;

import entity.Animal;
import entity.ration.herbivore.*;
import entity.ration.omnivore.Boar;
import entity.ration.predator.*;
import setting.Setting;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Simulation implements Runnable {
    private final Island island;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService simulationExecutor = Executors.newFixedThreadPool(Setting.PROCCESORS_CORE);
    private int day = 0;
    private final List<Class<? extends Animal>> animalTypes = Arrays.asList(
            Buffalo.class, Bear.class, Horse.class, Deer.class, Boar.class, Sheep.class, Goat.class,
            Wolf.class, Boa.class, Fox.class, Eagle.class, Rabbit.class, Duck.class, Mouse.class, Caterpillar.class);

    public Simulation() {
        this.island = new Island();
    }

    public void run() {
        scheduler.scheduleAtFixedRate(this::simulationStep, 50, 50, TimeUnit.MILLISECONDS);
    }

    private void simulationStep() {
        CountDownLatch animalMove = new CountDownLatch(island.getISLAND_MAP().size());
        for (Cell cell : island.getISLAND_MAP().values()) {
            simulationExecutor.submit(() -> {
                try {
                    cell.run();
                } finally {
                    animalMove.countDown();
                }
            });
        }
        try {
            animalMove.await();
            simulationOutput();
            day++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Симуляция прервана: " + e.getMessage());
        }
    }

    private void simulationOutput(){
        System.out.println(day);
        int animalCount=0;
        for (Cell value : island.getISLAND_MAP().values()) {
            animalCount+=value.getAnimalsOnCell().size();
        }
        System.out.println(animalCount);

    }
}
