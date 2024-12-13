package island;

import entity.Animal;
import entity.ration.herbivore.*;
import entity.ration.omnivore.Boar;
import entity.ration.predator.*;
import setting.Setting;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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
        scheduler.scheduleAtFixedRate(this::simulationStep, 0, Setting.SCHEDULER_PERIOD_MS, TimeUnit.MILLISECONDS);
    }

    private void simulationStep() {
        int islandSize = island.getISLAND_MAP().size();
        System.out.println("размер карты = "+islandSize);
        if (islandSize<=0) System.err.println("КАРТА ПУСТА ИЛИ ОТРИЦАТЕЛЬНА");
        CountDownLatch animalMove = new CountDownLatch(island.getISLAND_MAP().size());
        for (Cell cell : island.getISLAND_MAP().values()) {
            simulationExecutor.execute(() -> {
                try {
                    cell.run();
                } catch (Exception e){
                    throw  new RuntimeException("АШИПКА"+e);
                }
                finally {
                    animalMove.countDown();
                }
            });
        }
        try {
            animalMove.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        simulationOutput();
        day++;
    }


    private void simulationOutput() {
        System.out.println("ДЕНЬ №" + day);
        System.out.println("Всего на острове - " + island.getTotalAnimalCount());
        System.out.println("Хищники: " + island.getTotalCountOfPredators() + " Травоядные: " + island.getTotalCountOfHerbivorous());

        Map<Class<? extends Animal>, Integer> animalCounts = island.getAnimalCountsOnCell();

        StringBuilder animalSummary = new StringBuilder();
        for (Class<? extends Animal> animalType : animalTypes) {
            Integer count = animalCounts.get(animalType);
            Animal animal = null;
            try {
                animal = animalType.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            String animalRepresentation = (animal != null) ? animal.toString() : "?";
            animalSummary.append(animalRepresentation).append(count != null ? count : 0).append("  ");
        }
        animalSummary.append("🍀").append(island.getTotalPlantWeight());
        System.out.println(animalSummary);
    }

}
