package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;

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

        // OPSI 1: Cek lokasi dengan LocationType (lebih fleksibel)
        if (player.getCurrentLocationType() != LocationType.FARM) {
            System.out.println("Menonton TV hanya bisa dilakukan di rumah.");
            return false;
        }


        boolean hasTV = player.getInventory().hasItem(ItemDatabase.getItem("TV"), 1);
        if (!hasTV) {
            System.out.println("Kamu membutuhkan TV untuk menonton.");
            return false;
        }

        if (player.getEnergy() < ENERGY_COST) {
            System.out.println("Energi tidak cukup untuk menonton TV.");
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
        System.out.println("Kamu menonton TV dan menghabiskan " + ENERGY_COST + " energi serta " + TIME_COST_MINUTES + " menit.");
    }
}