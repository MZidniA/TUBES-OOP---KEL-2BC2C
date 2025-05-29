package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.enums.LocationType;
import org.example.controller.GameController;

// Mengganti nama kelas menjadi sesuatu yang lebih deskriptif seperti UpdateAndShowLocationAction
// atau jika hanya mengupdate dan mengembalikan string: UpdateAndGetLocationAction
public class UpdateAndShowLocationAction implements Action { // Asumsi masih ada interface Action

    private GameController controller;

    public UpdateAndShowLocationAction(GameController controller) {
        this.controller = controller;
    }

    @Override
    public boolean canExecute(Farm farm) {
        return true;
    }

   
    public String updateAndGetDetailedLocationString(Farm farm) {
        if (farm == null || farm.getPlayerModel() == null) {
            return "Lokasi: Error (Farm/Player null)";
        }
    
        Player player = farm.getPlayerModel();
        int currentMapIndex = farm.getCurrentMap();
        LocationType currentActualLocationInPlayerModel = player.getCurrentLocationType(); 
        LocationType newDeterminedLocation = currentActualLocationInPlayerModel; 
    
        int playerTileX = player.getTileX();
        int playerTileY = player.getTileY();
    
        if (currentMapIndex == 0) { 
            int mlakeMinCol = 15; int mlakeMaxCol = 31; int mlakeMinRow = 0;  int mlakeMaxRow = 12;
            int pondMinCol = 0; int pondMaxCol = 7; int pondMinRow = 21; int pondMaxRow = 26;
    
            if (playerTileX >= mlakeMinCol && playerTileX <= mlakeMaxCol &&
                playerTileY >= mlakeMinRow && playerTileY <= mlakeMaxRow) {
                newDeterminedLocation = LocationType.MOUNTAIN_LAKE;
            } else if (playerTileX >= pondMinCol && playerTileX <= pondMaxCol &&
                       playerTileY >= pondMinRow && playerTileY <= pondMaxRow) {
                newDeterminedLocation = LocationType.POND;
            } else {
                newDeterminedLocation = LocationType.FARM;
            }
        } 
        
        if (currentActualLocationInPlayerModel != newDeterminedLocation) {
            player.setCurrentLocationType(newDeterminedLocation);
            System.out.println("UpdateAndShowLocationAction: Player location UPDATED from " + currentActualLocationInPlayerModel + " to " + newDeterminedLocation);
        }
    
        return newDeterminedLocation.toString().replace("_", " ") +
               " (" + playerTileX + ", " + playerTileY + ")";
    }
    
    @Override
    public void execute(Farm farm) { 
        String detailedLocation = updateAndGetDetailedLocationString(farm); 
        System.out.println("--- Lokasi Saat Ini ---");
        System.out.println("Lokasi: " + detailedLocation);
        System.out.println("-----------------------");
    }
    @Override
    public String getActionName() {
        // Nama bisa diubah jika kelasnya berganti nama
        return "Update/Show Location";
    }
}