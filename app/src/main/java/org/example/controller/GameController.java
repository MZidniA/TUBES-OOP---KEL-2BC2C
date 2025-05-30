package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

<<<<<<< Updated upstream

=======
import org.example.controller.action.CookingAction;
>>>>>>> Stashed changes
import org.example.controller.action.PlantingAction;
import org.example.controller.action.RecoverLandAction;
import org.example.controller.action.TillingAction;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Inventory;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Items.Seeds;
import org.example.model.Player;
import org.example.model.Recipe;
import org.example.model.Sound;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.enums.LocationType;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.FishingPanel;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;
import org.example.view.InteractableObject.MountainLakeObject;
import org.example.view.InteractableObject.OceanObject;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.InteractableObject.UnplantedTileObject;
import org.example.model.Map.Tile;

public class GameController implements Runnable {

    private final GamePanel gamePanel;
    private final Farm farm;
    private final PlayerView playerViewInstance;
    private final TileManager tileManager; 
    private final GameStateUI gameStateUI; 

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

    public GameController(GamePanel gamePanel, Farm farm) {
        this.gamePanel = gamePanel;
        this.farm = farm;

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
        if (playerViewInstance == null || cChecker == null || gameState == null) return;
        Player playerModel = farm.getPlayerModel();
        if (playerModel.isPassedOut()) {
            passedOutSleep();
            System.out.println("Kamu pingsan dan seseorang membawamu pulang...");
        } 
        if (playerModel.isForceSleepByTime()) {
            passedOutSleep();
            System.out.println("Sudah jam 02:00, kamu kelelahan dan pingsan");
        }
        if (gameState.getGameState() == gameState.play) {
            playerViewInstance.update(movementState, cChecker);
            if (playerModel != null && playerViewInstance != null) {
                playerModel.setTilePosition(playerViewInstance.worldX / getTileSize(), playerViewInstance.worldY / getTileSize());
            }
        }
        if (gameState.getGameState() == gameState.play) { // Atau state lain yang relevan
            playerViewInstance.update(movementState, cChecker);
            if (farm != null) {
                farm.updateCookingProgress(); // PANGGIL INI SECARA BERKALA
            }
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
                // if (heldItem != null && heldItem.getName().equalsIgnoreCase("Fishing Rod") && targetObject.name.equalsIgnoreCase("Pond")) {
                //     System.out.println(playerModel.getName() + " is fishing at the " + targetObject.name + "!");
                //     return;
                // }
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
            } else if (heldItem.getName().equalsIgnoreCase("Pickaxe")) {
                RecoverLandAction recoverAction = new RecoverLandAction(this, targetCol, targetRow);
                if (recoverAction.canExecute(farm)) {
                    recoverAction.execute(farm);
                } else {
                    System.out.println("Tidak bisa mengembalikan tanah ini lagi");
                }
            } else if (heldItem instanceof Seeds) { 
                Seeds seedBeingHeld = (Seeds) heldItem;
                InteractableObject objectAtTargetTile = farm.getObjectAtTile(currentMap, targetCol, targetRow, tileSize);
                if (objectAtTargetTile instanceof UnplantedTileObject) { 
                    PlantingAction plantingAction = new PlantingAction(this, seedBeingHeld, targetCol, targetRow);
                    if (plantingAction.canExecute(farm)) {
                        plantingAction.execute(farm);
                
                    } else {
                        System.out.println("PlantingAction tidak bisa dieksekusi.");
                    }
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
        int musicIdx = 0; // Default music
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

    public void navigateCookingMenuUI(String direction) {

        if (gameState.getGameState() == gameState.cooking_menu && gameStateUI != null) {
            System.out.println("GameController: navigateCookingMenuUI - " + direction); // Debugging
            if ("up".equals(direction)) {
                // Jika fokus di resep:
                gameStateUI.setSelectedRecipeIndex(gameStateUI.getSelectedRecipeIndex() - 1);
            } else if ("down".equals(direction)) {
                // Jika fokus di resep:
                gameStateUI.setSelectedRecipeIndex(gameStateUI.getSelectedRecipeIndex() + 1);
            } else if ("left".equals(direction)) {
                // Bisa untuk pindah antar fuel, atau dari resep ke fuel, atau antar tombol aksi
                // Contoh: pindah antar tombol aksi jika ada 2 tombol (COOK, CANCEL)
                int currentCommand = gameStateUI.getCookingMenuCommandNum();
                gameStateUI.setCookingMenuCommandNum(currentCommand == 0 ? 1 : 0); // Toggle antara 0 dan 1
                // Atau jika fokus di fuel:
                // gameStateUI.setSelectedFuelIndex(gameStateUI.getSelectedFuelIndex() - 1);
            } else if ("right".equals(direction)) {
                int currentCommand = gameStateUI.getCookingMenuCommandNum();
                gameStateUI.setCookingMenuCommandNum(currentCommand == 0 ? 1 : 0); // Toggle

            }
        }
    }


    public void confirmCookingMenuSelection() {
        if (gameState.getGameState() == gameState.cooking_menu && gameStateUI != null) {
            System.out.println("GameController: confirmCookingMenuSelection"); // Debugging
            int commandNum = gameStateUI.getCookingMenuCommandNum(); // Dapatkan command yang dipilih dari UI

            if (commandNum == 0) { // Asumsi 0 adalah untuk "COOK"
                handleConfirmCooking(); // Metode ini sudah ada dari langkah sebelumnya
            } else if (commandNum == 1) { // Asumsi 1 adalah untuk "CANCEL"
                exitCookingMenu(); // Metode ini sudah ada dari langkah sebelumnya
            }
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
        return (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
    }

    public void openFishingPanel() {
        FishingPanel fishingPanel = new FishingPanel(farm, this);
        JFrame frame = getMainFrame();
        frame.setContentPane(fishingPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void returnToGamePanel() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
        frame.setContentPane(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
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

        
        // Asumsi FarmMap memiliki getSize() atau getWidth()/getHeight()
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
<<<<<<< Updated upstream
}
=======

    public void handleConfirmCooking() {
        // Pastikan game sedang dalam state cooking_menu dan komponen UI serta model farm ada
        if (gameState.getGameState() != gameState.cooking_menu || gameStateUI == null || farm == null) {
            System.err.println("GameController: handleConfirmCooking dipanggil pada state yang salah atau komponen null.");
            return;
        }
        System.out.println("GameController: handleConfirmCooking dipanggil.");

        // Dapatkan resep dan bahan bakar yang dipilih dari GameStateUI
        // GameStateUI perlu memiliki getter untuk ini
        List<Recipe> recipes = gameStateUI.getAvailableRecipesForUI(); // Asumsi metode ini ada di GameStateUI
        List<Items> fuels = gameStateUI.getAvailableFuelsForUI();     // Asumsi metode ini ada di GameStateUI

        if (recipes == null || recipes.isEmpty()) {
            gameStateUI.showTemporaryMessage("No recipes available to cook.");
            System.out.println("GameController: Tidak ada resep yang tersedia untuk dimasak.");
            return;
        }
        if (fuels == null || fuels.isEmpty()) {
            // Jika tidak ada fuel yang dikonfigurasi di GameStateUI, beri pesan.
            // Namun, spesifikasi hanya menyebut Firewood dan Coal, yang seharusnya ada.
            gameStateUI.showTemporaryMessage("No fuel items configured.");
             System.out.println("GameController: Tidak ada bahan bakar yang terkonfigurasi.");
            return;
        }
        
        int recipeIdx = gameStateUI.getSelectedRecipeIndex(); // Asumsi metode ini ada
        int fuelIdx = gameStateUI.getSelectedFuelIndex();     // Asumsi metode ini ada

        // Validasi indeks (meskipun GameStateUI seharusnya sudah menangani wrap-around)
        if (recipeIdx < 0 || recipeIdx >= recipes.size()) {
            gameStateUI.showTemporaryMessage("Invalid recipe selection.");
            System.err.println("GameController: Indeks resep tidak valid: " + recipeIdx);
            return;
        }
         if (fuelIdx < 0 || fuelIdx >= fuels.size()) {
            gameStateUI.showTemporaryMessage("Invalid fuel selection.");
            System.err.println("GameController: Indeks bahan bakar tidak valid: " + fuelIdx);
            return;
        }

        Recipe selectedRecipe = recipes.get(recipeIdx);
        Items selectedFuel = fuels.get(fuelIdx);

        // Buat instance CookingAction dengan resep dan bahan bakar yang dipilih
        CookingAction cookingAction = new CookingAction(selectedRecipe, selectedFuel);

        if (cookingAction.canExecute(farm)) {
            cookingAction.execute(farm); // Ini akan mengurangi energi, bahan, dan memulai CookingInProgress
            gameStateUI.showTemporaryMessage(selectedRecipe.getDisplayName() + " is now cooking!");
            
            // Setelah memasak dimulai, kembalikan game ke state PLAY
            gameState.setGameState(gameState.play); // Menggunakan field instance
            gameStateUI.resetCookingMenuState(); // Reset UI menu memasak
            resetMovementState(); // Reset status gerakan pemain jika perlu
            System.out.println("GameController: Cooking started. Game state set to PLAY.");
        } else {
            // Pesan error spesifik seharusnya sudah dicetak oleh CookingAction.canExecute() ke konsol.
            // Tampilkan pesan umum di UI melalui GameStateUI.
            // Pesan ini bisa diset oleh canExecute() atau di sini.
            // Untuk sekarang, kita biarkan uiMessage dari GameStateUI yang mungkin sudah diset,
            // atau tambahkan pesan default jika tidak ada.
            if (gameStateUI.getUiMessage() == null) { // getUiMessage() perlu ada di GameStateUI
                gameStateUI.showTemporaryMessage("Cannot cook " + selectedRecipe.getDisplayName() + ". Check requirements.");
            }
            System.out.println("GameController: Tidak bisa mengeksekusi CookingAction untuk " + selectedRecipe.getDisplayName());
        }
    }

    /**
     * Keluar dari menu memasak dan kembali ke state PLAY.
     * Dipanggil oleh KeyHandler saat tombol ESC atau opsi "CANCEL" dipilih di Cooking Menu.
     */
    public void exitCookingMenu() {
        if (gameState.getGameState() == gameState.cooking_menu) { // Cek apakah memang sedang di menu memasak
            System.out.println("GameController: Exiting COOKING_MENU.");
            gameState.setGameState(gameState.play); // Kembali ke mode play
            if (gameStateUI != null) {
                gameStateUI.resetCookingMenuState(); // Reset UI cooking
            }
            resetMovementState(); // Reset status gerakan pemain jika perlu
        }
    }
    
}
>>>>>>> Stashed changes
