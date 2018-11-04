package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;

public abstract class Visual extends Placeable{
    private BufferedImage tmpSprites;
    private BufferedImage tmpSprites2;
    private BufferedImage scaledBuffered;
    private double scale;
    private int[] pixelsOriginal;
    private int[] pixelsModified;
    private String source;

    public boolean solid; //temporary for testing.

    public Visual(String pSprite, double pPosX, double pPosY, double pScale){
        super(pPosX, pPosY);
        loadSprite(pSprite);
        scale = pScale;
        source = pSprite;
        Size hitbox = new Size((int)Math.floor(tmpSprites.getWidth()*scale), (int)Math.floor((tmpSprites.getHeight()*scale)));
        setHitbox(hitbox);

        scaledBuffered = new BufferedImage(hitbox.getWidth(), hitbox.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledBuffered.createGraphics();
        g2d.drawImage(tmpSprites, 0, 0, hitbox.getWidth(),hitbox.getHeight(), null);
        g2d.dispose();
        tmpSprites = deepCopy(scaledBuffered);
        tmpSprites2 = deepCopy(scaledBuffered);

        pixelsOriginal = ((DataBufferInt)tmpSprites.getRaster().getDataBuffer()).getData();
        pixelsModified = ((DataBufferInt)tmpSprites2.getRaster().getDataBuffer()).getData();

        solid=true;
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

    public void lightingUpdate(){
        for(int x = 0; x < getHitbox().getWidth(); x++) {
            for(int y = 0; y < getHitbox().getHeight(); y++) {
                pixelUpdate(x,y);
            }
        }
        scaledBuffered = deepCopy(tmpSprites2);
    }

    public void lightingUpdate(int startX, int endX, int startY, int endY){
        for(int y = startY; y < endY; y++) {
            for(int x = startX; x < endX; x++) {
                pixelUpdate(x,y);
            }
        }
        scaledBuffered = deepCopy(tmpSprites2);
    }

    public void partialLightingUpdate(int startX, int endX, int startY, int endY, int excludePosX, int excludePosY, int radius){
        for(int y = startY; y < endY; y++) {
            for(int x = startX; x < endX; x++) {
                if(!((x>excludePosX && x<excludePosX+radius)&&(y>excludePosY && y<excludePosY+radius))) {
                    pixelUpdate(x,y);
                }
            }
        }
        scaledBuffered = deepCopy(tmpSprites2);
    }

    private void pixelUpdate(int x, int y){
        int colorOriginal;
        colorOriginal = pixelsOriginal[x+(getHitbox().getWidth()*y)];

        Color c = new Color(colorOriginal);
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        int redLight = 0;
        int greenLight = 0;
        int blueLight = 0;
        double i;

        for (Light light : Level.lights) {
            i = light.withinRadius(getPosX() + x, getPosY() + y);
            if (i != -1) {
                double otherMod = 1;
                int shadowDistance = (light.inShadow((int) getPosX() + x, (int) getPosY() + y));
                if (shadowDistance != -1) {
                    otherMod = (10 - (double) shadowDistance) / 10;
                }

                redLight = (int) (redLight + Math.max(0, ((light.getColor().getRed() * i) * otherMod)));
                greenLight = (int) (greenLight + Math.max(0, ((light.getColor().getGreen() * i) * otherMod)));
                blueLight = (int) (blueLight + Math.max(0, ((light.getColor().getBlue() * i) * otherMod)));
            }
        }
        //Not sure this is how I want to do this. I think blocks should still blackout after time, but I'm unsure how I'd accomplish that.
        redLight = (redLight + Math.max(0, ((Level.ambientLight.getRed()))));
        greenLight = (greenLight + Math.max(0, ((Level.ambientLight.getGreen()))));
        blueLight = (blueLight + Math.max(0, ((Level.ambientLight.getBlue()))));

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
        //this could be optimized for ones that can't change.

        pixelsModified[x+(getHitbox().getWidth()*y)] = colorOriginal;
    }

    public String getSource() {
        return source;
    }

    public double getScale() {
        return scale;
    }
}
