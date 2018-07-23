package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Placeable {
    private double defaultPosX;
    private double defaultPosY;
    private double posX;
    private double posY;
    private double scale;
    private Size hitbox;
    private BufferedImage tmpSprites;
    private Image sprites;

    public Placeable(String pSprite, double pPosX, double pPosY, double pScale){
        loadSprite(pSprite);
        posX = pPosX;
        scale = pScale;
        hitbox = new Size((int)Math.floor(tmpSprites.getWidth()*scale), (int)Math.floor((tmpSprites.getHeight()*scale)));
        posY = pPosY-hitbox.height;
        sprites = tmpSprites.getScaledInstance(hitbox.width,hitbox.height, Image.SCALE_DEFAULT);
        defaultPosX = posX;
        defaultPosY = posY;
    }

    private void loadSprite(String s){
        try {
            tmpSprites = ImageIO.read(new File(s));
        }
        catch(Exception e){
            System.out.println("ERROR");
        }
    }

    public Image getSprite(){
        return sprites;
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
}
