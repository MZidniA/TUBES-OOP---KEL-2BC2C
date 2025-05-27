package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.example.model.Farm;
import org.example.model.Player;
// import org.example.model.Player; // Tidak perlu di sini jika PlayerView mengambil dari Farm
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.model.Sound;
import org.example.model.Items.Items;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager; // Import TileManager jika belum ada
// import org.example.model.Sound; // Jika Anda ingin mengelola Sound dari sini

public class GameController implements Runnable {

    private final GamePanel gamePanel;            // Referensi ke View utama
    private final Farm farm;                      // Referensi ke Model utama
    private final PlayerView playerViewInstance;  // Controller sekarang memiliki PlayerView

    private Thread gameThread;
    private final KeyHandler keyHandler;
    private final CollisionChecker cChecker;
    private final AssetSetter aSetter;
    private final GameState gameState;
    private final Sound music;       // Instance GameState yang akan digunakan
    // private Sound music; // Opsional: kelola musik dari controller

    private final Map<String, Boolean> movementState = new HashMap<>();

    public GameController(GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;
        this.gameState = new GameState(); // Membuat instance GameState

        // 1. Buat PlayerView di sini, menggunakan Player model dari Farm dan GamePanel untuk konstanta
        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);

        // 2. Inisialisasi komponen helper
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);

        // 3. Setup KeyHandler
        this.keyHandler = new KeyHandler(this);
        this.music = new Sound();
        this.music.setFile(); // Inisialisasi musik
        if (this.gamePanel != null) {
            this.gamePanel.addKeyListener(this.keyHandler);
            this.gamePanel.setFocusable(true); // Pastikan GamePanel bisa fokus
        }


        movementState.put("up", false);
        movementState.put("down", false);
        movementState.put("left", false);
        movementState.put("right", false);

        setupGame();
        // setupMusic(); // Panggil setup musik
    }

    // Di dalam GameController.java
    public GameStateUI getGameStateUIFromPanel() { // Atau nama lain
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
        gameState.setGameState(this.gameState.play); // Gunakan konstanta dari instance gameState
    }

    // private void setupMusic() {
    //     music = new Sound();
    //     music.setFile(0); // Asumsi 0 adalah musik utama
    //     music.loop();
    //     music.play();
    // }    

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic(); // Panggil playMusic tanpa parameter
    }
    
    public void playMusic() { // Tidak ada parameter int i
        if (music != null) {
            // music.setFile(); // Tidak perlu dipanggil lagi jika sudah di konstruktor GameController
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
        double drawInterval = 1000000000.0 / 60.0; // Target 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0; // Untuk menghitung FPS
        int drawCount = 0; // Untuk menghitung FPS

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                if (gamePanel != null) {
                    gamePanel.repaint(); // Memberi tahu GamePanel untuk menggambar ulang
                }
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) { // Setiap 1 detik
                System.out.println("FPS: " + drawCount); // Cetak FPS
                drawCount = 0;
                timer = 0;
            }
        }
    }

    private void update() {
        // Hanya update game jika tidak null
        if (playerViewInstance == null || cChecker == null) return;

        if (gameState.getGameState() == this.gameState.play) {
             playerViewInstance.update(movementState, cChecker);
        }
        // Tambahkan logika update untuk NPC, TimeManager, dll. di sini jika perlu
    }

    public void handlePlayerMove(String direction, boolean isMoving) {
        if (gameState.getGameState() == this.gameState.play) {
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
                    int tileSize = getTileSize(); // Ambil tileSize dari controller
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
                            teleportPlayer(1, 31 * tileSize, 0* tileSize); 
                        } else if (currentMap == 2 && playerCol == 0 && playerRow == 31) { 
                            teleportPlayer(3, 15 * tileSize, 31* tileSize); 
                        } else if (currentMap == 3 && playerCol == 15 && playerRow == 31) { 
                            teleportPlayer(2, 0 * tileSize, 31 * tileSize);
                        } else if (currentMap == 4 && playerCol == 3 && playerRow == 11) {
                            teleportPlayer(0, 4 * tileSize, 9 * tileSize);
                        }
                    }
                }
            }
        }
    }

    public void togglePause() {
        if (gameState.getGameState() == this.gameState.play) {
            gameState.setGameState(this.gameState.pause);
        } else if (gameState.getGameState() == this.gameState.pause) {
            gameState.setGameState(this.gameState.play);
        }
    }

    public void toggleInventory() {
        if (gameState.getGameState() == this.gameState.play) {
            gameState.setGameState(this.gameState.inventory);
        } else if (gameState.getGameState() == this.gameState.inventory) {
            gameState.setGameState(this.gameState.play);
        }
    }

