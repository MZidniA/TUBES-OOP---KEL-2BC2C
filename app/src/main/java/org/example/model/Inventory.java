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
        inventory.put(ItemDatabase.getItem("Parsnip Seeds"), 15); 
        inventory.put(ItemDatabase.getItem("Hoe"), 1);            
        inventory.put(ItemDatabase.getItem("Watering Can"), 1);   
        inventory.put(ItemDatabase.getItem("Pickaxe"), 1);        
        inventory.put(ItemDatabase.getItem("Fishing Rod"), 1);   
    }

    public Map<Items, Integer> getInventory() {
        return inventory;
    }

    public void addInventory(Items item, int quantity) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantity);
    }

    public void removeInventory(Items item, int quantity) {
        if (inventory.containsKey(item)) {
            int currentQuantity = inventory.get(item);
            if (currentQuantity <= quantity) {
                inventory.remove(item);
            } else {
                inventory.put(item, currentQuantity - quantity);
            }
        }
    }

    public boolean isRemoveInventory(Items item, int quantity) {
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
        return false; // Gagal dikurangi (tidak cukup atau item tidak ada)
    }



    public boolean hasItem(Items itemToEat, int quantity) {
        return inventory.getOrDefault(itemToEat, 0) >= quantity;
    }

    public boolean hasItem(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return false;
        }
        for (Items item : inventory.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return inventory.get(item) > 0; 
            }
        }
        return false;
    }

    public int getItemQuantity(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return 0;
        }
        for (Items item : inventory.keySet()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return inventory.getOrDefault(item, 0);
            }
        }
        return 0;
    }

    public List<Items> removeAnyFish(int requiredQuantity) {
<<<<<<< Updated upstream
        List<Items> removedFish = new ArrayList<>();
        int removed = 0;
        // Buat salinan agar tidak ConcurrentModificationException
        for (Map.Entry<Items, Integer> entry : new HashMap<>(inventory).entrySet()) {
            Items item = entry.getKey();
            // Pastikan instanceof Food dan punya method isFish()
            if (item instanceof Food && ((Food) item).isFish()) {
                int available = entry.getValue();
                int toRemove = Math.min(requiredQuantity - removed, available);
                // Tambahkan ke list hasil
                for (int i = 0; i < toRemove; i++) {
                    removedFish.add(item);
                }
                // Kurangi dari inventory
                removeInventory(item, toRemove);
                removed += toRemove;
                if (removed >= requiredQuantity) break;
            }
        }
        return removedFish;
    }
=======
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

>>>>>>> Stashed changes
}
