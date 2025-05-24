package org.example.model.Map;

import org.example.model.enums.LocationType;
import org.example.controller.action.Action;
import org.example.model.Player;
import java.util.List;

public interface Location {
    String getLocation();
    List<Action> getActions(Player player);
    LocationType getLocationType();
}
