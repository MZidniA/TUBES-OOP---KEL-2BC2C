package org.example.model.NPC;
import org.example.model.enums.RelationshipStats;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;

public class CarolineNPC extends NPC{    
    public CarolineNPC() {
        super("Caroline", LocationType.RUMAH_CAROLINE, 
              ItemDatabase.itemList(new String[] {"Firewood", "Coal"}), 
              ItemDatabase.itemList(new String[] {"Potato", "Wheat"}), 
              ItemDatabase.itemList(new String[] {"Hot Pepper"}), 
              0, RelationshipStats.SINGLE);
    }
}
