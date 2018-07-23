package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class Player extends Placeable {
    private double maxJumpTime;

    private double xSpeed = 1;
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

    public Player(String pSprite, double pPosX, double pPosY, double pScale, double pMaxJumpTime){
        super(pSprite, pPosX, pPosY, pScale);
        maxJumpTime = pMaxJumpTime;
        t.setDelay(10);
        t.start();
    }

    public double getMaxJumpTime() {
        return maxJumpTime;
    }

    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if(active) {
                height();
                xMovement();
            }
        }
    }

    public KeyListener getPlayerKey(){
        return key;
    }

    //This isn't how I really want to do this but I will do it for now.
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void xMovement(){
        //System.out.println(currentXSpeed);
        if(right && left || (!right && !left)){
            if(currentXSpeed > 0) {
                currentXSpeed = currentXSpeed - acc;
                currentXSpeed = round(currentXSpeed, 3);
            }
            else if(currentXSpeed < 0) {
                currentXSpeed = currentXSpeed + acc;
                currentXSpeed = round(currentXSpeed, 3);
            }
        }
        else if(canMoveRight && right) {
            if(currentXSpeed < maxSpeed) {
                currentXSpeed = currentXSpeed + acc;
                currentXSpeed = round(currentXSpeed, 3);
            }
        }
        else if(canMoveLeft && left) {
            if(currentXSpeed > -maxSpeed) {
                currentXSpeed = currentXSpeed - acc;
                currentXSpeed = round(currentXSpeed, 3);
            }
        }

        if(!canMoveRight && currentXSpeed > 0){
            currentXSpeed = 0;
        }
        if(!canMoveLeft && currentXSpeed < 0){
            currentXSpeed = 0;
        }
        setPosX(getPosX()+currentXSpeed);
    }

    public class PlayerKey implements KeyListener {
        public void keyPressed(KeyEvent e){
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
        public void keyReleased(KeyEvent e){
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

        private void pressedUp(){
            tryJumping = true;
        }

        private void pressedRight(){
            right = true;
        }

        private void pressedLeft(){
            left = true;
        }

        private void releaseUp(){
            if (jumping) {
                jumping = false;
                canJump = false;
            }
            tryJumping = false;
        }

        private void releaseRight(){
            right = false;
        }

        private void releaseLeft(){
            left = false;
        }
    }


    public void onGround(){
        canJump = true;
        jumping = false;
        jumpTime = 0;
        onGround = true;
        currentYSpeed = 0;
    }

    public void inAir(){
        onGround = false;
        currentYSpeed = currentYSpeed + gravity;
    }

    public void setObjects(ArrayList<Obj> objects){
        this.objects = objects;
    }

    //purely for player, adding in collision for enemies next.
    public void objectCollision(double mod, double modX){
        mod = round(mod, 3);
        boolean collide = false;
        for(Obj o : objects) {
            double top;
            double left;
            double right;
            double bottom;
            if (((getPosY() + mod + getHitbox().height) >= (o.getPosY()) &&
                    (getPosY() + mod) <= (o.getPosY() + o.getHitbox().height) &&
                    (getPosX() + getHitbox().width + modX >= o.getPosX()) &&
                    (getPosX() + modX <= o.getPosX() + o.getHitbox().width)
            )) {
                top = getPosY() + mod + getHitbox().height - (o.getPosY());
                bottom = (o.getPosY() + o.getHitbox().height) - (getPosY() + mod);
                right = (getPosX() + getHitbox().width + modX - o.getPosX());
                left = (o.getPosX() + o.getHitbox().width - getPosX() + modX);
                if(top <= bottom && top <= right && top <= left){
                    setPosY(o.getPosY() - getHitbox().height);
                    onGround();
                    collide = true;
                }
                else if(bottom <= right && bottom <= left){
                    setPosY(o.getPosY() + o.getHitbox().height);
                    jumping = false;
                    currentYSpeed = 0;
                }
                else if(left >= right){
                    setPosX(o.getPosX() - getHitbox().width);
                    currentXSpeed = 0;
                }
                else{
                    setPosX(o.getPosX() + o.getHitbox().width);
                    currentXSpeed = 0;
                }
            }

        }
        if(!collide){
            inAir();
            setPosY(getPosY() + mod);
        }
    }

    private void height(){
        if (canJump && tryJumping){
            jumping = true;
            jumpTime = 0;
            canJump = false;
        }
        if (jumpTime > maxJumpTime){
            jumping = false;
        }

        if(currentXSpeed != 0 && onGround){
            currentYSpeed = -Math.abs(currentXSpeed)/2;
        }
        if((right || left) && onGround){
            currentYSpeed = -jumpSpeed;
        }
        if(jumping){
            currentYSpeed = -jumpSpeed;
        }
        objectCollision(currentYSpeed, currentXSpeed);
        jumpTime++;
    }

    public void reset(){
        setPosY(getDefaultPosY());
        setPosX(getDefaultPosX());

        xSpeed = 1;
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

    public boolean isCanMoveRight(){
        return canMoveRight;
    }

    public boolean isCanMoveLeft(){
        return canMoveLeft;
    }

    public void setCanMoveLeft(boolean b){
        canMoveLeft = b;
    }

    public void setCanMoveRight(boolean b){
        canMoveRight = b;
    }
}
