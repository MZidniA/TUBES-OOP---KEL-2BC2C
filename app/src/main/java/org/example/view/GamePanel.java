package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.example.controller.CollisionChecker;
import org.example.controller.GameController;
import org.example.controller.GameState;
import org.example.controller.action.UpdateAndShowLocationAction;
import org.example.model.Farm;
import org.example.model.Inventory;
import org.example.model.Player;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;

public class GamePanel extends JPanel {

    private Font customFont;

    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxMap = 6;

    private GameController gameController;
    private UpdateAndShowLocationAction updateLocationAction;
    public final TileManager tileM;
    public final GameStateUI gameStateUI;

    private JPanel miniGamePanel = null;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        this.tileM = new TileManager(this);
        this.gameStateUI = new GameStateUI(this);
        loadCustomFont();

        new Timer(16, e -> repaint()).start();
    }

    private void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            if (is == null) {
                System.out.println("Font kustom TIDAK ditemukan!");
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 12f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            System.out.println("Font kustom berhasil dimuat di GamePanel.");
        } catch (Exception e) {
            System.out.println("Font kustom tidak ditemukan, menggunakan Arial.");
            customFont = new Font("Arial", Font.PLAIN, 14);
        }
    }

    public GameStateUI getGameStateUI() {
        return this.gameStateUI;
    }

    public GameController getController() {
        return this.gameController;
    }

    public void setController(GameController controller) {
        this.gameController = controller;
        if (this.gameController != null) {
            this.updateLocationAction = new UpdateAndShowLocationAction(this.gameController);
        }
    }

    public void setMiniGamePanel(JPanel panel) {
        if (miniGamePanel != null) {
            this.remove(miniGamePanel);
        }
        this.miniGamePanel = panel;
        this.add(panel);
        this.revalidate();
        this.repaint();
    }

    public void removeMiniGamePanel() {
        if (miniGamePanel != null) {
            this.remove(miniGamePanel);
            this.miniGamePanel = null;
            this.revalidate();
            this.repaint();
        }
    }

    public String getPlayerCurrentLocationDetail() {
        if (this.gameController == null) return "Lokasi: Controller N/A";
        if (this.updateLocationAction == null) {
            if (this.gameController != null) {
                this.updateLocationAction = new UpdateAndShowLocationAction(this.gameController);
            } else {
                return "Lokasi: Action N/A";
            }
        }

        Farm farmModel = this.gameController.getFarmModel();
        if (farmModel != null) {
            return this.updateLocationAction.updateAndGetDetailedLocationString(farmModel);
        }
        return "Lokasi: Farm N/A";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameController == null) {
            g.setColor(Color.WHITE);
            g.drawString("Controller tidak terinisialisasi...", screenWidth / 2 - 100, screenHeight / 2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        Farm farmModel = gameController.getFarmModel();
        PlayerView playerView = gameController.getPlayerViewInstance(); 
        GameState currentGameState = gameController.getGameState();    

        Player playerModel = null;
        Inventory playerInventory = null;

        if (farmModel != null) {
            playerModel = farmModel.getPlayerModel();
            if (playerModel != null) {
                playerInventory = playerModel.getInventory();
            }
        }

        if (farmModel != null && playerView != null) {
            tileM.draw(g2, playerView, farmModel.getCurrentMap());

            InteractableObject[] objectsOnCurrentMap = farmModel.getObjectsForCurrentMap();
            if (objectsOnCurrentMap != null) { 
                for (InteractableObject obj : objectsOnCurrentMap) {
                    if (obj != null) {
                        obj.draw(g2, this, playerView); 
                    }
                }
            }


            int playerScreenX = screenWidth / 2 - (tileSize / 2);
            int playerScreenY = screenHeight / 2 - (tileSize / 2);
            int worldWidth = maxWorldCol * tileSize;
            int worldHeight = maxWorldRow * tileSize;

            if (playerView.worldX < screenWidth / 2) {
                playerScreenX = playerView.worldX;
            } else if (playerView.worldX > worldWidth - screenWidth / 2) {
                playerScreenX = playerView.worldX - (worldWidth - screenWidth);
            }

            if (playerView.worldY < screenHeight / 2) {
                playerScreenY = playerView.worldY;
            } else if (playerView.worldY > worldHeight - screenHeight / 2) {
                playerScreenY = playerView.worldY - (worldHeight - screenHeight);
            }

            playerView.draw(g2, this, playerScreenX, playerScreenY); 
            if (currentGameState != null && currentGameState.getGameState() == currentGameState.play) { 
                CollisionChecker cChecker = gameController.getCollisionChecker();
                if (cChecker != null) {
                    int objIndex = cChecker.checkObject(playerView);

                    if (objIndex != 999 && objectsOnCurrentMap != null &&
                        objIndex < objectsOnCurrentMap.length &&
                        objectsOnCurrentMap[objIndex] != null) {

                        g2.setColor(Color.WHITE);
                        Font interactFont = (this.customFont != null) ? this.customFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16);
                        g2.setFont(interactFont);

                        String text = "[F] Interact with " + objectsOnCurrentMap[objIndex].name;
                        int textWidth = g2.getFontMetrics().stringWidth(text);
                        int x = (screenWidth - textWidth) / 2;
                        int y = screenHeight - 40; // Posisi prompt interaksi

                        g2.setColor(new Color(0, 0, 0, 150)); // Latar belakang semi-transparan
                        g2.fillRect(x - 10, y - g2.getFontMetrics().getAscent() - 2, textWidth + 20, g2.getFontMetrics().getHeight() + 4);

                        g2.setColor(Color.WHITE);
                        g2.drawString(text, x, y);
                    }
                }
            }
        } else {
            g2.setColor(Color.RED);
            String errorMessage = "Data game (";
            if (farmModel == null) errorMessage += "FarmModel N/A";
            if (playerView == null) {
                if (farmModel == null) errorMessage += ", ";
                errorMessage += "PlayerView N/A";
            }
            errorMessage += ") belum siap.";
            g2.drawString(errorMessage, 20, 40);
        }

        if (currentGameState != null && gameStateUI != null) {
            if (farmModel != null && playerModel != null && playerInventory != null) {
                gameStateUI.draw(g2, currentGameState, playerInventory);
            } else {
                if (currentGameState.getGameState() == currentGameState.cooking_menu) {
                    System.err.println("GamePanel.paintComponent: Data (Farm/Player/Inventory) tidak lengkap untuk UI cooking_menu.");
                    g2.setColor(Color.RED);
                    g2.setFont(new Font("Arial", Font.BOLD, 14));
                    String cookingErrorMsg = "Error: Tidak bisa menampilkan menu memasak (data tidak lengkap).";
                    int errorMsgWidth = g2.getFontMetrics().stringWidth(cookingErrorMsg);
                    g2.drawString(cookingErrorMsg, (screenWidth - errorMsgWidth) / 2, screenHeight / 2);
                } else {
                    System.err.println("GamePanel.paintComponent: Data (Farm/Player/Inventory) tidak lengkap untuk UI draw state: " + currentGameState.getGameState());
                }
            }
        } else {
            if (currentGameState == null) return;
            if (gameStateUI == null) return;
        }

        g2.dispose();
    }
}
