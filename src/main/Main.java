package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("OOP Assignment 1");

        MainMenu mainMenu = new MainMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the game by replacing the MainMenu panel with the GamePanel
                GamePanel gamePanel = new GamePanel();
                window.setContentPane(gamePanel);
                window.pack();
                gamePanel.startGameThread();
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the game by closing the window
                window.dispose();
            }
        });

        window.setContentPane(mainMenu);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
