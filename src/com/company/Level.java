package com.company;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Level {
    //WILL CHANGE TO JSON OBJECT WITH MORE STUFF SOON.

    public static Sounds music = new Sounds("src\\sprites\\music.wav");

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Obj> objects = new ArrayList<>();
    public static ArrayList<Background> backgrounds = new ArrayList<>();
    public static int deathLevel = 1000;
    public static ArrayList<Light> lights = new ArrayList<>();

    public static Player player;

    public static void addBackgrounds(){
        Level.backgrounds.add(new Background("src\\sprites\\background.png", 10, 2, 0));
        Level.backgrounds.add(new Background("src\\sprites\\background2.png", 5, 2, 400));
    }

    public static void addEnemies(){
        Level.enemies.add(new Enemy("src\\sprites\\slimeStatic.png", 1000, 0, 1, 10, 0.5, 7, 10, 0.6));

        lights.add(new Light(1000, 400, 800, new Color(75, 219, 255)));
        //lights.add(new Light(1500, 400, 800, new Color(255, 0, 0)));
        //lights.add(new Light(1250, 100, 800, new Color(0, 255, 0)));


        objects.add(new DamageObj("src\\sprites\\enemy.png", 4000, 450, 3, 1, 1, 10));
    }

    //Must be modified in future.
    public static void loadLevel(String name){
        System.out.println("Loading.");
        try{
            BufferedReader r = new BufferedReader(new FileReader("lvl-"+name+".csv"));
            String tmp = r.readLine();
            while(!(tmp == null)){
                String[] obj = tmp.split(",");
                objects.add(new Obj(obj[0], Integer.parseInt(obj[1]),Integer.parseInt(obj[2]),Integer.parseInt(obj[3]),0));
                tmp = r.readLine();
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
