package org.example.controller;

import org.example.model.Farm;
import org.example.view.InteractableObject.AbigailHouse;
import org.example.view.InteractableObject.BedObject;
import org.example.view.InteractableObject.CarolineHouse;
import org.example.view.InteractableObject.DascoHouse;
import org.example.view.InteractableObject.DoorObject;
import org.example.view.InteractableObject.Emily;
import org.example.view.InteractableObject.EmilyStore;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.MayorHouse;
import org.example.view.InteractableObject.MountainLakeObject;
import org.example.view.InteractableObject.OceanObject;
import org.example.view.InteractableObject.PerryHouse;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.InteractableObject.ShippingBinObject;
import org.example.view.InteractableObject.StoreDoor;
import org.example.view.InteractableObject.StoveObject;
import org.example.view.InteractableObject.TVObject;

public class AssetSetter {
    private final GameController controller;
    private final UtilityTool uTool = new UtilityTool();

    public AssetSetter(GameController controller) {
        this.controller = controller;
    }

    public void setInteractableObject() {
        Farm farmModel = controller.getFarmModel();
        if (farmModel == null) {
            //System.err.println("AssetSetter Error: farmModel is null!");
            return;
        }

        InteractableObject[][] allObjects = farmModel.getAllObjects();
        int tileSize = controller.getTileSize();
        if (tileSize <= 0) {
            //System.err.println("AssetSetter Error: tileSize is invalid (" + tileSize + ")!");
            return;
        }

        int currentMapIndex = farmModel.getCurrentMap();
        System.out.println("AssetSetter: Setting objects for map index " + currentMapIndex);

        if (currentMapIndex == 0) { // FARM
            InteractableObject[] farmObjects = {
                new DoorObject(),
                new PondObject(),
                new ShippingBinObject(),
                new MountainLakeObject()
            };
            int[][] farmPositions = {
                {5, 7},
                {3, 22},
                {11, 8},
                {29, 10}
            };

            for (int i = 0; i < farmObjects.length; i++) {
                assignObject(farmObjects[i], farmPositions[i], allObjects[currentMapIndex], i, tileSize);
            }

        } else if (currentMapIndex == 1) { 
            OceanObject ocean = new OceanObject();
            assignObject(ocean, new int[] {10, 21}, allObjects[currentMapIndex], 0, tileSize);

        } else if (currentMapIndex == 2) { 
            RiverObject river = new RiverObject();
            assignObject(river, new int[] {22, 7}, allObjects[currentMapIndex], 0, tileSize);

        } else if (currentMapIndex == 3) { 
            InteractableObject[] townObjects = {
                new CarolineHouse(), new PerryHouse(), new MayorHouse(),
                new EmilyStore(), new DascoHouse(), new AbigailHouse()
            };
            int[][] townPositions = {
                {5, 17}, {5, 28}, {7, 7},
                {23, 7}, {26, 17}, {25, 28}
            };

            for (int i = 0; i < townObjects.length; i++) {
                assignObject(townObjects[i], townPositions[i], allObjects[currentMapIndex], i, tileSize);
            }

        } else if (currentMapIndex == 4) { 
            InteractableObject[] houseObjects = {
                new StoveObject(),
                new BedObject(),
                new TVObject() 
            };
            int[][] housePositions = {
                {6, 3},
                {9, 10},
                {4, 3}
            };

            for (int i = 0; i < houseObjects.length; i++) {
                assignObject(houseObjects[i], housePositions[i], allObjects[currentMapIndex], i, tileSize);
            }

        } else if (currentMapIndex == 5) { 
            InteractableObject[] storeObjects = {
                new StoreDoor(),
                new Emily()
            };
            int[][] storePositions = {
                {9, 24}, {6, 11}
            };

            for (int i = 0; i < storeObjects.length; i++) {
                assignObject(storeObjects[i], storePositions[i], allObjects[currentMapIndex], i, tileSize);
            }
        } else {
            System.err.println("AssetSetter Warning: No interactable objects assigned for mapIndex " + currentMapIndex);
        }
    }

    private void assignObject(InteractableObject obj, int[] position, InteractableObject[] objectArray, int index, int tileSize) {
        if (obj == null) return;
        if (obj.image != null) {
            obj.image = uTool.scaleImage(obj.image, tileSize, tileSize);
        }
        obj.worldX = position[0] * tileSize;
        obj.worldY = position[1] * tileSize;
        if (index < objectArray.length) {
            objectArray[index] = obj;
        } else {
            System.err.println("AssetSetter Error: Index " + index + " out of bounds for objectArray at mapIndex " + controller.getFarmModel().getCurrentMap());
        }
    }
}