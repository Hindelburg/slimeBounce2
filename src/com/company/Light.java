package com.company;

public class Light extends Placeable {
    int radius;
    int brightness;
    public Light(double posX, double posY, int radius){
        super(posX, posY);
        this.radius = radius;
    }


    public double withinRadius(double posX1, double posY1){
        return distance(posX1, posY1, getPosX(), getPosY());
    }

    private double distance(double posX1, double posY1, double posX2, double posY2){
        return Math.sqrt(square(posX2-posX1) + square(posY2-posY1));
    }

    private double square(double value){
        return value * value;
    }
}
