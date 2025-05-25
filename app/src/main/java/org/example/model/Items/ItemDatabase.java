// Lokasi: src/main/java/org/example/model/Items/ItemDatabase.java
package org.example.model.Items; // Pastikan package ini benar

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDatabase {
    private static final Map<String, Items> items = new HashMap<>();
    private static boolean isInitialized = false;

    // Tambahkan instance singleton
    private static final ItemDatabase INSTANCE = new ItemDatabase();

    // Private constructor untuk singleton
    private ItemDatabase() {
        // Inisialisasi hanya jika belum
        if (!isInitialized) {
            initialize();
        }
    }

    // Method untuk mendapatkan instance singleton
    public static ItemDatabase getInstance() {
        return INSTANCE;
    }

    // Panggil initialize() sekali saja, misalnya di awal permainan.
    public static synchronized void initialize() { // Tambahkan synchronized jika ada potensi akses multi-thread
        if (isInitialized) {
            return;
        }
        items.clear(); // Bersihkan jika dipanggil ulang (meskipun idealnya tidak)
        items.putAll(FoodFactory.createFood());
        items.putAll(SeedFactory.createSeeds()); // Pastikan SeedFactory membuat Seeds dengan yieldCropName yang benar
        items.putAll(FishFactory.createFish());
        items.putAll(CropsFactory.createCropsMap()); // Ini akan berisi template Crops
        items.putAll(EquipmentFactory.createEquipment());
        items.putAll(MiscFactory.createMisc());
        items.putAll(FurnitureFactory.createFurniture());
        isInitialized = true;
        System.out.println("LOG: ItemDatabase initialized with " + items.size() + " items.");
    }

    public static Items getItem(String name) {
        if (!isInitialized) {
            initialize(); // Inisialisasi jika belum
        }
        Items item = items.get(name);
        if (item == null) {
            // Melempar exception lebih baik untuk kasus item tidak ditemukan
            // agar bisa ditangani secara eksplisit.
            System.err.println("Error: Item not found in ItemDatabase: " + name);
            // throw new IllegalArgumentException("Item not found in ItemDatabase: " + name);
            return null; // Atau kembalikan null jika itu preferensi Anda, tapi waspadai NPE
        }
        return item;
    }

    // Method itemList dan validateItemList bisa dipertahankan jika Anda membutuhkannya
    public static List<Items> itemList(String[] itemNames) {
        if (!isInitialized) initialize();
        List<Items> itemsList = new ArrayList<>();
        for (String itemName : itemNames) {
            Items item = items.get(itemName); // Menggunakan map internal, tidak perlu getItem() lagi
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
        if (itemsToCheck == null) return validItems; // Handle null input
        for (Items item : itemsToCheck) {
            if (item != null && items.containsKey(item.getName())) { // Cek juga apakah ada di database kita
                validItems.add(item);
            } else {
                System.err.println("Warning: Null item or item not in database found in item list. Ignoring it.");
            }
        }
        return validItems;
    }


    public static List<Items> getItemsByCategory(String category) {
        if (!isInitialized) initialize();
        List<Items> filteredItems = new ArrayList<>();
        for (Items item : items.values()) {
            // Cara yang lebih baik adalah jika Items punya method getCategory()
            if (category.equalsIgnoreCase("Seeds") && item instanceof Seeds) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Food") && item instanceof Food) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Crops") && item instanceof Crops) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Equipment") && item instanceof Equipment) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Misc") && item instanceof Misc) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Furniture") && item instanceof Furniture) {
                filteredItems.add(item);
            } else if (category.equalsIgnoreCase("Fish") && item instanceof Fish) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    public static Map<String, Items> getAllItems() {
        if (!isInitialized) initialize();
        return new HashMap<>(items); // Kembalikan copy agar map internal tidak termodifikasi dari luar
    }
}