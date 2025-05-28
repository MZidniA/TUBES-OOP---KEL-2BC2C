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
                case KeyEvent.VK_1: gameController.selectHotbarItem(0); break;
                case KeyEvent.VK_2: gameController.selectHotbarItem(1); break;
                case KeyEvent.VK_3: gameController.selectHotbarItem(2); break;
                case KeyEvent.VK_4: gameController.selectHotbarItem(3); break;
                case KeyEvent.VK_5: gameController.selectHotbarItem(4); break;
                case KeyEvent.VK_6: gameController.selectHotbarItem(5); break;
                case KeyEvent.VK_7: gameController.selectHotbarItem(6); break;
                case KeyEvent.VK_8: gameController.selectHotbarItem(7); break;
                case KeyEvent.VK_9: gameController.selectHotbarItem(8); break;
                case KeyEvent.VK_0: gameController.selectHotbarItem(9); break;
                case KeyEvent.VK_MINUS: gameController.selectHotbarItem(10); break;
                case KeyEvent.VK_EQUALS: gameController.selectHotbarItem(11); break;
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