public void navigatePauseUI(String direction) {
    if (gamePanel != null && gamePanel.gameStateUI != null) {
        GameStateUI ui = gamePanel.gameStateUI;
        if (direction.equals("up")) {
            ui.commandNum--;
            if (ui.commandNum < 0) ui.commandNum = 1; // Asumsi 2 pilihan (0 dan 1)
        } else if (direction.equals("down")) {
            ui.commandNum++;
            if (ui.commandNum > 1) ui.commandNum = 0; // Asumsi 2 pilihan
        }
    }
}

public void confirmPauseUISelection() {
    if (gamePanel != null && gamePanel.gameStateUI != null) {
        GameStateUI ui = gamePanel.gameStateUI;
        if (ui.commandNum == 0) { // Continue
            gameState.setGameState(this.gameState.play);
            // playMusic(farm.getCurrentMapMusicIndex()); // Jika musik di-pause
        } else if (ui.commandNum == 1) { // Exit Game
            // Logika untuk kembali ke MenuPanel atau keluar game
            // Misalnya, panggil metode di Main atau JFrame
            System.out.println("Exit Game dipilih (implementasi kembali ke menu/quit diperlukan)");
            // Contoh: frame.exitToMenu(); // Jika ada metode seperti itu di Main/JFrame
            System.exit(0); // Cara paling sederhana untuk keluar
        }
    }
}

public void navigateInventoryUI(String direction) {
    if (gamePanel != null && gamePanel.gameStateUI != null) {
        GameStateUI ui = gamePanel.gameStateUI;
        int slotsPerRow = 12; // Harus konsisten dengan GameStateUI.drawInventoryScreen
        int totalDisplayRows = 3; // Harus konsisten

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
        
        int slotIndex = ui.slotCol + (ui.slotRow * 12); // 12 adalah slotsPerRow

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
        gameState.setGameState(this.gameState.play); // Kembali ke play state
    }
}

    public void teleportPlayer(int mapIndex, int worldX, int worldY) {
        if (farm != null && playerViewInstance != null && gamePanel != null && gamePanel.tileM != null && aSetter != null) {
            // music.stop(); // Hentikan musik map lama
            farm.setCurrentMap(mapIndex);
            playerViewInstance.worldX = worldX;
            playerViewInstance.worldY = worldY;
            playerViewInstance.direction = "down"; // Atur arah default setelah teleport

            gamePanel.tileM.loadMap(farm.getMapPathFor(mapIndex), mapIndex);

            farm.clearObjects(mapIndex); // Selalu bersihkan objek map target dulu
            if (mapIndex == 0) { // Kemudian isi objek jika itu peta utama
                aSetter.setInteractableObject(); // Seharusnya ini mengisi objek untuk mapIndex yang baru diset
            }
            // music.setFile(mapIndex); // Ganti musik sesuai map baru
            // music.play();
            // music.loop();
            System.out.println("Player diteleportasi ke map " + mapIndex + " di (" + worldX/getTileSize() + "," + worldY/getTileSize() + ")");
        }
    }

    // --- Getters untuk digunakan oleh komponen lain (terutama View) ---
    public Farm getFarmModel() { return this.farm; }
    public GameState getGameState() { return this.gameState; }
    public PlayerView getPlayerViewInstance() { return this.playerViewInstance; }

    public int getTileSize() { return gamePanel != null ? gamePanel.tileSize : 48; } // Default jika gp null
    public int getMaxWorldCol() { return gamePanel != null ? gamePanel.maxWorldCol : 32; }
    public int getMaxWorldRow() { return gamePanel != null ? gamePanel.maxWorldRow : 32; }
    public TileManager getTileManager() { return gamePanel != null ? gamePanel.tileM : null; }
}