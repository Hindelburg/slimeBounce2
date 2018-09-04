package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Obj extends Visual{
    public int collision;
    public Obj(String sprite, double x, double y, double scale, int collision){
        super(sprite, x, y, scale);
        this.collision = collision;
    }
}