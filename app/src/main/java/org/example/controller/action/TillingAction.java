// Lokasi: src/main/java/org/example/controller/action/TillingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase;



public class TillingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5; // Dari spesifikasi "5 menit dalam game / tile"

    @Override
    public String getActionName() {
        return "Membajak Tanah (Till)";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        // Cek energi
        if (player.getEnergy() <= (ENERGY_COST - 21)) { // Mengikuti pola FishingAction untuk batas energi
            return false;
        }
        // Cek apakah punya Hoe
        if (!player.getInventory().hasItem(ItemDatabase.getItem("Hoe"), 1)) {
            return false;
        }

            return true; // Placeholder, logika tile target perlu implementasi
        }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        System.out.println("LOG: " + player.getName() + " membajak tanah.");
    }
}