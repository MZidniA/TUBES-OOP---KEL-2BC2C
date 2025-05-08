package org.example.model;
import java.util.ArrayList;

public class EmilyNPC extends NPC {
    public EmilyNPC(Location location, int heartPoints, RelationshipStats relationshipsStatus) {
        super("Emily", location,
              new ArrayList<String>() {{ add("All Seeds"); }}, 
              new ArrayList<String>() {{ add("Catfish"); add("Salmon"); add("Sardine"); }}, 
              new ArrayList<String>() {{ add("Coal"); add("Wood"); }}, 
              0, relationshipsStatus);
    }
}