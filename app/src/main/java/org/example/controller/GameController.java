package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.example.model.Farm;
import org.example.model.Items.Items;
import org.example.model.Player;
import org.example.model.Sound;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;
import java.awt.Graphics2D;

public class GameController implements Runnable {

    // --- Komponen utama ---
    private final GamePanel gamePanel;
    final Farm farm;
    private final PlayerView playerViewInstance;
    private final TileManager tileManager;
    private final GameStateUI gameStateUI;

    // --- Helper/logic ---
    private final KeyHandler keyHandler;
    private final CollisionChecker cChecker;
    private final AssetSetter aSetter;
    private final GameState gameState;
    private final Sound music;

    private Thread gameThread;
    private final Map<String, Boolean> movementState = new HashMap<>();

    public GameController(GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;
        this.gameState = new GameState();
        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);
        this.keyHandler = new KeyHandler(this);
        this.music = new Sound();
        this.music.setFile();

        this.tileManager = new TileManager(gamePanel);
        this.gameStateUI = new GameStateUI(gamePanel);



        // --- Integrasi KeyHandler ke panel ---
        if (this.gamePanel != null) {
            this.gamePanel.addKeyListener(this.keyHandler);
            this.gamePanel.setFocusable(true);
        }
        movementState.put("up", false);
        movementState.put("down", false);
        movementState.put("left", false);
        movementState.put("right", false);
        setupGame();
    }

    public GameStateUI getGameStateUIFromPanel() {
        if (this.gamePanel != null) {
            return this.gamePanel.getGameStateUI();
        }
        return null;
    }

    public CollisionChecker getCollisionChecker() {
        return this.cChecker;
    }

    private void setupGame() {
        if (aSetter != null) {
            aSetter.setInteractableObject();
        }
        gameState.setGameState(this.gameState.play);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic();
    }

    public void playMusic() {
        if (music != null) {
            music.play();
            music.loop();
        }
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60.0;
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
                if (gamePanel != null) {
                    gamePanel.repaint();
                }
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

    private void update() {
        if (playerViewInstance == null || cChecker == null) return;

        if (gameState.getGameState() == this.gameState.play) {
            playerViewInstance.update(movementState, cChecker);
        }
    }

    // --- Input handler ---
    public void handlePlayerMove(String direction, boolean isMoving) {
        if (gameState.getGameState() == gameState.play) {
            movementState.put(direction, isMoving);
        }
    }

    public void handleInteraction() {
        if (gameState.getGameState() == this.gameState.play) {
            if (playerViewInstance != null && cChecker != null && farm != null) {
                int objIndex = cChecker.checkObject(playerViewInstance);
                if (objIndex != 999) {
                    InteractableObject[] currentObjects = farm.getObjectsForCurrentMap();
                    if (currentObjects != null && objIndex < currentObjects.length && currentObjects[objIndex] != null) {
                        currentObjects[objIndex].interact(this);
                    }
                } else {
                    int tileSize = getTileSize();
                    int playerCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width / 2) / tileSize;
                    int playerRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height / 2) / tileSize;
                    int currentMap = farm.getCurrentMap();
                    if (playerCol >= 0 && playerCol < getMaxWorldCol() && playerRow >= 0 && playerRow < getMaxWorldRow()) {
                        if (currentMap == 0 && playerCol == 31 && playerRow == 15) {
                            teleportPlayer(1, 1 * tileSize, 1 * tileSize);
                        } else if (currentMap == 1 && playerCol == 0 && playerRow == 0) {
                            teleportPlayer(0, 31 * tileSize, 15 * tileSize);
                        } else if (currentMap == 1 && playerCol == 31 && playerRow == 0) {
                            teleportPlayer(2, 29 * tileSize, 0 * tileSize);
                        } else if (currentMap == 2 && playerCol == 29 && playerRow == 0) {
                            teleportPlayer(1, 31 * tileSize, 0 * tileSize);
                        } else if (currentMap == 2 && playerCol == 0 && playerRow == 31) {
                            teleportPlayer(3, 15 * tileSize, 31 * tileSize);
                        } else if (currentMap == 3 && playerCol == 15 && playerRow == 31) {
                            teleportPlayer(2, 0 * tileSize, 31 * tileSize);
                        } else if (currentMap == 4 && playerCol == 3 && playerRow == 11) {
                            teleportPlayer(0, 4 * tileSize, 9 * tileSize);
                        }
                    }
                    // Tambahkan logika teleportasi lain sesuai kebutuhan
                }
            }
        }
    }

    public void togglePause() {
        if (gameState.getGameState() == gameState.play) {
            gameState.setGameState(gameState.pause);
        } else if (gameState.getGameState() == gameState.pause) {
            gameState.setGameState(gameState.play);
        }
    }

    public void toggleInventory() {
        if (gameState.getGameState() == gameState.play) {
            gameState.setGameState(gameState.inventory);
        } else if (gameState.getGameState() == gameState.inventory) {
            gameState.setGameState(gameState.play);
        }
    }

    public void navigatePauseUI(String direction) {
        if (gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            if (direction.equals("up")) {
                ui.commandNum--;
                if (ui.commandNum < 0) ui.commandNum = 1;
            } else if (direction.equals("down")) {
                ui.commandNum++;
                if (ui.commandNum > 1) ui.commandNum = 0;
            }
        }
    }

    public void confirmPauseUISelection() {
        if (gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            if (ui.commandNum == 0) {
                gameState.setGameState(this.gameState.play);
            } else if (ui.commandNum == 1) {
                System.out.println("Exit Game dipilih (implementasi kembali ke menu/quit diperlukan)");
                System.exit(0);
            }
        }
    }

    public void navigateInventoryUI(String direction) {
        if (gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            int slotsPerRow = 12;
            int totalDisplayRows = 3;

            switch (direction) {
                case "up":
                    ui.slotRow--;
                    if (ui.slotRow < 0) ui.slotRow = totalDisplayRows - 1;
                    break;
                case "down":
                    ui.slotRow++;
                    if (ui.slotRow >= totalDisplayRows) ui.slotRow = 0;
                    break;
                case "left":
                    ui.slotCol--;
                    if (ui.slotCol < 0) ui.slotCol = slotsPerRow - 1;
                    break;
                case "right":
                    ui.slotCol++;
                    if (ui.slotCol >= slotsPerRow) ui.slotCol = 0;
                    break;
            }
        }
    }

    public void confirmInventoryUISelection() {
        if (farm != null && farm.getPlayerModel() != null && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            Player playerModel = farm.getPlayerModel();
            int slotIndex = ui.slotCol + (ui.slotRow * 12);
            ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(playerModel.getInventory().getInventory().entrySet());

            if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
                Items selectedItem = inventoryList.get(slotIndex).getKey();
                playerModel.setCurrentHeldItem(selectedItem);
                System.out.println("Selected from inventory: " + selectedItem.getName());
            } else {
                playerModel.setCurrentHeldItem(null);
                System.out.println("Selected empty slot or out of bounds.");
            }
            gameState.setGameState(this.gameState.play);
        }
    }

    // --- Teleportasi player antar map ---
    public void teleportPlayer(int mapIndex, int worldX, int worldY) {
        if (farm != null && playerViewInstance != null && gamePanel != null && gamePanel.tileM != null && aSetter != null) {
            farm.setCurrentMap(mapIndex);
            playerViewInstance.worldX = worldX;
            playerViewInstance.worldY = worldY;
            playerViewInstance.direction = "down";
            tileManager.loadMap(farm.getMapPathFor(mapIndex), mapIndex);
            farm.clearObjects(mapIndex);
            aSetter.setInteractableObject();
            System.out.println("Player diteleportasi ke map " + mapIndex + " di (" + worldX/getTileSize() + "," + worldY/getTileSize() + ")");
        }
    }

    // --- Getters untuk digunakan oleh komponen lain (terutama View) ---
    public Farm getFarmModel() { return this.farm; }
    public GameState getGameState() { return this.gameState; }
    public PlayerView getPlayerViewInstance() { return this.playerViewInstance; }

    public int getTileSize() { return gamePanel != null ? gamePanel.tileSize : 32; } // Default jika gp null
    public int getMaxWorldCol() { return gamePanel != null ? gamePanel.maxWorldCol : 32; }
    public int getMaxWorldRow() { return gamePanel != null ? gamePanel.maxWorldRow : 32; }
    public TileManager getTileManager() {
        return tileManager;
    }
    public GameStateUI getGameStateUI() {
     
        return gameStateUI;
    }
}