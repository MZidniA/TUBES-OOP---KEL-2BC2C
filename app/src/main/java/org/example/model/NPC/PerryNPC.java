package org.example.model.NPC;
import java.util.ArrayList;

import org.example.model.Location;
import org.example.model.enums.RelationshipStats;

public class PerryNPC extends NPC{    
    public PerryNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Perry", location, 
              new ArrayList<String>() {{ add("Cranberry"); add("Blueberry");}}, 
              new ArrayList<String>() {{ add("Wine"); add("Wheat"); }},
              new ArrayList<String>() {{ add("Fish"); }}, 
              0, relationshipsStatus);
    }
}
