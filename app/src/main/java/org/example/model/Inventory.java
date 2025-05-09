package org.example.model;
import java.util.HashMap;
import java.util.Map;

import org.example.model.Items.Items;


public class Inventory {
    private Map<Items, Integer> items; 

    public Inventory() {
        items = new HashMap<>();
    }

    public Map<Items, Integer> getItems() {
        return items;
    }

    public void addItem(Items item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(Items item, int quantity) {
        if (items.containsKey(item)) {
            int currentQuantity = items.get(item);
            if (currentQuantity <= quantity) {
                items.remove(item); 
            } else {
                items.put(item, currentQuantity - quantity); 
            }
        } 
    }
}
