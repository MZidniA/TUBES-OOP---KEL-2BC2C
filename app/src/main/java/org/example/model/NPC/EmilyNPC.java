package org.example.model.NPC;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;

public class EmilyNPC extends NPC {
    public EmilyNPC() {
        super("Emily", LocationType.STORE, 
              ItemDatabase.getItemsByCategory("Seeds"),
              ItemDatabase.itemList(new String[] {"Catfish", "Salmon", "Sardine"}), 
              ItemDatabase.itemList(new String[] {"Coal", "Wood"}), 
              0, RelationshipStats.SINGLE);
    }
}
