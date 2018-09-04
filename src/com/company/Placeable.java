package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Placeable {
    private double defaultPosX;
    private double defaultPosY;
    private double posX, posY;
    private Size hitbox;

    public Placeable(double pPosX, double pPosY){
        posX = pPosX;
        posY = pPosY;
        defaultPosX = posX;
        defaultPosY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getDefaultPosX() {
        return defaultPosX;
    }

    public double getDefaultPosY() {
        return defaultPosY;
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
    public void setHitbox(Size hitbox){
        this.hitbox = hitbox;
    }
}
