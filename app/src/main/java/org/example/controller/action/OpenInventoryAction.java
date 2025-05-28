package org.example.controller.action;

import java.util.Map;

import org.example.model.Farm;
import org.example.model.Inventory;
import org.example.model.Items.Items;
import org.example.model.Player;

public class OpenInventoryAction implements Action {

    @Override
    public boolean canExecute(Farm farm) {
        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel(); 
        Inventory inventory = player.getInventory(); 

        System.out.println("--- Isi Inventaris " + player.getName() + " ---"); 
        Map<Items, Integer> itemsInInventory = inventory.getInventory();

        if (itemsInInventory.isEmpty()) {
            System.out.println("Inventaris kosong.");
        } else {
            int i = 1;
            for (Map.Entry<Items, Integer> entry : itemsInInventory.entrySet()) {
                Items item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(i + ". " + item.getName() + " (Jumlah: " + quantity + ")");
                i++;
            }
        }
        System.out.println("------------------------------------");
    }

    @Override
    public String getActionName() {
        return "Open Inventory";
    }
}   