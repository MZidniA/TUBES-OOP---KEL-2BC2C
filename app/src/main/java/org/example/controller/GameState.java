package org.example.controller;

public class GameState {
    
    public int gamestate;
    public final int play = 1;
    public final int pause = 2;

    public GameState() {
        this.gamestate = play; 
    }

    public void setGameState(int state) {
        this.gamestate = state;
    }

    public int getGameState() {
        return this.gamestate;
    }
}
