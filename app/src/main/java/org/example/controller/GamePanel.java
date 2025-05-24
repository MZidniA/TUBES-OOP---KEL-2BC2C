package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.nio.file.spi.FileSystemProvider;

import javax.swing.JPanel;

import org.example.view.entitas.PlayerView;
import org.example.controller.CollisionChecker;
import org.example.controller.TileManager;

public class GamePanel extends JPanel implements Runnable {
    // FPS

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // = 32px
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol; // 1024 px
    public final int screenHeight = tileSize * maxScreenRow; // 1024 px

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    int FPS = 60;
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public PlayerView player = new PlayerView(this, keyH);

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

        double drawInterval = 1000000000 / 60; // FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        // double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update(); // Perbarui posisi player
                repaint(); // Gambar ulang layar
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }

            // try{
            // double remainingTime = nextDrawTime - System.nanoTime();
            // remainingTime = remainingTime/1000000;
            // if (remainingTime<0){
            // remainingTime = 0;
            // }

            // Thread.sleep((long) remainingTime);

            // nextDrawTime += drawInterval;
            // }
            // catch (InterruptedException e){
            // e.printStackTrace();

            // }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);

        player.draw(g2); // Tetap gambar player
        g2.dispose();
    }
}

