package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.example.model.Sound;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // = 32px
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol; // 1024 px
    public final int screenHeight = tileSize * maxScreenRow; // 1024 px

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    Thread gameThread;
    
    // GAME SETTINGS
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public PlayerView player = new PlayerView(this, keyH);
    public InteractableObject obj[] = new InteractableObject[20];

    Sound music = new Sound();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setInteractableObject();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();

        // PLAY SOUND
        music.setFile();
        music.play();
        music.loop();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / 60; // FPS
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
                update(); 
                repaint(); 
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
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

        for (InteractableObject obj : obj) {
            if (obj != null) {
                obj.draw(g2, this); 
            }
        }

        player.draw(g2); 

        int objIndex = cChecker.checkObject(player, obj);
        if (objIndex != 999) {
            // INTERACT
            g2.setColor(Color.WHITE);
            g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
            String text = "[F] Interact with " + obj[objIndex].name;
            int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2;
            int y = getHeight() - 50;
            g2.drawString(text, x, y);
        }

        g2.dispose();
    }
}

