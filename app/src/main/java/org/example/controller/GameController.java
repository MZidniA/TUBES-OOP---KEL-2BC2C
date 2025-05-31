package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.controller.action.Action;
import org.example.controller.action.CookingAction;
import org.example.controller.action.MarryingAction;
import org.example.controller.action.PlantingAction;
import org.example.controller.action.RecoverLandAction;
import org.example.controller.action.TillingAction;
import org.example.controller.CheatManager;
import org.example.controller.KeyHandler;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Inventory;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.Map.Tile;
import org.example.model.NPC.AbigailNPC;
import org.example.model.NPC.CarolineNPC;
import org.example.model.NPC.DascoNPC;
import org.example.model.NPC.EmilyNPC;
import org.example.model.NPC.MayorTadiNPC;
import org.example.model.NPC.NPC;
import org.example.model.NPC.PerryNPC;
import org.example.model.Player;
import org.example.model.PlayerStats;
import org.example.model.Recipe;
import org.example.model.Sound;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;
import org.example.model.enums.Season;
import org.example.model.enums.SleepReason;
import org.example.model.enums.Weather;
import org.example.view.FishingPanel;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.MountainLakeObject;
import org.example.view.InteractableObject.OceanObject;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.MenuPanel;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;

public class GameController implements Runnable {

    private final GamePanel gamePanel;
    private final Farm farm;
    private final Player playerModel;
    private final PlayerView playerViewInstance;
    private final TileManager tileManager; 
    private final GameStateUI gameStateUI; 
    private final JFrame mainFrame; 
    private final CheatManager cheatManager;   
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

    private static final int END_GAME_GOLD_MILESTONE = 17209;

    public GameController(JFrame frame,GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;
        this.mainFrame = frame; 

        this.gameState = new GameState();
        this.playerModel = farm.getPlayerModel();
        
        this.tileManager = gamePanel.tileM; 
        this.gameStateUI = gamePanel.gameStateUI; 

        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);
        
