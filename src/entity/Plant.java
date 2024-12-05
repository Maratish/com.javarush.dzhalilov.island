package entity;

import setting.Setting;

public class Plant {
    public Plant() {
    }

    public static final double PLANT_MAX_WEIGHT_PER_UNIT = Setting.PLANT_MAX_WEIGHT_PER_UNIT;
    public static final int MAX_PLANTS_PER_CELL = Setting.MAX_PLANTS_PER_CELL;
    public static final double PLANT_GROWTH_PER_CYCLE = Setting.PLANT_GROWTH_PER_CYCLE;
    private double weight;

    public void grow() {
        weight += PLANT_GROWTH_PER_CYCLE;
    }

    public void reduceWeight(double amount) {
        weight = Math.max(weight - amount, 0);
    }
}


