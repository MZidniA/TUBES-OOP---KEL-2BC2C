package org.example.model.Map;

import java.util.List;

import org.example.controller.action.Action;
import org.example.model.Player;
import org.example.model.enums.LocationType;

public interface Location {
    String getLocation();
    List<Action> getActions(Player player);
    LocationType getLocationType();
}
