package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.example.view.Entity;
import org.example.view.TileManager;
import org.example.view.Player;


public class GamePanel extends JPanel implements Runnable {

    // ========== SCREEN SETTINGS ==========
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 32px

    public final int maxScreenCol = 32;
    public final int maxScreenRow = 32;
    public final int screenWidth = tileSize * maxScreenCol; // 1024 px
    public final int screenHeight = tileSize * maxScreenRow; // 1024 px

    // ========== SYSTEM ==========
    int FPS = 60;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    // ========== ENTITY & TILE ==========
    TileManager tileM = new TileManager(this);
    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();   // logic game
                repaint();  // render ulang
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update(); // gerak berdasarkan input
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // DRAW ORDER:
        // 1. Tile Background
        tileM.draw(g2); // atau: drawBackground(g2);

        // 2. Player
        player.draw(g2);

        g2.dispose();
    }

    // Optional: jika kamu tidak pakai TileManager
    public void drawBackground(Graphics2D g2) {
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                int x = col * tileSize;
                int y = row * tileSize;

                g2.setColor(new Color(30, 120, 40)); // hijau
                g2.fillRect(x, y, tileSize, tileSize);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, tileSize, tileSize);
            }
        }
    }
}
