package org.example.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.example.model.Items.Fish;
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
        inventory.put(ItemDatabase.getItem("Fish Sandwich"), 10);
        inventory.put(ItemDatabase.getItem("Wine"), 10);
        inventory.put(ItemDatabase.getItem("Salmon"), 10);
        inventory.put(ItemDatabase.getItem("Proposal Ring"), 1);
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
            System.err.println("Inventory.isRemoveInventory: Attempted to remove null item or non-positive quantity. Action failed.");
            return false; // Gagal karena input tidak valid
        }

        if (hasItem(item, quantity)) { // Gunakan hasItem untuk mengecek apakah item ada dan jumlahnya cukup
            int currentQuantity = inventory.get(item); // Kita tahu item ada karena hasItem true
            if (currentQuantity == quantity) {
                inventory.remove(item); // Hapus item jika jumlahnya pas
            } else {
                inventory.put(item, currentQuantity - quantity); // Kurangi jumlahnya jika lebih
            }
            System.out.println("Inventory: Removed " + quantity + "x " + item.getName() + ". Remaining: " + inventory.getOrDefault(item, 0));
            return true; // Berhasil mengurangi/menghapus
        } else {
            System.out.println("Inventory.isRemoveInventory: Not enough " + item.getName() + " (Need " + quantity + ", Have " + inventory.getOrDefault(item,0) + ") or item not found. Action failed.");
            return false; // Gagal karena tidak cukup item atau item tidak ada
        }
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
        List<Items> removedFishList = new ArrayList<>();
        if (requiredQuantity <= 0) {
            return removedFishList; // Tidak ada yang perlu dihapus
        }

        int fishStillNeeded = requiredQuantity;

        // Gunakan Iterator untuk aman menghapus elemen dari map saat iterasi
        Iterator<Map.Entry<Items, Integer>> iterator = inventory.entrySet().iterator();

        while (iterator.hasNext() && fishStillNeeded > 0) {
            Map.Entry<Items, Integer> entry = iterator.next();
            Items currentItem = entry.getKey();
            
            // Cek apakah item adalah instance dari Fish dan tidak null
            if (currentItem instanceof Fish) {
                int quantityInStock = entry.getValue();
                int quantityToRemoveFromThisStack = Math.min(quantityInStock, fishStillNeeded);

                for (int i = 0; i < quantityToRemoveFromThisStack; i++) {
                    removedFishList.add(currentItem); // Menambahkan referensi ikan yang sama
                }

                fishStillNeeded -= quantityToRemoveFromThisStack;

                // Perbarui inventory
                if (quantityInStock == quantityToRemoveFromThisStack) {
                    iterator.remove(); 
                } else {
                    entry.setValue(quantityInStock - quantityToRemoveFromThisStack);
                }
                System.out.println("Inventory: Removed " + quantityToRemoveFromThisStack + "x " + currentItem.getName() + " (as AnyFish).");
            }
        }

        if (fishStillNeeded > 0) {
            System.out.println("Inventory.removeAnyFish: Could not remove the full required quantity. Needed " + 
                               requiredQuantity + ", removed " + removedFishList.size() + ".");
        }
        
        return removedFishList;
    }
}
