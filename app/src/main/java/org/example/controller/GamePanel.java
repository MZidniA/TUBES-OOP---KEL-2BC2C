package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.model.Sound;
import org.example.view.GameStateUI;
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
    public final int maxMap = 6;
    public int currentMap = 0; // Indeks p
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    Thread gameThread;
    
    // GAME SETTINGS
    public TileManager tileM = new TileManager(this);

    public KeyHandler keyH = new KeyHandler(this);
    public GameState gameState = new GameState();
    public GameStateUI gameStateUI = new GameStateUI(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public PlayerView player = new PlayerView(this, keyH);
    public InteractableObject obj[][] = new InteractableObject[maxMap][20];
    private JFrame frame;

    Sound music = new Sound();

    public GamePanel(JFrame frame) {
        this.frame = frame;
        this.keyH = new KeyHandler(this);
        this.player = new PlayerView(this, keyH);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setInteractableObject();
        gameState.setGameState(gameState.play); 
        
    }

    public void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt(); 
            try {
                gameThread.join(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.err.println("Gagal menghentikan game thread sebelumnya.");
            }
        }
        gameThread = new Thread(this);
        gameThread.start();

        // PLAY SOUND
        music.setFile();
        music.play();
        music.loop();

    }

    public void stopGameThread() {
        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
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
        if (keyH.escapePressed) {
            if (gameState.getGameState() == gameState.play) {
                gameState.setGameState(gameState.pause);
            } else if (gameState.getGameState() == gameState.pause) {
                gameState.setGameState(gameState.play);
            }
            keyH.escapePressed = false; 
        }

        if (gameState.getGameState() == gameState.play) { 
            player.update(); 
            
            int objIndex = cChecker.checkObject(player, obj, currentMap);
            if (objIndex != 999 && keyH.interactPressed) {
                 if (obj[currentMap][objIndex] != null) { 
                    obj[currentMap][objIndex].interact();
                 }
                 keyH.interactPressed = false; 
            }

        } else if (gameState.getGameState() == gameState.pause) { 
            if (keyH.enterPressed) {
                if (gameStateUI.commandNum == 0) { 
                    gameState.setGameState(gameState.play);
                } else if (gameStateUI.commandNum == 1) { 
                    exitToMenu(); 
                }
                keyH.enterPressed = false; 
            }
        }
     
    }

    public void teleportPlayer(int mapIndex, int newWorldX, int newWorldY) {
        music.stop(); 
        currentMap = mapIndex;
        player.worldX = newWorldX;
        player.worldY = newWorldY;
    
        for (int i = 0; i < obj[currentMap].length; i++) {
            obj[currentMap][i] = null;
        }
    
        String mapPath = "";
        if (currentMap == 0) {
            mapPath = "/maps/map.txt";
        } else if (currentMap == 1) {
            mapPath = "/maps/beachmap.txt";
        }
    
        if (!mapPath.isEmpty()) {
            tileM.loadMap(mapPath, currentMap); 
        } else {
            System.err.println("Path peta tidak valid untuk mapIndex: " + currentMap);
        }
        
        music.play();
        
        System.out.println("Player diteleportasi ke map " + currentMap + " di tile (" + player.worldX/tileSize + "," + player.worldY/tileSize + ")");
    }

    private void exitToMenu() {
        stopGameThread(); 

        frame.getContentPane().removeAll();
        MenuPanel menuPanel = new MenuPanel(frame); 
        frame.setContentPane(menuPanel);
        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(menuPanel::requestFocusInWindow);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState.getGameState() == gameState.play || gameState.getGameState() == gameState.pause) {
            tileM.draw(g2);
            for (InteractableObject interactableObjItem : obj[currentMap]) {
                if (interactableObjItem != null) {
                    interactableObjItem.draw(g2, this);
                }
            }
            player.draw(g2);
        }

        if (gameState.getGameState() == gameState.play) {
            int objIndex = cChecker.checkObject(player, obj, currentMap);
            if (objIndex != 999) {
                // INTERACT
                g2.setColor(Color.WHITE);
                g2.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 20));
                String text = "[F] Interact with " + obj[currentMap] [objIndex].name;
                int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2;
                int y = getHeight() - 50;
                g2.drawString(text, x, y);
            }
        }

        gameStateUI.draw(g2); // Gambar UI game state

        g2.dispose();
    }
}

