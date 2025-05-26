package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.model.Sound;
import org.example.view.GameStateUI;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.model.Sound;
import org.example.controller.GameState;
import org.example.controller.CollisionChecker;
import org.example.controller.TileManager;
import org.example.model.Player;
import org.example.model.NPC.NPC;
import org.example.model.Inventory;



public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // = 32px
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxMap = 6;
    public int currentMap = 0;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    Thread gameThread;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public GameState gameState = new GameState();
    public GameStateUI gameStateUI = new GameStateUI(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    NPC partner = null;
    public Inventory inventory = new Inventory();
    Player p = new Player("John", "Male", "Sunny Farm", partner, inventory);
    public PlayerView player = new PlayerView(this, keyH, p);
 // Player kedua jika diperlukan
    public InteractableObject obj[][] = new InteractableObject[maxMap][20];
    private JFrame frame;
    Sound music = new Sound();

    // FONT KUSTOM
    public Font customFont;

    public GamePanel(JFrame frame) {
        this.frame = frame;
        this.keyH = new KeyHandler(this);
        this.player = new PlayerView(this, keyH, p);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        loadCustomFont();
    }

    public void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 18f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            System.out.println("Font kustom berhasil dimuat.");
        } catch (Exception e) {
            System.out.println("Font kustom tidak ditemukan, menggunakan Arial.");
            customFont = new Font("Arial", Font.PLAIN, 18);
        }
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
        double drawInterval = 1000000000 / 60;
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

        if (keyH.inventoryPressed) {
            if (gameState.getGameState() == gameState.play) {
                gameState.setGameState(gameState.inventory);
            } else if (gameState.getGameState() == gameState.inventory) {
                gameState.setGameState(gameState.play);
            }
            keyH.inventoryPressed = false; 
        }


        if (gameState.getGameState() == gameState.play) {
            player.update();
    

            if (keyH.interactPressed) {
                boolean interactionHandled = false;
    

                int objIndex = cChecker.checkObject(player, obj, currentMap);
                if (objIndex != 999) {
                    if (obj[currentMap][objIndex] != null) {
                        obj[currentMap][objIndex].interact();
                        interactionHandled = true;
                    }
                }

                keyH.interactPressed = false; // Reset flag setelah interaksi
                
                // Jika TIDAK ada interaksi dengan objek, cek interaksi dengan TILE (misal: teleport)
                if (!interactionHandled) {
                    int playerCol = (player.worldX + player.solidArea.x + player.solidArea.width / 2) / tileSize;
                    int playerRow = (player.worldY + player.solidArea.y + player.solidArea.height / 2) / tileSize;
    
                    if (playerCol >= 0 && playerCol < maxWorldCol && playerRow >= 0 && playerRow < maxWorldRow) {
    
                        // Contoh logika teleport dari map 0 ke 1
                        if (currentMap == 0 && playerCol == 31 && playerRow == 31) { // Ganti 69 dengan ID tile teleport Anda
                            teleportPlayer(1, 5 * tileSize, 5 * tileSize); 
                        }
                        else if (currentMap == 1 && playerCol == 0 && playerRow == 0 ) { 
                            teleportPlayer(0, 4 * tileSize, 9 * tileSize); 
                        } else if (currentMap == 1 && playerCol == 31 && playerRow == 31) { 
                            teleportPlayer(2, 4 * tileSize, 9 * tileSize); 
                        } else if (currentMap == 2 && playerCol == 31 && playerRow == 0) { 
                            teleportPlayer(1, 5 * tileSize, 5 * tileSize); 
                        } else if (currentMap == 2 && playerCol == 31 && playerRow == 31) { 
                            teleportPlayer(3, 4 * tileSize, 9 * tileSize); 
                        } else if (currentMap == 3 && playerCol == 31 && playerRow == 29) { 
                            teleportPlayer(2, 5 * tileSize, 5 * tileSize);
                        } else if (currentMap == 1 && playerCol == 31 && playerRow == 0) {
                            teleportPlayer(2, 4 * tileSize, 9 * tileSize); 
                        } else if (currentMap == 2 && playerCol == 0 && playerRow == 0) { 
                            teleportPlayer(1, 5 * tileSize, 5 * tileSize); 
                        } else if (currentMap == 2 && playerCol == 0 && playerRow == 31) { 
                            teleportPlayer(3, 4 * tileSize, 9 * tileSize); 
                        } else if (currentMap == 3 && playerCol == 0 && playerRow == 29) { 
                            teleportPlayer(2, 5 * tileSize, 5 * tileSize); 
                        } 
                            
                            }
                        }
                    }
                    
                    keyH.interactPressed = false;
                        
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

        System.out.println("Player diteleportasi ke map " + currentMap + " di tile (" + player.worldX / tileSize + "," + player.worldY / tileSize + ")");
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
        } else if (gameState.getGameState() == gameState.inventory) {
            // Saat di inventory, Anda mungkin tetap ingin menggambar game di belakangnya
            // dengan efek blur atau gelap, atau hanya menggambar background inventory.
            // Untuk saat ini, kita gambar juga game world agar ada latar.
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
                g2.setColor(Color.WHITE);
                g2.setFont(customFont);
                String text = "[F] Interact with " + obj[currentMap][objIndex].name;
                int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2;
                int y = getHeight() - 50;
                g2.drawString(text, x, y);
            }
        }

        gameStateUI.draw(g2);
        g2.dispose();
    }
}
