package com.company;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Level {
    //WILL CHANGE TO JSON OBJECT WITH MORE STUFF SOON,
    //ONCE THE DEVTOOLS ARE UP TO DATE WHICH SHOULD HAPPEN ONCE LIGHTING IS DONE.
    //UPDATE: DEVTOOLS ARE NOW GOING TO BE BUILT INTO THE BASE GAME, BUT THIS
    //WILL STILL BE CHANGED COMPLETELY.

    public static Sounds music = new Sounds("src\\sprites\\music.wav");
    //I'm gonna have to make this better once devtools are done...
    public static Enemy[] enemies = new Enemy[0];
    public static Obj[] objects = new Obj[18];
    public static Background[] backgrounds = new Background[3];
    public static int deathLevel = 1000;
    public static Light[] lights = new Light[2];

    public static Player player;

    public static void addBackgrounds(){
        Level.backgrounds[0] = (new Background("src\\sprites\\background3.png", 100, 3, -50));
        Level.backgrounds[1] = (new Background("src\\sprites\\background.png", 10, 2, 0));
        Level.backgrounds[2] = (new Background("src\\sprites\\background2.png", 5, 2, 400));
    }

    public static void addEnemies(){
        //Level.enemies[0] = (new Enemy("src\\sprites\\slimeStatic.png", 1000, 0, 1, 10, 0.5, 7, 10, 0.6));
        objects[0] = new Obj("src\\sprites\\enemy.png",450, 50, 5, 2);
        objects[0].solid = false;

        //Not enemies in any way at all but since all of this will be gone soon, might as well put it here.
        lights[0] = (new Light(1000000, 400, 60, new Color(3, 187, 0)));
        lights[1] = (new Light(980, 300, 1000, new Color(185, 0, 16)));
    }

    //Must be modified in future.
    public static void loadLevel(String name){
        System.out.println("Loading.");
        int stupid = 0;
        try{
            BufferedReader r = new BufferedReader(new FileReader("lvl-"+name+".csv"));
            String tmp = r.readLine();
            while(!(tmp == null)){
                String[] obj = tmp.split(",");
                objects[stupid] = (new Obj(obj[0], Integer.parseInt(obj[1]),Integer.parseInt(obj[2]),Integer.parseInt(obj[3]),0));
                tmp = r.readLine();
                stupid++;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void gameover(){
        Panel00.mode = "menu";
        player.reset();

        for(Enemy i : enemies){
            i.reset();
        }
        for(Obj o : objects){
            o.setPosX(o.getDefaultPosX());
            o.setPosY(o.getDefaultPosY());
        }
    }
}
