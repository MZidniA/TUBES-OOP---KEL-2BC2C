// Lokasi: src/main/java/org/example/model/Items/ItemDatabase.java
package org.example.model.Items; // Pastikan package ini benar

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Import RecipeDatabase untuk mengakses nama placeholder jika diperlukan, atau definisikan di sini
import org.example.model.RecipeDatabase;


public class ItemDatabase {
    private static final Map<String, Items> items = new HashMap<>();
    private static boolean isInitialized = false;

    // Instance singleton (sudah benar)
    private static final ItemDatabase INSTANCE = new ItemDatabase();

    // Private constructor untuk singleton (sudah benar)
    private ItemDatabase() {
        // Inisialisasi dipanggil dari getInstance() jika belum, atau bisa juga di sini
        // jika Anda ingin memastikan initialize() selalu dipanggil saat INSTANCE dibuat.
        // Namun, dengan pola synchronized initialize(), pemanggilan dari getInstance() atau getItem() sudah aman.
    }

    // Method untuk mendapatkan instance singleton (sudah benar)
    public static ItemDatabase getInstance() {
        // Memastikan inisialisasi terjadi saat instance pertama kali diminta
        if (!isInitialized) {
            initialize();
        }
        return INSTANCE;
    }

    public static synchronized void initialize() {
        if (isInitialized) {
            return;
        }
        items.clear(); 

        // Muat semua item dari factory Anda
        items.putAll(FoodFactory.createFood());
        items.putAll(SeedFactory.createSeeds());
        items.putAll(FishFactory.createFish());
        items.putAll(CropsFactory.createCropsMap());
        items.putAll(EquipmentFactory.createEquipment());
        items.putAll(MiscFactory.createMisc());
        items.putAll(FurnitureFactory.createFurniture());

        // --- PENAMBAHAN PENTING: Item Placeholder untuk Resep ---
        // Pastikan nama string ini (RecipeDatabase.ANY_FISH_INGREDIENT_NAME)
        // konsisten dengan yang digunakan di RecipeDatabase.
        // Kita buat instance Misc sederhana untuk placeholder ini.
        // Jika item ini tidak ada, RecipeDatabase.getItem(RecipeDatabase.ANY_FISH_INGREDIENT_NAME) akan null.
        
        // Ambil nama placeholder dari RecipeDatabase agar konsisten
        String anyFishName = RecipeDatabase.ANY_FISH_INGREDIENT_NAME;
        if (anyFishName != null && !items.containsKey(anyFishName)) {
            items.put(anyFishName, new Misc(anyFishName, 0, 0)); // Harga jual/beli 0 karena hanya placeholder
            System.out.println("LOG: Placeholder '" + anyFishName + "' added to ItemDatabase.");
        }
        isInitialized = true;
        System.out.println("LOG: ItemDatabase initialized with " + items.size() + " items.");
    }

    public static Items getItem(String name) {
        if (!isInitialized) {
            initialize(); // Panggil initialize jika belum terinisialisasi
        }
        if (name == null) {
            System.err.println("Error: Attempted to get item with null name from ItemDatabase.");
            return null;
        }
        Items item = items.get(name);
        if (item == null) {
            System.err.println("Error: Item not found in ItemDatabase: '" + name + "'");
            // Melempar exception bisa jadi pilihan yang lebih baik untuk error kritis
            // throw new IllegalArgumentException("Item not found in ItemDatabase: " + name);
        }
        return item; // Akan mengembalikan null jika tidak ditemukan dan tidak ada exception
    }

    public static List<Items> itemList(String[] itemNames) {
        if (!isInitialized) initialize();
        List<Items> itemsList = new ArrayList<>();
        if (itemNames == null) return itemsList; // Handle null input

        for (String itemName : itemNames) {
            if (itemName == null) {
                System.err.println("Warning: Null item name encountered in itemList.");
                continue;
            }
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
            if (item != null && item.getName() != null && items.containsKey(item.getName())) { 
                validItems.add(item);
            } else {
                String itemName = (item != null) ? item.getName() : "null item object";
                System.err.println("Warning: Item '" + itemName + "' is null, has a null name, or not in database. Ignoring it in validateItemList.");
            }
        }
        return validItems;
    }

    public static List<Items> getItemsByCategory(String category) {
        if (!isInitialized) initialize();
        List<Items> itemcategory = new ArrayList<>();
        if (category == null) return itemcategory;

        for (Items item : items.values()) {
            // Tambahkan pengecekan null sebelum instanceof jika ada kemungkinan item null di map (seharusnya tidak)
            if (item == null) continue; 
            
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