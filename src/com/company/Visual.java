package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;

public abstract class Visual extends Placeable{
    private BufferedImage tmpSprites;
    private BufferedImage scaledBuffered;
    private double scale;
    private int[] pixelsOriginal;
    private int[] pixels;

    public Visual(String pSprite, double pPosX, double pPosY, double pScale){
        super(pPosX, pPosY);
        loadSprite(pSprite);
        scale = pScale;
        Size hitbox = new Size((int)Math.floor(tmpSprites.getWidth()*scale), (int)Math.floor((tmpSprites.getHeight()*scale)));
        setHitbox(hitbox);

        scaledBuffered = new BufferedImage(hitbox.getWidth(), hitbox.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledBuffered.createGraphics();
        g2d.drawImage(tmpSprites, 0, 0, hitbox.getWidth(),hitbox.getHeight(), null);
        g2d.dispose();
        tmpSprites = deepCopy(scaledBuffered);

        pixelsOriginal = ((DataBufferInt)tmpSprites.getRaster().getDataBuffer()).getData();
        pixels = ((DataBufferInt)scaledBuffered.getRaster().getDataBuffer()).getData();
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
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
        return scaledBuffered;
    }

    public Image getSprite(double xc, double yc){
        return scaledBuffered;
    }

    @Override
    public void setPosX(double posX) {
        super.setPosX(posX);
        lightingUpdate();
    }

    @Override
    public void setPosY(double posY) {
        super.setPosY(posY);
        lightingUpdate();
    }

    /**
     *
     */
    public void lightingUpdate(){
        int color;
        int colorOriginal;

        int width2 = scaledBuffered.getWidth();

        for (int pixel = 0, y = 0, x = 0; pixel < pixels.length; pixel++) {
            colorOriginal = pixelsOriginal[pixel];
            color = pixels[pixel];

            Color c = new Color(colorOriginal);

            int r = c.getRed();
            int g = c.getGreen();
            int b = c.getBlue();

            int redLight = 0;
            int greenLight = 0;
            int blueLight = 0;
            double i;

            for(Light light : Level.lights) {
                i = light.withinRadius(getPosX() + x, getPosY() + y);//next problem point

                if(i != -1) {
                    redLight = (int)(redLight + (light.getColor().getRed() * i));
                    greenLight = (int)(greenLight + (light.getColor().getGreen() * i));
                    blueLight = (int)(blueLight + (light.getColor().getBlue() * i));
                }
            }
//58 is goal.
            r = r - (int) (r * ((255 - (double) redLight) / 255));
            r = Math.max(r, 0);
            r = Math.min(r, 255);

            g = g - (int) (g * ((255 - (double) greenLight) / 255));
            g = Math.max(g, 0);
            g = Math.min(g, 255);

            b = b - (int) (b * ((255 - (double) blueLight) / 255));
            b = Math.max(b, 0);
            b = Math.min(b, 255);
            //converts to single integer, constant alpha for now.
            colorOriginal = (-16777216) + (r << 16) + (g << 8) + b;

            if(colorOriginal != (color)){
                pixels[pixel] = colorOriginal;
            }

            x++;
            if (x == width2) {
                x = 0;
                y++;
            }
        }
    }
}
