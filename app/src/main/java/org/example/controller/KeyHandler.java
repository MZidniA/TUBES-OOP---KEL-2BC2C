package org.example.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, interactPressed, escapePressed, enterPressed;
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        } 
        if (code == KeyEvent.VK_F) {
            interactPressed = true; 
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (gp.gameState.getGameState() == gp.gameState.pause) { 
            if (code == KeyEvent.VK_W ) {
                gp.gameStateUI.commandNum--;
                if (gp.gameStateUI.commandNum < 0) {
                    gp.gameStateUI.commandNum = 1; 
                }
            }
            if (code == KeyEvent.VK_S ) {
                gp.gameStateUI.commandNum++;
                if (gp.gameStateUI.commandNum > 1) {
                    gp.gameStateUI.commandNum = 0;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            interactPressed = false; 
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
    }
}

