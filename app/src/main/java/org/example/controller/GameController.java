package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.controller.action.Action;
import org.example.controller.action.CookingAction;
import org.example.controller.action.EatingAction;
import org.example.controller.action.MarryingAction;
import org.example.controller.action.TillingAction;
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
import org.example.view.ViewPlayerInfoPanel;
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
        if (playerViewInstance == null || cChecker == null || gameState == null || farm == null) {
            return;
        }
        

        if (this.playerModel == null) { 
            return;
        }

        if (this.gameState.getGameState() == this.gameState.play) {
            if (this.timeManager != null && !this.timeManager.isRunning()) { 
                this.timeManager.startTimeSystem();
            }
        } else {
            if (this.timeManager != null && this.timeManager.isRunning()) {
                this.timeManager.stopTimeSystem();
            }
        }


        farm.updateCookingProgress(); 

        if (gameState.getGameState() == gameState.play) { 
            if (playerModel.isPassedOut()) {
                //System.out.println("GameController (update): Player passed out due to energy."); 
                playerModel.setSleepReason(SleepReason.PASSED_OUT_ENERGY); 
                initiateSleepSequence(); 
            } 
            else if (playerModel.isForceSleepByTime()) { 
                //System.out.println("GameController (update): Player forced to sleep due to time."); 
                playerModel.setSleepReason(SleepReason.PASSED_OUT_TIME); 
                initiateSleepSequence(); 
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
            
            boolean isExitingHouse = currentMap == 4 && playerCol == 3 && playerRow == 11; 

            if (isExitingHouse) { // Kelu
                visitingAction(0, 4 * tileSize, 9 * tileSize, false); 
            }

            else if (currentMap == 0 && playerCol == 31 && playerRow == 15) { visitingAction(1, 1 * tileSize, 1 * tileSize); } 
            else if (currentMap == 1 && playerCol == 1 && playerRow == 1) { visitingAction(0, 31 * tileSize, 15 * tileSize); } 
            else if (currentMap == 0 && playerCol == 20 && playerRow == 31) { visitingAction(2, 29 * tileSize, 0 * tileSize); } 
            else if (currentMap == 2 && playerCol == 29 && playerRow == 0) { visitingAction(0, 20 * tileSize, 31 * tileSize); } 
            else if (currentMap == 0 && playerCol == 31 && playerRow == 31) { visitingAction(3, 15 * tileSize, 31* tileSize); } 
            else if (currentMap == 3 && playerCol == 15 && playerRow == 31) { visitingAction(0, 30 * tileSize, 31 * tileSize); } 
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
            if (gameStateUI.commandNum == 0) { 
                gameState.setGameState(gameState.play);

            } else if (gameStateUI.commandNum == 1) { 
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
            //System.out.println("GameController: Returning to MenuPanel.");
            MenuPanel menuPanel = new MenuPanel(mainFrame);
            mainFrame.setContentPane(menuPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
            SwingUtilities.invokeLater(menuPanel::requestFocusInWindow);
        } else {
            //System.err.println("GameController: mainFrame is null, cannot switch to MenuPanel.");

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
        visitingAction(mapIndex, worldX, worldY, true); 
    }


    public void visitingAction(int mapIndex, int worldX, int worldY, boolean reduceEnergy) {
        if (farm != null && playerViewInstance != null && gamePanel != null && tileManager != null && aSetter != null) {
            stopMusic(); 
            farm.setCurrentMap(mapIndex);
            Player currentPlayer = farm.getPlayerModel(); 
            GameClock gameClock = farm.getGameClock();

            if (reduceEnergy) { 
                currentPlayer.decreaseEnergy(10); 
                gameClock.advanceTimeByMinutes(farm, 15); 
            }
            
            playerViewInstance.worldX = worldX; 
            playerViewInstance.worldY = worldY; 
            playerViewInstance.direction = "down"; 

            if (currentPlayer != null) {
                switch (mapIndex) {
                    case 0: currentPlayer.setCurrentLocationType(LocationType.FARM); break;
                    case 1: currentPlayer.setCurrentLocationType(LocationType.OCEAN); break;
                    case 2: currentPlayer.setCurrentLocationType(LocationType.FOREST_RIVER); break;
                    case 3: currentPlayer.setCurrentLocationType(LocationType.TOWN); break;
                    case 4: currentPlayer.setCurrentLocationType(LocationType.HOUSE); break;
                    case 5: currentPlayer.setCurrentLocationType(LocationType.STORE); break;
                    case 6: currentPlayer.setCurrentLocationType(LocationType.POND); break;  
                    default: currentPlayer.setCurrentLocationType(LocationType.FARM); break; 
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
            reasonMessage = "Kamu Tidur Paksa Karena Waktu Sudah Larut."; 
        } else if (playerModel.isPassedOut()) { 
            reasonMessage = "Kamu Pingsan Karena Energi Terlalu Rendah."; 
        } else {
            reasonMessage = "Kamu Tertidur Dengan Nyenyak."; 
        }
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

        if (gameStateUI.isSelectingFuel()) { 
            if (gameStateUI.fuelSelectionCommandNum == 1) { 
                gameStateUI.setSelectingFuelMode(false);
                if(gamePanel != null) gamePanel.repaint();
                return;
            }

            if (gameStateUI.availableFuelsForUI == null || gameStateUI.availableFuelsForUI.isEmpty()) {
                gameStateUI.showTemporaryMessage("No fuel available to select!");
                return;
            }
            if (gameStateUI.selectedFuelIndex < 0 || gameStateUI.selectedFuelIndex >= gameStateUI.availableFuelsForUI.size()) {
                gameStateUI.showTemporaryMessage("Invalid fuel selection!");
                return;
            }
            Items actualSelectedFuel = gameStateUI.availableFuelsForUI.get(gameStateUI.selectedFuelIndex);
            

            Recipe previouslySelectedRecipe = null;
            if (gameStateUI.availableRecipesForUI != null && 
                gameStateUI.selectedRecipeIndex >= 0 && 
                gameStateUI.selectedRecipeIndex < gameStateUI.availableRecipesForUI.size()) {
                previouslySelectedRecipe = gameStateUI.availableRecipesForUI.get(gameStateUI.selectedRecipeIndex);
            }

            if (previouslySelectedRecipe == null) {
                gameStateUI.showTemporaryMessage("Error: Recipe not found after fuel selection.");
                gameStateUI.setSelectingFuelMode(false);
                return;
            }

 
            CookingAction cookingAction = new CookingAction(previouslySelectedRecipe, actualSelectedFuel);
            if (cookingAction.canExecute(farm)) {
                cookingAction.execute(farm);
                gameStateUI.showTemporaryMessage("Memasak " + previouslySelectedRecipe.getDisplayName() + " dimulai!");
            } else {
                gameStateUI.showTemporaryMessage("Tidak bisa memasak " + previouslySelectedRecipe.getDisplayName() + ".");
            }
            exitCookingMenu();

        } else { 
            if (gameStateUI.cookingMenuCommandNum == 1) { 
                exitCookingMenu();
                return;
            }
            if (gameStateUI.availableRecipesForUI == null || gameStateUI.availableRecipesForUI.isEmpty()) {
                gameStateUI.showTemporaryMessage("No recipes available!");
                return;
            }
            if (gameStateUI.selectedRecipeIndex < 0 || gameStateUI.selectedRecipeIndex >= gameStateUI.availableRecipesForUI.size()) {
                gameStateUI.showTemporaryMessage("Please select a recipe!");
                return;
            }
            

            gameStateUI.setSelectingFuelMode(true);
            if(gamePanel != null) gamePanel.repaint();
        }
    }
    public void exitCookingMenu() {
        if (gameState != null) {
            gameState.setGameState(gameState.play);
        }
        if (gameStateUI != null) {
            gameStateUI.resetCookingMenuState(); 
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

        if (gameStateUI.isSelectingFuel()) { 
            if ("up_fuel".equalsIgnoreCase(direction) || "up_recipe".equalsIgnoreCase(direction)) { 
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
                if (gameStateUI.fuelSelectionCommandNum == 1) gameStateUI.fuelSelectionCommandNum = 0; 
            } else if ("right_command".equalsIgnoreCase(direction)) {
                if (gameStateUI.fuelSelectionCommandNum == 0) gameStateUI.fuelSelectionCommandNum = 1; 
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
        }
        if (gamePanel != null) gamePanel.repaint(); 
    }

    public void checkForEndGameStatsTrigger() {
        if (playerModel == null || playerModel.getPlayerStats() == null || gameState == null) {
            return;
        }

        PlayerStats stats = playerModel.getPlayerStats();

        if (stats.hasShownEndGameStats()) {
            return;
        }

        boolean playerIsMarried = playerModel.getPartner() != null && playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.SPOUSE;
        boolean goldMilestoneReached = playerModel.getGold() >= END_GAME_GOLD_MILESTONE;

        if (playerIsMarried || goldMilestoneReached) {
            String triggerReason = playerIsMarried ? "player is married" : ("gold milestone (" + playerModel.getGold() + "g) reached");

            
            stats.setHasShownEndGameStats(true);

            if (this.timeManager != null) {
                this.timeManager.stopTimeSystem();
            } else {
                return;
            }
            this.gameState.setGameState(this.gameState.end_game_stats);
            
            if (this.gamePanel != null) {
                this.gamePanel.repaint(); 
            }
        }
    }

  
    public void dismissEndGameStatisticsScreen() {
        if (this.gameState != null && this.gameState.getGameState() == this.gameState.end_game_stats) {
            this.gameState.setGameState(this.gameState.play); // Kembali ke state bermain normal
            if (this.timeManager != null) {
                this.timeManager.startTimeSystem();
            } else {
                return;
            }
            
            if (this.gamePanel != null) {
                this.gamePanel.repaint();
            }
        }
    }

    public void processEndOfDayAndCheckStats() {
        int goldFromShipment = this.farm.getAndClearGoldFromLastShipment();
        if (goldFromShipment > 0) {
            this.playerModel.addGold(goldFromShipment);
            if (this.playerModel.getPlayerStats() != null) {
                this.playerModel.getPlayerStats().recordIncome(goldFromShipment);
            }

        }
        checkForEndGameStatsTrigger();
    }

   
    public void initiateSleepSequence() {
        Player playerModel = farm.getPlayerModel(); 
       

        if (this.timeManager != null) {
            this.timeManager.stopTimeSystem();
        }

        if (this.farm.getGameClock() != null && playerModel.getPlayerStats() != null) { 
            this.farm.getGameClock().nextDay(playerModel.getPlayerStats()); 
            this.farm.setCurrentSeason(this.farm.getGameClock().getCurrentSeason());
            this.farm.setCurrentWeather(this.farm.getGameClock().getTodayWeather());

        } 

        int revenue = farm.processShippedItemsAndGetRevenue();
        farm.setGoldFromLastShipment(revenue);
        


        String sleepMessage = "Kamu tidur nyenyak."; 
        SleepReason currentSleepReason = playerModel.getSleepReason();

        if (currentSleepReason == SleepReason.PASSED_OUT_ENERGY) {
            sleepMessage = "Kamu pingsan karena kehabisan energi, Tuhan menggotongmu";
        } else if (currentSleepReason == SleepReason.PASSED_OUT_TIME) {
            sleepMessage = "Sudah terlalu larut, kamu tertidur karena kelelahan.";
        } else if (currentSleepReason == SleepReason.NORMAL) { 
            sleepMessage = "Kamu tidur dengan nyenyak setelah memilih tidur di kasur.";
        }


        if (this.gameStateUI != null) {
            this.gameStateUI.setEndOfDayInfo(sleepMessage, this.farm.getGoldFromLastShipment());
        }

        this.gameState.setGameState(this.gameState.day_report);
        

        if (this.gamePanel != null) this.gamePanel.repaint();
        
    }


    
    public void handleEndOfDayReportDismissal() {
        Player playerModel = farm.getPlayerModel(); 
        
        processEndOfDayAndCheckStats();

        if (this.gameState.getGameState() == this.gameState.end_game_stats) {

            if (this.gamePanel != null) this.gamePanel.repaint();
            return;
        }

        PlayerView playerView = getPlayerViewInstance();
        int tileSize = getTileSize();

        SleepReason sleepReason = playerModel.getSleepReason();
        int maxEnergy = playerModel.getMaxEnergy();

        if (sleepReason == SleepReason.PASSED_OUT_ENERGY) {
            playerModel.setEnergy(10);
        } else if (sleepReason == SleepReason.PASSED_OUT_TIME) {
            int energySaatJamDuaPagi = playerModel.getEnergy(); 
            int energyForNextDay;
            if (energySaatJamDuaPagi < (0.1 * maxEnergy) && energySaatJamDuaPagi > 0) {
                energyForNextDay = (int) (maxEnergy * 0.5);
            } else if (energySaatJamDuaPagi <= 0) {
                energyForNextDay = 10;
            } else {
                energyForNextDay = maxEnergy;
            }
            playerModel.setEnergy(energyForNextDay);
        } else if (sleepReason == SleepReason.NORMAL) {
            //System.out.println("GameController LOG: Normal sleep, energy restored to max.");
        } else {
            playerModel.setEnergy(maxEnergy);
            
        }
   


        if (farm.getCurrentMap() != 4) {
            visitingAction(4, 6 * tileSize, 10 * tileSize);
        } else {
            if (playerView != null) {
                playerView.worldX = 6 * tileSize;
                playerView.worldY = 10 * tileSize;
                playerView.direction = "down";
                if (tileSize != 0) {
                    playerModel.setTilePosition(playerView.worldX / tileSize, playerView.worldY / tileSize);
                }
                playerModel.setCurrentLocationType(LocationType.HOUSE);
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

        if (this.gamePanel != null) this.gamePanel.repaint();

    }

    public void handleSuccessfulMarriage(Player player, NPC spouse) {
        checkForEndGameStatsTrigger();
        if (this.gamePanel != null) this.gamePanel.repaint();
    }

    
    public void executePlayerAction(Action action) {
        if (action == null) {
            //System.err.println("GameController ERROR: Attempted to execute a null action.");
            return;
        }

        if (action.canExecute(this.farm)) {
            action.execute(this.farm); 
            if (action instanceof MarryingAction) {
                if (playerModel.getPartner() != null && 
                    playerModel.getPartner().getRelationshipsStatus() == RelationshipStats.SPOUSE) {
                    handleSuccessfulMarriage(playerModel, playerModel.getPartner());
                }
            }
        } else {
            if (this.gameStateUI != null) {
                return;
            }
        }
        if (this.gamePanel != null) this.gamePanel.repaint(); 
    }

 
    public Player getPlayerModel() { return this.playerModel; }
    public AssetSetter getAssetSetter() { return this.aSetter; } 

    public void playMusic(int i) {
        // music.setFile(i); 
        // music.play();
        // music.loop();
    }

    public void playSE(int i) { // Sound Effect
        // music.setFile(i); 
        // music.play();
    }

 
    public void teleportPlayer(int mapIndex, int tileX, int tileY) {
        if (farm == null || playerViewInstance == null || tileManager == null || aSetter == null) {
            //System.out.println("Teleport failed: missing required components.");
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

    public void showPlayerInfo() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
        JDialog dialog = new JDialog(frame, "Player Info", true);
        dialog.setUndecorated(true);
        dialog.setContentPane(new ViewPlayerInfoPanel(frame, getFarmModel().getPlayerModel()));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

  
public void handleEatAction() {
        Player player = farm.getPlayerModel();
        if (player == null) return;

        Items itemToEat = player.getCurrentHeldItem();
        if (itemToEat == null) {
            //System.out.println("No item selected to eat.");
            return;
        }

        EatingAction eatingAction = new EatingAction(itemToEat);
        if (eatingAction.canExecute(farm)) {
            eatingAction.execute(farm);
            player.setCurrentHeldItem(null); 

            if (gamePanel != null) {
                gamePanel.repaint();
            }

            GameStateUI ui = getGameStateUI();
            if (ui != null) {
                ui.showTemporaryMessage("You ate " + itemToEat.getName() + " and restored energy!");
            }
        } else {
            //System.out.println("Cannot eat " + itemToEat.getName());
        }
    }
}
