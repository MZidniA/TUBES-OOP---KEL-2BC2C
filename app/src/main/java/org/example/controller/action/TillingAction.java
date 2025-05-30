package org.example.controller.action;

import org.example.controller.GameController;
import org.example.view.tile.TileManager;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Player;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.UnplantedTileObject; 
import org.example.model.Map.Tile;
import org.example.model.Map.Tillableland;
import org.example.model.Map.Tilledland;
import org.example.model.Map.FarmMap;

public class TillingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5;

    private final GameController controller;
    private final int targetCol;
    private final int targetRow;

    public TillingAction(GameController controller, int targetCol, int targetRow) {
        this.controller = controller;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    @Override
    public String getActionName() {
        return "TillingAction";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        FarmMap farmMap = farm.getFarmMap();
        if (farm == null || controller == null || farmMap == null) return false;
        
        if (player == null) return false;

        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            System.out.println("TillingAction: Energi terlalu rendah.");
            return false;
        }
        Items heldItem = player.getCurrentHeldItem();
        if (heldItem == null || !heldItem.getName().equalsIgnoreCase("Hoe")) {
            System.out.println("TillingAction: Bukan Hoe yang dipegang.");
            return false;
        }
        if (!player.getInventory().hasItem(ItemDatabase.getItem("Hoe"), 1)) {
            System.out.println("TillingAction: Hoe tidak ada di inventory.");
            return false;
        }
        if (targetCol < 0 || targetCol >= controller.getMaxWorldCol() || 
            targetRow < 0 || targetRow >= controller.getMaxWorldRow()) {
            System.out.println("LOG: Target cangkul di luar peta.");
            return false; 
        }
        
        int currentMap = farm.getCurrentMap();
        if (currentMap == 0) {
            final int MIN_COL = controller.getTillableAreaMinCol(currentMap);
            final int MAX_COL = controller.getTillableAreaMaxCol(currentMap);
            final int MIN_ROW = controller.getTillableAreaMinRow(currentMap);
            final int MAX_ROW = controller.getTillableAreaMaxRow(currentMap);
            if (!(targetCol >= MIN_COL && targetCol <= MAX_COL && targetRow >= MIN_ROW && targetRow <= MAX_ROW)) {
                //System.out.println("DEBUG: Tilling target ("+targetCol+","+targetRow+") OUTSIDE defined area ["+MIN_COL+"-"+MAX_COL+", "+MIN_ROW+"-"+MAX_ROW+"]");
                return false;
            }
        } else { 
            // System.out.println("TillingAction: Mencangkul hanya diizinkan di Farm (map 0).");
            return false; 
        }
        TileManager tileM = controller.getTileManager();
        if (tileM == null) return false;
        
        InteractableObject[] objectsOnMap = farm.getObjectsForCurrentMap();
        if (objectsOnMap != null) {
            for (InteractableObject objCheck : objectsOnMap) {
                if (objCheck != null &&
                    (objCheck.worldX / controller.getTileSize() == targetCol) && 
                    (objCheck.worldY / controller.getTileSize() == targetRow)) {
                    // System.out.println("TillingAction: Sudah ada objek (" + objCheck.name + ") di tile target.");
                    return false;
                }
            }
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
        
        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            player.setPassedOut(true);
            return;
        }

        player.decreaseEnergy(ENERGY_COST);


        farmMap.setTile(targetCol, targetRow, new Tilledland(targetCol, targetRow));


        InteractableObject[][] allObjects = farm.getAllObjects();
        int emptySlotIndex = -1;
        for (int i = 0; i < allObjects[currentMap].length; i++) {
            if (allObjects[currentMap][i] == null) {
                emptySlotIndex = i;
                break;
            }
        }

        
        if (emptySlotIndex != -1) {
            UnplantedTileObject tilled = new UnplantedTileObject(); 
           

            tilled.worldX = targetCol * tileSize;
            tilled.worldY = targetRow * tileSize;
            
            allObjects[currentMap][emptySlotIndex] = tilled;
            System.out.println(player.getName() + " mencangkul tanah di (" + targetCol + ", " + targetRow + "). Objek visual UnplantedTileObject dibuat.");
        } else {
            System.err.println("TillingAction: Tidak ada slot objek kosong di map " + currentMap + " untuk UnplantedTileObject.");
            player.increaseEnergy(ENERGY_COST); 
            farmMap.setTile(targetCol, targetRow, new Tillableland(targetCol, targetRow)); 
            return;
        }

        // 4. Majukan waktu
        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_MINUTES);
        }
    }
}