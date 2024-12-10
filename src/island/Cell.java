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

@Data
public class Cell implements Runnable {
    Coordinate coordinate;
    @Getter
    private final CopyOnWriteArrayList<Animal> animalsOnCell = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Plant> plants = new CopyOnWriteArrayList<>();
    public final ConcurrentHashMap<Class<? extends Animal>, Integer> typeOfAnimalOnCell = new ConcurrentHashMap<>();

    public Cell(Coordinate coordinate) {
        this.coordinate = coordinate;
        populateAnimalOnCell();
        populatePlantsOnCell();

    }

    public int countOfAnimalsOnCell() {
        return animalsOnCell.size();
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

    public void populatePlantsOnCell() {
        for (int i = 0; i < Plant.MAX_PLANTS_PER_CELL; i++) {
            plants.add(new Plant());
        }
    }

    public boolean addAnimal(Animal animal) {
        animalsOnCell.add(animal);
        animal.setCoordinate(coordinate);
        return true;
    }



    @Override
    public void run() {
        List<Animal> animalOnCellCopy=new ArrayList<>(animalsOnCell);
        for (Animal animal : animalOnCellCopy) {
            animal.tryToSex(this);
            animal.move(this);
            animal.eat(this);
        }
    }
    public int getXcoordynate(){
        return this.getCoordinate().getX();
    }
    public int getYcoordynate(){
        return this.getCoordinate().getY();
    }
    public void removeAnimalFromCell(Animal animal){
        animalsOnCell.remove(animal);
    }

}




