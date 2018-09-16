package com.company;

import java.awt.*;

public class Light extends Placeable {
    int radius;
    int radius2;
    int brightness;
    public Color color;

    public Light(double posX, double posY, int radius, Color color){
        super(posX, posY);
        this.radius = radius;
        this.color = color;
        radius2 = radius;
    }


    public int withinRadius(double posX1, double posY1){
        return distance(posX1, posY1, getPosX(), getPosY());
    }

    private int distance(double posX1, double posY1, double posX2, double posY2){
        return (int)Math.sqrt(square(posX2-posX1) + square(posY2-posY1));
    }

    private double square(double value){
        return value * value;
    }

    public void strobe(){
        if (radius == 0) {
            radius = radius2;
        }
        else{
            radius = 0;
        }
    }

    public void fade(){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if(r > 0 && b == 0){
            r--;
            g++;
        }
        if(g > 0 && r == 0){
            g--;
            b++;
        }
        if(b > 0 && g == 0){
            r++;
            b--;
        }
        color = new Color(r,g,b);
    }
}
