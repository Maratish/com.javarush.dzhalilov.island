package entity;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public abstract class Animal {
    double weight;
    int maxSpeed;
    final double maxSatiety = 0;
    double actualSatiety;
    public void eat() {}//кушать
    public void move() {}//движение
    public void chooseDirection() {}
    public void reproduce() {}// секс
    public void worker(){}// уменьшает сытость
    public void die() {}
}
