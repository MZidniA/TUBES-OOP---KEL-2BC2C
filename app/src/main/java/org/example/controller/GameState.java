package org.example.controller;

public class GameState {
    public int gamestate;
    public final int play = 1;
    public final int pause = 2;
    public final int inventory = 3;
<<<<<<< Updated upstream
    public final int cooking_menu = 4;
=======
    public final int cooking_menu = 4; 
>>>>>>> Stashed changes

    public GameState() {
        this.gamestate = this.play; 
    }

    public void setGameState(int state) {
        this.gamestate = state;
    }

    public int getGameState() {
        return this.gamestate;
    }
}