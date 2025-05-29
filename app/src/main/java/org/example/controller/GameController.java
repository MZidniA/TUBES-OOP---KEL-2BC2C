package org.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// import javax.swing.JFrame; // Unused import
// import javax.swing.SwingUtilities; // Unused import
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Items.Items;
// import org.example.model.Items.Seeds; // Unused import
import org.example.model.Player;
import org.example.model.Sound;
import org.example.model.Inventory;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
// import org.example.view.MenuPanel; // Unused import
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;
import org.example.view.tile.TileManager;
import org.example.controller.action.CookingAction;
import org.example.controller.action.TillingAction;

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

        this.gameState = new GameState(); // Assumes GameState defines static state constants

        this.tileManager = gamePanel.tileM;
        this.gameStateUI = gamePanel.getGameStateUI(); // Use getter

        this.playerViewInstance = new PlayerView(farm.getPlayerModel(), gamePanel);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);
        this.keyHandler = new KeyHandler(this);
        this.music = new Sound();
        this.music.setFile(); // Consider error handling if file not found

        if (farm.getGameClock() != null && this.gameStateUI != null) {
            this.timeManager = new TimeManager(farm, farm.getGameClock());
            this.timeManager.addObserver(this.gameStateUI);
        } else {
            this.timeManager = null;
            System.err.println("GameController Error: Farm, GameClock, or GameStateUI null when creating TimeManager.");
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

    public GamePanel getGamePanel() { return gamePanel; }
    public GameStateUI getGameStateUIFromPanel() { return gamePanel != null ? gamePanel.getGameStateUI() : null; } // Use getter
    public CollisionChecker getCollisionChecker() { return this.cChecker; }
    public int getTillableAreaMinCol(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MIN_COL : -1; }
    public int getTillableAreaMaxCol(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MAX_COL : -1; }
    public int getTillableAreaMinRow(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MIN_ROW : -1; }
    public int getTillableAreaMaxRow(int mapIndex) { return mapIndex == 0 ? TILLABLE_AREA_MAP0_MAX_ROW : -1; }


    private void setupGame() {
        if (aSetter != null) aSetter.setInteractableObject();
        gameState.setGameState(GameState.PLAY); // Use static constant
        if (timeManager != null) timeManager.startTimeSystem();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        playMusic();
    }

    public Thread getGameThread() { return this.gameThread; }
    public void playMusic() { if (music != null) { music.play(); music.loop(); } }
    public void stopMusic() { if (music != null) music.stop(); }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60.0; // Target 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null && !gameThread.isInterrupted()) { // Check for interruption
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
        if (playerViewInstance == null || cChecker == null || gameState == null || farm == null) return;
        Player playerModel = farm.getPlayerModel();
        if (playerModel == null) return;

        if (playerModel.isPassedOut()) {
            passedOutSleep();
            // System.out.println("Kamu pingsan dan seseorang membawamu pulang..."); // Message handled in passedOutSleep or UI
        }
        if (playerModel.isForceSleepByTime()) {
            passedOutSleep();
            // System.out.println("Sudah jam 02:00, kamu kelelahan dan pingsan"); // Message handled in passedOutSleep or UI
        }
        if (gameState.getGameState() == GameState.PLAY) { // Use static constant
             playerViewInstance.update(movementState, cChecker);
        }
    }

    public void handlePlayerMove(String direction, boolean isMoving) {
        if (gameState.getGameState() == GameState.PLAY) { // Use static constant
            movementState.put(direction, isMoving);
        }
    }

    public void handleInteraction() {
        if (gameState.getGameState() != GameState.PLAY) return; // Use static constant
        if (playerViewInstance == null || cChecker == null || farm == null || gamePanel == null || farm.getPlayerModel() == null) return;

        Player playerModel = farm.getPlayerModel();
        Items heldItem = playerModel.getCurrentHeldItem();
        int tileSize = getTileSize();
        int currentMap = farm.getCurrentMap();

        // Check for interactable objects first
        int objIndex = cChecker.checkObject(playerViewInstance);
        if (objIndex != 999) { // 999 indicates no object found
            InteractableObject[] currentObjects = farm.getObjectsForCurrentMap();
            if (currentObjects != null && objIndex < currentObjects.length && currentObjects[objIndex] != null) {
                InteractableObject targetObject = currentObjects[objIndex];
                // Specific interaction example (fishing)
                if (heldItem != null && "Fishing Rod".equalsIgnoreCase(heldItem.getName()) && "Pond".equalsIgnoreCase(targetObject.name)) {
                    // Consider moving this logic to the FishingRod item's use method or Pond's interact method
                    System.out.println(playerModel.getName() + " is fishing at the " + targetObject.name + "!");
                    // gameStateUI.showTemporaryMessage(playerModel.getName() + " is fishing!"); // Example UI feedback
                    return;
                }
                targetObject.interact(this); // Generic interaction
                return;
            }
        }

        // If no object interaction, check for item-based tile interaction or map transitions
        if (heldItem != null) {
            // Determine target tile based on player direction and held item
            int targetCol = 0, targetRow = 0;
            if (playerViewInstance.solidArea == null) return; // Should not happen if player is initialized
            int currentSpeed = playerViewInstance.speed != 0 ? playerViewInstance.speed : 4; // Default speed if 0

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
                default: return; // Invalid direction
            }

            if (targetCol < 0 || targetCol >= getMaxWorldCol() || targetRow < 0 || targetRow >= getMaxWorldRow()) return; // Out of bounds
            // System.out.println("DEBUG: Target Tile for Interaction: (" + targetCol + ", " + targetRow + ")"); // For debugging

            if ("Hoe".equalsIgnoreCase(heldItem.getName())) {
                TillingAction tilling = new TillingAction(this, targetCol, targetRow);
                if (tilling.canExecute(farm)) {
                    tilling.execute(farm);
                } else {
                    // System.out.println("GameController: TillingAction cannot be executed (Hoe)."); // For debugging
                    if(gameStateUI != null) gameStateUI.showTemporaryMessage("Cannot till here.");
                }
            }
            // Add other item interactions here (e.g., planting seeds)
        } else {
            // No item held, check for map transitions (teleportation)
            if (playerViewInstance.solidArea == null) return;
            int playerCol = (playerViewInstance.worldX + playerViewInstance.solidArea.x + playerViewInstance.solidArea.width / 2) / tileSize;
            int playerRow = (playerViewInstance.worldY + playerViewInstance.solidArea.y + playerViewInstance.solidArea.height / 2) / tileSize;

            // Consider moving teleportation points to a data structure or map configuration
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
        if (gameState.getGameState() == GameState.PLAY) gameState.setGameState(GameState.PAUSE); // Use static constant
        else if (gameState.getGameState() == GameState.PAUSE) gameState.setGameState(GameState.PLAY); // Use static constant
    }
    public void toggleInventory() {
        if (gameState.getGameState() == GameState.PLAY) gameState.setGameState(GameState.INVENTORY); // Use static constant
        else if (gameState.getGameState() == GameState.INVENTORY) { // Use static constant
            gameState.setGameState(GameState.PLAY); // Use static constant
            resetMovementState();
        }
    }
    public void navigatePauseUI(String direction) {
        if (gameState.getGameState() == GameState.PAUSE && gamePanel != null && gamePanel.getGameStateUI() != null) { // Use static constant
            GameStateUI ui = gamePanel.getGameStateUI();
            if ("up".equals(direction)) { ui.commandNum--; if (ui.commandNum < 0) ui.commandNum = 1; } // Max 2 options
            else if ("down".equals(direction)) { ui.commandNum++; if (ui.commandNum > 1) ui.commandNum = 0; }
        }
    }
    public void confirmPauseUISelection() {
        if (gameState.getGameState() == GameState.PAUSE && gamePanel != null && gamePanel.getGameStateUI() != null) { // Use static constant
            GameStateUI ui = gamePanel.getGameStateUI();
            if (ui.commandNum == 0) gameState.setGameState(GameState.PLAY); // Continue // Use static constant
            else if (ui.commandNum == 1) { /* Exit Game - Implement this */ System.exit(0); } // Placeholder for exit
        }
    }
    public void navigateInventoryUI(String direction) {
        if (gameState.getGameState() == GameState.INVENTORY && gamePanel != null && gamePanel.getGameStateUI() != null) { // Use static constant
            GameStateUI ui = gamePanel.getGameStateUI();
            int slotsPerRow = 12; // Should be in GameStateUI or a config
            int totalDisplayRows = 3; // Should be in GameStateUI or a config
            switch (direction) {
                case "up": ui.slotRow--; if (ui.slotRow < 0) ui.slotRow = totalDisplayRows - 1; break;
                case "down": ui.slotRow++; if (ui.slotRow >= totalDisplayRows) ui.slotRow = 0; break;
                case "left": ui.slotCol--; if (ui.slotCol < 0) ui.slotCol = slotsPerRow - 1; break;
                case "right": ui.slotCol++; if (ui.slotCol >= slotsPerRow) ui.slotCol = 0; break;
            }
        }
    }
    public void confirmInventoryUISelection() {
        if (gameState.getGameState() == GameState.INVENTORY && farm != null && farm.getPlayerModel() != null && gamePanel != null && gamePanel.getGameStateUI() != null) { // Use static constant
            GameStateUI ui = gamePanel.getGameStateUI();
            Player playerModel = farm.getPlayerModel();
            int slotIndex = ui.slotCol + (ui.slotRow * 12); // 12 is slotsPerRow
            ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(playerModel.getInventory().getInventory().entrySet());
            if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
                Items selectedItem = inventoryList.get(slotIndex).getKey();
                playerModel.setCurrentHeldItem(selectedItem);
            } else {
                playerModel.setCurrentHeldItem(null);
            }
            gameState.setGameState(GameState.PLAY); // Use static constant
            resetMovementState();
        }
    }
    public void selectHotbarItem(int slotIndex) { // Assuming 0-indexed hotbar
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

    public void executePlayerAction(CookingAction action) {
        if (action != null && farm != null) {
            action.execute(farm);
        }
    }

    private void resetMovementState() {
        movementState.put("up", false); movementState.put("down", false);
        movementState.put("left", false); movementState.put("right", false);
    }
    public void teleportPlayer(int mapIndex, int worldX, int worldY) {
        int musicIdx = 0; // Default music, consider map-specific music
        teleportPlayer(mapIndex, worldX, worldY, musicIdx);
    }
    public void teleportPlayer(int mapIndex, int worldX, int worldY, int musicIndex) {
        if (farm != null && playerViewInstance != null && gamePanel != null && tileManager != null && aSetter != null) {
            stopMusic(); // Stop current music
            farm.setCurrentMap(mapIndex);
            playerViewInstance.worldX = worldX;
            playerViewInstance.worldY = worldY;
            playerViewInstance.direction = "down"; // Default direction after teleport
            tileManager.loadMap(farm.getMapPathFor(mapIndex), mapIndex); // Reload map for the new area
            aSetter.setInteractableObject(); // Reset objects for the new map
            // playMusic(musicIndex); // Play new music for the map - if Sound class supports playing specific index
            playMusic(); // Or play default music
        }
    }

    public void passedOutSleep() {
        if (farm == null || farm.getPlayerModel() == null || farm.getGameClock() == null || gameStateUI == null || tileManager == null || aSetter == null || playerViewInstance == null) {
            System.err.println("Error during passedOutSleep: Critical component is null.");
            return;
        }

        gameState.setGameState(GameState.PAUSE); // Pause game during transition // Use static constant

        Player playerModel = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();
        int tileSize = getTileSize();

        // UI Message for passing out
        String passOutMessage = playerModel.isForceSleepByTime() ? "It's 2 AM! You collapse from exhaustion." : "You fainted! Someone brought you home.";
        gameStateUI.setDialogue(passOutMessage); // Show message, setDialogue usually persists longer

        gameClock.nextDay(farm.getPlayerStats());
        playerModel.setEnergy(playerModel.getMaxEnergy() / 2); // Restore partial energy
        playerModel.setCurrentHeldItem(null); // Player might drop items or they are put away

        // Teleport to home map (assuming map 4 is home)
        int homeMapIndex = 4;
        if (farm.getCurrentMap() != homeMapIndex) {
            farm.setCurrentMap(homeMapIndex);
            tileManager.loadMap(farm.getMapPathFor(homeMapIndex), homeMapIndex);
            aSetter.setInteractableObject();
        }

        playerViewInstance.worldX = 7 * tileSize; // Default home X
        playerViewInstance.worldY = 10 * tileSize; // Default home Y
        playerViewInstance.direction = "down";

        // System.out.println("You wake up the next morning. Your energy is partially restored."); // Use UI for this
        // System.out.println("A new day has begun: Day " + gameClock.getDay());
        gameStateUI.showTemporaryMessage("A new day has begun: Day " + gameClock.getDay() + ". Energy partially restored.");


        if (playerModel.isPassedOut()) {
            playerModel.setPassedOut(false);
        }
        if (playerModel.isForceSleepByTime()) {
            playerModel.setForceSleepByTime(false);
        }

        // Short delay before returning to play state to allow player to read message (if using modal dialogs)
        // For simple temporary messages, direct transition is fine.
        gameState.setGameState(GameState.PLAY); // Use static constant
    }


    public void activateSetTimeTo2AMCheat() {
        if (farm == null || farm.getGameClock() == null || timeManager == null) {
            // System.out.println("CHEAT FAILED: Cannot set time to 2 AM."); // For debugging
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("Cheat failed: Components missing.");
            return;
        }

        GameClock gameClock = farm.getGameClock();
        gameClock.setCurrentTime(java.time.LocalTime.of(1, 58)); // Set close to 2 AM to test pass out
        this.timeManager.notifyObservers(); // Remove extra semicolon
        if (gameStateUI != null) gameStateUI.showTemporaryMessage("Cheat activated: Time set near 2 AM.");
    }

    public Farm getFarmModel() { return this.farm; }
    public GameState getGameState() { return this.gameState; }
    public PlayerView getPlayerViewInstance() { return this.playerViewInstance; }
    public int getTileSize() { return gamePanel != null ? gamePanel.tileSize : 32; } // Default if gamePanel is null
    public int getMaxWorldCol() { return gamePanel != null ? gamePanel.maxWorldCol : 32; }
    public int getMaxWorldRow() { return gamePanel != null ? gamePanel.maxWorldRow : 32; }
    public TileManager getTileManager() { return this.tileManager; }
    public GameStateUI getGameStateUI() {
        return gamePanel != null ? gamePanel.getGameStateUI() : null; // Use getter
    }
    public Farm getFarm() { // Redundant with getFarmModel(), consider removing one
        return this.farm;
    }
}