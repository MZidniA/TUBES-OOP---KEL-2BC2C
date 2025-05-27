package org.example.model.Items;

import java.util.HashMap;
import java.util.Map;

public class EquipmentFactory {
    public static Map<String, Equipment> createEquipment() {
        Map<String, Equipment> equipment = new HashMap<>();
        
        equipment.put("Hoe", new Equipment("Hoe", 50, 100, 100)); 
        equipment.put("Watering Can", new Equipment("Watering Can", 40, 80, 150));
        equipment.put("Pickaxe", new Equipment("Pickaxe", 70, 120, 200));
        equipment.put("Fishing Rod", new Equipment("Fishing Rod", 60, 110, 180));

        return equipment;
    }
}