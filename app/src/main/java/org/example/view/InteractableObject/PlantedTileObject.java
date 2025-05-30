package org.example.view.InteractableObject;


import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.GameController;
import org.example.controller.action.HarvestingAction;
import org.example.controller.action.WateringAction;
import org.example.controller.action.RecoverLandAction;
import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.Items;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.Map.Tile;

import java.awt.Rectangle;

public class PlantedTileObject extends InteractableObject {
    public PlantedTileObject(String seedName, int worldX, int worldY, GameController controller) {
        super("Tanaman " + seedName); 
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = false; 
        this.solidArea = new Rectangle(0, 0, 10, 10); 
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PlantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact(GameController controller) {
        Player player = controller.getFarmModel().getPlayerModel();
        Farm farm = controller.getFarmModel();
        FarmMap farmMap = farm.getFarmMap();
        Items heldItem = player.getCurrentHeldItem();

        int tileX = this.worldX / controller.getTileSize();
        int tileY = this.worldY / controller.getTileSize();


        Tile dataTile = farmMap.getTile(tileX, tileY);
        if (!(dataTile instanceof Plantedland)) {
            System.err.println("PlantedTileObject: Error - Tile data di (" + tileX + "," + tileY + ") bukan Plantedland!");
            return;
        }
        
        Plantedland plantData = (Plantedland) dataTile;


        if (plantData.isHarvestable()) { 
            System.out.println("PlantedTileObject: Mencoba memanen di (" + tileX + "," + tileY + ")");
            HarvestingAction harvestingAction = new HarvestingAction(controller, tileX, tileY);
            if (harvestingAction.canExecute(farm)) {
                harvestingAction.execute(farm);
                return; 
            } else {
                 System.out.println("PlantedTileObject: Tidak bisa memanen saat ini (mungkin energi kurang).");
            }
        }
        else if (heldItem != null && heldItem.getName().equalsIgnoreCase("Watering Can")) {
            System.out.println("PlantedTileObject: Mencoba menyiram di (" + tileX + "," + tileY + ")");
            WateringAction wateringAction = new WateringAction(controller, tileX, tileY);
            if (wateringAction.canExecute(farm)) {
                wateringAction.execute(farm);
            } else {
                System.out.println("PlantedTileObject: Tidak bisa menyiram saat ini (mungkin sudah disiram/mati/energi kurang).");
            }
        } else if (heldItem != null && heldItem.getName().equalsIgnoreCase("Pickaxe")) {
            System.out.println("PlantedTileObject: Mencoba mengembalikan tanah di (" + tileX + "," + tileY + ")");
            RecoverLandAction recoverLandAction = new RecoverLandAction(controller, tileX, tileY);
            if (recoverLandAction.canExecute(farm)) {
                recoverLandAction.execute(farm);
            } else {
                System.out.println("PlantedTileObject: Tidak bisa mengembalikan tanah saat ini (mungkin energi kurang).");
            }
        } else {
            System.out.println("PlantedTileObject: Tidak ada aksi yang bisa dilakukan di sini.");
        }
    }
}