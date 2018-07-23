package com.company;

import java.awt.*;

public class BoundingBox {
    private double posX = 0;
    private double posY = 0;
    private Size hitbox;

    public BoundingBox(int width, int height){
        hitbox = new Size(width, height);
    }
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public Size getHitbox(){
        return hitbox;
    }
}
