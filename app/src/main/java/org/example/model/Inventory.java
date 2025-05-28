package org.example.model;
import java.util.HashMap;
import java.util.Map;

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

    public void removeItem(Items requiredItem, int requiredQuantity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeItem'");
    }
}
