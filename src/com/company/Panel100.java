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

    private Sounds music = new Sounds("src\\sprites\\music.wav");

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Obj> objects = new ArrayList<>();
    private ArrayList<Background> backgrounds = new ArrayList<>();

    private Player player;
    private BoundingBox bBox;


    public Panel00(int x, int y){
        super();
        width = x;
        height = y;
        player = new Player("src\\sprites\\slimeStatic.png", 1200, -500, 1, 25);
        addEnemies();
        loadLevel("tutorial");
        addBackgrounds();

        player.setObjects(objects);

        bBox = new BoundingBox(400, 300);

        t.setDelay(10);
        t.start();

        music.loop();

        addKeyListener(player.getPlayerKey());

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
                objects.add(new Obj(obj[0], Integer.parseInt(obj[1]),Integer.parseInt(obj[2]),Integer.parseInt(obj[3])));
                tmp = r.readLine();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addBackgrounds(){
        backgrounds.add(new Background("src\\sprites\\background.png", 10, 2, 0));
        backgrounds.add(new Background("src\\sprites\\background2.png", 5, 2, 400));
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

        for(Background b : backgrounds){
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
        if((bBox.getPosY() > player.getPosY())){
            bBox.setPosY(player.getPosY());
        }
        if(((bBox.getPosY() + bBox.getHitbox().height) < player.getPosY()+player.getHitbox().height)){
            bBox.setPosY(player.getPosY()+(player.getHitbox().height-bBox.getHitbox().height));
        }
        if((bBox.getPosX() > player.getPosX())){
            bBox.setPosX(player.getPosX());
        }
        if(((bBox.getPosX() + bBox.getHitbox().width) < player.getPosX()+player.getHitbox().width)){
            bBox.setPosX(player.getPosX()+(player.getHitbox().width-bBox.getHitbox().width));
        }
        //Center of bounding box.
        double tmp1 = bBox.getPosX()+bBox.getHitbox().width/2-width /2;
        double tmp2 = bBox.getPosY()+bBox.getHitbox().height/2-height/2;

        g.drawImage(player.getSprite(), (int) Math.floor(player.getPosX()-tmp1), (int) Math.floor(player.getPosY()-tmp2), null);

        //ENEMIES!
        for(Enemy e : enemies){
            g.drawImage(e.getSprite(), (int) Math.floor(e.getPosX()-tmp1), (int) Math.floor(e.getPosY()-tmp2), null);
        }
        //OBJECTS!
        for(Obj o : objects){
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
        for(Enemy e : enemies){
            boolean tmp1 = (e.getPosY() <= player.getPosY() + player.getHitbox().height) && ((e.getPosY() + e.getHitbox().height) >= player.getPosY());
            boolean tmp2 = (e.getPosX() <= player.getPosX() + player.getHitbox().width) && ((e.getPosX() + e.getHitbox().width) >= player.getPosX());
            if(tmp1 && tmp2){
                System.out.println("DEAD!");
                gameover();
            }
        }
        //This should not be hardcoded in the end.
        if(player.getPosY() > 800){
            System.out.println("DEAD!");
            gameover();
        }
    }

    //This should all be changed eventually.
    private void gameover(){
        mode = "menu";
        player.reset();

        for(Enemy i : enemies){
            i.setPosX(i.getDefaultPosX());
            i.setPosY(i.getDefaultPosY());
        }
        for(Obj o : objects){
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
                    player.reset();
                    mode = "game";
                    player.active = true;
                }
            }
        }
        public void keyTyped(KeyEvent e) {/* Not used */ }
    }
}
