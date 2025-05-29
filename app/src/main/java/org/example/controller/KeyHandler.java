package org.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.example.model.Farm;
// import org.example.model.Player; // Not directly used here, accessed via Farm
import org.example.model.Items.Items;
import org.example.model.Recipe;
import org.example.view.GameStateUI;
import org.example.controller.action.CookingAction;

public class KeyHandler implements KeyListener {
    private final GameController gameController;

    public KeyHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not typically used in game loops for discrete actions
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameController == null || gameController.getGameState() == null) return;

        GameState currentGameStateManager = gameController.getGameState(); // The manager object
        int currentState = currentGameStateManager.getGameState(); // The actual state value

        if (currentState == GameState.PLAY) { // Use static constant
            handlePlayStateInput(code);
        } else if (currentState == GameState.PAUSE) { // Use static constant
            handlePauseStateInput(code);
        } else if (currentState == GameState.INVENTORY) { // Use static constant
            handleInventoryStateInput(code);
        } else if (currentState == GameState.COOKING_MENU) { // Use static constant
            handleCookingMenuInput(code);
        }
    }

    private void handlePlayStateInput(int code) {
        switch (code) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.handlePlayerMove("up", true); break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.handlePlayerMove("down", true); break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.handlePlayerMove("left", true); break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.handlePlayerMove("right", true); break;
            case KeyEvent.VK_F: gameController.handleInteraction(); break;
            case KeyEvent.VK_ESCAPE: gameController.togglePause(); break;
            case KeyEvent.VK_I: gameController.toggleInventory(); break;
            case KeyEvent.VK_P: gameController.activateSetTimeTo2AMCheat(); break; // Example key for cheat
            // Hotbar keys 1-9 (VK_1 to VK_9)
            case KeyEvent.VK_1: gameController.selectHotbarItem(0); break;
            case KeyEvent.VK_2: gameController.selectHotbarItem(1); break;
            case KeyEvent.VK_3: gameController.selectHotbarItem(2); break;
            // Add more for 4-9 if needed
        }
    }

    private void handlePauseStateInput(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE: gameController.togglePause(); break;
            case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.navigatePauseUI("up"); break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.navigatePauseUI("down"); break;
            case KeyEvent.VK_ENTER: gameController.confirmPauseUISelection(); break;
        }
    }

    private void handleInventoryStateInput(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE: case KeyEvent.VK_I: gameController.toggleInventory(); break;
            case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.navigateInventoryUI("up"); break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.navigateInventoryUI("down"); break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.navigateInventoryUI("left"); break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.navigateInventoryUI("right"); break;
            case KeyEvent.VK_ENTER: gameController.confirmInventoryUISelection(); break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameController == null || gameController.getGameState() == null) return;

        GameState currentGameStateManager = gameController.getGameState();
        if (currentGameStateManager.getGameState() == GameState.PLAY) { // Use static constant
            switch (code) {
                case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.handlePlayerMove("up", false); break;
                case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.handlePlayerMove("down", false); break;
                case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.handlePlayerMove("left", false); break;
                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.handlePlayerMove("right", false); break;
            }
        }
    }

    private void handleCookingMenuInput(int code) {
        GameStateUI ui = gameController.getGameStateUI();
        if (ui == null) {
            System.err.println("KeyHandler.handleCookingMenuInput: GameStateUI is null.");
            return;
        }

        List<Recipe> recipes = ui.getAvailableRecipesForUI(); // May be null if UI hasn't populated it
        List<Items> fuels = ui.getAvailableFuelsForUI();     // May be null or empty

        switch (code) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP: // Navigate recipes up
                ui.setSelectedRecipeIndex(ui.getSelectedRecipeIndex() - 1);
                break;

            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: // Navigate recipes down
                ui.setSelectedRecipeIndex(ui.getSelectedRecipeIndex() + 1);
                break;

            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: // Navigate fuels left OR switch from Cancel to Cook
                if (ui.getCookingMenuCommandNum() == 1) { // If "Cancel" is selected
                    ui.setCookingMenuCommandNum(0);       // Switch to "Cook"
                } else { // If "Cook" is selected or focus is implicitly on fuels
                    ui.setSelectedFuelIndex(ui.getSelectedFuelIndex() - 1);
                }
                break;

            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: // Navigate fuels right OR switch from Cook to Cancel
                 if (ui.getCookingMenuCommandNum() == 0) { // If "Cook" is selected
                    ui.setCookingMenuCommandNum(1);       // Switch to "Cancel"
                } else { // If "Cancel" is selected or focus is implicitly on fuels
                    ui.setSelectedFuelIndex(ui.getSelectedFuelIndex() + 1);
                }
                break;

            case KeyEvent.VK_ENTER: case KeyEvent.VK_F: // Confirm selection or action
                if (ui.getCookingMenuCommandNum() == 0) { // "COOK" action
                    if (recipes != null && !recipes.isEmpty() &&
                        ui.getSelectedRecipeIndex() >= 0 && ui.getSelectedRecipeIndex() < recipes.size() &&
                        fuels != null && !fuels.isEmpty() &&
                        ui.getSelectedFuelIndex() >= 0 && ui.getSelectedFuelIndex() < fuels.size()) {

                        Recipe selectedRecipe = recipes.get(ui.getSelectedRecipeIndex());
                        Items selectedFuel = fuels.get(ui.getSelectedFuelIndex());
                        Farm farm = gameController.getFarmModel();

                        if (farm == null || farm.getPlayerModel() == null) {
                            System.err.println("KeyHandler.handleCookingMenuInput: Farm or Player model is null.");
                            ui.showTemporaryMessage("Error: Game data missing.");
                            return;
                        }

                        CookingAction cookingAction = new CookingAction(selectedRecipe, selectedFuel);

                        if (cookingAction.canExecute(farm)) {
                            gameController.executePlayerAction(cookingAction);
                            // Message for starting cook should come from CookingAction.execute() or be more generic
                            ui.showTemporaryMessage("Started cooking: " + selectedRecipe.getDisplayName());
                            // Transition back to play state is handled by the action or a delay if needed
                            gameController.getGameState().setGameState(GameState.PLAY); // Use static constant
                            ui.resetCookingMenuState(); // Reset menu after successful cook
                        } else {
                            // Specific error message should ideally be determined by canExecute()
                            ui.showTemporaryMessage("Cannot cook: Check ingredients, fuel, energy, or if stove is busy.");
                            // System.out.println("LOG (KeyHandler): Cannot execute cooking action for " + selectedRecipe.getDisplayName()); // For debugging
                        }
                    } else {
                        ui.showTemporaryMessage("Please select a valid recipe and fuel.");
                        // System.out.println("LOG (KeyHandler): Invalid selection for cooking (recipe/fuel list empty or index out of bounds)."); // For debugging
                    }
                } else if (ui.getCookingMenuCommandNum() == 1) { // "CANCEL" action
                    gameController.getGameState().setGameState(GameState.PLAY); // Use static constant
                    ui.resetCookingMenuState();
                }
                break;

            case KeyEvent.VK_ESCAPE: // Exit cooking menu
                gameController.getGameState().setGameState(GameState.PLAY); // Use static constant
                ui.resetCookingMenuState();
                break;
        }
    }
}