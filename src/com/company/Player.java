package com.company;

import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Player extends Placeable {
    private double maxJumpTime;

    private double currentXSpeed = 0;

    private double gravity = 0.5;
    private double jumpSpeed = 7;
    //private double smallJumpSpeed = 10;
    private double currentYSpeed = 0;

    private boolean onGround = false;
    public boolean active = false;

    private boolean jumping = false;
    private boolean canJump = false;
    public boolean right = false;
    public boolean left = false;
    private boolean tryJumping = false;
    private boolean canMoveRight = true;
    private boolean canMoveLeft = true;

    private double maxSpeed = 10;
    private double acc = 0.6;
    public double floorPos = 0;

    private double jumpTime = 0;

    private Timer t = new Timer(1, new Player.Listener());
    private KeyListener key = new PlayerKey();

    public Player(String pSprite, double pPosX, double pPosY, double pScale, double pMaxJumpTime) {
        super(pSprite, pPosX, pPosY, pScale);
        maxJumpTime = pMaxJumpTime;
        t.setDelay(10);
        t.start();
    }

    public double getMaxJumpTime() {
        return maxJumpTime;
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

    public KeyListener getPlayerKey() {
        return key;
    }

    //This isn't how I really want to do this but I will do it for now.
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void xSpeed() {
        if (right && left || (!right && !left)) {
            if (currentXSpeed > 0) {
                currentXSpeed = currentXSpeed - acc;
                currentXSpeed = round(currentXSpeed, 2);
            } else if (currentXSpeed < 0) {
                currentXSpeed = currentXSpeed + acc;
                currentXSpeed = round(currentXSpeed, 2);
            }
        } else if (right) {
            if (currentXSpeed < maxSpeed) {
                currentXSpeed = currentXSpeed + acc;
                currentXSpeed = round(currentXSpeed, 2);
            }
        } else if (left) {
            if (currentXSpeed > -maxSpeed) {
                currentXSpeed = currentXSpeed - acc;
                currentXSpeed = round(currentXSpeed, 2);
            }
        }
    }

    private void ySpeed() {
        currentYSpeed = currentYSpeed + gravity;

        if (canJump && tryJumping) {
            jumping = true;
            jumpTime = 0;
            canJump = false;
        }
        if (jumpTime > maxJumpTime) {
            jumping = false;
        }

        if (currentXSpeed != 0 && onGround) {
            currentYSpeed = -Math.abs(currentXSpeed) / 2;
        }
        if ((right || left) && onGround) {
            currentYSpeed = -jumpSpeed;
        }
        if (jumping) {
            currentYSpeed = -jumpSpeed;
        }
        jumpTime++;
    }


    private boolean overlaps(double x, double y, Obj o){
        Rectangle r = new Rectangle((int)Math.round(x), (int)Math.round(y), getHitbox().width, getHitbox().height);
        if(r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().width, o.getHitbox().height)){
            return true;
        }
        return false;
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
                else if((below >= above) && (below >= left) && (below >= right)){
                    pPosY = o.getPosY()+o.getHitbox().height;
                    currentYSpeed = 0;
                    jumping = false;
                }
                else if((left >= above) && (left >= below) && (left >= right)){
                    pPosX = (o.getPosX()-getHitbox().width);
                    currentXSpeed = 0;
                }
                else{
                    pPosX = o.getPosX()+o.getHitbox().width;
                    currentXSpeed = 0;
                }
            }
        }
        if(!g){
            inAir();
        }
        setPosX(pPosX);
        setPosY(pPosY);
    }

    public class PlayerKey implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                pressedUp();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                pressedRight();
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                pressedLeft();
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                releaseUp();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                releaseRight();
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                releaseLeft();
            }
        }

        public void keyTyped(KeyEvent e) {/* Not used */ }

        private void pressedUp() {
            tryJumping = true;
        }

        private void pressedRight() {
            right = true;
        }

        private void pressedLeft() {
            left = true;
        }

        private void releaseUp() {
            if (jumping) {
                jumping = false;
                canJump = false;
            }
            tryJumping = false;
        }

        private void releaseRight() {
            right = false;
        }

        private void releaseLeft() {
            left = false;
        }
    }


    public void onGround() {
        canJump = true;
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
        //smallJumpSpeed = 10;
        currentYSpeed = 0;

        onGround = false;
        active = false;

        jumping = false;
        canJump = false;
        right = false;
        left = false;
        tryJumping = false;
        canMoveRight = true;
        canMoveLeft = true;

        maxSpeed = 10;
        floorPos = 0;

        jumpTime = 0;
    }
}

