package com.company;

import java.awt.event.KeyListener;


public class Player extends Entity {

    private KeyListener key = new PlayerKeyStandard();

    public Player(String pSprite, double posX, double posY, double scale, double maxJumpTime, double gravity, double jumpSpeed, double maxSpeed, double acc) {
        super(pSprite, posX, posY, scale, maxJumpTime, gravity, jumpSpeed, maxSpeed, acc);
    }

    public KeyListener getPlayerKey() {
        return key;
    }

    @Override
    public void death(){
        Level.gameover();
    }

    public void pressedSpace() {
        jump();
    }

    public void pressedD() {
        moveRight();
    }

    public void pressedA() {
        moveLeft();
    }

    public void releaseSpace() {
        endJump();
    }

    public void releaseD() {
        stopRight();
    }

    public void releaseA() {
        stopLeft();
    }

    private void jump(){
        tryJumping = true;
    }
    private void moveRight(){
        right = true;
    }
    private void moveLeft(){
        left = true;
    }
    private void endJump(){
        jumping = false;
        tryJumping = false;
    }
    private void stopRight(){
        right = false;
    }
    private void stopLeft(){
        left = false;
    }
}

