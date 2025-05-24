package org.example.model.Map;
import org.example.model.Player;
import org.example.controller.action.Action;
import java.util.ArrayList;
import java.util.List;

public class Tillableland extends Tile {
    public Tillableland(int x, int y) {
        super(x, y, true, '.'); 
    }
    @Override
    public List<Action> getActions(Player player) {
        // Implement the logic to return actions specific to the Tillableland tile
        return new ArrayList<>();
    }
}