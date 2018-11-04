package com.company;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import groovy.json.JsonParser;
import org.json.*;

public class Level {
    //WILL CHANGE TO JSON OBJECT WITH MORE STUFF SOON,
    //ONCE THE DEVTOOLS ARE UP TO DATE WHICH SHOULD HAPPEN ONCE LIGHTING IS DONE.
    //UPDATE: DEVTOOLS ARE NOW GOING TO BE BUILT INTO THE BASE GAME, BUT THIS
    //WILL STILL BE CHANGED COMPLETELY.

    //I'm gonna have to make this better once devtools are done...
    //public static Enemy[] enemies = new Enemy[0];
    //public static Obj[] objects = new Obj[18];
    //public static Background[] backgrounds = new Background[3];
    //public static Light[] lights = new Light[2];
    public static String name;
    public static int deathLevel;
    public static Sounds music;
    public static Color ambientLight = new Color(44, 0, 0);

    public static ArrayList<Obj> objects = new ArrayList();
    public static ArrayList<Enemy> enemies = new ArrayList();
    public static ArrayList<Light> lights = new ArrayList();
    public static ArrayList<Background> backgrounds = new ArrayList();

    public static Player player;

    public static void loadLevel(String name) throws JSONException{
        String json = null;
        try{
            json = new String(Files.readAllBytes(Paths.get("src\\sprites\\"+ name+".json")), StandardCharsets.UTF_8);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        JSONObject levelObject = new JSONObject(json);
        name = levelObject.getString("name");
        deathLevel = levelObject.getInt("deathLevel");
        music = new Sounds(levelObject.getString("music"));
        JSONObject alo = levelObject.getJSONObject("ambientLight");
        ambientLight = new Color(alo.getInt("red"),alo.getInt("green"),alo.getInt("blue"));
        //Skipping spawn for now.
        JSONArray objectArray = levelObject.getJSONArray("objects");
        for(int i = 0; i < objectArray.length(); i++) {
            JSONObject object = objectArray.getJSONObject(i);

            objects.add(new Obj(object.getString("sprite"), object.getInt("x"), object.getInt("y"), object.getInt("scale"), object.getInt("collision")));
        }
        JSONArray lightsArray = levelObject.getJSONArray("lights");
        for(int i = 0; i < lightsArray.length(); i++) {
            JSONObject object = lightsArray.getJSONObject(i);

            lights.add(new Light(object.getInt("x"), object.getInt("y"), object.getInt("radius"), new Color(object.getInt("red"),object.getInt("green"),object.getInt("blue"))));
        }
    }


    public void saveLevel(String levelName){
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("deathLevel", deathLevel);
            obj.put("music", music);
            JSONObject ambientJSON = new JSONObject();
            ambientJSON.put("red", ambientLight.getRed());
            ambientJSON.put("green", ambientLight.getGreen());
            ambientJSON.put("blue", ambientLight.getBlue());
            obj.put("ambientLight", ambientJSON);
            //Skipping spawn for now.
            JSONArray objectArray = new JSONArray();
            for (Obj o : objects){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("sprite", o.getSource());
                jsonObj.put("x", o.getPosX());
                jsonObj.put("y", o.getPosY());
                jsonObj.put("scale", o.getScale());
                jsonObj.put("collision", o.getCollision());

                objectArray.put(jsonObj);
            }

            obj.put("objects", objectArray);

            JSONArray lightArray = new JSONArray();
            for (Light l : lights){
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("x", l.getPosX());
                jsonObj.put("y", l.getPosY());
                jsonObj.put("radius", l.getRadius());
                jsonObj.put("red", l.getColor().getRed());
                jsonObj.put("green", l.getColor().getGreen());
                jsonObj.put("blue", l.getColor().getBlue());

                lightArray.put(jsonObj);
            }

            obj.put("lights", lightArray);

            FileWriter writer = new FileWriter(levelName + ".json");
            writer.write(obj.toString());
            writer.close();

            System.out.println(obj.toString());
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
