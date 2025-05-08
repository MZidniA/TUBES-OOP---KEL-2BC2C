package org.example.model;
import java.util.ArrayList;

public class AbigailNPC extends NPC {
    public AbigailNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Abigail", location,
              new ArrayList<String>() {{ add("Blueberry"); add("Melon"); add("Pumpkin"); add("Grape"); add("Cranberry"); }}, 
              new ArrayList<String>() {{ add("Baguette"); add("Pumpkin Pie"); add("Wine"); }}, 
              new ArrayList<String>() {{ add("Hot Pepper"); add("Cauliflower"); add("Parsnip"); add("Wheat"); }}, 
              0, relationshipsStatus);
    }
}