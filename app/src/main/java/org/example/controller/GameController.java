package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.model.Farm;
import org.example.model.Player;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.model.Sound;
import org.example.model.Items.Items;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;

public class GameController implements Runnable {

    // --- Komponen utama ---
    private final GamePanel gamePanel;
    final Farm farm;
    private final PlayerView playerView;
    private final TileManager tileManager;
    private final GameStateUI gameStateUI;

    // --- Helper/logic ---
    private final KeyHandler keyHandler;
    private final CollisionChecker collisionChecker;
    private final AssetSetter assetSetter;
    private final GameState gameState;
    private final Sound music;

    private Thread gameThread;
    private final Map<String, Boolean> movementState = new HashMap<>();

    public GameController(GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;
        this.gameState = new GameState();

        // --- Inisialisasi komponen view dan logic ---
        this.playerView = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.tileManager = new TileManager(gamePanel);
        this.gameStateUI = new GameStateUI(gamePanel);

        this.collisionChecker = new CollisionChecker(this);
        this.assetSetter = new AssetSetter(this);
        this.keyHandler = new KeyHandler(this);
        this.music = new Sound();

        // --- Integrasi KeyHandler ke panel ---
        if (this.gamePanel != null) {
            this.gamePanel.addKeyListener(this.keyHandler);
            this.gamePanel.setFocusable(true);
        }

        // --- State movement awal ---
        movementState.put("up", false);
        movementState.put("down", false);
        movementState.put("left", false);
        movementState.put("right", false);

        setupGame();
    }

    public void setupGame() {
        assetSetter.setInteractableObject();
        gameState.setGameState(gameState.play);
    }

    // --- Game Loop ---
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
                if (gamePanel != null) gamePanel.repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // --- Update logic utama ---
    public void update() {
        if (playerView == null || collisionChecker == null) return;
        if (gameState.getGameState() == gameState.play) {
            playerView.update(movementState, collisionChecker);
            // Tambahkan update NPC, waktu, dsb jika perlu
        }
    }

    // --- Input handler ---
    public void handlePlayerMove(String direction, boolean isMoving) {
        if (gameState.getGameState() == gameState.play) {
            movementState.put(direction, isMoving);
        }
    }

