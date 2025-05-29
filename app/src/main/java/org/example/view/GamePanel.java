package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JPanel;
import org.example.controller.CollisionChecker;
import org.example.controller.GameController;
import org.example.controller.GameState;
import org.example.model.Farm;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;

public class GamePanel extends JPanel {

    private Font customFont; // For interaction prompts or other panel-specific text

    // Screen settings
    public final int originalTileSize = 16;
    public final int scale = 2; // Upscaling factor for pixel art
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20; // Tiles visible horizontally
    public final int maxScreenRow = 18; // Tiles visible vertically
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // World settings (max size of a map)
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    // public final int maxMap = 6; // This should ideally be sourced from a config or TileManager if it's dynamic

    private GameController gameController; // Reference to the main controller
    public final TileManager tileM;      // Manages tile loading and drawing
    public final GameStateUI gameStateUI;  // Manages drawing UI elements

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK); // Default background, usually covered by tiles
        this.setDoubleBuffered(true);    // For smoother rendering
        this.setFocusable(true);         // To receive key inputs

        this.tileM = new TileManager(this);
        this.gameStateUI = new GameStateUI(this);
        loadCustomPanelFont(); // Font for elements drawn directly by GamePanel (e.g., interaction prompt)
    }

    private void loadCustomPanelFont() {
        // Tries to load PressStart2P, then slkscr as fallback
        try (InputStream isPrimary = getClass().getResourceAsStream("/font/PressStart2P.ttf");
             InputStream isFallback = getClass().getResourceAsStream("/font/slkscr.ttf")) {

            Font baseFont = null;
            if (isPrimary != null) {
                baseFont = Font.createFont(Font.TRUETYPE_FONT, isPrimary);
            } else if (isFallback != null) {
                System.out.println("[GamePanel INFO] Primary font PressStart2P.ttf not found, using fallback slkscr.ttf.");
                baseFont = Font.createFont(Font.TRUETYPE_FONT, isFallback);
            } else {
                throw new IOException("Custom font files for GamePanel not found.");
            }
            customFont = baseFont.deriveFont(Font.PLAIN, 12f); // Default size for panel font
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            System.out.println("[GamePanel INFO] Custom font successfully loaded.");

        } catch (FontFormatException | IOException e) {
            System.err.println("[GamePanel WARNING] Failed to load custom font for GamePanel, using Arial. Error: " + e.getMessage());
            customFont = new Font("Arial", Font.PLAIN, 14); // Fallback to Arial
        }
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
        Graphics2D g2 = (Graphics2D) g;

        if (gameController == null) {
            drawErrorMessage(g2, "Controller not initialized...");
            g2.dispose();
            return;
        }

        Farm farmModel = gameController.getFarmModel();
        PlayerView playerView = gameController.getPlayerViewInstance();
        GameState currentGameStateManager = gameController.getGameState();

        if (farmModel == null || playerView == null || currentGameStateManager == null) {
            drawErrorMessage(g2, "Critical game data (Farm/PlayerView/GameState) not ready.");
            g2.dispose();
            return;
        }

        // 1. Draw Tiles (Background)
        if (tileM != null) {
            tileM.draw(g2, playerView, farmModel.getCurrentMap());
        }

        // 2. Draw Interactable Objects
        InteractableObject[] objectsOnCurrentMap = farmModel.getObjectsForCurrentMap();
        if (objectsOnCurrentMap != null) {
            for (InteractableObject obj : objectsOnCurrentMap) {
                if (obj != null && obj.isVisibleOnScreen(playerView, this)) { // Add visibility check
                    obj.draw(g2, this, playerView);
                }
            }
        }

        // 3. Draw Player
        // Player is drawn relative to the screen, typically centered unless at map edges.
        // Camera logic in TileManager handles world scrolling. Player's screen position is calculated here.
        int playerScreenX = screenWidth / 2 - (tileSize / 2);
        int playerScreenY = screenHeight / 2 - (tileSize / 2);

        // Adjust player's screen position if camera is at world boundaries
        if (playerView.worldX < screenWidth / 2.0) {
            playerScreenX = playerView.worldX;
        } else if (playerView.worldX > (maxWorldCol * tileSize) - screenWidth / 2.0) {
            playerScreenX = playerView.worldX - ((maxWorldCol * tileSize) - screenWidth);
        }

        if (playerView.worldY < screenHeight / 2.0) {
            playerScreenY = playerView.worldY;
        } else if (playerView.worldY > (maxWorldRow * tileSize) - screenHeight / 2.0) {
            playerScreenY = playerView.worldY - ((maxWorldRow * tileSize) - screenHeight);
        }
        playerView.draw(g2, this, playerScreenX, playerScreenY);


        // 4. Draw Interaction Prompt (only in PLAY state and if an object is in range)
        if (currentGameStateManager.getGameState() == GameState.PLAY) { // Use static constant
            drawInteractionPrompt(g2, playerView, objectsOnCurrentMap);
        }

        // 5. Draw Game State UI (Pause Menu, Inventory, Dialogue, etc.)
        if (gameStateUI != null) {
            gameStateUI.draw(g2, currentGameStateManager, farmModel);
        }

        g2.dispose(); // Release graphics context resources
    }

    private void drawInteractionPrompt(Graphics2D g2, PlayerView playerView, InteractableObject[] objects) {
        CollisionChecker cChecker = gameController.getCollisionChecker();
        if (cChecker == null || objects == null) return;

        int objIndex = cChecker.checkObject(playerView); // Check for nearby interactable objects
        if (objIndex != 999 && objIndex < objects.length && objects[objIndex] != null) {
            InteractableObject targetObject = objects[objIndex];
            String text = "[F] Interact with " + targetObject.name;

            Font interactFont = (this.customFont != null) ? this.customFont.deriveFont(14f) : new Font("Arial", Font.BOLD, 14);
            g2.setFont(interactFont);
            int textWidth = g2.getFontMetrics().stringWidth(text);
            int textHeight = g2.getFontMetrics().getHeight();
            int ascent = g2.getFontMetrics().getAscent();

            int boxX = (screenWidth - textWidth) / 2 - 10; // Centered with padding
            int boxY = screenHeight - tileSize - textHeight; // Positioned above bottom of screen
            int boxWidth = textWidth + 20;
            int boxHeight = textHeight + 5; // Adjusted padding

            // Draw semi-transparent background for prompt
            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

            // Draw prompt text
            g2.setColor(Color.WHITE);
            g2.drawString(text, boxX + 10, boxY + ascent + 2); // Adjust text position within box
        }
    }

    private void drawErrorMessage(Graphics2D g2, String message) {
        g2.setColor(Color.RED);
        Font errorFont = (this.customFont != null) ? this.customFont.deriveFont(Font.BOLD, 16f) : new Font("Arial", Font.BOLD, 16);
        g2.setFont(errorFont);
        int textWidth = g2.getFontMetrics().stringWidth(message);
        g2.drawString(message, (screenWidth - textWidth) / 2, screenHeight / 2);
    }
}