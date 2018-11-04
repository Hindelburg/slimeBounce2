package com.company;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyStandard implements KeyListener {
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Level.player.pressedSpace();
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            Level.player.pressedD();
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            Level.player.pressedA();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Level.player.releaseSpace();
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            Level.player.releaseD();
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            Level.player.releaseA();
        }
    }

    public void keyTyped(KeyEvent e) {/* Not used */ }
}
