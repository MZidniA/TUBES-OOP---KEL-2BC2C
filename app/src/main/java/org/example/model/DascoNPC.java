package org.example.model;
import java.util.ArrayList;

public class DascoNPC extends NPC {
    public DascoNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Dasco", location,
              new ArrayList<String>() {{ add("The Legends of Spakbor"); add("Cooked Pig's Head"); add("Wine"); add("Fugu"); add("Spakbor Salad"); }}, 
              new ArrayList<String>() {{ add("Fish Sandwich"); add("Fish Stew"); add("Baguette"); add("Fish n’ Chips"); }}, 
              new ArrayList<String>() {{ add("Legend"); add("Grape"); add("Cauliflower"); add("Wheat"); add("Pufferfish"); add("Salmon"); }}, 
              0, relationshipsStatus);
    }
}