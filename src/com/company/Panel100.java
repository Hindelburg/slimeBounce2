package com.company;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import static jdk.nashorn.internal.objects.NativeMath.round;

/*
Story:
    1. Who am I?
    2. Hear of the Beast.
    3. Something that, through the narrative, lets you know you must collect all the power ups.

Enemies:

Items:

Powers:
    1. High jump?
    2. Wall stick.
    3. Liquefy.
    4. Electric field.
    5. Parasite.

Enemies:
    1. Citizen. (Any unarmed person.)
    2. Cop.
    3. Riot cop.
    4. Floating police drone.
    MORE NEEDED!

NOTES:
    1. Localise the collision variables such as floor and ceiling to each entity so that much of the logic can be moved into their class and then physics can
       be added to enemies through a super class.
    2. Make background stuff its own class. DONE
    3. Add support for multiple sprites and hitboxes for each entity.
    4. Remake the game over method.
    5. Make a menu.
    6. Default positions for placeables. DONE
*/

class Panel00 extends JPanel {
    private Timer t = new Timer(1, new Listener());

    private double height;
    private double width;

    private String mode = "menu";

    private BoundingBox bBox;


    public Panel00(int x, int y){
        super();
        width = x;
        height = y;

        Level.player = new Player("src\\sprites\\slimeStatic.png", 1200, -500, 1, 25);
        addEnemies();
        loadLevel("tutorial");
        Level.addBackgrounds();

        bBox = new BoundingBox(400, 300);

        t.setDelay(10);
        t.start();

        Level.music.loop();

        addKeyListener(Level.player.getPlayerKey());

        addKeyListener(new Key());
        setFocusable(true);
        this.setLayout(new BorderLayout());
        this.add(test, BorderLayout.CENTER);
    }

    private void loadLevel(String name){
        System.out.println("Loading.");
        try{
            BufferedReader r = new BufferedReader(new FileReader("lvl-"+name+".csv"));
            String tmp = r.readLine();
            while(!(tmp == null)){
                String[] obj = tmp.split(",");
                Level.objects.add(new Obj(obj[0], Integer.parseInt(obj[1]),Integer.parseInt(obj[2]),Integer.parseInt(obj[3])));
                tmp = r.readLine();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void addEnemies(){
        //enemies.add(new Enemy("src\\sprites\\enemy.png", 1250 ,Math.floor(500), 2, 0.20));
        //enemies.add(new JumpingEnemy("src\\sprites\\enemy.png", 1250 ,Math.floor(500), 1, 1.50, 3));
        //enemies.add(new JumpingEnemy("src\\sprites\\enemy.png", 2250 ,Math.floor(500), 1, 0.50,5));
    }

    private JPanel test = new JPanel() {
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

    //Handles the background painting.
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
        super.paintComponent(g);
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
        //Center of bounding box.
        double tmp1 = bBox.getPosX()+bBox.getHitbox().width/2-width /2;
        double tmp2 = bBox.getPosY()+bBox.getHitbox().height/2-height/2;

        g.drawImage(Level.player.getSprite(), (int) Math.floor(Level.player.getPosX()-tmp1), (int) Math.floor(Level.player.getPosY()-tmp2), null);

        //ENEMIES!
        for(Enemy e : Level.enemies){
            g.drawImage(e.getSprite(), (int) Math.floor(e.getPosX()-tmp1), (int) Math.floor(e.getPosY()-tmp2), null);
        }
        //OBJECTS!
        for(Obj o : Level.objects){
            g.drawImage(o.getSprite(), (int) Math.floor(o.getPosX()-tmp1), (int) Math.floor(o.getPosY()-tmp2), null);
        }
    }

    //The background workers.
    private class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if(mode.equals("game")) {
                collision();
                test.repaint();
            }
        }
    }

    private void collision(){
        enemyCollision();
    }



    private void enemyCollision(){
        for(Enemy e : Level.enemies){
            boolean tmp1 = (e.getPosY() <= Level.player.getPosY() + Level.player.getHitbox().height) && ((e.getPosY() + e.getHitbox().height) >= Level.player.getPosY());
            boolean tmp2 = (e.getPosX() <= Level.player.getPosX() + Level.player.getHitbox().width) && ((e.getPosX() + e.getHitbox().width) >= Level.player.getPosX());
            if(tmp1 && tmp2){
                System.out.println("DEAD!");
                gameover();
            }
        }
        //This should not be hardcoded in the end.
        if(Level.player.getPosY() > 800){
            System.out.println("DEAD!");
            gameover();
        }
    }

    //This should all be changed eventually.
    private void gameover(){
        mode = "menu";
        Level.player.reset();

        for(Enemy i : Level.enemies){
            i.setPosX(i.getDefaultPosX());
            i.setPosY(i.getDefaultPosY());
        }
        for(Obj o : Level.objects){
            o.setPosX(o.getDefaultPosX());
            o.setPosY(o.getDefaultPosY());
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
                }
            }
        }
        public void keyTyped(KeyEvent e) {/* Not used */ }
    }
}
