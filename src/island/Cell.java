package island;

import entity.Animal;
import entity.Plant;
import lombok.Data;
import lombok.Getter;
import setting.AnimalFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Data
    public class Cell implements Runnable {
        Coordinate coordinate;
        @Getter
        private final List<Animal> animalsOnCell = new ArrayList<>();
        @Getter
        private final ReentrantLock lock = new ReentrantLock();
        @Getter
        private final Plant plantOnCell = new Plant();


        public Cell(Coordinate coordinate) {
            this.coordinate = coordinate;
            populateAnimalOnCell();
            populatePlantsOnCell();

        }
        public void populateAnimalOnCell() {
            for (Animal animal : AnimalFactory.getAllAnimalList()) {
                double maxOnCell = animal.getMaxPerCell();
                for (int i = 0; i < maxOnCell * ThreadLocalRandom.current().nextDouble(0.6); i++) {
                    Animal newAnimal = null;
                    try {
                        newAnimal = (animal.getClass().getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException("Ошибка заселения клетки" + e);
                    }
                    animalsOnCell.add(newAnimal);
                }
            }
        }

        public boolean addAnimal(Animal animal) {
            lock.lock();
            try {
                if (countSameTypeOnCell(animal.getClass()) < animal.getMaxPerCell()) {
//                    animalsOnCell.add(animal);
                    animal.setCoordinate(coordinate);
                    return true;
                } else {
                    return false;
                }
            } finally {
                lock.unlock();
            }
        }

        public int countOfAllAnimalsOnCell() {
            return animalsOnCell.size();
        }


        public void removeAnimalFromCell(Animal animal) {
            lock.lock();
            animalsOnCell.remove(animal);
            lock.unlock();
        }

        public void populatePlantsOnCell() {
            plantOnCell.setWeight(Plant.MAX_PLANTS_PER_CELL*0.4);
        }

        public long countSameTypeOnCell(Class<? extends Animal> animal) {
            return animalsOnCell.stream()
                    .filter(e -> animal.isInstance(e))
                    .count();
        }




            @Override
            public void run () {
                for (Animal animal : animalsOnCell) {
                    animal.tryToSex(this);
                    animal.move(this);
                    animal.eat(this);
                    plantOnCell.growthPlants();
                }
            }

            public int getXcoordynate () {
                return this.getCoordinate().getX();
            }

            public int getYcoordynate () {
                return this.getCoordinate().getY();
            }


        }




