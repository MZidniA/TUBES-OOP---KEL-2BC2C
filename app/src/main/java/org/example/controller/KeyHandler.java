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
        GameState currentGameState = gameController.getGameState();

        if (currentGameState.getGameState() == currentGameState.play) {
            switch (code) {
                case KeyEvent.VK_W: gameController.handlePlayerMove("up", true); break;
                case KeyEvent.VK_S: gameController.handlePlayerMove("down", true); break;
                case KeyEvent.VK_A: gameController.handlePlayerMove("left", true); break;
                case KeyEvent.VK_D: gameController.handlePlayerMove("right", true); break;
                case KeyEvent.VK_F: gameController.handleInteraction(); break;
                case KeyEvent.VK_ESCAPE: gameController.togglePause(); break;
                case KeyEvent.VK_I: gameController.toggleInventory(); break;
            }
        } else if (currentGameState.getGameState() == currentGameState.pause) {
            if (code == KeyEvent.VK_ESCAPE) {
                gameController.togglePause();
            } else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gameController.navigatePauseUI("up");
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gameController.navigatePauseUI("down");
            } else if (code == KeyEvent.VK_ENTER) {
                gameController.confirmPauseUISelection();
            }
        } else if (currentGameState.getGameState() == currentGameState.inventory) {
<<<<<<< Updated upstream
            if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_I) {
                gameController.toggleInventory();
            } else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gameController.navigateInventoryUI("up");
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gameController.navigateInventoryUI("down");
            } else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gameController.navigateInventoryUI("left");
            } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gameController.navigateInventoryUI("right");
            } else if (code == KeyEvent.VK_ENTER) {
                gameController.confirmInventoryUISelection();
            }
=======
            if (code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_I) gameController.toggleInventory();
            else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) gameController.navigateInventoryUI("up");
            else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) gameController.navigateInventoryUI("down");
            else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) gameController.navigateInventoryUI("left");
            else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) gameController.navigateInventoryUI("right");
            else if (code == KeyEvent.VK_ENTER) gameController.confirmInventoryUISelection();
        } else if (currentGameState.getGameState() == currentGameState.COOKING_MENU){
            handleCookingMenuInput(code);
>>>>>>> Stashed changes
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        switch (code) {
            case KeyEvent.VK_W: gameController.handlePlayerMove("up", false); break;
            case KeyEvent.VK_S: gameController.handlePlayerMove("down", false); break;
            case KeyEvent.VK_A: gameController.handlePlayerMove("left", false); break;
            case KeyEvent.VK_D: gameController.handlePlayerMove("right", false); break;
        }
    }
}