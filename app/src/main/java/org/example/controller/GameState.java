// Lokasi: org.example.controller.GameState.java
package org.example.controller;

public class GameState {
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int INVENTORY = 3;
    public static final int COOKING_MENU = 4;


    private int currentGameState; 

    public GameState() {
        this.currentGameState = PLAY; 
    }

    public void setGameState(int state) {
        this.currentGameState = state;
    }

    public int getGameState() {
        return this.currentGameState;
    }
}