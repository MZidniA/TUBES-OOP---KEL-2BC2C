package org.example.model.Map;

import java.util.ArrayList;
import java.util.List;

import org.example.controller.action.Action;
import org.example.model.Player;

public class House extends Tile {
    public House(int x, int y) {
        super(x, y, false, 'h'); 
    }

    @Override
    public List<Action> getActions(Player player) {
        return new ArrayList<>();
    }
}