package com.company;

import java.awt.*;

public class Light extends Placeable {
    private int radius;
    private Color color;
    public Shadow[] shadows;

    //private inner class
    private static class Shadow extends Polygon{
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

    //constructor
    public Light(double posX, double posY, int radius, Color color){
        super(posX, posY);
        this.radius = radius;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setPos(double posX, double posY) {
        double tmp1 = getPosX();
        double tmp2 = getPosY();
        super.setPosX(posX);
        super.setPosY(posY);
        lightingUpdate();
        partialLightingUpdate(tmp1, tmp2, posX, posY);
    }

    public void lightingUpdate(){
        shadowMap(Level.objects);
        for(Visual v : Level.objects){
            int x = (int)getPosX()-radius;//left
            int y = (int)getPosY()-radius;//top
            int x2 = (int)(v.getPosX()+v.getHitbox().getWidth()-(getPosX()+radius));//right
            int y2 = (int)(v.getPosY()+v.getHitbox().getHeight()-(getPosY()+radius));//bottom
            int startX = (int)Math.max(0, (x-v.getPosX()));
            int startY = (int)Math.max(0, (y-v.getPosY()));
            int endX = Math.min(v.getHitbox().getWidth(), v.getHitbox().getWidth()-x2);
            int endY = Math.min(v.getHitbox().getHeight(), (v.getHitbox().getHeight()-y2));

            if(overlaps(x, y, radius*2, v)){
                v.lightingUpdate(startX, endX, startY, endY);
            }
        }
    }

    private void partialLightingUpdate(double posX, double posY, double excludePosX, double excludePosY){
        shadowMap(Level.objects);
        for(Visual v : Level.objects){
            int x = (int)posX-radius;//left
            int y = (int)posY-radius;//top
            int x2 = (int)(v.getPosX()+v.getHitbox().getWidth()-(posX+radius));//right
            int y2 = (int)(v.getPosY()+v.getHitbox().getHeight()-(posY+radius));//bottom
            int startX = (int)Math.max(0, (x-v.getPosX()));
            int startY = (int)Math.max(0, (y-v.getPosY()));
            int endX = Math.min(v.getHitbox().getWidth(), v.getHitbox().getWidth()-x2);
            int endY = Math.min(v.getHitbox().getHeight(), (v.getHitbox().getHeight()-y2));

            int x3 = (int)excludePosX-radius;//left
            int y3 = (int)excludePosY-radius;//top
            excludePosX = (x3-v.getPosX());
            excludePosY = (y3-v.getPosY());

            if(overlaps(x, y, radius*2, v)){
                v.partialLightingUpdate(startX, endX, startY, endY, (int)excludePosX, (int)excludePosY, radius);
            }
        }
    }

    private boolean overlaps(int x, int y, int width, Visual o){
        Rectangle r = new Rectangle((x), (y), width, width);
        return (r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().getWidth(), o.getHitbox().getHeight()));
    }

    public double withinRadius(double posX1, double posY1){
        double distance = distanceFromPoint(new Point((int)posX1, (int)posY1), new Point((int)getPosX(), (int)getPosY()));
        double fakeRad = square(radius);
        if(distance >= fakeRad){
            return -1;
        }
        else{
            return (((fakeRad-distance))/(fakeRad));
        }
    }

    private boolean above(Obj o){
        return getPosY() <= o.getPosY();
    }
    private boolean below(Obj o){
        return getPosY() >= o.getPosY()+o.getHitbox().getHeight();
    }
    private boolean left(Obj o){
        return getPosX() <= o.getPosX();
    }
    private boolean right(Obj o){
        return getPosX() >= o.getPosX() + o.getHitbox().getWidth();
    }

