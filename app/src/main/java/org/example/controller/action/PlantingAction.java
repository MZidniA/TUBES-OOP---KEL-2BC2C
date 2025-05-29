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
        FarmMap farmMap = farm.getFarmMap(); 
        int currentMap = farm.getCurrentMap();
        int tileSize = controller.getTileSize();


        player.decreaseEnergy(ENERGY_COST);
        player.getInventory().removeInventory(seedToPlant, 1);

        farm.removeObjectAtTile(currentMap, targetCol, targetRow, tileSize);
     

   
        boolean dataTilePlanted = farmMap.plantSeedOnTile(targetCol, targetRow, seedToPlant);
        
        if (!dataTilePlanted) {
            System.err.println("PlantingAction GAGAL: Tidak bisa mengubah tile data di FarmMap menjadi Plantedland di (" + targetCol + "," + targetRow + ").");
            player.increaseEnergy(ENERGY_COST); 
            player.getInventory().addInventory(seedToPlant, 1); 
 
            return;
        }
        
        
        int worldX = targetCol * tileSize;
        int worldY = targetRow * tileSize;
        
        PlantedTileObject newPlantedCropVisual = new PlantedTileObject(seedToPlant.getName(), worldX, worldY, controller);
        


        InteractableObject[][] allObjects = farm.getAllObjects();
        boolean placedVisual = false;
        for (int i = 0; i < allObjects[currentMap].length; i++) {
            if (allObjects[currentMap][i] == null) {
                allObjects[currentMap][i] = newPlantedCropVisual;
                placedVisual = true;
            }
        }

        if (!placedVisual) {
            System.err.println("PlantingAction Error: Tidak ada slot objek kosong untuk menempatkan PlantedTileObject baru!");
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