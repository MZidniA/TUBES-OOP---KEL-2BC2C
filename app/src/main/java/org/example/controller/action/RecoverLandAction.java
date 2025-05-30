package org.example.controller.action;

import org.example.controller.GameController; // Diperlukan untuk akses tileSize dan komponen lain
import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase; // Asumsi ItemDatabase Anda berfungsi seperti ini
import org.example.model.Items.Items;
import org.example.model.GameClock;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.UnplantedTileObject; // Atau Tilledland jika itu nama kelasnya
import org.example.view.InteractableObject.PlantedTileObject; // Jika Anda perlu memeriksa objek yang ditanam
import org.example.view.tile.TileManager; // Untuk mengubah tile visual (opsional)

public class RecoverLandAction implements Action {

    private static final int ENERGY_COST_PER_TILE = 5; // Sesuai spesifikasi
    private static final int TIME_COST_PER_TILE_MINUTES = 5; // Sesuai spesifikasi
    private static final Items REQUIRED_ITEM = ItemDatabase.getItem("Pickaxe"); // Item yang dibutuhkan adalah Pickaxe

    private GameController controller;
    private int targetCol;
    private int targetRow;

    public RecoverLandAction(GameController controller, int targetCol, int targetRow) {
        this.controller = controller;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }
    

    @Override
    public String getActionName() {
        return "Recover Land";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (player == null || controller == null) {
            //System.err.println("RecoverLandAction Error: Player or Controller is null.");
            return false;
        }

 
        if (player.getEnergy() < ENERGY_COST_PER_TILE) {
            
            return false;
        }


        if (REQUIRED_ITEM == null) {
            //System.err.println("RecoverLandAction ERROR: Definisi item Pickaxe tidak ditemukan di ItemDatabase.");
            return false;
        }
   
        Items currentHeldItem = player.getCurrentHeldItem();
        if (currentHeldItem == null || !currentHeldItem.getName().equalsIgnoreCase(REQUIRED_ITEM.getName())) {
            //System.out.println(player.getName() + " membutuhkan Pickaxe untuk mengembalikan tanah.");
            return false;
        }

    
        InteractableObject targetObject = farm.getObjectAtTile(farm.getCurrentMap(), targetCol, targetRow, controller.getTileSize());
        if (!(targetObject instanceof UnplantedTileObject || targetObject instanceof PlantedTileObject)) { 
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
        int currentMapIndex = farm.getCurrentMap();


        player.decreaseEnergy(ENERGY_COST_PER_TILE);
        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            player.setPassedOut(true); 
            return; 
        }
       


        boolean objectRemoved = farm.removeObjectAtTile(currentMapIndex, targetCol, targetRow, controller.getTileSize());
        
        if (objectRemoved) {
            TileManager tileManager = controller.getTileManager();
            if (tileManager != null) {
                int defaultTileID = 37;; 
                tileManager.setTile(currentMapIndex, targetCol, targetRow, defaultTileID);
            }

        } else {
            System.err.println("R Gagal mengembalikan tanah tercangkul di (" + targetCol + ", " + targetRow + ").");
        }

        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_PER_TILE_MINUTES);

        }
    }
}