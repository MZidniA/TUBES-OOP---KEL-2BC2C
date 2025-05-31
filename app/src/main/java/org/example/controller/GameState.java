package org.example.controller;

public class GameState {
    public int gamestate;
    public final int play = 1;
    public final int pause = 2;
    public final int inventory = 3;
    public final int cooking_menu = 4;
    public final int shipping_bin = 5;
    public final int day_report = 6;
    public final int end_game_stats = 7;

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