        this.music = new Sound();
        this.music.setFile();
        

        
        if (farm.getGameClock() != null && this.gameStateUI != null) {
            this.timeManager = new TimeManager(farm, farm.getGameClock());
            this.timeManager.addObserver(this.gameStateUI);
        } else {
            this.timeManager = null; 
        }
        this.cheatManager = new CheatManager(this);
        this.keyHandler = new KeyHandler(this);

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
    public CheatManager getCheatManager() {
        return this.cheatManager;
    }
    public KeyHandler getKeyHandler() { 
        return this.keyHandler; 
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
        // Pengecekan null untuk komponen krusial di awal (sudah ada di kode Anda)
        if (playerViewInstance == null || cChecker == null || gameState == null || farm == null) {
            return;
        }
        
        // Player playerModel = farm.getPlayerModel(); // playerModel sudah menjadi field kelas, tidak perlu dideklarasi ulang
        if (this.playerModel == null) { 
            return;
        }

        // --- Kontrol TimeManager berdasarkan GameState ---
        // Ini adalah penambahan/penyesuaian yang penting
        if (this.gameState.getGameState() == this.gameState.play) {
            if (this.timeManager != null && !this.timeManager.isRunning()) { // Anda perlu method isRunning() di TimeManager
                this.timeManager.startTimeSystem();
            }
        } else {
            // Jika bukan state play (misal: pause, inventory, menu, stats, day_report), pastikan waktu berhenti
            if (this.timeManager != null && this.timeManager.isRunning()) {
                this.timeManager.stopTimeSystem();
            }
        }

        // --- Update Progres Memasak --- (sudah ada di kode Anda)
        farm.updateCookingProgress(); 

        // --- Cek kondisi pingsan atau tidur paksa karena waktu ---
        // Hanya jika game dalam state 'play' dan belum dalam proses tidur/laporan
        if (gameState.getGameState() == gameState.play) { // Pengecekan state play ditambahkan di sini
            if (playerModel.isPassedOut()) { // Flag ini di-set oleh Player.java
                System.out.println("GameController (update): Player passed out due to energy."); // DEBUG
                playerModel.setSleepReason(SleepReason.PASSED_OUT_ENERGY); // Gunakan enum SleepReason
                initiateSleepSequence(); // Memulai urutan tidur/laporan hari
            } 
            else if (playerModel.isForceSleepByTime()) { // Flag ini di-set oleh TimeManager
                System.out.println("GameController (update): Player forced to sleep due to time."); // DEBUG
                playerModel.setSleepReason(SleepReason.PASSED_OUT_TIME); // Gunakan enum SleepReason
                initiateSleepSequence(); // Memulai urutan tidur/laporan hari
            }
        }

        // Logika update spesifik untuk state PLAY (pergerakan pemain, dll.) (sudah ada di kode Anda)
        if (gameState.getGameState() == gameState.play) {
            playerViewInstance.update(movementState, cChecker);
            playerModel.setTilePosition(playerViewInstance.worldX / getTileSize(), playerViewInstance.worldY / getTileSize());
        }
        // Anda bisa menambahkan logika update untuk state lain jika diperlukan
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
            if (currentMap == 0 && playerCol == 31 && playerRow == 15) { visitingAction(1, 1 * tileSize, 1 * tileSize); } 
            else if (currentMap == 1 && playerCol == 1 && playerRow == 1) { visitingAction(0, 31 * tileSize, 15 * tileSize); } 
            else if (currentMap == 1 && playerCol == 30 && playerRow == 1) { visitingAction(2, 29 * tileSize, 0 * tileSize); } 
            else if (currentMap == 2 && playerCol == 29 && playerRow == 0) { visitingAction(1, 30 * tileSize, 1 * tileSize); } 
            else if (currentMap == 2 && playerCol == 0 && playerRow == 31) { visitingAction(3, 15 * tileSize, 31* tileSize); } 
            else if (currentMap == 3 && playerCol == 15 && playerRow == 31) { visitingAction(2, 0 * tileSize, 31 * tileSize); } 
            else if (currentMap == 4 && playerCol == 3 && playerRow == 11) { visitingAction(0, 4 * tileSize, 9 * tileSize); }
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

            } else if (gameStateUI.commandNum == 1) { // "Exit Game"
                exitToMainMenu();
            }
        } else {
            return;
        }
    }

    private void cleanUpForExit() {
        if (timeManager != null) {
            timeManager.stopTimeSystem();
        }
        stopMusic(); 

        if (gameThread != null) {
            Thread threadToStop = gameThread;
            gameThread = null; 
            try {
                if (threadToStop.isAlive()) {
                    threadToStop.join(100); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            return;
        }
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
    public void visitingAction(int mapIndex, int worldX, int worldY) {
        int musicIdx = 0; 
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
                    case 5: player.setCurrentLocationType(LocationType.STORE); break;
                    case 6: player.setCurrentLocationType(LocationType.POND); break;  
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

        if (timeManager != null) {
            timeManager.stopTimeSystem();
        }
        gameState.setGameState(gameState.day_report);
    
        Player playerModel = farm.getPlayerModel();
        String reasonMessage;
    
        if (playerModel.isForceSleepByTime()) { 
            playerModel.setEnergy(playerModel.getMaxEnergy() / 2);
            reasonMessage = "Kamu Tidur Paksa Karena Waktu Sudah Larut.";
        } else if (playerModel.isPassedOut()) { 
            playerModel.setSleepReason(SleepReason.PASSED_OUT_ENERGY);
            reasonMessage = "Kamu Pingsan Karena Energi Terlalu Rendah.";
            playerModel.setEnergy(10);
        } else {
            playerModel.setSleepReason(SleepReason.NORMAL);
            reasonMessage = "Kamu Tertidur Dengan Nyenyak.";
        }

        processEndOfDayEvents();
        if (getGameStateUI() != null) {
            getGameStateUI().setEndOfDayInfo(reasonMessage, farm.getGoldFromLastShipment());
        }
    }

    public void activateSetTimeTo2AMCheat() {
        if (farm == null || farm.getGameClock() == null || timeManager == null) {
            return;
        }
    
        GameClock gameClock = farm.getGameClock();
        gameClock.setCurrentTime(java.time.LocalTime.of(1, 50));
        
        this.timeManager.notifyObservers(); 
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
            return;
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
            }
            return true;
        }
        return false;
    }
    public void processEndOfDayEvents() {
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null || farm.getGameClock() == null) {
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

    public void closeShippingBinMenu() {
        if (gameState.getGameState() == gameState.shipping_bin) {
            gameState.setGameState(gameState.play);
            resetMovementState();
        }
    }

    public void navigateShippingBinUI(String direction) {
        if (gameState.getGameState() == gameState.shipping_bin && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            final int slotsPerRow = 4;

            int itemCount = 0;
            if (farm != null && farm.getPlayerModel() != null && farm.getPlayerModel().getInventory() != null) {
                itemCount = farm.getPlayerModel().getInventory().getInventory().size();
            }
            if (itemCount == 0) { 
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
                    else ui.slotRow = (maxSlotIndex / slotsPerRow); 
                    break;
                case "down":
                    if (currentRow < (maxSlotIndex / slotsPerRow)) ui.slotRow++;
                    else ui.slotRow = 0; 
                    break;
                case "left":
                    if (currentCol > 0) ui.slotCol--;
                    else {
                        ui.slotCol = slotsPerRow - 1; 
                        if (currentRow > 0) ui.slotRow--; 
                        else ui.slotRow = (maxSlotIndex / slotsPerRow); 
                    }
                    break;
                case "right":
                    if (currentCol < slotsPerRow - 1) ui.slotCol++;
                    else {
                        ui.slotCol = 0; 
                        if (currentRow < (maxSlotIndex / slotsPerRow)) ui.slotRow++; 
                        else ui.slotRow = 0; 
                    }
                    break;
            }
            
            int newLinearIndex = ui.slotRow * slotsPerRow + ui.slotCol;
            if (newLinearIndex > maxSlotIndex) {
                ui.slotCol = maxSlotIndex % slotsPerRow;
                ui.slotRow = maxSlotIndex / slotsPerRow;
            }
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
  
                if (farm.addItemToShippingBin(selectedItem, 1)) { 
                    String message = "Item Ditambahkan " + selectedItem.getName() + " Ke Shpping Bin .";
    
                 
                    System.out.println(message);
    
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
                } else {
                    String reason = selectedItem.getName() + " tidak bisa ditambahkan ke Shipping Bin.";
                    if (!selectedItem.isShippable()) {
                        reason = selectedItem.getName() + " TIdak bisa dijual";
                    } else if (farm.getUniqueItemCountInBin() >= Farm.getMaxUniqueItemsInBin() && !farm.getItemsInShippingBin().containsKey(selectedItem)) {
                        reason = "full";
                    } else if (quantityInPlayerInventory <= 0) {
                        reason = "Tidak ada " + selectedItem.getName() + " tersisa di inventory";
                    }
                    System.out.println(reason);
                }
            } else {
                return;
            }
        }
    }
    
    public void proceedToNextDayFromReport() {
        if (gameState.getGameState() != gameState.day_report) return; 

        Player playerModel = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();
        PlayerView playerView = getPlayerViewInstance(); 
        int tileSize = getTileSize();

        gameClock.nextDay(playerModel.getPlayerStats());

        int shippedGold = farm.getAndClearGoldFromLastShipment();
        if (shippedGold > 0) {
            playerModel.addGold(shippedGold);
        }

        if (farm.getCurrentMap() != 4) {
             visitingAction(4, 6 * tileSize, 10 * tileSize); 
        } else {
            if (playerView != null) {
                playerView.worldX = 6 * tileSize;
                playerView.worldY = 10 * tileSize; 
                playerView.direction = "down";
            }
        }

        playerModel.setCurrentHeldItem(null);
        playerModel.setPassedOut(false);
        playerModel.setForceSleepByTime(false);
        playerModel.setSleepReason(SleepReason.NOT_SLEEPING); 

        gameState.setGameState(gameState.play);
        if (timeManager != null) {
            timeManager.startTimeSystem();
        }
        
    }

    public void confirmCookingMenuSelection() {
        if (gameState.getGameState() != gameState.cooking_menu || gameStateUI == null || farm == null) {
            System.err.println("GameController: confirmCookingMenuSelection() - Invalid state or null components.");
            return;
        }

        if (gameStateUI.isSelectingFuel()) { // Jika sedang dalam mode memilih fuel
            if (gameStateUI.fuelSelectionCommandNum == 1) { // Tombol "Back to Recipes"
                gameStateUI.setSelectingFuelMode(false);
                // Tidak keluar menu, hanya kembali ke tampilan pemilihan resep
                if(gamePanel != null) gamePanel.repaint();
                return;
            }
            // Jika fuelSelectionCommandNum == 0 (Pilih fuel ini)
            if (gameStateUI.availableFuelsForUI == null || gameStateUI.availableFuelsForUI.isEmpty()) {
                gameStateUI.showTemporaryMessage("No fuel available to select!");
                return;
            }
            if (gameStateUI.selectedFuelIndex < 0 || gameStateUI.selectedFuelIndex >= gameStateUI.availableFuelsForUI.size()) {
                gameStateUI.showTemporaryMessage("Invalid fuel selection!");
                return;
            }
            Items actualSelectedFuel = gameStateUI.availableFuelsForUI.get(gameStateUI.selectedFuelIndex);
            
            // Dapatkan resep yang sudah dipilih sebelumnya
            Recipe previouslySelectedRecipe = null;
            if (gameStateUI.availableRecipesForUI != null && 
                gameStateUI.selectedRecipeIndex >= 0 && 
                gameStateUI.selectedRecipeIndex < gameStateUI.availableRecipesForUI.size()) {
                previouslySelectedRecipe = gameStateUI.availableRecipesForUI.get(gameStateUI.selectedRecipeIndex);
            }

            if (previouslySelectedRecipe == null) {
                gameStateUI.showTemporaryMessage("Error: Recipe not found after fuel selection.");
                gameStateUI.setSelectingFuelMode(false); // Kembali ke pemilihan resep
                return;
            }

            // Buat CookingAction dengan resep dan fuel yang sudah dipilih
            CookingAction cookingAction = new CookingAction(previouslySelectedRecipe, actualSelectedFuel);
            if (cookingAction.canExecute(farm)) {
                cookingAction.execute(farm);
                gameStateUI.showTemporaryMessage("Memasak " + previouslySelectedRecipe.getDisplayName() + " dimulai!");
                System.out.println("GameController: Cooking " + previouslySelectedRecipe.getDisplayName() + " with " + actualSelectedFuel.getName());
            } else {
                gameStateUI.showTemporaryMessage("Tidak bisa memasak " + previouslySelectedRecipe.getDisplayName() + ".");
                System.out.println("GameController: Cannot cook " + previouslySelectedRecipe.getDisplayName() + " with " + actualSelectedFuel.getName());
            }
            exitCookingMenu(); // Keluar menu setelah mencoba memasak

        } else { // Jika sedang dalam mode memilih resep
            if (gameStateUI.cookingMenuCommandNum == 1) { // Tombol "Cancel" resep
                exitCookingMenu();
                return;
            }
            // Jika cookingMenuCommandNum == 0 (Tombol "Select Fuel" atau "Cook" jika resep tidak butuh fuel spesifik)
            if (gameStateUI.availableRecipesForUI == null || gameStateUI.availableRecipesForUI.isEmpty()) {
                gameStateUI.showTemporaryMessage("No recipes available!");
                return;
            }
            if (gameStateUI.selectedRecipeIndex < 0 || gameStateUI.selectedRecipeIndex >= gameStateUI.availableRecipesForUI.size()) {
                gameStateUI.showTemporaryMessage("Please select a recipe!");
                return;
            }
            
            // Transisi ke mode pemilihan fuel
            gameStateUI.setSelectingFuelMode(true);
            System.out.println("GameController: Switched to fuel selection mode.");
            if(gamePanel != null) gamePanel.repaint();
            // Tidak keluar menu, UI akan di-update untuk menampilkan pilihan fuel
        }
    }
    public void exitCookingMenu() {
        if (gameState != null) {
            gameState.setGameState(gameState.play);
        }
        if (gameStateUI != null) {
            gameStateUI.resetCookingMenuState(); // Panggil reset state UI
        }
        resetMovementState();
        if (gamePanel != null) {
            gamePanel.requestFocusInWindow();
        }
    }

    public void navigateCookingMenu(String direction) {
        if (gameState.getGameState() != gameState.cooking_menu || gameStateUI == null) {
            return;
        }

        if (gameStateUI.isSelectingFuel()) { // Navigasi saat memilih fuel
            if ("up_fuel".equalsIgnoreCase(direction) || "up_recipe".equalsIgnoreCase(direction)) { // "up_recipe" untuk kompatibilitas jika hanya ada fuel
                if (gameStateUI.availableFuelsForUI != null && !gameStateUI.availableFuelsForUI.isEmpty()) {
                    gameStateUI.selectedFuelIndex--;
                    if (gameStateUI.selectedFuelIndex < 0) {
                        gameStateUI.selectedFuelIndex = gameStateUI.availableFuelsForUI.size() - 1;
                    }
                }
            } else if ("down_fuel".equalsIgnoreCase(direction) || "down_recipe".equalsIgnoreCase(direction)) {
                if (gameStateUI.availableFuelsForUI != null && !gameStateUI.availableFuelsForUI.isEmpty()) {
                    gameStateUI.selectedFuelIndex++;
                    if (gameStateUI.selectedFuelIndex >= gameStateUI.availableFuelsForUI.size()) {
                        gameStateUI.selectedFuelIndex = 0;
                    }
                }
            } else if ("left_command".equalsIgnoreCase(direction)) {
                if (gameStateUI.fuelSelectionCommandNum == 1) gameStateUI.fuelSelectionCommandNum = 0; // Dari Back ke Cook with this
            } else if ("right_command".equalsIgnoreCase(direction)) {
                if (gameStateUI.fuelSelectionCommandNum == 0) gameStateUI.fuelSelectionCommandNum = 1; // Dari Cook with this ke Back
            }
            System.out.println("  -> FuelIndex: " + gameStateUI.selectedFuelIndex + ", FuelCommand: " + gameStateUI.fuelSelectionCommandNum);

        } else { // Navigasi saat memilih resep (logika Anda sebelumnya)
            if ("up_recipe".equalsIgnoreCase(direction)) {
                if (gameStateUI.availableRecipesForUI != null && !gameStateUI.availableRecipesForUI.isEmpty()) {
                    gameStateUI.selectedRecipeIndex--;
                    if (gameStateUI.selectedRecipeIndex < 0) {
                        gameStateUI.selectedRecipeIndex = gameStateUI.availableRecipesForUI.size() - 1;
                    }
                }
            } else if ("down_recipe".equalsIgnoreCase(direction)) {
                if (gameStateUI.availableRecipesForUI != null && !gameStateUI.availableRecipesForUI.isEmpty()) {
                    gameStateUI.selectedRecipeIndex++;
                    if (gameStateUI.selectedRecipeIndex >= gameStateUI.availableRecipesForUI.size()) {
                        gameStateUI.selectedRecipeIndex = 0;
                    }
                }
            } else if ("left_command".equalsIgnoreCase(direction)) {
                if (gameStateUI.cookingMenuCommandNum == 1) gameStateUI.cookingMenuCommandNum = 0;
            } else if ("right_command".equalsIgnoreCase(direction)) {
                if (gameStateUI.cookingMenuCommandNum == 0) gameStateUI.cookingMenuCommandNum = 1;
            }
            System.out.println("  -> RecipeIndex: " + gameStateUI.selectedRecipeIndex + ", RecipeCommand: " + gameStateUI.cookingMenuCommandNum);
        }
        if (gamePanel != null) gamePanel.repaint(); // Selalu repaint setelah navigasi
    }

    public void checkForEndGameStatsTrigger() {
        if (playerModel == null || playerModel.getPlayerStats() == null || gameState == null) {
            System.err.println("GameController ERROR: Cannot check stats trigger. playerModel, PlayerStats, or GameState is null.");
            return;
        }

        PlayerStats stats = playerModel.getPlayerStats();

        if (stats.hasShownEndGameStats()) {
            return; // Statistik sudah pernah ditampilkan, jangan tampilkan lagi
        }

        boolean playerIsMarried = playerModel.getPartner() != null &&
                                 playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.SPOUSE;
        boolean goldMilestoneReached = playerModel.getGold() >= END_GAME_GOLD_MILESTONE;

        if (playerIsMarried || goldMilestoneReached) {
            String triggerReason = playerIsMarried ? "player is married" : ("gold milestone (" + playerModel.getGold() + "g) reached");
            System.out.println("GameController LOG: End game milestone triggered because " + triggerReason);
            
            stats.setHasShownEndGameStats(true);

            if (this.timeManager != null) {
                this.timeManager.stopTimeSystem();
            } else {
                System.out.println("GameController WARNING: TimeManager is null, cannot stop time system for stats.");
            }
            this.gameState.setGameState(this.gameState.end_game_stats); // Gunakan konstanta dari instance gameState
            System.out.println("GameController LOG: GameState changed to END_GAME_STATS. Time system (if available) stopped.");
            
            if (this.gamePanel != null) {
                this.gamePanel.repaint(); // Minta repaint untuk update UI
            }
        }
    }

    /**
     * Dipanggil ketika pemain menutup layar statistik (misalnya, oleh KeyHandler setelah Enter ditekan).
     */
    public void dismissEndGameStatisticsScreen() {
        if (this.gameState != null && this.gameState.getGameState() == this.gameState.end_game_stats) {
            this.gameState.setGameState(this.gameState.play); // Kembali ke state bermain normal
            if (this.timeManager != null) {
                this.timeManager.startTimeSystem();
            } else {
                System.out.println("GameController WARNING: TimeManager is null, cannot resume time system.");
            }
            System.out.println("GameController LOG: End game statistics dismissed. GameState changed to PLAY. Time system (if available) resumed.");
            
            if (this.gamePanel != null) {
                this.gamePanel.repaint();
            }
        }
    }

    /**
     * Memproses logika akhir hari, termasuk pendapatan dan pengecekan statistik.
     * Dipanggil setelah laporan harian ditutup.
     */
    public void processEndOfDayAndCheckStats() {
        System.out.println("GameController LOG: Processing end of day procedures and checking stats...");

        // 1. Proses pendapatan dari shipping bin
        int goldFromShipment = this.farm.getAndClearGoldFromLastShipment();
        if (goldFromShipment > 0) {
            this.playerModel.addGold(goldFromShipment);
            if (this.playerModel.getPlayerStats() != null) {
                this.playerModel.getPlayerStats().recordIncome(goldFromShipment);
            }
            System.out.println("GameController LOG: Player received " + goldFromShipment + "g from shipment.");
        }
        // Info untuk UI sudah di-set sebelumnya saat transisi ke DAY_REPORT

        // 2. Cek pemicu statistik End Game
        checkForEndGameStatsTrigger();
        System.out.println("GameController LOG: End of day and stats check procedures complete.");
    }

    /**
     * Dipanggil oleh SleepAction atau ketika TimeManager memaksa tidur.
     * Metode ini akan mengatur transisi ke layar laporan harian.
     */
    public void initiateSleepSequence() {
        System.out.println("GameController: Player is initiating sleep sequence.");

        // 1. Majukan hari (GameClock.nextDay akan mengupdate PlayerStats.totalDaysPlayed)
        if (this.farm.getGameClock() != null && this.playerModel.getPlayerStats() != null) {
            this.farm.getGameClock().nextDay(this.playerModel.getPlayerStats());
        } else {
             System.err.println("GameController ERROR: GameClock or PlayerStats is null during sleep sequence.");
        }
        
        int goldEarnedForReport = this.farm.getGoldFromLastShipment(); // Ambil nilai sebelum di-clear
                                                                       // Jika ingin menampilkan apa yg akan diterima besok, maka ini adalah
                                                                       // `farm.calculatePotentialRevenueFromBin()` -- tapi ini belum dijual.
                                                                       // Untuk yang *sudah* dijual dan diterima,
                                                                       // `setEndOfDayInfo` perlu dipanggil *sebelum* `getAndClearGoldFromLastShipment`
                                                                       // di `processEndOfDayAndCheckStats`.

        // Atau, cara lebih mudah: `setEndOfDayInfo` dipanggil DARI `processEndOfDayAndCheckStats`
        // Tepatnya, sebelum `getAndClearGoldFromLastShipment`.
        // Untuk alur Anda:
        // Sleep -> nextDay() -> set DAY_REPORT state -> (UI shows report)
        // -> Player dismisses report -> processEndOfDayAndCheckStats() (yg ini akan `recordIncome` dan `checkForEndGameStatsTrigger`)
        // -> set PLAY state
        // Jadi, `setEndOfDayInfo` di `GameStateUI` perlu data pendapatan yang baru diterima.

        String sleepMessage = "Kamu tidur nyenyak.";
        if(playerModel.isPassedOut()){
            sleepMessage = "Kamu pingsan dan terbangun di rumah.";
            playerModel.setPassedOut(false);
        } else if (playerModel.isForceSleepByTime()){
            sleepMessage = "Kamu kelelahan dan tertidur pulas hingga pagi.";
            playerModel.setForceSleepByTime(false);
        }

        if (this.gameStateUI != null) {
            // goldFromShipment akan diproses dan DITAMBAHKAN ke player di processEndOfDayAndCheckStats
            // jadi untuk laporan, kita tampilkan apa yang BARU SAJA akan diterima.
            this.gameStateUI.setEndOfDayInfo(sleepMessage, this.farm.getGoldFromLastShipment()); // Menampilkan gold yang akan diterima (dari bin hari ini)
        }


        // 3. Ubah game state ke laporan harian
        if (this.timeManager != null) {
            this.timeManager.stopTimeSystem(); // Hentikan waktu selama laporan
        }
        this.gameState.setGameState(this.gameState.day_report); // Gunakan konstanta dari instance gameState
        System.out.println("GameController LOG: GameState changed to DAY_REPORT.");

        if (this.gamePanel != null) this.gamePanel.repaint();
    }

    /**
     * Dipanggil saat pemain menutup (dismiss) layar laporan akhir hari (misalnya, oleh KeyHandler).
     */
    public void handleEndOfDayReportDismissal() {
        System.out.println("GameController: End of day report dismissed by player.");
        
        // Proses pendapatan hari ini dan cek statistik end game
        processEndOfDayAndCheckStats(); // Ini akan memanggil recordIncome dan checkForEndGameStatsTrigger

        // Setelah semua proses selesai, kembalikan ke state bermain normal,
        // KECUALI jika checkForEndGameStatsTrigger mengubah state ke END_GAME_STATS.
        if (this.gameState.getGameState() != this.gameState.end_game_stats) {
            this.gameState.setGameState(this.gameState.play);
            if (this.timeManager != null) {
                this.timeManager.startTimeSystem(); // Mulai lagi waktu permainan
            }
            System.out.println("GameController LOG: Returned to PLAY state. Time system (if available) resumed.");
        }
        
        if (this.gamePanel != null) this.gamePanel.repaint();
    }


    /**
     * Dipanggil oleh MarryingAction setelah pernikahan berhasil.
     */
    public void handleSuccessfulMarriage(Player player, NPC spouse) {
        // Status pemain dan NPC diasumsikan sudah diupdate oleh MarryingAction
        System.out.println("GameController LOG: Handling successful marriage for " + player.getName() + " and " + spouse.getName());
        checkForEndGameStatsTrigger();
        if (this.gamePanel != null) this.gamePanel.repaint();
    }

    /**
     * Metode untuk menjalankan aksi pemain.
     * Kelas Aksi akan memanggil metode di PlayerStats secara langsung melalui objek Farm.
     */
    public void executePlayerAction(Action action) {
        if (action == null) {
            System.err.println("GameController ERROR: Attempted to execute a null action.");
            return;
        }

        if (action.canExecute(this.farm)) {
            action.execute(this.farm); // Metode execute di Action akan memanggil PlayerStats jika perlu

            // Penanganan khusus setelah aksi tertentu
            if (action instanceof MarryingAction) {
                if (playerModel.getPartner() != null && 
                    playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.SPOUSE) {
                    handleSuccessfulMarriage(playerModel, playerModel.getPartner());
                }
            }
            // Untuk aksi lain, checkForEndGameStatsTrigger() umumnya dipanggil di akhir hari.
        } else {
            String cantExecuteMsg = "Cannot do: " + action.getActionName();
            System.out.println("GameController: " + cantExecuteMsg);
            if (this.gameStateUI != null) {
                this.gameStateUI.showTemporaryMessage(cantExecuteMsg);
            }
        }
        if (this.gamePanel != null) this.gamePanel.repaint(); // Update UI setelah setiap aksi
    }

    /**
     * Metode update utama game, dipanggil dari game loop di GamePanel.
     */
    
    public Player getPlayerModel() { return this.playerModel; }
    public AssetSetter getAssetSetter() { return this.aSetter; } // Getter untuk AssetSetter

    // Metode untuk musik jika ada
    public void playMusic(int i) {
        // music.setFile(i); // Anda perlu path atau index yang benar
        // music.play();
        // music.loop();
    }

    public void playSE(int i) { // Sound Effect
        // music.setFile(i); // Anda perlu path atau index yang benar
        // music.play();
    }

    /**
     * Teleport player to a specific map and tile position.
     * @param mapIndex Target map index.
     * @param tileX Target tile X (column).
     * @param tileY Target tile Y (row).
     */
    public void teleportPlayer(int mapIndex, int tileX, int tileY) {
        if (farm == null || playerViewInstance == null || tileManager == null || aSetter == null) {
            System.out.println("Teleport failed: missing required components.");
            return;
        }
        int tileSize = getTileSize();
        stopMusic();
        farm.setCurrentMap(mapIndex);
        Player player = farm.getPlayerModel();
        playerViewInstance.worldX = tileX * tileSize;
        playerViewInstance.worldY = tileY * tileSize;
        playerViewInstance.direction = "down";
        if (player != null) {
            switch (mapIndex) {
                case 0: player.setCurrentLocationType(LocationType.FARM); break;
                case 1: player.setCurrentLocationType(LocationType.OCEAN); break;
                case 2: player.setCurrentLocationType(LocationType.FOREST_RIVER); break;
                case 3: player.setCurrentLocationType(LocationType.TOWN); break;
                case 4: player.setCurrentLocationType(LocationType.HOUSE); break;
                case 5: player.setCurrentLocationType(LocationType.STORE); break;
                case 6: player.setCurrentLocationType(LocationType.POND); break;
                default: player.setCurrentLocationType(LocationType.FARM); break;
            }
        }
        tileManager.loadMap(farm.getMapPathFor(mapIndex), mapIndex);
        aSetter.setInteractableObject();
        playMusic();
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }


    // public void cheat_setGoldToWinningAmount() {
    //     if (farm != null && farm.getPlayerModel() != null) {
    //         farm.getPlayerModel().setGold(17209); // Anda perlu metode setGold di Player.java
    //         System.out.println("CHEAT ACTIVATED: Player gold set to 17209g.");
    //         if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: Gold set to 17209g!");
    //         if (gamePanel != null) gamePanel.repaint(); // Update UI jika menampilkan gold
    //     } else {
    //         System.out.println("CHEAT FAILED: Player model not available.");
    //     }
    // }

    // /**
    //  * CHEAT: Menikahkan pemain dengan NPC tertentu.
    //  * @param npcName Nama NPC yang akan dinikahi.
    //  */
    // public void cheat_marryNpc(String npcName) {
    //     if (farm != null && farm.getPlayerModel() != null) {
    //         Player playerModel = farm.getPlayerModel();
    //         NPC targetNpc = farm.getNPCByName(npcName); 

    //         if (targetNpc != null) {
    //             playerModel.setPartner(targetNpc, RelationshipStatus.SPOUSE); 
    //             targetNpc.setRelationshipStatus(RelationshipStatus.SPOUSE); 
                
    //             if (playerModel.getPlayerStats() != null) {
    //                 playerModel.getPlayerStats().setNpcFriendship(targetNpc.getName(), 150);
    //             }

    //             System.out.println("CHEAT ACTIVATED: Player is now married to " + targetNpc.getName() + ".");
    //             if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: Married to " + targetNpc.getName() + "!");
    //             if (gamePanel != null) gamePanel.repaint();
    //         } else {
    //             System.out.println("CHEAT FAILED: NPC '" + npcName + "' not found.");
    //             if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: NPC " + npcName + " not found!");
    //         }
    //     } else {
    //         System.out.println("CHEAT FAILED: Player model not available.");
    //     }
    // }

    // /**
    //  * CHEAT: Mengatur cuaca ke nilai tertentu.
    //  * @param weather Cuaca yang diinginkan (Weather.SUNNY atau Weather.RAINY).
    //  */
    // public void cheat_setWeather(Weather weather) {
    //     if (farm != null && farm.getGameClock() != null && timeManager != null) {
    //         farm.getGameClock().setTodayWeather(weather); 
    //         timeManager.notifyObservers(); 
    //         System.out.println("CHEAT ACTIVATED: Weather set to " + weather + ".");
    //         if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: Weather set to " + weather + "!");
    //     } else {
    //         System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting weather.");
    //     }
    // }

    // public void cheat_setWeatherToRainy() {
    //     cheat_setWeather(Weather.RAINY);
    // }

    // public void cheat_cycleWeather() {
    //     if (farm != null && farm.getGameClock() != null) {
    //         Weather currentWeather = farm.getGameClock().getTodayWeather();
    //         Weather nextWeather = (currentWeather == Weather.SUNNY) ? Weather.RAINY : Weather.SUNNY;
    //         cheat_setWeather(nextWeather);
    //     } else {
    //         System.out.println("CHEAT FAILED: GameClock not available for cycling weather.");
    //     }
    // }

    // /**
    //  * CHEAT: Mengatur musim ke nilai tertentu.
    //  * @param season Musim yang diinginkan.
    //  */
    // public void cheat_setSeason(Season season) {
    //     if (farm != null && farm.getGameClock() != null && timeManager != null) {
    //         farm.getGameClock().setCurrentSeason(season); // Anda perlu metode setCurrentSeason di GameClock.java
    //         // Mengganti musim mungkin juga perlu mereset hari ke 1, atau Anda biarkan hari tetap?
    //         // farm.getGameClock().setDay(1); // Opsional: reset hari ke 1 saat ganti musim via cheat
    //         timeManager.notifyObservers(); // Update UI dan komponen lain
    //         System.out.println("CHEAT ACTIVATED: Season set to " + season + ".");
    //         if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: Season set to " + season + "!");
    //     } else {
    //         System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting season.");
    //     }
    // }

    // /**
    //  * CHEAT: Mengganti ke musim berikutnya dalam siklus.
    //  */
    // public void cheat_setNextSeason() {
    //     if (farm != null && farm.getGameClock() != null) {
    //         Season currentSeason = farm.getGameClock().getCurrentSeason();
    //         Season nextSeason;
    //         switch (currentSeason) {
    //             case SPRING: nextSeason = Season.SUMMER; break;
    //             case SUMMER: nextSeason = Season.FALL; break;
    //             case FALL:   nextSeason = Season.WINTER; break;
    //             case WINTER: nextSeason = Season.SPRING; break;
    //             default:     nextSeason = Season.SPRING; // Fallback
    //         }
    //         cheat_setSeason(nextSeason);
    //     } else {
    //         System.out.println("CHEAT FAILED: GameClock not available for cycling season.");
    //     }
    // }
  
}
