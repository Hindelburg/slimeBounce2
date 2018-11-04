package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * We now have a bug where you can get a jump by clipping into the wall kinda I guess. Happens where two Objs overlap or stack or something.
*/

class Panel00 extends JPanel {
    private Timer master = new Timer();

    private double height;
    private double width;
    private int framesPassed = 0;
    private double offsetX;
    private double offsetY;

    public static String mode = "menu";

    private BoundingBox bBox;

    private class Frames extends TimerTask {
        public void run(){
            System.out.println(framesPassed/10 + "/50 frames per second.");
            framesPassed = 0;
        }
    }

    private class Test extends Thread{
        private Timer framerateCounter = new Timer();
        public void run(){
            framerateCounter.scheduleAtFixedRate(new Frames(), 10000, 10000);
        }
    }



    public Panel00(int x, int y){
        super();
        width = x;
        height = y;

        Level.player = new Player("src\\sprites\\slimeStatic.png", 990, 300, 1, 12, 1, 14, 15, 1.5);
        Level.player.solid = false;////tmp!

        try{
            Level.loadLevel("level");
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        bBox = new BoundingBox(400, 300);
        Test t = new Test();

        master.scheduleAtFixedRate(new Run(), 0, 20);

        try {
            t.run();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        //Level.music.loop(); ////Muting the music so I don't have to listen to it while coding. Terraria music is fine for a placeholder but gets a bit annoying after hearing the start that many times.

        addKeyListener(Level.player.getPlayerKey());

        addKeyListener(new Key());
        addMouseListener(new Mouse());

        setFocusable(true);
        this.setLayout(new BorderLayout());
        this.add(painter, BorderLayout.CENTER);

        for(Light l : Level.lights){
            l.lightingUpdate();
        }

        for(Visual o : Level.objects){
            o.lightingUpdate();
        }
    }

    //The background workers.
    private class Run extends TimerTask {
        public void run(){
            framesPassed++;
            if(mode.equals("game")){
                painter.repaint();
                fall();//this doesn't belong here...
                Level.player.run();

                for(Enemy e : Level.enemies){
                    e.run();
                }
            }
        }
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
            if(b.getDistance() != -1) {
                double tmp3 = b.getPosX() - (bBox.getPosX() + bBox.getHitbox().getWidth() / 2 - width / 2) / b.getDistance() % b.getHitbox().getWidth();
                double tmp4 = b.getPosY() - (bBox.getPosY() - bBox.getHitbox().getHeight() / 2 - height / 2) / b.getDistance();

                //A somewhat crude repeating background algorithm.
                g.drawImage(b.getSprite(), (int) Math.floor(tmp3), (int) Math.floor(tmp4), null);
                g.drawImage(b.getSprite(), (int) Math.floor(tmp3 - b.getHitbox().getWidth()), (int) Math.floor(tmp4), null);
                g.drawImage(b.getSprite(), (int) Math.floor(tmp3 + b.getHitbox().getWidth()), (int) Math.floor(tmp4), null);
            }
            else{
                g.drawImage(b.getSprite(), 0, 0, null);
            }
        }
    }

    private void paintMenu(Graphics g){
        super.paintComponent(g);
        setOpaque(false);
    }

    private void paintGame(Graphics g){
        setOpaque(false);

        //Might refactor to a new location, unsure.
        //Moves the bounding box when the player gets to the edge of it.
        if((bBox.getPosY() > Level.player.getPosY())){
            bBox.setPosY(Level.player.getPosY());
        }
        if(((bBox.getPosY() + bBox.getHitbox().getHeight()) < Level.player.getPosY()+Level.player.getHitbox().getHeight())){
            bBox.setPosY(Level.player.getPosY()+(Level.player.getHitbox().getHeight()-bBox.getHitbox().getHeight()));
        }
        if((bBox.getPosX() > Level.player.getPosX())){
            bBox.setPosX(Level.player.getPosX());
        }
        if(((bBox.getPosX() + bBox.getHitbox().getWidth()) < Level.player.getPosX()+Level.player.getHitbox().getWidth())){
            bBox.setPosX(Level.player.getPosX()+(Level.player.getHitbox().getWidth()-bBox.getHitbox().getWidth()));
        }
        //Drawing offset.
        offsetX = bBox.getPosX()+bBox.getHitbox().getWidth()/2-width/2;
        offsetY = bBox.getPosY()+bBox.getHitbox().getHeight()/2-height/2;
        //Center of bounding box.
        double test1 = bBox.getPosX()+bBox.getHitbox().getWidth()/2;
        double test2 = bBox.getPosY()+bBox.getHitbox().getHeight()/2;


        //ENEMIES!
        for(Enemy e : Level.enemies){
            if(onScreen(e, test1, test2)) {
                g.drawImage(e.getSprite(), (int) Math.floor(e.getPosX() - offsetX), (int) Math.floor(e.getPosY() - offsetY), null);
            }
        }
        //OBJECTS!
        for(Obj o : Level.objects){
            if(onScreen(o, test1, test2)) {
                g.drawImage(o.getSprite(), (int) Math.floor(o.getPosX() - offsetX), (int) Math.floor(o.getPosY() - offsetY), null);
            }
        }
        g.drawImage(Level.player.getSprite(), (int) Math.floor(Level.player.getPosX()-offsetX), (int) Math.floor(Level.player.getPosY()-offsetY), null);
    }

    private boolean onScreen(Visual o, double xc, double yc){
        return !(((yc-(height/2) > o.getPosY()+o.getHitbox().getHeight())) || ((yc+(height/2)) < o.getPosY()) || ((xc-(width/2) > o.getPosX()+o.getHitbox().getWidth())) || ((xc+(width/2)) < o.getPosX()));
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


    /**
     * Currently a work in progress.
     */
    private class Mouse implements MouseListener {
        public void mouseEntered(MouseEvent e){

        }
        public void mouseClicked(MouseEvent e){
        }

        public void mousePressed(MouseEvent e) {
            //Level.lights[0].setPos(e.getX()+offsetX, e.getY()+offsetY);//more work than that.
            //Level.lights.get(0).setPos(e.getX()+offsetX, e.getY()+offsetY);//more work than that.
        }

        public void mouseReleased(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){

        }
    }
}
