package com.company;

public class Obj extends Visual{
    public int collision;
    public Obj(String sprite, double x, double y, double scale, int collision){
        super(sprite, x, y, scale);
        this.collision = collision;
    }
}