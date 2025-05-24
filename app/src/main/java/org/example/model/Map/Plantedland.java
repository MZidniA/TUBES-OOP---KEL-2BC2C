package org.example.model.Map;

import java.util.List;
import java.util.ArrayList;
import org.example.model.Player;
import org.example.controller.action.Action;

public class Plantedland extends Tile {
    public Plantedland(int x, int y) {
        super(x, y, true, 'l'); 
    }

    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the Plantedland tile
        return new ArrayList<>();
    }
}