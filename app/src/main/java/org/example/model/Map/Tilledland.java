package org.example.model.Map;
import org.example.model.Player;
import org.example.controller.action.Action;
import java.util.ArrayList;
import java.util.List;

public class Tilledland extends Tile {
    public Tilledland(int x, int y) {
        super(x, y, true, 't'); 
    }
    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the Tilledland tile
        return new ArrayList<>();
    }
}