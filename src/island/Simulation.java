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
    private int day=0;
    private final List<Class<? extends Animal>> animalTypes = Arrays.asList(
            Buffalo.class, Bear.class, Horse.class, Deer.class, Boar.class, Sheep.class, Goat.class,
            Wolf.class, Boa.class, Fox.class, Eagle.class, Rabbit.class, Duck.class, Mouse.class, Caterpillar.class);

    public Simulation() {
        this.island = new Island();
    }

    public void run() {
        scheduler.scheduleAtFixedRate(this::simulationStep, 10, 10, TimeUnit.MILLISECONDS);
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
            printSimulationStep();
            day++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Симуляция прервана: " + e.getMessage());
        }
    }

    private void printSimulationStep() {
        Map<Class<? extends Animal>, Integer> animalCounts = new HashMap<>();
        int totalAnimals = 0;
        int totalPredators = 0;
        int totalHerbivores = 0;
        double totalPlants = 0;

        for (Cell cell : island.getISLAND_MAP().values()) {
            totalPlants += cell.getPlantOnCell().getWeight();
            for (Class<? extends Animal> animalType : animalTypes) {
                int count =(int) cell.countSameTypeOnCell(animalType);
                animalCounts.put(animalType, animalCounts.getOrDefault(animalType, 0) + count);
                totalAnimals += count;
                if (isPredator(animalType)) {
                    totalPredators += count;
                } else if (isHerbivore(animalType)) {
                    totalHerbivores += count;
                }
            }
        }

        StringBuilder animalSummary = new StringBuilder();
        for (Class<? extends Animal> animalType : animalTypes) {
            Animal animalInstance = null;
            try {
                animalInstance = animalType.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            animalSummary.append(animalInstance != null ? animalInstance.toString() : "?").append(animalCounts.get(animalType)).append("  ");
        }


        System.out.println("ДЕНЬ №" + day);
        System.out.println("Всего на острове - " + totalAnimals);
        System.out.println("Хищники: " + totalPredators + " Травоядные: " + totalHerbivores);
        System.out.println(animalSummary + "🍀" + totalPlants);
    }

    private boolean isPredator(Class<? extends Animal> animalClass) {
        return Predators.class.isAssignableFrom(animalClass);
    }

    private boolean isHerbivore(Class<? extends Animal> animalClass) {
        return Herbivorous.class.isAssignableFrom(animalClass);
    }
}


