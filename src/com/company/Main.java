package com.company;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.io.File;

public class Main {
    public static void main(String[] args)
    {
        try {
            JFrame frame = new JFrame();

            //1024 587
            //1800 1000

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1800, 1000);
            frame.setPreferredSize(new Dimension(1800, 1000));
            frame.setMinimumSize(new Dimension(1800, 1000));
            frame.setMaximumSize(new Dimension(1800, 1000));
            frame.setResizable(false);
            Panel00 panel = new Panel00(1800, 1000);
            frame.setTitle("Slime Bounce!");
            Image slimeStatic = ImageIO.read(new File("C:\\Users\\lego_\\IdeaProjects\\testGrid\\src\\sprites\\slimeJumping.png"));
            frame.setIconImage(slimeStatic);
            frame.add(panel);

            frame.setVisible(true);
        }
        catch(Exception e){

        }

    }
}