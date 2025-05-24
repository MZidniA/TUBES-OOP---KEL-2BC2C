package org.example.model.Map;
import java.util.List;
import org.example.model.Player;
import org.example.controller.action.Action;

public abstract class Tile {
    private int x; 
    private int y; 
    private boolean walkable; 
    private char symbol; 

    public Tile(int x, int y, boolean walkable, char symbol) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
        this.symbol = symbol;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public abstract List<Action> getActions(Player player);

}