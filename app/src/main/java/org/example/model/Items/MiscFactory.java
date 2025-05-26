package org.example.model.Items;

import java.util.HashMap;
import java.util.Map;


public class MiscFactory {
    public static Map<String, Items> createMisc() {
        Map<String, Items> misc = new HashMap<>();

        misc.put("Firewood", new Misc("Firewood", 50, 100));
        misc.put("Coal", new Misc("Coal", 100, 200));
        misc.put("Proposal Ring", new Misc("Proposal Ring", 0, 3000));
        misc.put("Wood", new Misc("Wood", 0, 100));
    
        return misc;
    }
}