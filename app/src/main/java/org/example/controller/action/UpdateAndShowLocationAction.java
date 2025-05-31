package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.enums.LocationType;
import org.example.controller.GameController;


public class UpdateAndShowLocationAction implements Action { 
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

        if (currentMapIndex == 3) {
            int perryMinCol = 0; int perryMaxCol = 10;
            int perryMinRow = 22; int perryMaxRow = 30; 

            int carolineMinCol = 0; int carolineMaxCol = 10;
            int carolineMinRow = 10; int carolineMaxRow = 20;


            int mayorMinCol = 1; int mayorMaxCol = 11;
            int mayorMinRow = 0; int mayorMaxRow = 10;

            int storeMinCol = 18; int storeMaxCol = 27;
            int storeMinRow = 0; int storeMaxRow = 9;

            int dascoMinCol = 21; int dascoMaxCol = 30;
            int dascoMinRow = 10; int dascoMaxRow = 20;

            int abigailMinCol = 22; int abigailMaxCol = 30;
            int abigailMinRow = 21; int abigailMaxRow = 29; 

            

            if (playerTileX >= perryMinCol && playerTileX <= perryMaxCol &&
                playerTileY >= perryMinRow && playerTileY <= perryMaxRow) {
                newDeterminedLocation = LocationType.RUMAH_PERRY;
            } else if (playerTileX >= carolineMinCol && playerTileX <= carolineMaxCol &&
                    playerTileY >= carolineMinRow && playerTileY <= carolineMaxRow) {
                newDeterminedLocation = LocationType.RUMAH_CAROLINE;
            } else if (playerTileX >= mayorMinCol && playerTileX <= mayorMaxCol &&
                    playerTileY >= mayorMinRow && playerTileY <= mayorMaxRow) {
                newDeterminedLocation = LocationType.RUMAH_MAYOR_TADI;
            } else if (playerTileX >= storeMinCol && playerTileX <= storeMaxCol &&
                    playerTileY >= storeMinRow && playerTileY <= storeMaxRow) {
                newDeterminedLocation = LocationType.STORE;
            } else if (playerTileX >= dascoMinCol && playerTileX <= dascoMaxCol &&
                    playerTileY >= dascoMinRow && playerTileY <= dascoMaxRow) {
                newDeterminedLocation = LocationType.RUMAH_DASCO;
            } else if (playerTileX >= abigailMinCol && playerTileX <= abigailMaxCol &&
                    playerTileY >= abigailMinRow && playerTileY <= abigailMaxRow) {
                newDeterminedLocation = LocationType.RUMAH_ABIGAIL;
            } else {
                newDeterminedLocation = LocationType.TOWN; 
            }
        }
        
        if (currentActualLocationInPlayerModel != newDeterminedLocation) {
            player.setCurrentLocationType(newDeterminedLocation);
        }
    
        return newDeterminedLocation.toString().replace("_", " ") + " (" + playerTileX + ", " + playerTileY + ")";
    }
    
    @Override
    public void execute(Farm farm) { 
        updateAndGetDetailedLocationString(farm); 
        
    }
    @Override
    public String getActionName() {
        return "Update/Show Location";
    }
}