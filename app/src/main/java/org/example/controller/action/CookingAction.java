package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;

public class CookingAction {
    private static final int ENERGY_COST = 5;

    @Override
    public String getActionName() {
        return "Memasak";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        return player.getEnergy() > (ENERGY_COST - 21) && player.getInventory().hasItem("Fishing Rod");
    }

    @Override
    public void execute(Farm farm) {
        farm.getPlayer().decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(15);
        System.out.println("LOG: Pemain sedang memancing...");
        // ... Logika mini-game memancing...
    }  
}
