package org.example.controller;

import org.example.view.InteractableObject.DoorObject;
import org.example.view.InteractableObject.UnplantedTileObject;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.PondObject; // Added import for Pond
import org.example.view.InteractableObject.ShippingBinObject;
public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setInteractableObject() {

        gp.obj[0] = new DoorObject();
        gp.obj[0].worldX = 5* gp.tileSize;
        gp.obj[0].worldY = 7* gp.tileSize;

        gp.obj[1] = new PlantedTileObject();
        gp.obj[1].worldX = 15 * gp.tileSize;
        gp.obj[1].worldY = 18 * gp.tileSize;
        
        gp.obj[2] = new PlantedTileObject();
        gp.obj[2].worldX = 16 * gp.tileSize;
        gp.obj[2].worldY = 18 * gp.tileSize;

        gp.obj[3] = new PlantedTileObject();
        gp.obj[3].worldX = 17 * gp.tileSize;
        gp.obj[3].worldY = 18 * gp.tileSize;

        gp.obj[4] = new PlantedTileObject();
        gp.obj[4].worldX = 18 * gp.tileSize;
        gp.obj[4].worldY = 18 * gp.tileSize;

        gp.obj[5] = new PlantedTileObject();
        gp.obj[5].worldX = 19 * gp.tileSize;
        gp.obj[5].worldY = 18 * gp.tileSize;
        
        gp.obj[6] = new PondObject();
        gp.obj[6].worldX = 3* gp.tileSize;
        gp.obj[6].worldY = 22* gp.tileSize;

        gp.obj[7] = new UnplantedTileObject();
        gp.obj[7].worldX = 22 * gp.tileSize;
        gp.obj[7].worldY = 18 * gp.tileSize;
        
        gp.obj[8] = new UnplantedTileObject();
        gp.obj[8].worldX = 23 * gp.tileSize;
        gp.obj[8].worldY = 18 * gp.tileSize;

        gp.obj[9] = new UnplantedTileObject();
        gp.obj[9].worldX = 24 * gp.tileSize;
        gp.obj[9].worldY = 18 * gp.tileSize;

        gp.obj[10] = new UnplantedTileObject();
        gp.obj[10].worldX = 25 * gp.tileSize;
        gp.obj[10].worldY = 18 * gp.tileSize;

        gp.obj[11] = new UnplantedTileObject();
        gp.obj[11].worldX = 26 * gp.tileSize;
        gp.obj[11].worldY = 18 * gp.tileSize;

        gp.obj[12] = new ShippingBinObject();
        gp.obj[12].worldX = 11 * gp.tileSize;
        gp.obj[12].worldY = 8 * gp.tileSize;
    }
}
