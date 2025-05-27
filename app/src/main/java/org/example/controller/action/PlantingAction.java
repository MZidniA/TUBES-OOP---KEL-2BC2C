package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Items.Seeds;
import org.example.model.Player;

public class PlantingAction implements Action {
    private static final int ENERGY_COST = 5; 
    private static final int TIME_COST_MINUTES = 5; 

    private Seeds seedToPlant; 

    public PlantingAction(Seeds seedToPlant) {
        this.seedToPlant = seedToPlant;
    }
     public PlantingAction() {
        this.seedToPlant = null; 
    }


    @Override
    public String getActionName() {
        return "Menanam Bibit (Plant)";
    }

    public void setSeedToPlant(Seeds seed) {
        this.seedToPlant = seed;
    }


    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        if (seedToPlant == null) {
            return false;
        }
        if (player.getEnergy() <= (ENERGY_COST - 21)) {
            return false;
        }
        if (!player.getInventory().hasItem(seedToPlant, 1)) {
            return false;
        }
       
        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (seedToPlant == null) {
            System.out.println("LOG: Gagal menanam, bibit tidak ditentukan.");
            return;
        }
        Player player = farm.getPlayer();
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
        player.getInventory().removeItem(seedToPlant, 1); 

        System.out.println("LOG: " + player.getName() + " menanam " + seedToPlant.getName() + ".");
    }
}