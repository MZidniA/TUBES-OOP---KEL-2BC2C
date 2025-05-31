package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.example.controller.action.Action;
import org.example.controller.action.CookingAction;
import org.example.controller.action.MarryingAction;
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
import org.example.model.Sound;
import org.example.model.NPC.NPC;
import org.example.model.PlayerStats;
import org.example.model.Recipe;
import org.example.model.enums.LocationType;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.model.enums.SleepReason;
import org.example.model.enums.RelationshipStats; 


import org.example.view.FishingPanel;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
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
    private final Player playerModel; // Bisa juga didapat dari farmModel.getPlayerModel()


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

    public GameController(JFrame frame, GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm; // Menggunakan 'farm'
        this.mainFrame = frame;

        this.gameState = new GameState(); // Inisialisasi di sini
        this.playerModel = farm.getPlayerModel();

        if (this.playerModel == null) {
            throw new IllegalStateException("Player model cannot be null (obtained from Farm) in GameController constructor.");
        }
        // Pastikan PlayerStats diinisialisasi di dalam constructor Player
        if (this.playerModel.getPlayerStats() == null) {
            System.err.println("GameController CRITICAL: PlayerStats is null for playerModel. Ensure it's initialized within Player constructor or Player.java.");
            // Jika perlu, this.playerModel.setPlayerStats(new PlayerStats()); tapi ini sebaiknya di Player.
        }


        this.tileManager = gamePanel.tileM;
        this.gameStateUI = gamePanel.gameStateUI; // Diambil dari gamePanel

        // Inisialisasi TimeManager dari konstruktor Anda
        if (farm.getGameClock() != null && this.gameStateUI != null) {
            this.timeManager = new TimeManager(farm, farm.getGameClock());
            this.timeManager.addObserver(this.gameStateUI);
        } else {
            this.timeManager = null;
            System.err.println("GameController Constructor: Farm.getGameClock() or GameStateUI is null. TimeManager not initialized.");
        }

        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);
        // KeyHandler di-pass 'this' (GameController)
        this.keyHandler = new KeyHandler(this); // Pastikan KeyHandler menerima GameController
        this.music = new Sound();
        // this.music.setFile(); // Anda mungkin punya cara spesifik untuk path file

        if (this.gamePanel != null) {
            this.gamePanel.addKeyListener(this.keyHandler);
            this.gamePanel.setFocusable(true);
        }

        movementState.put("up", false);
        movementState.put("down", false);
        movementState.put("left", false);
        movementState.put("right", false);
        setupGame(); // Metode setupGame Anda
        // Penambahan NPC dari konstruktor Anda
        farm.addNPC(new org.example.model.NPC.AbigailNPC()); // Gunakan full qualified name jika ada ambiguitas
        farm.addNPC(new org.example.model.NPC.CarolineNPC());
        farm.addNPC(new org.example.model.NPC.PerryNPC());
        farm.addNPC(new org.example.model.NPC.EmilyNPC());
        farm.addNPC(new org.example.model.NPC.DascoNPC());
        farm.addNPC(new org.example.model.NPC.MayorTadiNPC());

        System.out.println("GameController initialized.");
    }
    
    public GamePanel getGamePanel() { 
        return gamePanel; 
    }
    public GameStateUI getGameStateUIFromPanel() { 
        return gamePanel != null ? gamePanel.gameStateUI : null; }
    public CollisionChecker getCollisionChecker() { return this.cChecker; 
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
        if (playerViewInstance == null || cChecker == null || gameState == null || farm == null) {
            return;
        }
        // Player playerModel = farm.getPlayerModel(); // playerModel sudah menjadi field kelas
        if (this.playerModel == null) {
            return;
        }

        // Kontrol TimeManager berdasarkan GameState
        if (this.gameState.getGameState() == this.gameState.play) {
            if (this.timeManager != null && !this.timeManager.isRunning()) { // Perlu method isRunning() di TimeManager
                 this.timeManager.startTimeSystem();
            }
        } else {
            // Jika bukan state play (misal: pause, inventory, menu, stats, day_report), pastikan waktu berhenti
            if (this.timeManager != null && this.timeManager.isRunning()) {
                 this.timeManager.stopTimeSystem();
            }
        }

        farm.updateCookingProgress();

        // Cek kondisi pingsan atau tidur paksa hanya jika game dalam state 'play'
        if (gameState.getGameState() == gameState.play) {
            if (playerModel.isPassedOut()) { // Flag ini di-set oleh Player.java saat energi <= MIN_ENERGY_OPERATIONAL
                System.out.println("GameController: Player passed out due to energy."); // DEBUG
                playerModel.setSleepReason(SleepReason.PASSED_OUT_ENERGY);
                initiateSleepSequence(); // Memulai urutan tidur/laporan hari
            } else if (playerModel.isForceSleepByTime()) { // Flag ini di-set oleh TimeManager
                System.out.println("GameController: Player forced to sleep due to time."); // DEBUG
                playerModel.setSleepReason(SleepReason.PASSED_OUT_TIME);
                initiateSleepSequence(); // Memulai urutan tidur/laporan hari
            }
        }

        if (gameState.getGameState() == gameState.play) {
            playerViewInstance.update(movementState, cChecker);
            playerModel.setTilePosition(playerViewInstance.worldX / getTileSize(), playerViewInstance.worldY / getTileSize());
        }
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
        if (gameState.getGameState() == gameState.play) gameState.setGameState(gameState.pause);
        else if (gameState.getGameState() == gameState.pause) gameState.setGameState(gameState.play);
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
        if (gameState.getGameState() == gameState.pause && gamePanel != null && gamePanel.gameStateUI != null) {
            GameStateUI ui = gamePanel.gameStateUI;
            if (ui.commandNum == 0) gameState.setGameState(gameState.play);
            else if (ui.commandNum == 1) return;
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
        gameState.setGameState(gameState.pause); 
    
        
        Player playerModel = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();
        int tileSize = getTileSize();
        

        gameClock.nextDay(farm.getPlayerStats());
        processEndOfDayEvents();
        playerModel.setEnergy(10); 
        playerModel.setCurrentHeldItem(null);
        if (farm.getCurrentMap() != 4) {
            farm.setCurrentMap(4);
            tileManager.loadMap(farm.getMapPathFor(4), 4);
            aSetter.setInteractableObject();
        }

        playerViewInstance.worldX = 7 * tileSize;
        playerViewInstance.worldY = 10 * tileSize;
        playerViewInstance.direction = "down";
   
        System.out.println("Kamu terbangun keesokan paginya. Energimu hanya pulih sedikit.");
        System.out.println("Hari baru telah dimulai: Hari ke-" + gameClock.getDay());
        

        
        if (playerModel.isPassedOut()) {
            playerModel.setPassedOut(false);
        }
        if (playerModel.isForceSleepByTime()) {
            playerModel.setForceSleepByTime(false);
        }
        
  
        gameState.setGameState(gameState.play);
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
        System.out.println("===== END OF PLANT GROWTH PROCESSING =====\n");
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

    public void navigateMapSelectionMenu(String direction) {
        if (gameState.getGameState() != gameState.map_selection || gameStateUI == null || gameStateUI.mapOptions == null) {
            return;
        }
        System.out.println("GameController: Navigating Map Selection Menu - " + direction); // DEBUG

        if ("up".equalsIgnoreCase(direction)) {
            gameStateUI.mapSelectionCommandNum--;
            if (gameStateUI.mapSelectionCommandNum < 0) {
                gameStateUI.mapSelectionCommandNum = gameStateUI.mapOptions.size() - 1;
            }
        } else if ("down".equalsIgnoreCase(direction)) {
            gameStateUI.mapSelectionCommandNum++;
            if (gameStateUI.mapSelectionCommandNum >= gameStateUI.mapOptions.size()) {
                gameStateUI.mapSelectionCommandNum = 0;
            }
        }
        System.out.println("  -> mapSelectionCommandNum: " + gameStateUI.mapSelectionCommandNum); // DEBUG
    }

    public void confirmMapSelection() {
        if (gameState.getGameState() != gameState.map_selection || gameStateUI == null ||
            gameStateUI.mapOptions == null || gameStateUI.mapOptionTargetMapIndices == null || gameStateUI.mapOptionTargetCoords == null) {
            System.err.println("GameController: confirmMapSelection - Invalid state or UI data null.");
            return;
        }

        int selection = gameStateUI.mapSelectionCommandNum;
        String selectedOptionText = gameStateUI.mapOptions.get(selection);

        System.out.println("GameController: Confirming Map Selection - Option: " + selectedOptionText); // DEBUG

        if ("Cancel".equalsIgnoreCase(selectedOptionText)) {
            exitMapSelectionMenu();
            return;
        }

        // Karena "Cancel" adalah opsi terakhir, jika bukan cancel,
        // maka selection index harus valid untuk mapOptionTargetMapIndices dan mapOptionTargetCoords
        if (selection < gameStateUI.mapOptionTargetMapIndices.size()) {
            int targetMapIndex = gameStateUI.mapOptionTargetMapIndices.get(selection);
            int[] targetCoords = gameStateUI.mapOptionTargetCoords.get(selection);
            int targetTileX = targetCoords[0];
            int targetTileY = targetCoords[1];
            int tileSize = getTileSize();

            System.out.println("  -> Teleporting to Map: " + targetMapIndex + " at Tile (" + targetTileX + ", " + targetTileY + ")");
            teleportPlayer(targetMapIndex, targetTileX * tileSize, targetTileY * tileSize);
            gameState.setGameState(gameState.play); // Kembali ke play state setelah teleport
            resetMovementState();
            if (gamePanel != null) gamePanel.requestFocusInWindow();
        } else {
            System.err.println("GameController: confirmMapSelection - Selection index out of bounds for target map data.");
            exitMapSelectionMenu(); // Keluar jika ada error
        }
    }

    public void exitMapSelectionMenu() {
        if (gameState.getGameState() == gameState.map_selection) {
            gameState.setGameState(gameState.play);
            if (gameStateUI != null) {
                gameStateUI.resetMapSelectionMenuState(); // Reset menu UI
            }
            resetMovementState();
            if (gamePanel != null) gamePanel.requestFocusInWindow();
            System.out.println("GameController: Exited Map Selection Menu. Game state set to PLAY.");
        }
    }
  
    // === METODE BARU / DISESUAIKAN UNTUK END GAME STATISTICS ===


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

    /**
     * Dipanggil saat pemain menutup (dismiss) layar laporan akhir hari (misalnya, oleh KeyHandler).
     */
    public void handleEndOfDayReportDismissal() {
        // Hanya proses jika memang sedang di state day_report
        if (this.gameState.getGameState() != this.gameState.day_report) return;

        System.out.println("GameController: End of day report dismissed by player.");
        
        // 1. Proses pendapatan hari ini (tambahkan gold ke pemain & catat di PlayerStats)
        //    dan kemudian cek statistik end game.
        processDayRevenueAndCheckStats(); 

        // 2. Setelah semua proses selesai, kembalikan ke state bermain normal,
        //    KECUALI jika checkForEndGameStatsTrigger mengubah state ke END_GAME_STATS.
        if (this.gameState.getGameState() != this.gameState.end_game_stats) { // Perhatikan konstanta yang benar
            this.gameState.setGameState(this.gameState.play);
            if (this.timeManager != null) {
                this.timeManager.startTimeSystem(); // Mulai lagi waktu permainan
            }
            System.out.println("GameController LOG: Returned to PLAY state. Time system (if available) resumed.");
        }
        
        // Reset alasan tidur pemain ke default setelah laporan selesai
        playerModel.setSleepReason(SleepReason.NOT_SLEEPING);

        if (this.gamePanel != null) this.gamePanel.repaint();
    }


    /**
     * Dipanggil oleh MarryingAction setelah pernikahan berhasil.
     */
    // Duplicate method removed to resolve compile error.

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
                    playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.MARRIED) {
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

    public void initiateSleepSequence() {
        // Mencegah pemanggilan ganda jika sudah dalam proses transisi
        if (gameState.getGameState() != gameState.play) {
            System.out.println("GameController: initiateSleepSequence called, but game not in PLAY state. Current state: " + gameState.getGameState());
            return;
        }

        System.out.println("GameController: Initiating sleep sequence. Current SleepReason: " + playerModel.getSleepReason());

        // 1. Hentikan TimeManager terlebih dahulu
        if (this.timeManager != null) {
            this.timeManager.stopTimeSystem();
        }

        // 2. Majukan hari (GameClock.nextDay akan mengupdate PlayerStats.totalDaysPlayed)
        if (this.farm.getGameClock() != null && this.playerModel.getPlayerStats() != null) {
            this.farm.getGameClock().nextDay(this.playerModel.getPlayerStats()); // Ini juga merandomize weather untuk hari berikutnya
        } else {
             System.err.println("GameController ERROR: GameClock or PlayerStats is null during sleep sequence.");
        }
        
        // 3. Proses event akhir hari (pertumbuhan tanaman, kalkulasi revenue shipping bin)
        // Ini penting dilakukan SEBELUM mengambil goldFromLastShipment untuk laporan UI.
        processEndOfDayEvents(); // Metode ini sudah ada di file Anda

        // 4. Siapkan info untuk laporan harian UI
        String sleepMessage = "Kamu tidur nyenyak hingga pagi."; // Default
        if (playerModel.getSleepReason() == SleepReason.PASSED_OUT_ENERGY) {
            sleepMessage = "Kamu pingsan karena kelelahan dan \nseseorang membawamu pulang.";
            playerModel.setEnergy(playerModel.getMaxEnergy() / 2); // Penalti energi
        } else if (playerModel.getSleepReason() == SleepReason.PASSED_OUT_TIME) {
            sleepMessage = "Kamu begadang hingga larut dan \nakhirnya pingsan di tempat!";
            playerModel.setEnergy(playerModel.getMaxEnergy() / 2); // Penalti energi
        } else { // Tidur Normal
            playerModel.setEnergy(playerModel.getMaxEnergy()); // Energi penuh
        }
        // Reset flag pingsan dan tidur paksa di PlayerModel setelah diproses
        playerModel.setPassedOut(false);
        playerModel.setForceSleepByTime(false);

        if (this.gameStateUI != null) {
            // Menampilkan gold yang AKAN diterima (dari bin hari yang baru saja berakhir)
            this.gameStateUI.setEndOfDayInfo(sleepMessage, this.farm.getAndClearGoldFromLastShipment());
        }

        // 5. Ubah game state ke laporan harian
        this.gameState.setGameState(this.gameState.day_report);
        System.out.println("GameController LOG: GameState changed to DAY_REPORT.");

        if (this.gamePanel != null) this.gamePanel.repaint();
    }

    private void processDayRevenueAndCheckStats() {
        System.out.println("GameController LOG: Processing day revenue and checking stats...");

        int goldFromShipment = this.farm.getAndClearGoldFromLastShipment(); // Ambil dan reset di Farm
        if (goldFromShipment > 0) {
            this.playerModel.addGold(goldFromShipment);
            if (this.playerModel.getPlayerStats() != null) {
                this.playerModel.getPlayerStats().recordIncome(goldFromShipment);
            }
            System.out.println("GameController LOG: Player received " + goldFromShipment + "g from shipment. Player total gold: " + playerModel.getGold());
        }
        
        checkForEndGameStatsTrigger(); // Cek pemicu statistik End Game
        System.out.println("GameController LOG: Day revenue and stats check procedures complete.");
    }


    /**
     * Metode utama untuk memeriksa apakah kondisi End Game Statistics tercapai.
     */
    public void checkForEndGameStatsTrigger() {
        if (playerModel == null || playerModel.getPlayerStats() == null || gameState == null) {
            System.err.println("GameController ERROR: Cannot check stats trigger. playerModel, PlayerStats, or GameState is null.");
            return;
        }

        PlayerStats stats = playerModel.getPlayerStats();

        if (stats.hasShownEndGameStats()) { // Flag ini dicek untuk mencegah penampilan berulang
            return;
        }

        boolean playerIsMarried = playerModel.getPartner() != null &&
                                 playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.MARRIED;
        boolean goldMilestoneReached = playerModel.getGold() >= END_GAME_GOLD_MILESTONE;

        if (playerIsMarried || goldMilestoneReached) {
            String triggerReason = playerIsMarried ? "player is married" : ("gold milestone (" + playerModel.getGold() + "g) reached");
            System.out.println("GameController LOG: End game milestone triggered because " + triggerReason);
            
            stats.setHasShownEndGameStats(true); // Set flag agar tidak tampil lagi untuk pemicu yang sama

            // Hentikan TimeManager jika sedang berjalan (seharusnya sudah berhenti jika dari day_report)
            if (this.timeManager != null && this.timeManager.isRunning()) {
                this.timeManager.stopTimeSystem();
            }
            this.gameState.setGameState(this.gameState.end_game_stats); // Gunakan konstanta dari GameState.java
            System.out.println("GameController LOG: GameState changed to END_GAME_STATS.");
            
            if (this.gamePanel != null) {
                this.gamePanel.repaint();
            }
        }
    }

    /**
     * Dipanggil oleh MarryingAction atau setelah aksi menikah berhasil.
     */
    public void handleSuccessfulMarriage(Player player, NPC spouse) {
        System.out.println("GameController LOG: Handling successful marriage for " + player.getName() + " and " + spouse.getName());
        // Status pemain dan NPC diasumsikan sudah diupdate oleh MarryingAction
        checkForEndGameStatsTrigger(); // Langsung cek pemicu setelah menikah
        if (this.gamePanel != null) this.gamePanel.repaint();
    }
}
