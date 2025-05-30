package org.example.controller.action;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Items.Seeds;
import org.example.model.Map.FarmMap;
import org.example.model.Player;

import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.UnplantedTileObject; 

public class PlantingAction implements Action {
    private static final int ENERGY_COST = 5; 
    private static final int TIME_COST_MINUTES = 5; 

    private GameController controller;
    private Seeds seedToPlant;
    private int targetCol;
    private int targetRow;

    // Konstruktor utama
    public PlantingAction(GameController controller, Seeds seed, int targetCol, int targetRow) {
        this.controller = controller;
        this.seedToPlant = seed;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    @Override
    public String getActionName() {
        return seedToPlant.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (seedToPlant == null) {
            //System.err.println("PlantingAction: Tidak ada benih yang dipilih untuk ditanam.");
            return false;
        }
        if (player == null || controller == null) {
            //System.err.println("PlantingAction: Player atau Controller null.");
            return false;
        }

        if (player.getEnergy() <= player.getMinEnergyOperational())  {
            //System.out.println(player.getName() + " tidak punya cukup energi untuk menanam.");
            return false;
        }

 
        if (!player.getInventory().hasItem(seedToPlant, 1)) {
            //System.out.println(player.getName() + " tidak memiliki " + seedToPlant.getName() + " di inventory.");
            return false;
        }
        
 
        if (!seedToPlant.isPlantableInSeason(farm.getGameClock().getCurrentSeason())) {
            //System.out.println(seedToPlant.getName() + " tidak bisa ditanam di musim " + farm.getGameClock().getCurrentSeason());
            return false;
        }

        InteractableObject targetObject = farm.getObjectAtTile(farm.getCurrentMap(), targetCol, targetRow, controller.getTileSize());
        if (!(targetObject instanceof UnplantedTileObject)) { 
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
        int currentMap = farm.getCurrentMap();
        int tileSize = controller.getTileSize();
    
        
        player.decreaseEnergy(ENERGY_COST);
        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            player.setPassedOut(true); 
            return; 
        }
        player.getInventory().removeInventory(seedToPlant, 1);
    

        int freedSlotIndex = -1;
        InteractableObject[][] allObjects = farm.getAllObjects(); 
        for (int i = 0; i < allObjects[currentMap].length; i++) {
            InteractableObject obj = allObjects[currentMap][i];
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == targetCol && objRow == targetRow && obj instanceof UnplantedTileObject) {
                    System.out.println("PlantingAction: Menemukan UnplantedTileObject di slot " + i + " pada (" + targetCol + "," + targetRow + ") untuk dihapus.");
                    allObjects[currentMap][i] = null; 
                    freedSlotIndex = i;
                    System.out.println("PlantingAction: UnplantedTileObject di slot " + i + " BERHASIL DIHAPUS.");
                    break; 
                }
            }
        }
    
        
    

        FarmMap farmMap = farm.getFarmMap();
        boolean dataTilePlanted = farmMap.plantSeedOnTile(targetCol, targetRow, seedToPlant);
        if (!dataTilePlanted) {
            player.increaseEnergy(ENERGY_COST);
            player.getInventory().addInventory(seedToPlant, 1);
            return;
        }
    

        int worldX = targetCol * tileSize;
        int worldY = targetRow * tileSize;
        PlantedTileObject plantedTile = new PlantedTileObject(seedToPlant.getName(), worldX, worldY, controller);
    
  
        boolean placed = false;
    
        if (freedSlotIndex != -1 && allObjects[currentMap][freedSlotIndex] == null) {
            allObjects[currentMap][freedSlotIndex] = plantedTile;
            placed = true;
        } else {
            for (int i = 0; i < allObjects[currentMap].length; i++) {
                if (allObjects[currentMap][i] == null) {
                    allObjects[currentMap][i] = plantedTile;
                    placed = true;
                    break;
                }
            }
        }
    
        if (!placed) {
            farmMap.setTileToTillable(targetCol, targetRow); 
            player.increaseEnergy(ENERGY_COST);
            player.getInventory().addInventory(seedToPlant, 1);

            return;
        }
    
        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_MINUTES);
        }
    }
}