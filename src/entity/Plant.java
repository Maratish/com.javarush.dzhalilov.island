package entity;

import island.Coordinate;
import lombok.Getter;
import lombok.Setter;
import setting.Setting;

public class Plant {

    @Setter
    @Getter
    Coordinate coordinate;


    public static final int MAX_PLANTS_PER_CELL = 200;
    public static final double PLANT_GROWTH_PER_CYCLE = 0.1;
    @Getter @Setter
    private double weight;

    public Plant() {
    }

    public void growthPlants() {
        if (weight < 200) {
            weight += Math.min(weight * 0.01, MAX_PLANTS_PER_CELL - weight);
        }
    }

    public void reducePlantsWeight ( double reducedWeight){
        weight = Math.max(0,weight-reducedWeight);
        }
    }