    //Please find a way to rework this to be smaller.
    public void shadowMap(Obj[] objects){
        int shadowNumber = 0;
        for(Obj o : objects) {
            int x = (int)getPosX()-radius;
            int y = (int)getPosY()-radius;
            if(overlaps(x, y, radius*2, o) && o.solid){
                shadowNumber++;
            }
        }
        Shadow[] shadows2 = new Shadow[shadowNumber];
        shadowNumber = 0;
        for(Obj o : objects){
            int x = (int)getPosX()-radius;
            int y = (int)getPosY()-radius;
            if(overlaps(x, y, radius*2, o) && o.solid){
                Point[] points;
                if(above(o) && left(o)){
                    points = new Point[3];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    points[1] = (new Point((int)o.getPosX(), (int)o.getPosY()));
                    points[2] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()+radius));
                }
                else if(above(o) && right(o)){
                    points = new Point[3];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY()));
                    points[1] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY()));
                    points[2] = (new Point((int) o.getPosX() + o.getHitbox().getWidth(), (int) o.getPosY() + o.getHitbox().getHeight()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()+radius));
                }
                else if(above(o)){
                    points = new Point[2];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY()));
                    points[1] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()+radius));
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()+radius));
                }
                else if(below(o) && left(o)){
                    points = new Point[3];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY()));
                    points[1] = (new Point((int)o.getPosX(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    points[2] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()-radius));
                }
                else if(below(o) && right(o)){
                    points = new Point[3];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    points[1] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    points[2] = (new Point((int) o.getPosX() + o.getHitbox().getWidth(), (int) o.getPosY()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()-radius));
                }
                else if(below(o)){
                    points = new Point[2];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    points[1] = (new Point((int)o.getPosX()+o.getHitbox().getWidth(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()-radius));
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()-radius));
                }
                else if(left(o)){
                    points = new Point[2];
                    points[0] = (new Point((int)o.getPosX(), (int)o.getPosY()));
                    points[1] = (new Point((int)o.getPosX(), (int)o.getPosY() + o.getHitbox().getHeight()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()+radius));
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()+radius),(int)(getPosY()-radius));
                }
                else{
                    points = new Point[2];
                    points[0] = (new Point((int) o.getPosX() + o.getHitbox().getWidth(), (int) o.getPosY()));
                    points[1] = (new Point((int) o.getPosX() + o.getHitbox().getWidth(), (int) o.getPosY() + o.getHitbox().getHeight()));
                    shadows2[shadowNumber] = newShadow(points);
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()+radius));
                    shadows2[shadowNumber].addPointDiscrete((int)(getPosX()-radius),(int)(getPosY()-radius));
                }

                shadowNumber++;
            }
        }
        shadows = shadows2;
    }

    public Shadow newShadow(Point[] points){
        Shadow shadow = new Shadow();
        for(int i = 0; i < points.length; i++){
            Point p = points[i];
            if(i == 0){
                anotherPoint(p.x, p.y, getPosX(), getPosY(), shadow);
                shadow.addPoint(p.x, p.y);
            }
            else if(i == points.length-1){
                shadow.addPoint(p.x, p.y);
                anotherPoint(p.x, p.y, getPosX(), getPosY(), shadow);
            }
            else{
                shadow.addPoint(p.x, p.y);
            }
        }
        return shadow;
    }

    public int inShadow(int x, int y){
        int distance = -1;
        for (int i = 0; i < shadows.length; i++) {
            if(shadows[i].contains(x,y)) {
                distance = Math.max(distance, distanceFromShadowEdge(shadows[i], new Point(x, y)));
            }
        }
        return distance;
    }

    //Also find a way to shorten this.
    private void anotherPoint(double oX, double oY, double lX, double lY, Shadow shadow){
        double m = slope(oX, oY, lX, lY);
        double x2 = 0;
        double y = 0;
        //Change this stuff eventually.
        //Light above.
        if((m >= 1 && oY > lY)) {
            y = oY + radius;
            x2 = (y+(m*oX-oY))/m;
        }
        else if(m <= 1 && m >= 0 && oY > lY){
            x2 = oX + radius;
            y = (m*x2) - ((m*oX)-oY); //x2 = (y+(m*oX-oY))/m;
        }
        else if(m <= 0 && m >= -1 && oY > lY){
            x2 = oX - radius;
            y = (m*x2) - ((m*oX)-oY); //x2 = y+m*oX-oY/m;
        }
        else if(m <= -1 && oY > lY){
            y = oY + radius;
            x2 = (y+(m*oX-oY))/m;
        }
        //Light below.
        else if((m >= 1 && oY < lY)) {
            y = oY - radius;
            x2 = (y+(m*oX-oY))/m;
        }
        else if(m <= 1 && m >= 0 && oY < lY){
            x2 = oX - radius;
            y = (m*x2) - ((m*oX)-oY); //x2 = y+m*oX-oY/m;
        }
        else if(m <= 0 && m >= -1 && oY < lY){
            x2 = oX + radius;
            y = (m*x2) - ((m*oX)-oY); //x2 = y+m*oX-oY/m;
        }
        else if(m <= -1 && oY < lY){
            y = oY - radius;
            x2 = (y+(m*oX-oY))/m;
        }
        shadow.addPoint((int)x2,(int)y);
    }

    private static double slope(double oX, double oY, double lX, double lY){
        return (oY-lY)/(oX - lX); //204863
    }

    public static int distanceFromShadowEdge(Shadow poly, Point point){
        int distance = Integer.MAX_VALUE;
        for(int i = 0; i < poly.pointsLength-1; i++) {
            distance = Math.min(distanceFromLine(poly.getPoints()[i], poly.getPoints()[(i+1)], point), distance);
        }
        return distance;
    }

    public static int distanceFromLine(Point v, Point w, Point p){
        double l2 = distanceFromPoint(v, w);
        if(v.x < 0 || (v.y > w.y && v.x > w.x)){
            Point tmp = v;
            v = w;
            w = tmp;
        }
        if (l2 == 0) return (int)Math.sqrt(distanceFromPoint(p, v));
        double t = (((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y))/l2);
        t = Math.max(0, Math.min(1, t));
        return (int)Math.sqrt(distanceFromPoint(p, new Point((int)(v.x + t * (w.x - v.x)),(int)(v.y + t * (w.y - v.y)))));
    }

    private static double distanceFromPoint(Point p, Point p2){
        return square(p.x - p2.x) + square(p.y - p2.y);
    }

    private static double square(double value){
        return value * value;
    }
}
