package com.company;

import java.awt.*;

public class Light extends Placeable {
    private int radius;
    private int brightness;
    private Color color;

    public Light(double posX, double posY, int radius, Color color){
        super(posX, posY);
        this.radius = radius;
        this.color = color;
    }

    public void setPos(double posX, double posY) {
        double tmp1 = getPosX();
        double tmp2 = getPosY();
        super.setPosX(posX);
        super.setPosY(posY);
        lightingUpdate(tmp1, tmp2);
        lightingUpdate(posX, posY);
    }

    private void lightingUpdate(double posX, double posY){
        for(Visual v : Level.objects){
            int x = (int)posX-radius;
            int y = (int)posY-radius;
            if(overlaps(x, y, radius*2, v)){
                v.lightingUpdate();
            }
        }
    }

    private boolean overlaps(int x, int y, int width, Visual o){
        Rectangle r = new Rectangle((x), (y), width, width);
        return (r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().getWidth(), o.getHitbox().getHeight()));
    }

    public double withinRadius(double posX1, double posY1){
        double distance = distance(posX1, posY1, getPosX(), getPosY());
        double fakeRad = square(radius);
        if(distance > fakeRad){
            return -1;
        }
        else{
            return (((fakeRad-distance))/(fakeRad));
        }
    }

    //I should be square rooting but I'm not and later multiplying to increase efficiency.
    private double distance(double posX1, double posY1, double posX2, double posY2){
        return (square(posX2-posX1) + square(posY2-posY1));
    }

    private double square(double value){
        return value * value;
    }

    public Color getColor() {
        return color;
    }

    private void shadowMap(){
        for(Visual v : Level.objects){

        }
    }

    private static double angle(double oX, double oY, double lX, double lY){
        return Math.atan2(oX - lX, oY - lY);
    }

    private class Shadow{
        Line one;
        Line two;

        public Shadow(Line one, Line two){
            
        }

        private class Line{
            double x;
            double y;
            double angle;
        }
    }
}
