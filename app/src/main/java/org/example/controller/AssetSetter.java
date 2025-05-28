
package org.example.controller;

import org.example.model.Farm;
import org.example.view.InteractableObject.AbigailHouse;
import org.example.view.InteractableObject.CarolineHouse;
import org.example.view.InteractableObject.DascoHouse;
import org.example.view.InteractableObject.DoorObject;
import org.example.view.InteractableObject.EmilyStore;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.MayorHouse;
import org.example.view.InteractableObject.MountainLakeObject;
import org.example.view.InteractableObject.OceanObject;
import org.example.view.InteractableObject.PerryHouse;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.InteractableObject.ShippingBinObject;
import org.example.view.InteractableObject.StoveObject;
import org.example.view.InteractableObject.UnplantedTileObject;

public class AssetSetter {
    private final GameController controller;
    private final UtilityTool uTool = new UtilityTool();

    public AssetSetter(GameController controller) {
        this.controller = controller;
    }

    public void setInteractableObject() {
        Farm farmModel = controller.getFarmModel();
        if (farmModel == null) {
            System.err.println("AssetSetter Error: farmModel is null!");
            return;
        }

        InteractableObject[][] objects = farmModel.getAllObjects();
        int tileSize = controller.getTileSize();
        if (tileSize <= 0) {
            System.err.println("AssetSetter Error: tileSize is invalid (" + tileSize + ")!");
            return;
        }

        // --- Map 0 (Farm) ---
        int mapIndex = 0;

        InteractableObject[] farmObjects = {
            new DoorObject(), new PlantedTileObject(), new PlantedTileObject(),
            new PlantedTileObject(), new PlantedTileObject(), new PlantedTileObject(),
            new PondObject(), new UnplantedTileObject(), new UnplantedTileObject(),
            new UnplantedTileObject(), new UnplantedTileObject(), new UnplantedTileObject(),
            new ShippingBinObject(), new MountainLakeObject()
        };

        int[][] farmPositions = {
            {5, 7}, {15, 18}, {16, 18}, {17, 18}, {18, 18}, {19, 18},
            {3, 22}, {22, 18}, {23, 18}, {24, 18}, {25, 18}, {26, 18},
            {11, 8}, {29, 10}
        };

        for (int i = 0; i < farmObjects.length; i++) {
            if (farmObjects[i].image != null) {
                farmObjects[i].image = uTool.scaleImage(farmObjects[i].image, tileSize, tileSize);
            }
            farmObjects[i].worldX = farmPositions[i][0] * tileSize;
            farmObjects[i].worldY = farmPositions[i][1] * tileSize;

            if (i < objects[mapIndex].length) {
                objects[mapIndex][i] = farmObjects[i];
            } else {
                System.err.println("AssetSetter Error: Index " + i + " out of bounds for mapIndex 0 (Farm)");
            }
        }

        // --- Map 3 (Town) ---
        mapIndex = 3;

        Object[][] townObjects = {
            {new CarolineHouse(), 5, 17},
            {new PerryHouse(), 5, 28},
            {new MayorHouse(), 7, 7},
            {new EmilyStore(), 23, 7},
            {new DascoHouse(), 26, 17},
            {new AbigailHouse(), 25, 28}
        };

        for (int i = 0; i < townObjects.length; i++) {
            InteractableObject obj = (InteractableObject) townObjects[i][0];
            if (obj.image != null) {
                obj.image = uTool.scaleImage(obj.image, tileSize, tileSize);
            }
            obj.worldX = (int) townObjects[i][1] * tileSize;
            obj.worldY = (int) townObjects[i][2] * tileSize;

            int index = 13 + i;
            if (index < objects[mapIndex].length) {
                objects[mapIndex][index] = obj;
            } else {
                System.err.println("AssetSetter Error: Index " + index + " out of bounds for mapIndex 3 (Town)");
            }
        }

        // --- Map 2 (River) ---
        mapIndex = 2;
        RiverObject river = new RiverObject();
        if (river.image != null) {
            river.image = uTool.scaleImage(river.image, tileSize, tileSize);
        }
        river.worldX = 22 * tileSize;
        river.worldY = 7 * tileSize;
        if (19 < objects[mapIndex].length) {
            objects[mapIndex][19] = river;
        } else {
            System.err.println("AssetSetter Error: Index 19 out of bounds for mapIndex 2 (River)");
        }

        // --- Map 1 (Ocean) ---
        mapIndex = 1;
        OceanObject ocean = new OceanObject();
        if (ocean.image != null) {
            ocean.image = uTool.scaleImage(ocean.image, tileSize, tileSize);
        }
        ocean.worldX = 10 * tileSize;
        ocean.worldY = 21 * tileSize;
        if (20 < objects[mapIndex].length) {
            objects[mapIndex][20] = ocean;
        } else {
            System.err.println("AssetSetter Error: Index 20 out of bounds for mapIndex 1 (Ocean)");
        }
        
        // --- Map 4 (House) ---
        mapIndex = 4;
        StoveObject stove = new StoveObject();
        if (stove.image != null) {
            stove.image = uTool.scaleImage(stove.image, tileSize, tileSize);
        }
        stove.worldX = 6 * tileSize;
        stove.worldY = 3 * tileSize;

        if (21 < objects[mapIndex].length) {
            objects[mapIndex][21] = stove;
        } else {
            System.err.println("AssetSetter Error: Index 21 out of bounds for mapIndex 4 (House)");
        }

    }
}