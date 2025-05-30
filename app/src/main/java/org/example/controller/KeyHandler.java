package org.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private final GameController gameController;

    public KeyHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameController == null || gameController.getGameState() == null) return;
        GameState currentGameState = gameController.getGameState();

        if (currentGameState.getGameState() == currentGameState.play) {
            switch (code) {
                case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.handlePlayerMove("up", true); break;
                case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.handlePlayerMove("down", true); break;
                case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.handlePlayerMove("left", true); break;
                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.handlePlayerMove("right", true); break;
                case KeyEvent.VK_F: gameController.handleInteraction(); break;
                case KeyEvent.VK_ESCAPE: gameController.togglePause(); break;
                case KeyEvent.VK_I: gameController.toggleInventory(); break;
                case KeyEvent.VK_CONTROL: gameController.activateSetTimeTo2AMCheat();; break;
            }
        } else if (currentGameState.getGameState() == currentGameState.pause) {
            if (code == KeyEvent.VK_ESCAPE) gameController.togglePause();
            else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gameController.navigatePauseUI("up");
            else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gameController.navigatePauseUI("down");
            else if (code == KeyEvent.VK_ENTER) gameController.confirmPauseUISelection();
        } else if (currentGameState.getGameState() == currentGameState.inventory) {
            if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_I) gameController.toggleInventory();
            else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gameController.navigateInventoryUI("up");
            else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gameController.navigateInventoryUI("down");
            else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) gameController.navigateInventoryUI("left");
            else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) gameController.navigateInventoryUI("right");
            else if (code == KeyEvent.VK_ENTER) gameController.confirmInventoryUISelection();
<<<<<<< Updated upstream
=======
        } else if (currentGameState.getGameState() == currentGameState.cooking_menu) { 
            switch (code) {
                case KeyEvent.VK_W: case KeyEvent.VK_UP:
                    gameController.navigateCookingMenu("up_recipe"); // Lebih spesifik untuk navigasi resep
                    break;
                case KeyEvent.VK_S: case KeyEvent.VK_DOWN:
                    gameController.navigateCookingMenu("down_recipe"); // Lebih spesifik untuk navigasi resep
                    break;
                case KeyEvent.VK_A: case KeyEvent.VK_LEFT:
                    gameController.navigateCookingMenu("left_command"); // Untuk pindah ke tombol kiri (misal, Cook dari Cancel)
                    break;
                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT:
                    gameController.navigateCookingMenu("right_command"); // Untuk pindah ke tombol kanan (misal, Cancel dari Cook)
                    break;
                case KeyEvent.VK_ENTER:
                    System.out.println("KeyHandler: ENTER pressed in COOKING_MENU state."); // DEBUG
                    gameController.confirmCookingMenuSelection();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.out.println("KeyHandler: ESCAPE pressed in COOKING_MENU state."); // DEBUG
                    gameController.exitCookingMenu();
                    break;
            }
        } else if (currentGameState.getGameState() == currentGameState.map_selection) { // <-- BLOK BARU
            switch (code) {
                case KeyEvent.VK_W: case KeyEvent.VK_UP:
                    gameController.navigateMapSelectionMenu("up");
                    break;
                case KeyEvent.VK_S: case KeyEvent.VK_DOWN:
                    gameController.navigateMapSelectionMenu("down");
                    break;
                case KeyEvent.VK_ENTER:
                    System.out.println("KeyHandler: ENTER pressed in MAP_SELECTION_MENU state."); // DEBUG
                    gameController.confirmMapSelection();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.out.println("KeyHandler: ESCAPE pressed in MAP_SELECTION_MENU state."); // DEBUG
                    gameController.exitMapSelectionMenu(); // Kembali ke play state tanpa teleport
                    break;
            }
>>>>>>> Stashed changes
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameController == null || gameController.getGameState() == null) return;
        GameState currentGameState = gameController.getGameState();
        if (currentGameState.getGameState() == currentGameState.play) {
            switch (code) {
                case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.handlePlayerMove("up", false); break;
                case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.handlePlayerMove("down", false); break;
                case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.handlePlayerMove("left", false); break;
                case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.handlePlayerMove("right", false); break;
            }
        }
    }
}