package org.example.model.Map;
import java.util.ArrayList;
import java.util.List;

import org.example.controller.action.Action;
import org.example.model.Player;

public class Tillableland extends Tile {
    public Tillableland(int x, int y) {
        super(x, y, true, '.'); 
    }
    @Override
    public List<Action> getActions(Player player) {
        return new ArrayList<>();
    }
    public void interact() {
        System.out.println("Interacting with Tillableland at coordinates (" + getX() + ", " + getY() + ")");
    }
}