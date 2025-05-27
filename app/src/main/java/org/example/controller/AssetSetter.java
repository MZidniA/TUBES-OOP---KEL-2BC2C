package org.example.controller;

import org.example.view.InteractableObject.CarolineHouse;
import org.example.view.InteractableObject.DascoHouse;
import org.example.view.InteractableObject.DoorObject;
import org.example.view.InteractableObject.EmilyHouse;
import org.example.view.InteractableObject.EmilyStore;
import org.example.view.InteractableObject.MayorHouse;
import org.example.view.InteractableObject.PerryHouse;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.PondObject; 
import org.example.view.InteractableObject.ShippingBinObject;
import org.example.view.InteractableObject.UnplantedTileObject;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setInteractableObject() {
        int mapIndex = 0;

        gp.obj[mapIndex][0] = new DoorObject(gp);
        gp.obj[mapIndex][0].worldX = 5* gp.tileSize;
        gp.obj[mapIndex][0].worldY = 7* gp.tileSize;

        gp.obj[mapIndex][1] = new PlantedTileObject(gp);
        gp.obj[mapIndex][1].worldX = 15 * gp.tileSize;
        gp.obj[mapIndex][1].worldY = 18 * gp.tileSize;
        
        gp.obj[mapIndex][2] = new PlantedTileObject(gp);
        gp.obj[mapIndex][2].worldX = 16 * gp.tileSize;
        gp.obj[mapIndex][2].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][3] = new PlantedTileObject(gp);
        gp.obj[mapIndex][3].worldX = 17 * gp.tileSize;
        gp.obj[mapIndex][3].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][4] = new PlantedTileObject(gp);
        gp.obj[mapIndex][4].worldX = 18 * gp.tileSize;
        gp.obj[mapIndex][4].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][5] = new PlantedTileObject(gp);
        gp.obj[mapIndex][5].worldX = 19 * gp.tileSize;
        gp.obj[mapIndex][5].worldY = 18 * gp.tileSize;
        
        gp.obj[mapIndex][6] = new PondObject(gp);
        gp.obj[mapIndex][6].worldX = 3* gp.tileSize;
        gp.obj[mapIndex][6].worldY = 22* gp.tileSize;

        gp.obj[mapIndex][7] = new UnplantedTileObject(gp);
        gp.obj[mapIndex][7].worldX = 22 * gp.tileSize;
        gp.obj[mapIndex][7].worldY = 18 * gp.tileSize;
        
        gp.obj[mapIndex][8] = new UnplantedTileObject(gp);
        gp.obj[mapIndex][8].worldX = 23 * gp.tileSize;
        gp.obj[mapIndex][8].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][9] = new UnplantedTileObject(gp);
        gp.obj[mapIndex][9].worldX = 24 * gp.tileSize;
        gp.obj[mapIndex][9].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][10] = new UnplantedTileObject(gp);
        gp.obj[mapIndex][10].worldX = 25 * gp.tileSize;
        gp.obj[mapIndex][10].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][11] = new UnplantedTileObject(gp);
        gp.obj[mapIndex][11].worldX = 26 * gp.tileSize;
        gp.obj[mapIndex][11].worldY = 18 * gp.tileSize;

        gp.obj[mapIndex][12] = new ShippingBinObject(gp);
        gp.obj[mapIndex][12].worldX = 11 * gp.tileSize;
        gp.obj[mapIndex][12].worldY = 8 * gp.tileSize;

        mapIndex = 3; 
        gp.obj[mapIndex][13] = new CarolineHouse(gp);
        gp.obj[mapIndex][13].worldX = 5 * gp.tileSize;
        gp.obj[mapIndex][13].worldY = 17 * gp.tileSize;

        gp.obj[mapIndex][14] = new PerryHouse(gp);
        gp.obj[mapIndex][14].worldX = 5 * gp.tileSize;
        gp.obj[mapIndex][14].worldY = 28 * gp.tileSize;

        gp.obj[mapIndex][15] = new MayorHouse(gp);
        gp.obj[mapIndex][15].worldX = 7 * gp.tileSize;
        gp.obj[mapIndex][15].worldY = 7 * gp.tileSize;

        gp.obj[mapIndex][16] = new EmilyStore(gp);
        gp.obj[mapIndex][16].worldX = 23 * gp.tileSize;
        gp.obj[mapIndex][16].worldY = 7 * gp.tileSize;

        gp.obj[mapIndex][17] = new DascoHouse(gp);
        gp.obj[mapIndex][17].worldX = 26 * gp.tileSize;
        gp.obj[mapIndex][17].worldY = 17 * gp.tileSize;

        gp.obj[mapIndex][18] = new EmilyHouse(gp);
        gp.obj[mapIndex][18].worldX = 25 * gp.tileSize;
        gp.obj[mapIndex][18].worldY = 28 * gp.tileSize;
    }
}
