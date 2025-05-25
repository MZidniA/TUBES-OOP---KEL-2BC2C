package org.example;

import javax.swing.JFrame;
import org.example.controller.GamePanel; // Ensure GamePanel is defined or imported
import org.example.controller.MenuPanel;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Spakbor Hills");
        window.setSize(640, 576); // ukuran sesuai menu kamu

        // HANYA TAMBAHKAN MENU PANEL DULU
        MenuPanel menuPanel = new MenuPanel(window);
        window.add(menuPanel);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
