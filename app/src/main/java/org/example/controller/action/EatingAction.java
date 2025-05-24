// Lokasi: src/main/java/org/example/controller/action/EatingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.item.EdibleItem; // Asumsi ada kelas/interface EdibleItem
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import java.util.List;

public class EatingAction implements Action {
    // Energy cost untuk makan biasanya 0, malah menambah energi
    // Time cost mungkin ada sedikit
    private static final int TIME_COST_MINUTES = 5;

    private EdibleItem itemToEat; // Item yang akan dimakan

    public EatingAction(EdibleItem itemToEat) {
        this.itemToEat = itemToEat;
    }
     public EatingAction() {
        this.itemToEat = null;
    }

    @Override
    public String getActionName() {
        return "Makan " + (itemToEat != null ? itemToEat.getName() : "Item");
    }
    
    public void setItemToEat(EdibleItem item) {
        this.itemToEat = item;
    }


    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        if (itemToEat == null) {
            return false;
        }
        // Cek apakah item termasuk kategori edible: Food, Crops, atau Fish
        List<Items> edibleItems = ItemDatabase.getItemsByCategory("Food");
        edibleItems.addAll(ItemDatabase.getItemsByCategory("Crops"));
        edibleItems.addAll(ItemDatabase.getItemsByCategory("Fish"));
        if (!edibleItems.contains(itemToEat)) {
            // System.out.println("LOG: Item bukan makanan yang bisa dimakan.");
            return false;
        }
        // Cek apakah punya item yang dipilih di inventory
        if (!player.getInventory().hasItem(itemToEat, 1)) {
            // System.out.println("LOG: Tidak punya " + itemToEat.getName() + " untuk dimakan.");
            return false;
        }
        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (itemToEat == null) {
            System.out.println("LOG: Gagal makan, item tidak ditentukan.");
            return;
        }
        Player player = farm.getPlayer();
        player.increaseEnergy(itemToEat.getEnergyRestored());
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
        player.getInventory().removeItem(itemToEat, 1);

        System.out.println("LOG: " + player.getName() + " memakan " + itemToEat.getName() + ". Energi pulih " + itemToEat.getEnergyRestored() + ".");
    }
}