package org.example.model;
import java.util.ArrayList;

public class CarolineNPC extends NPC{    
    public CarolineNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Caroline", location, 
              new ArrayList<String>() {{ add("Firewoord"); add("Coal");}}, 
              new ArrayList<String>() {{ add("Potato"); add("Wheat"); }}, 
              new ArrayList<String>() {{ add("Hot Pepper"); }}, 
              0, relationshipsStatus);
    }
}
