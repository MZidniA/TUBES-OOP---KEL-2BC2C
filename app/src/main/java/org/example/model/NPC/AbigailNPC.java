package org.example.model.NPC;
import java.util.ArrayList;

import org.example.model.Location;
import org.example.model.enums.RelationshipStats; 

public class AbigailNPC extends NPC {
    public AbigailNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Abigail", location, 
              new ArrayList<String>() {{ add("Pumpkin"); add("Chocolate Cake"); }}, 
              new ArrayList<String>() {{ add("Pufferfish"); add("Spicy Eel"); }}, 
              new ArrayList<String>() {{ add("Fish Taco"); }}, 
              0, relationshipsStatus);
    }
}