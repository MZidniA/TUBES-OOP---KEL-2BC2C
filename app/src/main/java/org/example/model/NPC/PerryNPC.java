package org.example.model.NPC;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;

public class PerryNPC extends NPC{    
    public PerryNPC() {
        super("Perry", LocationType.RUMAH_PERRY,
                ItemDatabase.itemList(new String[] {"Blueberry", "Cranberry"}),
                ItemDatabase.itemList(new String[] {"Wine"}),
                ItemDatabase.getItemsByCategory("Fish"),
                0, RelationshipStats.SINGLE);
        
    }
}
