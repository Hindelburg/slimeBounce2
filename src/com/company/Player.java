package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player extends Entity {

    private KeyListener key = new PlayerKey();

    public Player(String pSprite, double posX, double posY, double scale, double maxJumpTime, double gravity, double jumpSpeed, double maxSpeed, double acc) {
        super(pSprite, posX, posY, scale, maxJumpTime, gravity, jumpSpeed, maxSpeed, acc);
    }

    public KeyListener getPlayerKey() {
        return key;
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
            jumping = false;
            tryJumping = false;
        }

        private void releaseRight() {
            right = false;
        }

        private void releaseLeft() {
            left = false;
        }
    }

    @Override
    public void death(){
        Level.gameover();
    }
}

