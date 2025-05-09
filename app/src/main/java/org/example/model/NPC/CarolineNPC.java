package org.example.model.NPC;
import java.util.ArrayList;
import org.example.model.enums.RelationshipStats; // Ensure this is the correct package for RelationshipStats

import org.example.model.Location;

public class CarolineNPC extends NPC{    
    public CarolineNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Caroline", location, 
              new ArrayList<String>() {{ add("Firewoord"); add("Coal");}}, 
              new ArrayList<String>() {{ add("Potato"); add("Wheat"); }}, 
              new ArrayList<String>() {{ add("Hot Pepper"); }}, 
              0, relationshipsStatus);
    }
}
