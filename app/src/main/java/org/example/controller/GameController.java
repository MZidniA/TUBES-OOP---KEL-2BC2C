package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.controller.action.CookingAction;
import org.example.controller.action.PlantingAction;
import org.example.controller.action.RecoverLandAction;
import org.example.controller.action.TillingAction;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Inventory;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Items.Seeds;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.Map.Tile;
import org.example.model.NPC.AbigailNPC;
import org.example.model.NPC.CarolineNPC;
import org.example.model.NPC.DascoNPC;
import org.example.model.NPC.EmilyNPC;
import org.example.model.NPC.MayorTadiNPC;
import org.example.model.NPC.PerryNPC;
import org.example.model.Player;
import org.example.model.Recipe;
import org.example.model.Sound;
import org.example.model.enums.LocationType;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.model.enums.SleepReason;
import org.example.view.FishingPanel;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.view.MenuPanel;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.MountainLakeObject;
import org.example.view.InteractableObject.OceanObject;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.InteractableObject.UnplantedTileObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;

public class GameController implements Runnable {

    private final GamePanel gamePanel;
    private final Farm farm;
    private final PlayerView playerViewInstance;
    private final TileManager tileManager; 
    private final GameStateUI gameStateUI; 
    private final JFrame mainFrame; 

    private final KeyHandler keyHandler;
    private final CollisionChecker cChecker;
    private final AssetSetter aSetter;
    private final GameState gameState;
    private final Sound music;
    private final TimeManager timeManager;

    private Thread gameThread;
    private final Map<String, Boolean> movementState = new HashMap<>();
    private final int TILLABLE_AREA_MAP0_MIN_COL = 15;
    private final int TILLABLE_AREA_MAP0_MAX_COL = 27;
    private final int TILLABLE_AREA_MAP0_MIN_ROW = 19;
    private final int TILLABLE_AREA_MAP0_MAX_ROW = 28;

    public GameController(JFrame frame,GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;
        this.mainFrame = frame; 

        this.gameState = new GameState();
        
        this.tileManager = gamePanel.tileM; 
        this.gameStateUI = gamePanel.gameStateUI; 

        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);
        this.keyHandler = new KeyHandler(this);
        this.music = new Sound();
        this.music.setFile();
        
        if (farm.getGameClock() != null && this.gameStateUI != null) {
            this.timeManager = new TimeManager(farm, farm.getGameClock());
            this.timeManager.addObserver(this.gameStateUI);
        } else {
            this.timeManager = null; 
            System.err.println("GameController Error: Farm, GameClock, atau GameStateUI null saat membuat TimeManager.");
        }

        if (this.gamePanel != null) {
            this.gamePanel.addKeyListener(this.keyHandler);
            this.gamePanel.setFocusable(true);
        }
        
