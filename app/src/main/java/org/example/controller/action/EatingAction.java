package org.example.controller.action;

import java.util.List;

import org.example.model.Farm;
import org.example.model.Items.EdibleItem;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Player;

public class EatingAction implements Action {
    private static final int TIME_COST_MINUTES = 5;

    private Items itemToEat;

    public EatingAction(Items itemToEat) {
        this.itemToEat = itemToEat;
    }
     public EatingAction() {
        this.itemToEat = null;
    }

    @Override
    public String getActionName() {
        return "Makan " + (itemToEat != null ? itemToEat.getName() : "Item");
    }
    
    public void setItemToEat(Items item) {
        this.itemToEat = item;
    }


    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        if (itemToEat == null) {
            return false;
        }
        List<Items> edibleItems = ItemDatabase.getItemsByCategory("Food");
        edibleItems.addAll(ItemDatabase.getItemsByCategory("Crops"));
        edibleItems.addAll(ItemDatabase.getItemsByCategory("Fish"));
        if (!edibleItems.contains(itemToEat)) {
            return false;
        }
        if (!player.getInventory().hasItem(itemToEat, 1)) {
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
        if (itemToEat instanceof EdibleItem) {
            int restored = ((EdibleItem) itemToEat).getEnergyRestored();
            player.setEnergy(player.getEnergy() + restored);
            player.getInventory().removeInventory(itemToEat, 1);
            farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
            System.out.println("LOG: " + player.getName() + " memakan " + itemToEat.getName() + ". Energi pulih " + restored + ".");
        } else {
            System.out.println("LOG: Item tidak bisa dimakan.");
        }
    }
}