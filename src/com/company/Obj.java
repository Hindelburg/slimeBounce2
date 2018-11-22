package com.company;

import java.awt.*;

public class Obj extends Visual{
    private Point grabPos;

    private int collision;
    public Obj(String sprite, double x, double y, double scale, int collision){
        super(sprite, x, y, scale);
        this.collision = collision;
    }

    public int getCollision(){
        return collision;
    }

    public Point getGrabPos() {
        return grabPos;
    }

    public void setGrabPos(Point grabPos) {
        this.grabPos = grabPos;
    }

    public boolean tryGrabPos(int x, int y){
        if(pointInPlaceable(x, y)){
            grabPos = new Point(x-(int)getPosX(), y-(int)getPosY());
            return true;
        }
        return false;
    }

    private boolean pointInPlaceable(int x, int y){
        return (x > getPosX() && x < getPosX()+getHitbox().getWidth() && y > getPosY() && y < getPosY()+getHitbox().getHeight());
    }

    public void setPos(double x, double y){
        setPosX(x);
        setPosY(y);
    }
}