package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;

public class WatchingAction implements Action {

    public static final int ENERGY_COST = 5;
    public static final int TIME_COST_MINUTES = 15;

    @Override
    public String getActionName() {
        return "Watching";
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (farm == null || farm.getPlayerModel() == null) {
            return false;
        }
        Player player = farm.getPlayerModel();

<<<<<<< Updated upstream
        // Cek lokasi harus di rumah (FARM)
        if (player.getCurrentLocationType() != LocationType.FARM) {
            System.out.println("Menonton TV hanya bisa dilakukan di rumah.");
            return false;
        }

        // Cek punya TV
        boolean hasTV = player.getInventory().hasItem(ItemDatabase.getItem("TV"), 1);

        if (!hasTV) {
            System.out.println("Kamu membutuhkan TV untuk menonton.");
            return false;
        }

        // Cek energi cukup
=======
        // Kondisi 1: Pemain harus berada di dalam rumah (misal, map indeks 4)
        if (farm.getCurrentMap() != 4) {
            return false;
        }

        // Kondisi 2: Pemain harus memiliki energi yang cukup
>>>>>>> Stashed changes
        if (player.getEnergy() < ENERGY_COST) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();
        player.decreaseEnergy(ENERGY_COST);
        if (farm.getGameClock() != null) {
            farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
        }
    }
}