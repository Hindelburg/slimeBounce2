package com.company;

import java.util.ArrayList;

public class Level {
    public static Sounds music = new Sounds("src\\sprites\\music.wav");

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Obj> objects = new ArrayList<>();
    public static ArrayList<Background> backgrounds = new ArrayList<>();

    public static Player player;

    public static void addBackgrounds(){
        Level.backgrounds.add(new Background("src\\sprites\\background.png", 10, 2, 0));
        Level.backgrounds.add(new Background("src\\sprites\\background2.png", 5, 2, 400));
    }
}