    public void handleInteraction() {
        if (gameState.getGameState() == gameState.play) {
            int objIndex = collisionChecker.checkObject(playerView);
            if (objIndex != 999) {
                InteractableObject[] currentObjects = farm.getObjectsForCurrentMap();
                if (currentObjects != null && objIndex < currentObjects.length && currentObjects[objIndex] != null) {
                    currentObjects[objIndex].interact(this);
                }
            } else {
                // Teleportasi jika tidak ada objek interaktif
                int tileSize = getTileSize();
                int playerCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width / 2) / tileSize;
                int playerRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height / 2) / tileSize;
                int currentMap = farm.getCurrentMap();
                if (playerCol >= 0 && playerCol < getMaxWorldCol() && playerRow >= 0 && playerRow < getMaxWorldRow()) {
                    // Contoh logika teleportasi
                    if (currentMap == 0 && playerCol == 31 && playerRow == 15) {
                        teleportPlayer(1, 1 * tileSize, 1 * tileSize);
                    } else if (currentMap == 1 && playerCol == 0 && playerRow == 0) {
                        teleportPlayer(0, 31 * tileSize, 15 * tileSize);
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

    // --- UI Navigation ---
    public void navigatePauseUI(String direction) {
        if (gameStateUI != null) {
            if (direction.equals("up")) {
                gameStateUI.commandNum--;
                if (gameStateUI.commandNum < 0) gameStateUI.commandNum = 1;
            } else if (direction.equals("down")) {
                gameStateUI.commandNum++;
                if (gameStateUI.commandNum > 1) gameStateUI.commandNum = 0;
            }
        }
    }

    public void confirmPauseUISelection() {
        if (gameStateUI != null) {
            if (gameStateUI.commandNum == 0) {
                gameState.setGameState(gameState.play);
            } else if (gameStateUI.commandNum == 1) {
                System.exit(0);
            }
        }
    }

    public void navigateInventoryUI(String direction) {
        if (gameStateUI != null) {
            int slotsPerRow = 12;
            int totalDisplayRows = 3;
            switch (direction) {
                case "up":
                    gameStateUI.slotRow--;
                    if (gameStateUI.slotRow < 0) gameStateUI.slotRow = totalDisplayRows - 1;
                    break;
                case "down":
                    gameStateUI.slotRow++;
                    if (gameStateUI.slotRow >= totalDisplayRows) gameStateUI.slotRow = 0;
                    break;
                case "left":
                    gameStateUI.slotCol--;
                    if (gameStateUI.slotCol < 0) gameStateUI.slotCol = slotsPerRow - 1;
                    break;
                case "right":
                    gameStateUI.slotCol++;
                    if (gameStateUI.slotCol >= slotsPerRow) gameStateUI.slotCol = 0;
                    break;
            }
        }
    }

    public void confirmInventoryUISelection() {
        if (farm != null && farm.getPlayerModel() != null && gameStateUI != null) {
            Player playerModel = farm.getPlayerModel();
            int slotIndex = gameStateUI.slotCol + (gameStateUI.slotRow * 12);
            ArrayList<Map.Entry<Items, Integer>> inventoryList =
                new ArrayList<>(playerModel.getInventory().getInventory().entrySet());

            if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
                Items selectedItem = inventoryList.get(slotIndex).getKey();
                playerModel.setCurrentHeldItem(selectedItem);
                System.out.println("Selected from inventory: " + selectedItem.getName());
            } else {
                playerModel.setCurrentHeldItem(null);
                System.out.println("Selected empty slot or out of bounds.");
            }
            gameState.setGameState(gameState.play);
        }
    }

    // --- Teleportasi player antar map ---
    public void teleportPlayer(int mapIndex, int worldX, int worldY) {
        if (farm != null && playerView != null && tileManager != null && assetSetter != null) {
            farm.setCurrentMap(mapIndex);
            playerView.worldX = worldX;
            playerView.worldY = worldY;
            playerView.direction = "down";
            tileManager.loadMap(farm.getMapPathFor(mapIndex), mapIndex);
            farm.clearObjects(mapIndex);
            if (mapIndex == 0) {
                assetSetter.setInteractableObject();
            }
            System.out.println("Player diteleportasi ke map " + mapIndex + " di (" + worldX/getTileSize() + "," + worldY/getTileSize() + ")");
        }
    }

    // --- Draw method untuk GamePanel ---
    public void draw(Graphics2D g2) {
        // Draw tile
        tileManager.draw(g2, playerView, farm.getCurrentMap());
        // Draw objects
        InteractableObject[] objects = farm.getObjectsForCurrentMap();
        if (objects != null) {
            for (InteractableObject obj : objects) {
                if (obj != null) obj.draw(g2, this);
            }
        }
        // Draw player
        playerView.draw(g2);

        // Draw UI
        if (gameStateUI != null) {
            gameStateUI.draw(g2, gameState, farm.getPlayerModel().getInventory());
        }
    }

    // --- Getters untuk komponen lain ---
    public KeyHandler getKeyHandler() { return keyHandler; }
    public GameState getGameState() { return gameState; }
    public Farm getFarmModel() { return farm; }
    public PlayerView getPlayerView() { return playerView; }
    public TileManager getTileManager() { return tileManager; }
    public GameStateUI getGameStateUI() { return gameStateUI; }
    public int getTileSize() { return gamePanel != null ? gamePanel.tileSize : 48; }
    public int getMaxWorldCol() { return gamePanel != null ? gamePanel.maxWorldCol : 32; }
    public int getMaxWorldRow() { return gamePanel != null ? gamePanel.maxWorldRow : 32; }
}