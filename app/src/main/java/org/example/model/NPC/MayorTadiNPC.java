package org.example.model.NPC;
import java.util.ArrayList;

import org.example.model.Location;
import org.example.model.enums.RelationshipStats;

public class MayorTadiNPC extends NPC{    
    public MayorTadiNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Mayor Tadi", location, 
              new ArrayList<String>() {{ add("Legend"); }}, 
              new ArrayList<String>() {{ add("Angler"); add("Crimsonfish"); add("Glacierfish"); }}, 
              new ArrayList<String>() {{ add("Any item that is not Legend, Angler, Crimsonfish, or Glacierfish"); }}, 
              0, relationshipsStatus);
    }
}
