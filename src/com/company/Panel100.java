package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
*/

class Panel00 extends JPanel {
    private Timer t = new Timer(1, new Listener());

    private double height;
    private double width;

    public static String mode = "menu";

    private BoundingBox bBox;

    public Panel00(int x, int y){
        super();
        width = x;
        height = y;

        Level.player = new Player("src\\sprites\\slimeStatic.png", 1200, -500, 1, 25, 0.5, 7, 10, 0.6);
        Level.loadLevel("tutorial");
        Level.addBackgrounds();
        Level.addEnemies();

        bBox = new BoundingBox(400, 300);

        t.setDelay(10);
        t.start();

        Level.music.loop();

        addKeyListener(Level.player.getPlayerKey());

        addKeyListener(new Key());
        setFocusable(true);
        this.setLayout(new BorderLayout());
        this.add(painter, BorderLayout.CENTER);
    }


    private JPanel painter = new JPanel() {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintBackground(g);
            if (mode.equals("menu")) {
                paintMenu(g);
            }
            else {
                paintGame(g);
            }
        }
    };

    //Handles the background painting. Probably should be updated.
    private void paintBackground(Graphics g){
        super.paintComponent(g);

        for(Background b : Level.backgrounds){
            double tmp3 = b.getPosX()-(bBox.getPosX()+bBox.getHitbox().width/2-width /2)/b.getDistance() % b.getHitbox().width;
            double tmp4 = b.getPosY()-(bBox.getPosY()-bBox.getHitbox().height/2-height/2)/b.getDistance();

            //A somewhat crude repeating background algorithm.
            g.drawImage(b.getSprite(), (int) Math.floor(tmp3), (int) Math.floor(tmp4), null);
            g.drawImage(b.getSprite(), (int) Math.floor(tmp3-b.getHitbox().width), (int) Math.floor(tmp4), null);
            g.drawImage(b.getSprite(), (int) Math.floor(tmp3+b.getHitbox().width), (int) Math.floor(tmp4), null);
        }
    }

    private void paintMenu(Graphics g){
        super.paintComponent(g);
        setOpaque(false);
    }

    private void paintGame(Graphics g){
        for(Light light : Level.lights) {
            //light.strobe();//////////////////////////////////////////
        }
        setOpaque(false);

        //Might refactor to a new location, unsure.
        //Moves the bounding box when the player gets to the edge of it.
        if((bBox.getPosY() > Level.player.getPosY())){
            bBox.setPosY(Level.player.getPosY());
        }
        if(((bBox.getPosY() + bBox.getHitbox().height) < Level.player.getPosY()+Level.player.getHitbox().height)){
            bBox.setPosY(Level.player.getPosY()+(Level.player.getHitbox().height-bBox.getHitbox().height));
        }
        if((bBox.getPosX() > Level.player.getPosX())){
            bBox.setPosX(Level.player.getPosX());
        }
        if(((bBox.getPosX() + bBox.getHitbox().width) < Level.player.getPosX()+Level.player.getHitbox().width)){
            bBox.setPosX(Level.player.getPosX()+(Level.player.getHitbox().width-bBox.getHitbox().width));
        }
        //Drawing offset.
        double tmp1 = bBox.getPosX()+bBox.getHitbox().width/2-width/2;
        double tmp2 = bBox.getPosY()+bBox.getHitbox().height/2-height/2;
        //Center of bounding box.
        double test1 = bBox.getPosX()+bBox.getHitbox().width/2;
        double test2 = bBox.getPosY()+bBox.getHitbox().height/2;

        g.drawImage(Level.player.getSprite(test1, test2), (int) Math.floor(Level.player.getPosX()-tmp1), (int) Math.floor(Level.player.getPosY()-tmp2), null);

        //ENEMIES!
        for(Enemy e : Level.enemies){
            if(onScreen(e, test1, test2)) {
                g.drawImage(e.getSprite(test1, test2), (int) Math.floor(e.getPosX() - tmp1), (int) Math.floor(e.getPosY() - tmp2), null);
            }
        }
        //OBJECTS!
        for(Obj o : Level.objects){
            if(onScreen(o, test1, test2)) {
                g.drawImage(o.getSprite(test1, test2), (int) Math.floor(o.getPosX() - tmp1), (int) Math.floor(o.getPosY() - tmp2), null);
            }

        }
    }

    private boolean onScreen(Visual o, double xc, double yc){
        return !(((yc-(height/2) > o.getPosY()+o.getHitbox().height)) || ((yc+(height/2)) < o.getPosY()) || ((xc-(width/2) > o.getPosX()+o.getHitbox().width)) || ((xc+(width/2)) < o.getPosX()));
    }


    //The background workers.
    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if(mode.equals("game")){
                painter.repaint();
                fall();
            }
        }
    }

    private void fall(){
        if(Level.player.getPosY() > Level.deathLevel){
            Level.gameover();
        }
    }


    private class Key implements KeyListener {
        public void keyPressed(KeyEvent e){
            //Menu controls
            if(mode.equals("menu")){
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                }
            }
        }
        public void keyReleased(KeyEvent e){
            //Menu controls
            if(mode.equals("menu")){
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    Level.player.reset();
                    mode = "game";
                    Level.player.active = true;
                    for (Enemy enemy : Level.enemies) {
                        enemy.active = true;
                    }
                }
            }
        }
        public void keyTyped(KeyEvent e) {/* Not used */ }
    }
}
