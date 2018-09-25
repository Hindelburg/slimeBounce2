package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Background {
    private double posX = 0;
    private double posY = 0;
    private Size hitbox;
    private BufferedImage sprite;
    private Image test;
    private double distance;

    public Background(String pSprite, double pDistance, double scale, double y){
        loadSprite(pSprite);

        distance = pDistance;
        posY = y;
        hitbox = new Size((int)Math.floor(sprite.getWidth()*scale), (int)Math.floor((sprite.getHeight()*scale)));
        test = sprite.getScaledInstance(hitbox.getWidth(), hitbox.getHeight(), Image.SCALE_DEFAULT);
    }

    private void loadSprite(String s){
        try {
            sprite = ImageIO.read(new File(s));
        }
        catch(Exception e){
            System.out.println("ERROR");
        }
    }

    public double getDistance(){
        return distance;
    }

    public Image getSprite(){
        return test;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public Size getHitbox(){
        return hitbox;
    }
}
