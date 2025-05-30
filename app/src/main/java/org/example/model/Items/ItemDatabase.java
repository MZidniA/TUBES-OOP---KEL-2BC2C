package org.example.model.Items; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.RecipeDatabase;

public class ItemDatabase {
    private static final Map<String, Items> items = new HashMap<>();
    private static boolean isInitialized = false;

    private static final ItemDatabase INSTANCE = new ItemDatabase();

    private ItemDatabase() {
        if (!isInitialized) {
            initialize();
        }
    }

    public static ItemDatabase getInstance() {
        return INSTANCE;
    }

    public static synchronized void initialize() { 
        if (isInitialized) {
            return;
        }
        items.clear(); 
        items.putAll(FoodFactory.createFood());
        items.putAll(SeedFactory.createSeeds());
        items.putAll(FishFactory.createFish());
        items.putAll(CropsFactory.createCropsMap()); 
        items.putAll(EquipmentFactory.createEquipment());
        items.putAll(MiscFactory.createMisc());
        items.putAll(FurnitureFactory.createFurniture());
        isInitialized = true;
        System.out.println("LOG: ItemDatabase initialized with " + items.size() + " items.");


        String anyFishName = RecipeDatabase.ANY_FISH_INGREDIENT_NAME;
        if (anyFishName != null && !items.containsKey(anyFishName)) {
            items.put(anyFishName, new Misc(anyFishName, 0, 0)); // Harga jual/beli 0 karena placeholder
            System.out.println("LOG: Placeholder '" + anyFishName + "' added to ItemDatabase.");
        } else if (anyFishName == null) {
            System.err.println("ItemDatabase CRITICAL ERROR: RecipeDatabase.ANY_FISH_INGREDIENT_NAME is null. Cannot create placeholder.");
        } else if (items.containsKey(anyFishName)) {
            System.out.println("LOG: Placeholder '" + anyFishName + "' already exists in ItemDatabase (possibly from a factory).");
        }
        isInitialized = true;
        System.out.println("LOG: ItemDatabase initialization complete.");
    }

    public static Items getItem(String name) {
        if (!isInitialized) {
            initialize(); 
        }
        Items item = items.get(name);
        if (item == null) {
            System.err.println("Error: Item not found in ItemDatabase: " + name);
            return null; 
        }
        return item;
    }

    public static List<Items> itemList(String[] itemNames) {
        if (!isInitialized) initialize();
        List<Items> itemsList = new ArrayList<>();
        for (String itemName : itemNames) {
            Items item = items.get(itemName);
            if (item != null) {
                itemsList.add(item);
            } else {
                System.err.println("Warning: Item '" + itemName + "' not found in ItemDatabase during itemList creation.");
            }
        }
        return itemsList;
    }

    public static List<Items> validateItemList(List<Items> itemsToCheck) {
        if (!isInitialized) initialize();
        List<Items> validItems = new ArrayList<>();
        if (itemsToCheck == null) return validItems; 
        for (Items item : itemsToCheck) {
            if (item != null && items.containsKey(item.getName())) { 
                validItems.add(item);
            } else {
                System.err.println("Warning: Null item or item not in database found in item list. Ignoring it.");
            }
        }
        return validItems;
    }


    public static List<Items> getItemsByCategory(String category) {
        List<Items> itemcategory = new ArrayList<>();
        for (Items item : items.values()) {
            if (category.equals("Seeds") && item instanceof Seeds) {
                itemcategory.add(item);
            } else if (category.equals("Food") && item instanceof Food) {
                itemcategory.add(item);
            } else if (category.equals("Crops") && item instanceof Crops) {
                itemcategory.add(item);
            } else if (category.equals("Equipment") && item instanceof Equipment) {
                itemcategory.add(item);
            } else if (category.equals("Misc") && item instanceof Misc) {
                itemcategory.add(item);
            } else if (category.equals("Furniture") && item instanceof Furniture) {
                itemcategory.add(item);
            } else if (category.equals("Fish") && item instanceof Fish) {
                itemcategory.add(item);
            } 
        }
        return itemcategory;
    }

    public static Map<String, Items> getAllItems() {
        if (!isInitialized) initialize();
        return new HashMap<>(items); 
    }

    public static boolean isInitialized() {
        return isInitialized;
    }
}