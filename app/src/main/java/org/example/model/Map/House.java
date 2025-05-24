package org.example.model.Map;

import org.example.model.Player;
import org.example.controller.action.Action;
import java.util.ArrayList;
import java.util.List;

public class House extends Tile {
    public House(int x, int y) {
        super(x, y, false, 'h'); 
    }

    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the House tile
        return new ArrayList<>();
    }
}