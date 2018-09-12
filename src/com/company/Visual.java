package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

public abstract class Visual extends Placeable{
    private BufferedImage tmpSprites;
    private BufferedImage scaledBuffered;
    private double scale;
    private Graphics2D graphics;

    public Visual(String pSprite, double pPosX, double pPosY, double pScale){
        super(pPosX, pPosY);
        loadSprite(pSprite);
        scale = pScale;
        Size hitbox = new Size((int)Math.floor(tmpSprites.getWidth()*scale), (int)Math.floor((tmpSprites.getHeight()*scale)));
        setHitbox(hitbox);

        scaledBuffered = new BufferedImage(hitbox.width, hitbox.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledBuffered.createGraphics();
        g2d.drawImage(tmpSprites, 0, 0, hitbox.width,hitbox.height, null);
        g2d.dispose();
        tmpSprites = deepCopy(scaledBuffered);


        graphics = scaledBuffered.createGraphics();
        graphics.setPaint(new Color (0, 0, 0));
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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
        lightingTest();
        return scaledBuffered;
    }
    
    public void lightingTest(){
        graphics.fillRect(0, 0, scaledBuffered.getWidth(), scaledBuffered.getHeight());

        for (int y = 0; y < scaledBuffered.getHeight(); y = y + 4){
            for(int x = 0; x < scaledBuffered.getWidth(); x = x + 4){
                double i = Level.light.withinRadius(getPosX()+x, getPosY()+y);
                if(i < Level.light.radius){
                    scaledBuffered.setRGB(x, y, tmpSprites.getRGB(x,y));
                }
            }
        }
    }
}
