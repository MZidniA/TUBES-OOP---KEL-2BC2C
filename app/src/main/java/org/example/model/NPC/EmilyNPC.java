package org.example.model.NPC;
import java.util.ArrayList;

import org.example.model.Location;
import org.example.model.enums.RelationshipStats;

public class EmilyNPC extends NPC {
    public EmilyNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Emily", location,
              new ArrayList<String>() {{ add("All Seeds"); }}, 
              new ArrayList<String>() {{ add("Catfish"); add("Salmon"); add("Sardine"); }}, 
              new ArrayList<String>() {{ add("Coal"); add("Wood"); }}, 
              0, relationshipsStatus);
    }
}