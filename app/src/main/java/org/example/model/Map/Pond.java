package org.example.model.Map;
import org.example.model.Player;
import org.example.controller.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Pond extends Tile {
    public Pond(int x, int y) {
        super(x, y, false, 'o'); 
    }
    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the Pond tile
        return new ArrayList<>();
    }
}