        movementState.put("up", false);
        movementState.put("down", false);
        movementState.put("left", false);
        movementState.put("right", false);
        setupGame();
        farm.addNPC(new AbigailNPC());
        farm.addNPC(new CarolineNPC());
        farm.addNPC(new PerryNPC());
        farm.addNPC(new EmilyNPC());
        farm.addNPC(new DascoNPC());
        farm.addNPC(new MayorTadiNPC());
    }
    
    public GamePanel getGamePanel() { 
        return gamePanel; 
    }
    public GameStateUI getGameStateUIFromPanel() { 
        return gamePanel != null ? gamePanel.gameStateUI : null; }
    public CollisionChecker getCollisionChecker() { return this.cChecker; 
    }

    public TimeManager getTimeManager() { 
        return this.timeManager; 
    }
    


    public int getTillableAreaMinCol(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MIN_COL : -1; }
    public int getTillableAreaMaxCol(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MAX_COL : -1; }
    public int getTillableAreaMinRow(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MIN_ROW : -1; }
    public int getTillableAreaMaxRow(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MAX_ROW : -1; }


    private void setupGame() {
        if (aSetter != null) aSetter.setInteractableObject();
        gameState.setGameState(gameState.play);
        if (timeManager != null) timeManager.startTimeSystem();
        if (farm != null && farm.getGameClock() != null) {
            farm.setCurrentSeason(farm.getGameClock().getCurrentSeason());
            farm.setCurrentWeather(farm.getGameClock().getTodayWeather());
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic();
    }
    
    public Thread getGameThread() { return this.gameThread; }
    public void playMusic() { 
        if (music != null) { 
            music.play(); music.loop(); 
        } 
    }
    public void stopMusic() { 
        if (music != null) music.stop(); 
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                if (gamePanel != null) gamePanel.repaint();
                delta--;
            }
        }
    }

    private void update() {
        // Pengecekan null untuk komponen krusial di awal
        if (playerViewInstance == null || cChecker == null || gameState == null || farm == null) {
            // System.err.println("GameController.update() aborted: Critical component is null."); // Opsional: untuk debugging
            return;
        }
        
        Player playerModel = farm.getPlayerModel();
        if (playerModel == null) { // Pastikan playerModel juga tidak null
            // System.err.println("GameController.update() aborted: PlayerModel is null."); // Opsional: untuk debugging
            return;
        }

        // --- Update Progres Memasak ---
        // Ini akan memeriksa apakah ada masakan yang selesai dan otomatis menambahkannya ke inventory.
        // Dipanggil di setiap update loop agar proses memasak berjalan secara pasif.
        farm.updateCookingProgress(); // <-- PEMANGGILAN updateCookingProgress() DITAMBAHKAN DI SINI

        // Cek kondisi pingsan
        if (playerModel.isPassedOut()) {
            passedOutSleep();
            System.out.println("Kamu pingsan dan seseorang membawamu pulang..."); // Pesan ini bisa juga dihandle oleh UI
        } 
        // Cek kondisi tidur paksa karena waktu
        // Tambahkan 'else if' agar tidak terjadi dua kali passedOutSleep jika keduanya true di frame yang sama
        else if (playerModel.isForceSleepByTime()) { 
            passedOutSleep();
            System.out.println("Sudah jam 02:00, kamu kelelahan dan pingsan"); // Pesan ini bisa juga dihandle oleh UI
        }

        // Logika update spesifik untuk state PLAY
        if (gameState.getGameState() == gameState.play) {
            playerViewInstance.update(movementState, cChecker);
            // playerModel sudah dicek tidak null di atas
            // playerViewInstance juga sudah dicek tidak null di awal metode
            playerModel.setTilePosition(playerViewInstance.worldX / getTileSize(), playerViewInstance.worldY / getTileSize());
        }
        // Anda bisa menambahkan logika update untuk state lain jika diperlukan (misalnya, animasi UI di state PAUSE)
    }

    public void handlePlayerMove(String direction, boolean isMoving) {
        if (gameState.getGameState() == gameState.play) {
            movementState.put(direction, isMoving);
        }
    }

    public void handleInteraction() {
        if (gameState.getGameState() != gameState.play) return;
        if (playerViewInstance == null || cChecker == null || farm == null || gamePanel == null || farm.getPlayerModel() == null) return;

        Player playerModel = farm.getPlayerModel();
        Items heldItem = playerModel.getCurrentHeldItem();
        int tileSize = getTileSize();
        int currentMap = farm.getCurrentMap();

        int objIndex = cChecker.checkObject(playerViewInstance);
        if (objIndex != 999) {
            InteractableObject[] currentObjects = farm.getObjectsForCurrentMap();
            if (currentObjects != null && objIndex < currentObjects.length && currentObjects[objIndex] != null) {
                InteractableObject targetObject = currentObjects[objIndex];
                targetObject.interact(this);
                return;
            }
        }

        if (heldItem != null) {
            int targetCol = 0, targetRow = 0;
            if (playerViewInstance.solidArea == null) return;
            int currentSpeed = playerViewInstance.speed != 0 ? playerViewInstance.speed : 4;
            switch (playerViewInstance.direction) {
                case "up":
                    targetRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y - currentSpeed) / tileSize;
                    targetCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width / 2) / tileSize;
                    break;
                case "down":
                    targetRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height + currentSpeed) / tileSize;
                    targetCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width / 2) / tileSize;
                    break;
                case "left":
                    targetCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x - currentSpeed) / tileSize;
                    targetRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height / 2) / tileSize;
                    break;
                case "right":
                    targetCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width + currentSpeed) / tileSize;
                    targetRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height / 2) / tileSize;
                    break;
                default: return;
            }
            if (targetCol < 0 || targetCol >= getMaxWorldCol() || targetRow < 0 || targetRow >= getMaxWorldRow()) return;

            System.out.println("DEBUG: Target Tile for Interaction: (" + targetCol + ", " + targetRow + ")");

            if (heldItem.getName().equalsIgnoreCase("Hoe")) {
                TillingAction tilling = new TillingAction(this, targetCol, targetRow);
                if (tilling.canExecute(farm)) {
                    tilling.execute(farm);
                } else {
                    System.out.println("Tidak bisa mencangkul di sini");
                }
            } 
        } else {
            if (playerViewInstance.solidArea == null) return;
            int playerCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width / 2) / tileSize;
            int playerRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height / 2) / tileSize;
            if (currentMap == 0 && playerCol == 31 && playerRow == 15) { teleportPlayer(1, 1 * tileSize, 1 * tileSize); } 
            else if (currentMap == 1 && playerCol == 1 && playerRow == 1) { teleportPlayer(0, 31 * tileSize, 15 * tileSize); } 
            else if (currentMap == 1 && playerCol == 30 && playerRow == 1) { teleportPlayer(2, 29 * tileSize, 0 * tileSize); } 
            else if (currentMap == 2 && playerCol == 29 && playerRow == 0) { teleportPlayer(1, 30 * tileSize, 1 * tileSize); } 
            else if (currentMap == 2 && playerCol == 0 && playerRow == 31) { teleportPlayer(3, 15 * tileSize, 31* tileSize); } 
            else if (currentMap == 3 && playerCol == 15 && playerRow == 31) { teleportPlayer(2, 0 * tileSize, 31 * tileSize); } 
            else if (currentMap == 4 && playerCol == 3 && playerRow == 11) { teleportPlayer(0, 4 * tileSize, 9 * tileSize); }
        }
    }

    public void togglePause() {
        if (gameState.getGameState() == gameState.play) {
            gameState.setGameState(gameState.pause);
        }
        else if (gameState.getGameState() == gameState.pause) {
            gameState.setGameState(gameState.play);
        }
    }
    public void toggleInventory() {
        if (gameState.getGameState() == gameState.play) gameState.setGameState(gameState.inventory);
        else if (gameState.getGameState() == gameState.inventory) {
            gameState.setGameState(gameState.play);
            resetMovementState();
        }
    }
    public void navigatePauseUI(String direction) {
        if (gameState.getGameState() == gameState.pause && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            if (direction.equals("up")) { ui.commandNum--; if (ui.commandNum < 0) ui.commandNum = 1; } 
            else if (direction.equals("down")) { ui.commandNum++; if (ui.commandNum > 1) ui.commandNum = 0; }
        }
    }

    public void confirmPauseUISelection() {
        if (gameState.getGameState() == gameState.pause && gameStateUI != null) {
            if (gameStateUI.commandNum == 0) { // "Continue"
                gameState.setGameState(gameState.play);
                System.out.println("GameController: Resuming game. Game state set to PLAY.");
                // if (music != null && !music.isPlaying()) playMusic(); // Putar musik lagi jika dihentikan saat pause
            } else if (gameStateUI.commandNum == 1) { // "Exit Game"
                System.out.println("GameController: Exit Game selected from pause menu.");
                exitToMainMenu();
            }
        } else {
            System.err.println("GameController: confirmPauseUISelection called in invalid state or gameStateUI is null.");
        }
    }

    private void cleanUpForExit() {
        System.out.println("GameController: Cleaning up current game session...");
        if (timeManager != null) {
            timeManager.stopTimeSystem();
            System.out.println("GameController: Time system stopped.");
        }
        stopMusic(); // Hentikan musik game

        if (gameThread != null) {
            Thread threadToStop = gameThread;
            gameThread = null; 
            try {
                if (threadToStop.isAlive()) {
                    System.out.println("GameController: Waiting for game thread to stop...");
                    // threadToStop.interrupt(); // Gunakan jika loop run menangani InterruptedException
                    threadToStop.join(100); 
                    if (threadToStop.isAlive()) {
                        System.err.println("GameController: Game thread did not stop in time.");
                    } else {
                        System.out.println("GameController: Game thread stopped successfully.");
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("GameController: Interrupted while waiting for game thread to stop: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("GameController: Game thread was already null during cleanup.");
        }
        // Hapus KeyListener dari GamePanel lama agar tidak ada konflik jika GamePanel dibuat ulang
        if (gamePanel != null && keyHandler != null) {
            gamePanel.removeKeyListener(keyHandler);
        }
    }

    public void exitToMainMenu() {
        cleanUpForExit(); 

        if (mainFrame != null) {
            System.out.println("GameController: Returning to MenuPanel.");
            MenuPanel menuPanel = new MenuPanel(mainFrame); // Buat instance MenuPanel baru
            mainFrame.setContentPane(menuPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
            // Penting untuk memastikan panel baru mendapatkan fokus untuk input
            SwingUtilities.invokeLater(menuPanel::requestFocusInWindow);
        } else {
            System.err.println("GameController: mainFrame is null, cannot switch to MenuPanel.");
            // Sebagai fallback jika tidak ada frame, atau jika ini bukan cara yang diinginkan,
            // mungkin keluar dari aplikasi adalah pilihan terakhir.
            // System.exit(0);
        }
    }

    public void navigateInventoryUI(String direction) {
        if (gameState.getGameState() == gameState.inventory && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            int slotsPerRow = 12; int totalDisplayRows = 3;
            switch (direction) {
                case "up": ui.slotRow--; if (ui.slotRow < 0) ui.slotRow = totalDisplayRows - 1; break;
                case "down": ui.slotRow++; if (ui.slotRow >= totalDisplayRows) ui.slotRow = 0; break;
                case "left": ui.slotCol--; if (ui.slotCol < 0) ui.slotCol = slotsPerRow - 1; break;
                case "right": ui.slotCol++; if (ui.slotCol >= slotsPerRow) ui.slotCol = 0; break;
            }
        }
    }
    public void confirmInventoryUISelection() {
        if (gameState.getGameState() == gameState.inventory && farm != null && farm.getPlayerModel() != null && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            Player playerModel = farm.getPlayerModel();
            int slotIndex = ui.slotCol + (ui.slotRow * 12);
            ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(playerModel.getInventory().getInventory().entrySet());
            if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
                Items selectedItem = inventoryList.get(slotIndex).getKey();
                playerModel.setCurrentHeldItem(selectedItem);
            } else {
                playerModel.setCurrentHeldItem(null);
            }
            gameState.setGameState(gameState.play);
            resetMovementState();
        }
    }
    public void selectHotbarItem(int slotIndex) {
        if (farm == null || farm.getPlayerModel() == null) return;
        Player playerModel = farm.getPlayerModel();
        Inventory inventory = playerModel.getInventory();
        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());
        if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
            Items selectedItem = inventoryList.get(slotIndex).getKey();
            playerModel.setCurrentHeldItem(selectedItem);
        } else {
            playerModel.setCurrentHeldItem(null); 
        }
    }
    
    
    private void resetMovementState() {
        movementState.put("up", false); movementState.put("down", false);
        movementState.put("left", false); movementState.put("right", false);
    }
    public void teleportPlayer(int mapIndex, int worldX, int worldY) {
        int musicIdx = 0; 
        teleportPlayer(mapIndex, worldX, worldY, musicIdx);
    }
    public void teleportPlayer(int mapIndex, int worldX, int worldY, int musicIndex) {
        if (farm != null && playerViewInstance != null && gamePanel != null && tileManager != null && aSetter != null) {
            stopMusic();
            farm.setCurrentMap(mapIndex);
            Player player = farm.getPlayerModel();
            playerViewInstance.worldX = worldX; playerViewInstance.worldY = worldY; playerViewInstance.direction = "down";
            if (player != null) {
                switch (mapIndex) {
                    case 0: player.setCurrentLocationType(LocationType.FARM); break;
                    case 1: player.setCurrentLocationType(LocationType.OCEAN); break;
                    case 2: player.setCurrentLocationType(LocationType.FOREST_RIVER); break;
                    case 3: player.setCurrentLocationType(LocationType.TOWN); break;
                    case 4: player.setCurrentLocationType(LocationType.HOUSE); break;
                    case 5: player.setCurrentLocationType(LocationType.POND); break; 
                    default: player.setCurrentLocationType(LocationType.FARM); break; 
                }
            }
            tileManager.loadMap(farm.getMapPathFor(mapIndex), mapIndex);
            aSetter.setInteractableObject();
            playMusic();
        }
    }

    public void passedOutSleep() { 
        if (gameState.getGameState() == gameState.day_report) {
            return;
        }
    
    
        // 2. Hentikan TimeManager SEKALI di awal
        if (timeManager != null) {
            timeManager.stopTimeSystem();
        }
    

        gameState.setGameState(gameState.day_report);
        System.out.println("GameController: GameState set to day_report."); // DEBUG
    
        Player playerModel = farm.getPlayerModel();
        String reasonMessage;
    
        // Tentukan alasan dan atur energi
        if (playerModel.isForceSleepByTime()) { // Cek ini dulu jika bisa terjadi bersamaan dengan isPassedOut
            playerModel.setSleepReason(SleepReason.PASSED_OUT_TIME);
            reasonMessage = "You stayed up too late and collapsed!";
            playerModel.setEnergy(playerModel.getMaxEnergy() / 2);
            System.out.println("GameController: Reason is PASSED_OUT_TIME."); // DEBUG
        } else if (playerModel.isPassedOut()) { // Baru cek isPassedOut jika bukan karena waktu
            playerModel.setSleepReason(SleepReason.PASSED_OUT_ENERGY);
            reasonMessage = "Exhausted, you collapsed.\nSomeone brought you home.";
            playerModel.setEnergy(playerModel.getMaxEnergy() / 2);
            System.out.println("GameController: Reason is PASSED_OUT_ENERGY."); // DEBUG
        } else {
            // Ini seharusnya tidak terjadi jika dipanggil karena passed out atau force sleep
            // Tapi sebagai fallback, atau jika Anda juga menggunakan ini untuk tidur normal
            playerModel.setSleepReason(SleepReason.NORMAL); // Asumsi default jika tidak ada alasan spesifik
            reasonMessage = "The day has ended.";
            playerModel.setEnergy(playerModel.getMaxEnergy()); // Energi penuh untuk tidur normal
            System.out.println("GameController: Reason is fallback/NORMAL."); // DEBUG
        }
    
        // 4. Proses semua event akhir hari (tanaman, shipping bin) SEKALI
        processEndOfDayEvents();
    
        // 5. Siapkan info untuk ditampilkan di UI laporan akhir hari
        if (getGameStateUI() != null) {
            // Gunakan getGoldFromLastShipment() yang TIDAK meng-clear gold
            getGameStateUI().setEndOfDayInfo(reasonMessage, farm.getGoldFromLastShipment());
            System.out.println("GameController: End of day info set for UI."); // DEBUG
        }
        // Musik bisa dihentikan di sini jika perlu

    }
    

    public void activateSetTimeTo2AMCheat() {
        if (farm == null || farm.getGameClock() == null || timeManager == null) {
            System.out.println("CHEAT FAILED.");
            return;
        }
    
        GameClock gameClock = farm.getGameClock();
        gameClock.setCurrentTime(java.time.LocalTime.of(1, 45));
        
        this.timeManager.notifyObservers(); 
    
;
    }
    
    public Farm getFarmModel() { return this.farm; }
    public GameState getGameState() { return this.gameState; }
    public PlayerView getPlayerViewInstance() { return this.playerViewInstance; }
    public int getTileSize() { return gamePanel != null ? gamePanel.tileSize : 32; }
    public int getMaxWorldCol() { return gamePanel != null ? gamePanel.maxWorldCol : 32; }
    public int getMaxWorldRow() { return gamePanel != null ? gamePanel.maxWorldRow : 32; }
    public TileManager getTileManager() { return this.tileManager; }
    public GameStateUI getGameStateUI() { 
        return gamePanel != null ? gamePanel.gameStateUI : null; 
    }
    public Farm getFarm() { 
        return this.farm; 
    }

    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    public void openFishingPanel() {
        FishingPanel fishingPanel = new FishingPanel(farm, this);
        JFrame frame = getMainFrame();
        frame.setContentPane(fishingPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void returnToGamePanel() {
        if (this.mainFrame != null && this.gamePanel != null) {
            System.out.println("GameController: Kembali ke GamePanel menggunakan referensi mainFrame.");
            this.mainFrame.setContentPane(this.gamePanel);
            this.mainFrame.revalidate();
            this.mainFrame.repaint();
            this.gamePanel.requestFocusInWindow(); 

            if (gameState.getGameState() != gameState.play) {
                gameState.setGameState(gameState.play);
            }
        } else {
            if (this.mainFrame == null) {
                System.err.println("Error in returnToGamePanel: mainFrame adalah null di GameController.");
            }
            if (this.gamePanel == null) {
                System.err.println("Error in returnToGamePanel: gamePanel adalah null di GameController.");
            }
        }
    }

    public InteractableObject getNearestInteractableTile(Player player) {
        InteractableObject[] objects = farm.getObjectsForCurrentMap();
        int tileSize = getTileSize();

        int playerX = playerViewInstance.worldX / tileSize;
        int playerY = playerViewInstance.worldY / tileSize;

        for (InteractableObject obj : objects) {
            if (obj == null) continue;
            int objX = obj.getWorldX() / tileSize;
            int objY = obj.getWorldY() / tileSize;

            int dx = Math.abs(playerX - objX);
            int dy = Math.abs(playerY - objY);

            if ((dx + dy) == 1) {
                return obj;
            }
        }
        return null;
    }

    public boolean handleFishingIfNearby() {
        Player player = getFarmModel().getPlayerModel();
        InteractableObject obj = getNearestInteractableTile(player);
        if (obj instanceof PondObject || obj instanceof RiverObject ||
            obj instanceof MountainLakeObject || obj instanceof OceanObject) {
            if (player.getInventory().hasItem(ItemDatabase.getItem("Fishing Rod"), 1)) {
                openFishingPanel();
            } else {
                System.out.println("Butuh Fishing Rod untuk memancing.");
            }
            return true;
        }
        return false;
    }
    public void processEndOfDayEvents() {
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null || farm.getGameClock() == null) {
            System.err.println("GameController: FarmMap atau GameClock null, tidak bisa proses pertumbuhan tanaman.");
            return;
        }   
        Season newDaySeason = farm.getGameClock().getCurrentSeason();
        Weather newDayWeather = farm.getGameClock().getTodayWeather();
        
        for (int y = 0; y < farmMap.getSize(); y++) { 
            for (int x = 0; x < farmMap.getSize(); x++) {
                Tile currentTile = farmMap.getTile(x, y);
                if (currentTile instanceof Plantedland) {
                    Plantedland plant = (Plantedland) currentTile;
                    

                    plant.dailyGrow(newDaySeason, newDayWeather);

                   
                }
            }
        }

        int revenue = farm.processShippedItemsAndGetRevenue();
        farm.setGoldFromLastShipment(revenue);
    }

    // Metode baru untuk Shipping Bin
    public void closeShippingBinMenu() {
        if (gameState.getGameState() == gameState.shipping_bin) {
            gameState.setGameState(gameState.play);
            resetMovementState();
            // Jika ada state UI khusus shipping bin yang perlu direset, lakukan di sini
            // contoh: if (getGameStateUI() != null) getGameStateUI().resetShippingBinUIState();
        }
    }

    // In example/controller/GameController.java
    public void navigateShippingBinUI(String direction) {
        if (gameState.getGameState() == gameState.shipping_bin && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            final int slotsPerRow = 4; // << SESUAIKAN DENGAN invSlotsPerRow DI GameStateUI
            // final int totalDisplayRows = 4; // Ini lebih ke batas visual, navigasi berdasarkan itemCount

            int itemCount = 0;
            if (farm != null && farm.getPlayerModel() != null && farm.getPlayerModel().getInventory() != null) {
                itemCount = farm.getPlayerModel().getInventory().getInventory().size();
            }
            if (itemCount == 0) { // Tidak ada item di inventory, kursor tidak bergerak
                ui.slotCol = 0;
                ui.slotRow = 0;
                return;
            }

            int maxSlotIndex = itemCount - 1;
            int currentRow = ui.slotRow;
            int currentCol = ui.slotCol;

            switch (direction) {
                case "up":
                    if (currentRow > 0) ui.slotRow--;
                    else ui.slotRow = (maxSlotIndex / slotsPerRow); // Wrap ke baris terakhir
                    break;
                case "down":
                    if (currentRow < (maxSlotIndex / slotsPerRow)) ui.slotRow++;
                    else ui.slotRow = 0; // Wrap ke baris pertama
                    break;
                case "left":
                    if (currentCol > 0) ui.slotCol--;
                    else {
                        ui.slotCol = slotsPerRow - 1; // Pindah ke kolom terakhir
                        if (currentRow > 0) ui.slotRow--; // Pindah ke baris sebelumnya jika bukan baris pertama
                        else ui.slotRow = (maxSlotIndex / slotsPerRow); // Wrap ke baris terakhir kolom terakhir
                    }
                    break;
                case "right":
                    if (currentCol < slotsPerRow - 1) ui.slotCol++;
                    else {
                        ui.slotCol = 0; // Pindah ke kolom pertama
                        if (currentRow < (maxSlotIndex / slotsPerRow)) ui.slotRow++; // Pindah ke baris berikutnya jika bukan baris terakhir
                        else ui.slotRow = 0; // Wrap ke baris pertama kolom pertama
                    }
                    break;
            }
            
            // Pastikan kursor tetap dalam batas item yang ada
            int newLinearIndex = ui.slotRow * slotsPerRow + ui.slotCol;
            if (newLinearIndex > maxSlotIndex) {
                ui.slotCol = maxSlotIndex % slotsPerRow;
                ui.slotRow = maxSlotIndex / slotsPerRow;
            }
            System.out.println("Nav Shipping: New Slot (" + ui.slotCol + "," + ui.slotRow + ")"); // Log
        }
    }

    public void confirmShipItem() {
        if (gameState.getGameState() == gameState.shipping_bin &&
            farm != null && farm.getPlayerModel() != null &&
            gamePanel != null && gamePanel.gameStateUI != null) {
    
            GameStateUI ui = gamePanel.gameStateUI;
            Player playerModel = farm.getPlayerModel();
            Inventory inventory = playerModel.getInventory();
            ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());
    
            int selectedIndex = ui.slotCol + (ui.slotRow * 4);
    
            if (selectedIndex >= 0 && selectedIndex < inventoryList.size()) {
                Items selectedItem = inventoryList.get(selectedIndex).getKey();
                int quantityInPlayerInventory = inventoryList.get(selectedIndex).getValue();
    
                // Coba tambahkan ke model Farm (Farm akan mengurangi dari inventory pemain)
                // Metode addItemToShippingBin di Farm.java akan menangani logika penambahan ke bin
                // dan pengurangan dari inventory pemain.
                if (farm.addItemToShippingBin(selectedItem, 1)) { // Coba tambahkan 1 item
                    String message = "Added " + selectedItem.getName() + " to bin.";
    
                 
                    System.out.println(message);
    
                    // Penting: Tidak ada penambahan gold di sini. Gold ditambahkan saat tidur.
    
                    // Logika untuk menyesuaikan kursor jika item habis dari slot itu
                    if (playerModel.getInventory().getItemQuantity(selectedItem.getName()) == 0) {
                        if (ui.slotCol + (ui.slotRow * 4) >= playerModel.getInventory().getInventory().size() &&
                            !playerModel.getInventory().getInventory().isEmpty()) {
                            int lastValidIndex = playerModel.getInventory().getInventory().size() - 1;
                            ui.slotCol = lastValidIndex % 4;
                            ui.slotRow = lastValidIndex / 4;
                        } else if (playerModel.getInventory().getInventory().isEmpty()) {
                            ui.slotCol = 0;
                            ui.slotRow = 0;
                        }
                    }
                    // UI akan di-repaint oleh game loop, dan drawShippingBinScreen akan mengambil
                    // data terbaru dari farm.getItemsInShippingBin()
                } else {
                    // Pesan error jika gagal menambahkan (misalnya bin penuh item unik, atau item tidak shippable)
                    // Farm.addItemToShippingBin akan mencetak detail error ke console.
                    String reason = selectedItem.getName() + " cannot be added to bin.";
                    if (!selectedItem.isShippable()) {
                        reason = selectedItem.getName() + " is not shippable.";
                    } else if (farm.getUniqueItemCountInBin() >= Farm.getMaxUniqueItemsInBin() && !farm.getItemsInShippingBin().containsKey(selectedItem)) {
                        reason = "Bin is full for new unique items!";
                    } else if (quantityInPlayerInventory <= 0) {
                        reason = "No " + selectedItem.getName() + " left in inventory.";
                    }
                     // Menggunakan temporary message
                    System.out.println(reason);
                }
            } else {
                
                System.out.println("No item selected or empty slot.");
            }
            // Tidak perlu memanggil ui.setDialogue() secara eksplisit di sini jika sudah pakai showTemporaryMessage
            // gamePanel.repaint() akan dipanggil oleh game loop utama
        }
    }
    
    public void proceedToNextDayFromReport() {
        if (gameState.getGameState() != gameState.day_report) return; // Hanya jika dari state yang benar

        System.out.println("GameController: Proceeding to next day from report screen.");

        Player playerModel = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();
        PlayerView playerView = getPlayerViewInstance(); // Ambil dari getter
        int tileSize = getTileSize();

        // 1. Ganti hari secara formal di GameClock
        gameClock.nextDay(playerModel.getPlayerStats());

        int shippedGold = farm.getAndClearGoldFromLastShipment();
        System.out.println("GameController (proceedToNextDayFromReport): Gold from shipment to award: " + shippedGold); // DEBUG
        if (shippedGold > 0) {
            playerModel.addGold(shippedGold);
            System.out.println("GameController (proceedToNextDayFromReport): Player gold AFTER award: " + playerModel.getGold()); // DEBUG
        }

        // 3. Atur energi pemain berdasarkan alasan tidur sebelumnya
        //    (Ini sebenarnya lebih baik diatur sebelum masuk ke report screen,
        //     tapi kita terapkan sekarang jika belum)
        //    SleepingAction/PassedOutSleep akan mengatur energi sebelum memanggil report state
        //    Jika belum, contoh:
        //    if(playerModel.getCurrentSleepReason() == Player.SleepReason.PASSED_OUT_ENERGY ||
        //       playerModel.getCurrentSleepReason() == Player.SleepReason.PASSED_OUT_TIME) {
        //        playerModel.setEnergy(playerModel.getMaxEnergy() / 2);
        //    } else {
        //        playerModel.setEnergy(playerModel.getMaxEnergy());
        //    }


        // 4. Reset posisi pemain dan item di tangan
        if (farm.getCurrentMap() != 4) { // Asumsi map 4 adalah rumah
             teleportPlayer(4, 6 * tileSize, 10 * tileSize); // Sesuaikan spawn point rumah
        } else {
            if (playerView != null) {
                playerView.worldX = 6 * tileSize; // Sesuaikan
                playerView.worldY = 10 * tileSize; // Sesuaikan
                playerView.direction = "down";
            }
        }
        playerModel.setCurrentHeldItem(null);
        playerModel.setPassedOut(false);
        playerModel.setForceSleepByTime(false);
        playerModel.setSleepReason(SleepReason.NOT_SLEEPING); // Reset alasan tidur


        gameState.setGameState(gameState.play);
        if (timeManager != null) {
            timeManager.startTimeSystem();
        }
        
        System.out.println("GameController: New day started. Day: " + gameClock.getDay() +
                           ", Season: " + gameClock.getCurrentSeason() +
                           ", Weather: " + gameClock.getTodayWeather() +
                           ", Time: " + gameClock.getCurrentTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        
    }
    public void confirmCookingMenuSelection() {
        if (gameState.getGameState() != gameState.cooking_menu || gameStateUI == null || farm == null) {
            System.err.println("GameController: confirmCookingMenuSelection() - Invalid state or null components.");
            return;
        }

        // Jika command adalah "Cancel"
        if (gameStateUI.cookingMenuCommandNum == 1) {
            exitCookingMenu();
            return;
        }

        // Jika command adalah "Cook" (gameStateUI.cookingMenuCommandNum == 0)
        if (gameStateUI.availableRecipesForUI == null || gameStateUI.availableRecipesForUI.isEmpty()) {
            System.out.println("GameController: Attempted to cook with no available recipes.");
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("No recipes available to cook!");
            // Jangan keluar dari menu, biarkan pemain memilih "Cancel" atau tunggu resep tersedia
            return;
        }

        Recipe selectedRecipe = null;
        if (gameStateUI.selectedRecipeIndex >= 0 &&
            gameStateUI.selectedRecipeIndex < gameStateUI.availableRecipesForUI.size()) {
            selectedRecipe = gameStateUI.availableRecipesForUI.get(gameStateUI.selectedRecipeIndex);
        }

        if (selectedRecipe == null) {
            System.out.println("GameController: No recipe is currently selected to cook.");
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("Please select a recipe first!");
            // Jangan keluar dari menu, biarkan pemain memilih resep atau "Cancel"
            return;
        }

        // Lanjutkan dengan logika memasak jika resep sudah dipilih
        Items selectedFuel = null; // Implementasi jika ada pemilihan fuel
        CookingAction cookingAction = new CookingAction(selectedRecipe, selectedFuel); // Sesuaikan konstruktor

        if (cookingAction.canExecute(farm)) {
            cookingAction.execute(farm);
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("Memasak " + selectedRecipe.getDisplayName() + " dimulai!");
            System.out.println("GameController: Cooking " + selectedRecipe.getDisplayName());
        } else {
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("Tidak bisa memasak " + selectedRecipe.getDisplayName() + "."); // Pesan lebih spesifik
            System.out.println("GameController: Cannot cook " + selectedRecipe.getDisplayName() + ". Ingredients/Fuel might be missing or energy too low.");
        }

        // Setelah mencoba memasak (berhasil atau gagal karena canExecute), kembali ke state play atau tetap di menu.
        // Untuk sekarang, kita kembalikan ke play state.
        exitCookingMenu();
    }

    public void exitCookingMenu() {
        // Kembalikan state ke play
        if (gameState != null) {
            gameState.setGameState(gameState.play);
        }
        // Reset pilihan menu memasak jika perlu
        if (gameStateUI != null) {
            gameStateUI.cookingMenuCommandNum = 0;
            gameStateUI.selectedRecipeIndex = 0;

        }
        // Pastikan movement state direset agar player bisa bergerak lagi
        resetMovementState();
        // Fokuskan kembali ke game panel agar input keyboard aktif
        if (gamePanel != null) {
            gamePanel.requestFocusInWindow();
        }
    }

    public void navigateCookingMenu(String direction) {
        if (gameState.getGameState() != gameState.cooking_menu || gameStateUI == null) { // Gunakan gameState.cooking_menu
            System.err.println("GameController: navigateCookingMenu called in invalid state or gameStateUI is null.");
            return;
        }

        System.out.println("GameController: navigateCookingMenu - Direction: " + direction); // DEBUG

        if ("up_recipe".equalsIgnoreCase(direction)) {
            if (gameStateUI.availableRecipesForUI != null && !gameStateUI.availableRecipesForUI.isEmpty()) {
                gameStateUI.selectedRecipeIndex--;
                if (gameStateUI.selectedRecipeIndex < 0) {
                    gameStateUI.selectedRecipeIndex = gameStateUI.availableRecipesForUI.size() - 1;
                }
                // Saat mengganti resep, default-kan pilihan aksi ke "Cook" (commandNum = 0)
                // gameStateUI.cookingMenuCommandNum = 0; // Opsional, tergantung desain navigasi Anda
            }
        } else if ("down_recipe".equalsIgnoreCase(direction)) {
            if (gameStateUI.availableRecipesForUI != null && !gameStateUI.availableRecipesForUI.isEmpty()) {
                gameStateUI.selectedRecipeIndex++;
                if (gameStateUI.selectedRecipeIndex >= gameStateUI.availableRecipesForUI.size()) {
                    gameStateUI.selectedRecipeIndex = 0;
                }
                // gameStateUI.cookingMenuCommandNum = 0; // Opsional
            }
        } else if ("left_command".equalsIgnoreCase(direction)) {
            // Pindah dari Cancel (1) ke Cook (0)
            if (gameStateUI.cookingMenuCommandNum == 1) {
                gameStateUI.cookingMenuCommandNum = 0;
            }
            // Jika Anda punya lebih dari 2 tombol aksi horizontal, logikanya akan lebih kompleks
        } else if ("right_command".equalsIgnoreCase(direction)) {
            // Pindah dari Cook (0) ke Cancel (1)
            if (gameStateUI.cookingMenuCommandNum == 0) {
                gameStateUI.cookingMenuCommandNum = 1;
            }
        }
        // DEBUG: Cetak state setelah navigasi
        System.out.println("  -> RecipeIndex: " + gameStateUI.selectedRecipeIndex + ", CommandNum: " + gameStateUI.cookingMenuCommandNum);
    }    
  
}
