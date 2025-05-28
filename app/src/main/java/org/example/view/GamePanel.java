package org.example.view;

import java.awt.*;
import javax.swing.JPanel;
import org.example.controller.GameController;
import org.example.controller.GameState;
import org.example.model.Farm;
import org.example.model.Inventory;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;
import org.example.controller.CollisionChecker; // Ensure this is the correct package for CollisionChecker

public class GamePanel extends JPanel {

    private Font customFont;

    // Konfigurasi layar & dunia
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 32x32
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol;  // 640
    public final int screenHeight = tileSize * maxScreenRow; // 576

    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxMap = 6;

    private GameController gameController;
    public final TileManager tileM;
    public final GameStateUI gameStateUI;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        this.tileM = new TileManager(this);
        this.gameStateUI = new GameStateUI(this);

        // Initialize customFont
        this.customFont = new Font("Arial", Font.PLAIN, 12);
    }

    public GameStateUI getGameStateUI() {
        return this.gameStateUI;
    }

    public void setController(GameController controller) {
        this.gameController = controller;
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

        // Ambil semua data yang dibutuhkan untuk menggambar dari Model MELALUI Controller
        Farm farmModel = gameController.getFarmModel();
        PlayerView playerView = gameController.getPlayerViewInstance();
        GameState currentGameState = gameController.getGameState();

        Inventory playerInventory = null;
        if (farmModel != null && farmModel.getPlayerModel() != null) {
            playerInventory = farmModel.getPlayerModel().getInventory();
        }

        // Urutan render: Tile -> Objek -> Player -> UI
        if (farmModel != null && playerView != null) {
            // 1. Gambar Tile
            if (tileM != null) {
                tileM.draw(g2, playerView, farmModel.getCurrentMap());
            }


            InteractableObject[] objectsOnCurrentMap = farmModel.getObjectsForCurrentMap();
            if (objectsOnCurrentMap != null) {
                for (InteractableObject obj : objectsOnCurrentMap) {
                    if (obj != null) {
                        obj.draw(g2, this, playerView);
                    }
                }
            }

            playerView.draw(g2, this);

            if (currentGameState != null && currentGameState.getGameState() == currentGameState.play) {
                CollisionChecker cChecker = gameController.getCollisionChecker();
                if (cChecker != null) {
                    int objIndex = cChecker.checkObject(playerView);

                    if (objIndex != 999 && objectsOnCurrentMap != null && objIndex < objectsOnCurrentMap.length && objectsOnCurrentMap[objIndex] != null) {
                        g2.setColor(Color.WHITE);
                        Font interactFont = (this.customFont != null) ? this.customFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16);
                        g2.setFont(interactFont);

                        String text = "[F] Interact with " + objectsOnCurrentMap[objIndex].name;
                        int textWidth = g2.getFontMetrics().stringWidth(text);
                        int x = (screenWidth - textWidth) / 2;
                        int y = screenHeight - 40;

                        g2.setColor(new Color(0, 0, 0, 150));
                        g2.fillRect(x - 10, y - g2.getFontMetrics().getAscent() - 2, textWidth + 20, g2.getFontMetrics().getHeight() + 4);

                        g2.setColor(Color.WHITE);
                        g2.drawString(text, x, y);
                    }
                }
            }
        } else {
            g2.setColor(Color.RED);
            g2.drawString("Data game (Farm/PlayerView) belum siap.", 20, 40);
        }


        if (currentGameState != null && gameStateUI != null) {
            gameStateUI.draw(g2, currentGameState, playerInventory);
        }

        g2.dispose();
    }
}