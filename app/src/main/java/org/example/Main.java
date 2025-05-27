package org.example;

import javax.swing.JFrame;

import org.example.controller.MenuPanel; 

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Spakbor Hill");

        MenuPanel menuPanel = new MenuPanel(window);

        window.setContentPane(menuPanel);

        window.pack(); 
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}