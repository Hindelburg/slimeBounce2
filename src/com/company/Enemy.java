package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Enemy extends Entity{
    public Enemy(String pSprite, double posX, double posY, double scale, double maxJumpTime, double gravity, double jumpSpeed, double maxSpeed, double acc) {
        super(pSprite, posX, posY, scale, maxJumpTime, gravity, jumpSpeed, maxSpeed, acc);
        tryJumping = true;
    }
}
