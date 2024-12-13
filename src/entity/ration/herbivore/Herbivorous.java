package entity.ration.herbivore;

import entity.Animal;
import entity.Plant;
import island.Cell;
import island.Island;

import java.util.List;

public class Herbivorous extends Animal {
    public Herbivorous() {
        super();
    }

    @Override
    public void eat(Cell cell) {
        if (!(this.isPredator())){
        List<Animal>animalsOnCellCopy=cell.getAnimalsOnCell();
        Plant plantsOnCellCopy=cell.getPlantOnCell();
        if (!(animalsOnCellCopy.isEmpty())&&plantsOnCellCopy!=null)
        if (cell.getPlantOnCell().getWeight() > 0) {
            if (getActualSatiety() < getMaxSatiety() * 0.75) {
                double howFoodNeed = getMaxSatiety() - getActualSatiety();
                double plantsWeight = plantsOnCellCopy.getWeight();
                double howPlantsCanEat = Math.min(plantsWeight, howFoodNeed);
                setActualSatiety(getActualSatiety() + howPlantsCanEat);
                plantsOnCellCopy.reducePlantsWeight(howPlantsCanEat);
            }
        }
    }
}
}

