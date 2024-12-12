package entity.ration.herbivore;

import entity.Animal;
import island.Cell;

public class Herbivorous extends Animal {
    public Herbivorous() {
        super();
    }

    @Override
    public void eat(Cell cell) {
        this.checkForDie(cell);
        if (cell.getPlantOnCell().getWeight() > 0) {
            if (getActualSatiety() < getMaxSatiety() * 0.75) {
                double howFoodNeed = getMaxSatiety() - getActualSatiety();
                double plantsWeight = cell.getPlantOnCell().getWeight();
                double howPlantsCanEat = Math.min(plantsWeight, howFoodNeed);
                setActualSatiety(getActualSatiety() + howPlantsCanEat);
                cell.getPlantOnCell().reducePlantsWeight(howPlantsCanEat);
                this.checkForDie(cell);
            }
        }
    }
}

