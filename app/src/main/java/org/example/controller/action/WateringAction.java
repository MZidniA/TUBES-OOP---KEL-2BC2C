package org.example.controller.action;

import org.example.controller.GameController; // Diperlukan
import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.Map.Tile;
import org.example.model.GameClock;

public class WateringAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5;
    private static final Items REQUIRED_ITEM = ItemDatabase.getItem("Watering Can");

    private GameController controller;
    private int targetCol;
    private int targetRow;

    public WateringAction(GameController controller, int targetCol, int targetRow) {
        this.controller = controller;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    @Override
    public String getActionName() {
        return "Watering Action";
    }

    private Plantedland getTargetPlantedLand(Farm farm) {
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null) return null;
        
        Tile targetTile = farmMap.getTile(targetCol, targetRow);
        if (targetTile instanceof Plantedland) {
            return (Plantedland) targetTile;
        }
        return null;
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        if (player == null || controller == null) return false;


        if (player.getEnergy()  <= player.getMinEnergyOperational()) {
            
            return false;
        }

        if (REQUIRED_ITEM == null) {
            System.err.println("WateringAction ERROR: Definisi Watering Can tidak ditemukan.");
            return false;
        }
        Items currentHeldItem = player.getCurrentHeldItem();
        if (currentHeldItem == null || !currentHeldItem.getName().equalsIgnoreCase(REQUIRED_ITEM.getName())) {
            return false;
        }
        
        Plantedland targetPlant = getTargetPlantedLand(farm);
        if (targetPlant == null) {
            return false;
        }
        
        if (targetPlant.isWateredToday()) {
            // System.out.println("WateringAction: Tanaman " + targetPlant.getPlantedSeed().getName() + " sudah disiram hari ini.");
            return false;
        }
        if (targetPlant.isDead()) {
            // System.out.println("WateringAction: Tidak bisa menyiram tanaman " + targetPlant.getPlantedSeed().getName() + " yang sudah mati.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            return;
        }

        Player player = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();
        Plantedland targetPlant = getTargetPlantedLand(farm); 

        if (targetPlant == null) {
             System.err.println("WateringAction.execute: Critical - targetPlant null padahal canExecute true.");
            return;
        }

        player.decreaseEnergy(ENERGY_COST);

        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            player.setPassedOut(true); 
            return; 
        }

        targetPlant.setWatered(true);

        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_MINUTES);
        }
    }
}