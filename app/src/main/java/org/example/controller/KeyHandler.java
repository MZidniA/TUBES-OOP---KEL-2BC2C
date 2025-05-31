package org.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalTime;



public class KeyHandler implements KeyListener {
    private final GameController gameController;
    private  CheatManager cheatManager;

    public KeyHandler(GameController gameController) {
        this.gameController = gameController;
        this.cheatManager = this.gameController.getCheatManager();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (gameController == null || gameController.getGameState() == null) return;
        GameState currentGameState = gameController.getGameState();

        if (currentGameState.getGameState() == currentGameState.play) { //
            if (cheatManager != null && e.isControlDown()) {
                boolean cheatActivated = false;
                switch (code) {
                    case KeyEvent.VK_1: 
                        cheatManager.cheat_setGoldToWinningAmount();
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_2:
                        cheatManager.cheat_addGold5K();
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_3:
                        cheatManager.cheat_marryNpc("Abigail"); 
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_4: 
                        cheatManager.cheat_cycleWeather();
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_5:
                        cheatManager.cheat_setNextSeason();
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_6:
                        cheatManager.cheat_setTime(LocalTime.NOON); 
                        cheatActivated = true;
                        break;
                    case KeyEvent.VK_7:
                        cheatManager.activateSetTimeTo2AMCheat();
                        cheatActivated = true;
                        break; 
                    case KeyEvent.VK_8:
                        cheatManager.cheat_addRecipeBundleStarterPack(); 
                        cheatActivated = true;
                        break;               
                }

                if (cheatActivated) {
                    e.consume(); 
                    return;     
                }
            }
            if (!e.isConsumed()) {
                 switch (code) {
                    case KeyEvent.VK_W: case KeyEvent.VK_UP: gameController.handlePlayerMove("up", true); break;
                    case KeyEvent.VK_S: case KeyEvent.VK_DOWN: gameController.handlePlayerMove("down", true); break;
                    case KeyEvent.VK_A: case KeyEvent.VK_LEFT: gameController.handlePlayerMove("left", true); break;
                    case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: gameController.handlePlayerMove("right", true); break;
                    case KeyEvent.VK_E: gameController.handleEatAction(); break;
                    case KeyEvent.VK_F: gameController.handleInteraction(); break;
                    case KeyEvent.VK_ESCAPE: gameController.togglePause(); break;
                    case KeyEvent.VK_I: gameController.toggleInventory(); break;
                    case KeyEvent.VK_G: gameController.showPlayerInfo(); break;
                 }
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
        } else if (currentGameState.getGameState() == currentGameState.shipping_bin) { // << TAMBAHKAN BLOK INI
            if (code == KeyEvent.VK_ESCAPE) {
                gameController.closeShippingBinMenu();
            } else if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gameController.navigateShippingBinUI("up");
            } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gameController.navigateShippingBinUI("down");
            } else if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                gameController.navigateShippingBinUI("left");
            } else if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                gameController.navigateShippingBinUI("right");
            } else if (code == KeyEvent.VK_ENTER) {
                gameController.confirmShipItem();
            }
        } else if (currentGameState.getGameState() == currentGameState.day_report) {
            if (code == KeyEvent.VK_ENTER) {
                System.out.println("KeyHandler: ENTER pressed in DAY_REPORT state."); // DEBUG
                gameController.handleEndOfDayReportDismissal();
            }
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
        } else if (currentGameState.getGameState() == currentGameState.end_game_stats) { // Menggunakan konstanta dari instance
            if (code == KeyEvent.VK_ENTER) {
                System.out.println("KeyHandler: ENTER pressed in END_GAME_STATS state."); // DEBUG
                gameController.dismissEndGameStatisticsScreen();
            }
      // Tidak ada input lain yang dihandle di state ini selain Enter untuk melanjutkan
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