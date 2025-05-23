package org.example.model.Items;
import java.util.HashMap;
import java.util.Map;

public class FurnitureFactory {
    public static Map<String, Items> createFurniture() {
        Map<String, Items> furniture = new HashMap<>();

        furniture.put("Queen Bed", new Furniture("Queen Bed", 0, 10000));
        furniture.put("King Bed", new Furniture("King Bed", 0, 15000));
        furniture.put("Stove", new Furniture("Stove", 0,7500));


        return furniture;
    }
}
