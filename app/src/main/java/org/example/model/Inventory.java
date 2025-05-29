package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator; // Import Iterator

import org.example.model.Items.Food;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;


public class Inventory {
    private Map<Items, Integer> inventory;

    public Inventory() {
        inventory = new HashMap<>();
        // Item awal - pastikan ItemDatabase.getItem() mengembalikan objek yang benar
        // dan loadImage() terpanggil di konstruktor item tersebut.
        Items parsnipSeeds = ItemDatabase.getItem("Parsnip Seeds");
        if (parsnipSeeds != null) inventory.put(parsnipSeeds, 15);

        Items hoe = ItemDatabase.getItem("Hoe");
        if (hoe != null) inventory.put(hoe, 1);

        Items wateringCan = ItemDatabase.getItem("Watering Can");
        if (wateringCan != null) inventory.put(wateringCan, 1);

        Items pickaxe = ItemDatabase.getItem("Pickaxe");
        if (pickaxe != null) inventory.put(pickaxe, 1);

        Items fishingRod = ItemDatabase.getItem("Fishing Rod");
        if (fishingRod != null) inventory.put(fishingRod, 1);
    }

    public Map<Items, Integer> getInventory() {
        return inventory; // Sebaiknya kembalikan salinan jika ingin immutable dari luar: return new HashMap<>(inventory);
    }

    public void addInventory(Items item, int quantity) {
        if (item == null || quantity <= 0) {
            System.err.println("Inventory.addInventory: Attempted to add null item or non-positive quantity.");
            return;
        }
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
    }

    /**
     * Mengurangi item dari inventaris.
     * @param item Item yang akan dikurangi.
     * @param quantity Jumlah yang akan dikurangi.
     * @return true jika item berhasil dikurangi sejumlah yang diminta, false jika tidak (misalnya, item tidak ada atau jumlah tidak cukup).
     */
    public boolean removeInventory(Items item, int quantity) {
        if (item == null || quantity <= 0) {
            System.err.println("Inventory.removeInventory: Attempted to remove null item or non-positive quantity.");
            return false;
        }
        if (hasItem(item, quantity)) { // Gunakan hasItem yang sudah ada untuk cek kecukupan
            int currentQuantity = inventory.get(item);
            if (currentQuantity == quantity) { // Jika jumlahnya pas, hapus entri
                inventory.remove(item);
            } else { // Jika lebih, kurangi jumlahnya
                inventory.put(item, currentQuantity - quantity);
            }
            return true; // Berhasil dikurangi
        }
        // System.out.println("Inventory.removeInventory: Not enough " + item.getName() + " to remove. Needed: " + quantity + ", Has: " + inventory.getOrDefault(item, 0));
        return false; // Gagal dikurangi (tidak cukup atau item tidak ada)
    }

    public boolean hasItem(Items item, int quantity) {
        if (item == null || quantity <= 0) return false;
        return inventory.getOrDefault(item, 0) >= quantity;
    }

    // Overload hasItem dengan nama (mungkin berguna)
    public boolean hasItem(String itemName, int quantity) {
        Items item = ItemDatabase.getItem(itemName); // Dapatkan instance item dari database
        if (item != null) {
            return hasItem(item, quantity);
        }
        return false;
    }

    public int getItemQuantity(Items item) { // Lebih baik menerima objek Items
        if (item == null) return 0;
        return inventory.getOrDefault(item, 0);
    }

    // getItemQuantity dengan String yang Anda miliki sebelumnya, bisa dipertahankan
    public int getItemQuantity(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return 0;
        }
        // Lebih efisien jika ItemDatabase.getItem() digunakan untuk mendapatkan instance standar
        Items itemInstance = ItemDatabase.getItem(itemName);
        if (itemInstance != null) {
            return inventory.getOrDefault(itemInstance, 0);
        }
        // Fallback ke iterasi jika nama mungkin tidak 100% cocok dengan key di ItemDatabase
        // tapi ini kurang ideal karena objek Items harusnya unik berdasarkan nama.
        // for (Items itemKey : inventory.keySet()) {
        //     if (itemKey.getName().equalsIgnoreCase(itemName)) {
        //         return inventory.getOrDefault(itemKey, 0);
        //     }
        // }
        return 0;
    }

    /**
     * Mengurangi sejumlah ikan (apapun jenisnya) dari inventaris.
     * Akan mencoba mengurangi dari jenis ikan yang berbeda hingga kuantitas terpenuhi.
     * @param requiredQuantity Jumlah ikan yang dibutuhkan.
     * @return List dari objek Items (ikan) yang berhasil dikeluarkan. Ukuran list ini akan
     * sama dengan requiredQuantity jika cukup ikan tersedia, atau kurang jika tidak.
     */
    public List<Items> removeAnyFish(int requiredQuantity) {
        List<Items> consumedFish = new ArrayList<>();
        if (requiredQuantity <= 0) return consumedFish;

        int fishRemovedCount = 0;
        // Gunakan Iterator untuk menghindari ConcurrentModificationException saat menghapus dari map
        Iterator<Map.Entry<Items, Integer>> iterator = inventory.entrySet().iterator();

        while (iterator.hasNext() && fishRemovedCount < requiredQuantity) {
            Map.Entry<Items, Integer> entry = iterator.next();
            Items item = entry.getKey();

        if (item instanceof Food) { 
                int quantityInStock = entry.getValue();
                int quantityToConsumeFromThisStack = Math.min(quantityInStock, requiredQuantity - fishRemovedCount);

                for (int i = 0; i < quantityToConsumeFromThisStack; i++) {
                    consumedFish.add(item); // Tambahkan instance ikan yang sama (karena kita mengurangi jumlah)
                }

                fishRemovedCount += quantityToConsumeFromThisStack;

                if (quantityInStock == quantityToConsumeFromThisStack) {
                    iterator.remove(); // Hapus entri ikan ini dari inventaris jika habis
                } else {
                    entry.setValue(quantityInStock - quantityToConsumeFromThisStack); // Kurangi jumlahnya
                }
            }
        }
        return consumedFish;
    }
}