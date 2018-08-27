package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Entity extends Placeable{
    private double maxJumpTime;

    private static final int PLACES = 2;

    private double currentXSpeed = 0;
    private double currentYSpeed = 0;

    private double gravity;
    private double jumpSpeed;

    private boolean onGround = false;
    public boolean active = false;

    protected boolean jumping = false;
    protected boolean right = false;
    protected boolean left = false;
    protected boolean tryJumping = false;

    private double maxSpeed;
    private double acc;

    private double jumpTime = 0;

    private Timer t = new Timer(1, new Listener());

    public Entity(String pSprite, double posX, double posY, double scale, double maxJumpTime, double gravity, double jumpSpeed, double maxSpeed, double acc) {
        super(pSprite, posX, posY, scale);
        this.maxJumpTime = maxJumpTime;
        this.gravity = gravity;
        this.jumpSpeed = jumpSpeed;
        this.maxSpeed = maxSpeed;
        this.acc = acc;

        t.setDelay(10);
        t.start();
    }

    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (active) {
                ySpeed();
                xSpeed();
                safeMove();
            }
        }
    }

    private static double round(double value) {
        if (PLACES < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(PLACES, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void xSpeed() {
        if (right && left || (!right && !left)) {
            if (currentXSpeed > 0) {
                currentXSpeed = currentXSpeed - acc;
            } else if (currentXSpeed < 0) {
                currentXSpeed = currentXSpeed + acc;
            }
        } else if (right) {
            if (currentXSpeed < maxSpeed) {
                currentXSpeed = currentXSpeed + acc;
            }
        } else{
            if (currentXSpeed > -maxSpeed) {
                currentXSpeed = currentXSpeed - acc;
            }
        }
        currentXSpeed = round(currentXSpeed);
    }

    private void ySpeed() {
        currentYSpeed = currentYSpeed + gravity;
        if (onGround && tryJumping) {
            jumping = true;
            jumpTime = 0;
        }
        if (jumpTime > maxJumpTime) {
            jumping = false;
        }
        if (jumping) {
            currentYSpeed = -jumpSpeed;
        }
        if(!onGround) {
            jumpTime++;
        }
    }



    private boolean overlaps(double x, double y, Obj o){
        Rectangle r = new Rectangle((int)Math.round(x), (int)Math.round(y), getHitbox().width, getHitbox().height);
        return (r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().width, o.getHitbox().height));
    }

    private void safeMove(){
        double pPosX = getPosX() + currentXSpeed;
        double pPosY = getPosY() + currentYSpeed;
        boolean g = false;
        for (Obj o : Level.objects) {
            if (overlaps(pPosX, pPosY, o)) {
                double above = o.getPosY()-(getPosY()+getHitbox().height);
                double below = getPosY()-(o.getPosY()+o.getHitbox().height);
                double left = o.getPosX()-(getPosX()+getHitbox().width);
                double right = getPosX()-(o.getPosX()+o.getHitbox().width);

                if((above >= below) && (above >= left) && (above >= right)){
                    pPosY = (o.getPosY()-getHitbox().height);
                    currentYSpeed = 0;
                    onGround();
                    g=true;
                }
                else if((below >= left) && (below >= right)){
                    pPosY = o.getPosY()+o.getHitbox().height;
                    currentYSpeed = 0;
                    jumping = false;
                }
            }
        }
        if(!g){
            inAir();
        }
        for (Obj o : Level.objects) {
            if (overlaps(pPosX, pPosY, o)) {
                double above = o.getPosY()-(getPosY()+getHitbox().height);
                double below = getPosY()-(o.getPosY()+o.getHitbox().height);
                double left = o.getPosX()-(getPosX()+getHitbox().width);
                double right = getPosX()-(o.getPosX()+o.getHitbox().width);
                if((left >= below) && (left >= above) && (left >= right)){
                    pPosX = (o.getPosX()-getHitbox().width);
                    currentXSpeed = 0;
                }
                else if((right >= below) && (right >= above)){
                    pPosX = o.getPosX()+o.getHitbox().width;
                    currentXSpeed = 0;
                }
            }
        }
        setPosX(pPosX);
        setPosY(pPosY);
    }

    public void onGround() {
        jumping = false;
        jumpTime = 0;
        onGround = true;
        currentYSpeed = 0;
    }

    public void inAir() {
        onGround = false;
    }

    public void reset() {
        setPosY(getDefaultPosY());
        setPosX(getDefaultPosX());

        currentXSpeed = 0;

        gravity = 0.5;
        jumpSpeed = 7;
        currentYSpeed = 0;

        onGround = false;
        active = false;

        jumping = false;
        right = false;
        left = false;
        tryJumping = false;

        maxSpeed = 10;

        jumpTime = 0;
    }
}
