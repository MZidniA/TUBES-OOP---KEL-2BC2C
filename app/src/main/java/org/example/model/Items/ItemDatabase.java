package org.example.model.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDatabase {
    private static Map<String, Items> items = new HashMap<>();

    public static void initialize() {
        items.putAll(FoodFactory.createFood());
        items.putAll(SeedFactory.createSeeds());
        items.putAll(FishFactory.createFish());
        items.putAll(CropsFactory.createCropsMap());
        items.putAll(EquipmentFactory.createEquipment());
        items.putAll(MiscFactory.createMisc());
        items.putAll(FurnitureFactory.createFurniture());
    }


    public static Items getItem(String name) {
        if (items.isEmpty()) {
            initialize();
        } 
        if (!items.containsKey(name)) {
            throw new IllegalArgumentException("Item not found: " + name);
        }
        return items.get(name);
    }

    public static List<Items> itemList(String[] itemNames) {
        List<Items> itemsList = new ArrayList<>();
        for (String itemName : itemNames) {
            Items item = getItem(itemName); 
            if (item != null) {
                itemsList.add(item); 
            } else {
                System.err.println("Warning: Item '" + itemName + "' not found in ItemDatabase.");
            }
        }
        return itemsList;
    }


    public static List<Items> validateItemList(List<Items> items) {
        List<Items> validItems = new ArrayList<>();
        for (Items item : items) {
            if (item != null) {
                validItems.add(item);
            } else {
                System.err.println("Warning: Null item found in item list. Ignoring it.");
            }
        }
        return validItems;
    }

    public static List<Items> getItemsByCategory(String category) {
        List<Items> filteredItems = new ArrayList<>();
        for (Items item : items.values()) {
            if (category.equals("Seeds") && item instanceof Seeds) {
                filteredItems.add(item);
            } else if (category.equals("Food") && item instanceof Food) {
                filteredItems.add(item);
            } else if (category.equals("Crops") && item instanceof Crops) {
                filteredItems.add(item);
            } else if (category.equals("Equipment") && item instanceof Equipment) {
                filteredItems.add(item);
            } else if (category.equals("Misc") && item instanceof Misc) {
                filteredItems.add(item);
            } else if (category.equals("Furniture") && item instanceof Furniture) {
                filteredItems.add(item);
            } else if (category.equals("Fish") && item instanceof Fish) {
                filteredItems.add(item);
            } 
        }
        return filteredItems;
    }

    public static Map<String, Items> getAllItems() {
        return items;
    }
}