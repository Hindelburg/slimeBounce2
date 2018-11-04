package com.company;

import java.awt.*;

public class Shadow extends Polygon {
    private Point[] points = new Point[5];
    public int pointsLength = 0;
    @Override
    public void addPoint(int x, int y) {
        super.addPoint(x, y);
        points[pointsLength] = (new Point(x,y));
        pointsLength++;
    }
    public void addPointDiscrete(int x, int y) {
        super.addPoint(x, y);
    }
    public Point[] getPoints() {
        return points;
    }
}
