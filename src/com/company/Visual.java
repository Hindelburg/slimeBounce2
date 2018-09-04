package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public abstract class Visual extends Placeable{
    private BufferedImage tmpSprites;
    private Image sprites;
    private double scale;

    public Visual(String pSprite, double pPosX, double pPosY, double pScale){
        super(pPosX, pPosY);
        loadSprite(pSprite);
        scale = pScale;
        Size hitbox = new Size((int)Math.floor(tmpSprites.getWidth()*scale), (int)Math.floor((tmpSprites.getHeight()*scale)));
        setHitbox(hitbox);

        sprites = tmpSprites.getScaledInstance(hitbox.width,hitbox.height, Image.SCALE_DEFAULT);
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
}
