package org.example.model.Map;
import org.example.model.Player;
import org.example.controller.action.Action;

import java.util.ArrayList;
import java.util.List;

public class ShippingBin extends Tile {
    public ShippingBin(int x, int y) {
        super(x, y, false, 's'); 
    }

    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the ShippingBin tile
        return new ArrayList<>();
    }
}