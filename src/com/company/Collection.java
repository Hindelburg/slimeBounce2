package com.company;

import java.awt.*;

public class Collection extends Placeable{
    private Obj[] objects;

    public Collection(double pPosX, double pPosY){
        super(pPosX, pPosY);
    }

    private void paint(Graphics g, int x, int y){
        for(Obj o : objects){
            g.drawImage(o.getSprite(), x+(int)o.getPosX(), y+(int)o.getPosY(), null);
        }
    }
}
