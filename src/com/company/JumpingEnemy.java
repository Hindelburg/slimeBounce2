package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
public class JumpingEnemy extends Enemy {

    double peakTime = 0;
    double t = 0;
    double speed1;
    double factor;

    public JumpingEnemy(String sprite, double x, double y, double scale, double pSpeed, double pFactor) {
        super(sprite, x, y, scale, pSpeed);
        factor = pFactor;
    }

     @Override
     public void action(){
         if(getPosY() >= 500-getHitbox().height){
             peakTime = System.nanoTime();
             setPosY(getPosY() - 0.1);
         }
         else{
             t = ((System.nanoTime() - peakTime)/(100000000 * factor));
             speed1 = (t - 2)/2;

             //speed = 0.4;
             setPosY(getPosY() + speed1);
         }
         if (getPosX() <= -200) {
             setPosX(1115);
         } else {
             setPosX(getPosX() - getSpeed());
         }
     }
}