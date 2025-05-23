package org.example.model.NPC;


import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats; 
import org.example.model.Items.ItemDatabase;

public class AbigailNPC extends NPC {
    public AbigailNPC() {
       super("Abigail", LocationType.RUMAH_ABIGAIL, 
             ItemDatabase.itemList(new String[] {"Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry"}), 
             ItemDatabase.itemList(new String[] {"Baguette", "Pumpkin Pie", "Wine"}), 
             ItemDatabase.itemList(new String[] {"Hot Pepper", "Cauliflower", "Parsnip", "Wheat"}), 
             0, RelationshipStats.SINGLE);
    }
}