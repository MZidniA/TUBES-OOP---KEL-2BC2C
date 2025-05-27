package org.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements KeyListener {
    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, escapePressed, enterPressed, inventoryPressed, 
            num1Pressed, num2Pressed, num3Pressed, num4Pressed, num5Pressed, num6Pressed, num7Pressed, num8Pressed, num9Pressed, num0Pressed,
            minusPressed, equalsPressed;
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gp.gameState.getGameState() == gp.gameState.play) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_F) interactPressed = true;
            if (code == KeyEvent.VK_ESCAPE) gp.gameState.setGameState(gp.gameState.pause);
            if (code == KeyEvent.VK_I) gp.gameState.setGameState(gp.gameState.inventory);

            if (code == KeyEvent.VK_1) num1Pressed = true;
            if (code == KeyEvent.VK_2) num2Pressed = true;
            if (code == KeyEvent.VK_3) num3Pressed = true;
            if (code == KeyEvent.VK_4) num4Pressed = true;
            if (code == KeyEvent.VK_5) num5Pressed = true;
            if (code == KeyEvent.VK_6) num6Pressed = true;
            if (code == KeyEvent.VK_7) num7Pressed = true;
            if (code == KeyEvent.VK_8) num8Pressed = true;
            if (code == KeyEvent.VK_9) num9Pressed = true;
            if (code == KeyEvent.VK_0) num0Pressed = true;
            if (code == KeyEvent.VK_MINUS) minusPressed = true;
            if (code == KeyEvent.VK_EQUALS) equalsPressed = true;

        } else if (gp.gameState.getGameState() == gp.gameState.pause) {
            if (code == KeyEvent.VK_ESCAPE) gp.gameState.setGameState(gp.gameState.play);
            // Tambahkan navigasi menu pause jika ada
        } else if (gp.gameState.getGameState() == gp.gameState.inventory) {
            if (code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) {
                gp.gameState.setGameState(gp.gameState.play);
            }
            if (code == KeyEvent.VK_W) gp.gameStateUI.slotRow--;
            if (code == KeyEvent.VK_A) gp.gameStateUI.slotCol--;
            if (code == KeyEvent.VK_S) gp.gameStateUI.slotRow++;
            if (code == KeyEvent.VK_D) gp.gameStateUI.slotCol++;

            int slotsPerRow = 12;
            int totalDisplayRows = 3;
            if (gp.gameStateUI.slotRow < 0) gp.gameStateUI.slotRow = totalDisplayRows - 1;
            if (gp.gameStateUI.slotRow >= totalDisplayRows) gp.gameStateUI.slotRow = 0;
            if (gp.gameStateUI.slotCol < 0) gp.gameStateUI.slotCol = slotsPerRow - 1;
            if (gp.gameStateUI.slotCol >= slotsPerRow) gp.gameStateUI.slotCol = 0;
            
            if (code == KeyEvent.VK_ENTER) {
                gp.playerController.selectItemFromInventory();
                gp.gameState.setGameState(gp.gameState.play);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        // interactPressed di-reset di PlayerController setelah aksi
        // Flag tombol angka juga di-reset di PlayerController
    }
    
    public void resetHotbarKeys(){
        num1Pressed = false; num2Pressed = false; num3Pressed = false;
        num4Pressed = false; num5Pressed = false; num6Pressed = false;
        num7Pressed = false; num8Pressed = false; num9Pressed = false;
        num0Pressed = false; minusPressed = false; equalsPressed = false;
    }
}

