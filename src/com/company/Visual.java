package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;

public abstract class Visual extends Placeable{
    private BufferedImage tmpSprites;
    private BufferedImage scaledBuffered;
    private double scale;
    private Graphics2D graphics;
    private int[] pixelsOriginal;
    private int[] pixels;

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
        pixelsOriginal = ((DataBufferInt)tmpSprites.getRaster().getDataBuffer()).getData();
        pixels = ((DataBufferInt)scaledBuffered.getRaster().getDataBuffer()).getData();
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
        return scaledBuffered;
    }

    public Image getSprite(double xc, double yc){
        lightingTest(xc, yc);
        return scaledBuffered;
    }

    /***
     * I need to find a good way to calculate if a pixel is on the screen, or if a block is on the screen. First, lets
     * filter out blocks that are off screen entirely, that part should be simple. First, I find the barrier values, the
     * edge of the screen plus a little to be safe. Then, I simply check to see if any part of the object falls within
     * this field.
     *
     * The harder part will be actually making it so lighting isn't effecting things outside of this radius. So, I need
     * to have it modify the starting and ending y and x values. Then, using that, do the standard thingy. I don't think
     * it will solve all my problems, but it might make is easier to optimize this to make it work. I MIGHT not have to
     * rethink the whole thing from the ground up. Anyways, that's my bit, now you know what you have to do. I will
     * write more about how I will do it soon.
     */

    public void lightingTest(double xc, double yc){
        int color = 0;
        int colorOriginal = 0;

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
            int i;

            for(Light light : Level.lights) {
                i = light.withinRadius(getPosX() + x, getPosY() + y);

                int tmpB = Math.max(light.radius-i, 0);

                redLight = (int)(redLight + (light.color.getRed() * ((double)tmpB/light.radius)));
                greenLight = (int)(greenLight + (light.color.getGreen() * ((double)tmpB/light.radius)));
                blueLight = (int)(blueLight + (light.color.getBlue() * ((double)tmpB/light.radius)));
            }

            r = r - (int) (r * ((255 - (double) redLight) / 255));
            r = Math.max(r, 0);
            r = Math.min(r, 255);

            g = g - (int) (g * ((255 - (double) greenLight) / 255));
            g = Math.max(g, 0);
            g = Math.min(g, 255);

            b = b - (int) (b * ((255 - (double) blueLight) / 255));
            b = Math.max(b, 0);
            b = Math.min(b, 255);

            colorOriginal = new Color(r, g, b).getRGB();

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
