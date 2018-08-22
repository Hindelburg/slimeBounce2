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
    private double acc = 0.2;
    public double floorPos = 0;

    private ArrayList<Obj> objects = new ArrayList<>();


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


    private boolean overlaps(double x, double y){
        Rectangle r = new Rectangle((int)Math.round(x), (int)Math.round(y), getHitbox().width, getHitbox().height);
        for (Obj o : objects) {
            if(r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().width, o.getHitbox().height)){
                return false;
            }
        }
        return true;
    }



    private void safeMove(){
        double i = 0;
        double newPos;
        while(i <= Math.abs(currentXSpeed)){
            if(currentXSpeed > 0) {
                newPos = getPosX() + 0.01;
                if(overlaps(newPos, getPosY())) {
                    setPosX(newPos);
                }
                else{
                    currentXSpeed = 0;
                }
            }
            else if(currentXSpeed < 0) {
                newPos = getPosX() - 0.01;
                if(overlaps(newPos, getPosY())) {
                    setPosX(newPos);
                }
                else{
                    currentXSpeed = 0;
                }
            }
            i = i + 0.01;
        }

        i = 0;
        while(i <= Math.abs(currentYSpeed)){
            if(currentYSpeed > 0) {
                newPos = getPosY() + 0.01;
                if(overlaps(getPosX(), newPos)) {
                    setPosY(newPos);
                    inAir();
                }
                else{
                    onGround();
                }
            }
            else {
                newPos = getPosY() - 0.01;
                if(overlaps(getPosX(), newPos)) {
                    inAir();
                    setPosY(newPos);
                }
                else{
                    currentYSpeed = 0;
                    jumping = false;
                }
            }
            i = i + 0.01;
        }
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

    public void setObjects(ArrayList<Obj> objects) {
        this.objects = objects;
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
        acc = 0.2;
        floorPos = 0;

        jumpTime = 0;
    }
}

