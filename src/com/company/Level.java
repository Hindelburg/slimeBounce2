package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class Level {
    public static Sounds music = new Sounds("src\\sprites\\music.wav");

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Obj> objects = new ArrayList<>();
    public static ArrayList<Background> backgrounds = new ArrayList<>();
    public static int deathLevel = 1000;

    public static Player player;

    public static void addBackgrounds(){
        Level.backgrounds.add(new Background("src\\sprites\\background.png", 10, 2, 0));
        Level.backgrounds.add(new Background("src\\sprites\\background2.png", 5, 2, 400));
    }
    private static Random r = new Random();

    public static void addEnemies(){
        for(int i = 200; i <= 5000; i=i+10) {
            int i2 = 100+r.nextInt(500);
            int i3 = 10+r.nextInt(30);
            Level.enemies.add(new Enemy("src\\sprites\\slimeStatic.png", i, i2, 1, i3, 0.5, 7, 10, 0.6));
        }
    }


    public static void loadLevel(String name){
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
}
