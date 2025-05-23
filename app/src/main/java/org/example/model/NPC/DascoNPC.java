package org.example.model.NPC;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;

public class DascoNPC extends NPC {
    public DascoNPC() {
        super("Dasco", LocationType.RUMAH_DASCO, 
              ItemDatabase.itemList(new String[] {"The Legends of Spakbor", "Cooked Pig’s Head", "Wine", "Fugu", "Spakbor Salad"}), 
              ItemDatabase.itemList(new String[] {"Fish Sandwich", "Fish Stew", "Baguette", "Fish n’ Chips"}), 
              ItemDatabase.itemList(new String[] {"Legend","Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon" }), 
              0, RelationshipStats.SINGLE);
    }
}