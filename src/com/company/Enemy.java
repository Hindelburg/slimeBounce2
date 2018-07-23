package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Enemy extends Placeable{
    private Timer t = new Timer(1, new Enemy.Listener());

    //The background workers.
    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            action();
        }
    }

    public void action() {
        if (getPosX() <= -200) {
            setPosX(1115);
        } else {
            super.setPosX(super.getPosX() - speed);
        }
    }

    private double speed;

    public Enemy(String sprite, double x, double y, double pScale, double pSpeed){
        super(sprite,x,y,pScale);
        speed = pSpeed;
        t.start();
    }

    public double getSpeed(){
        return speed;
    }
}
