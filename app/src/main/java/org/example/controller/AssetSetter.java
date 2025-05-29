package org.example.controller;

import org.example.model.Farm;
import org.example.view.InteractableObject.AbigailHouse;
import org.example.view.InteractableObject.BedObject;
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
<<<<<<< Updated upstream
import org.example.view.InteractableObject.UnplantedTileObject;
=======
import org.example.view.InteractableObject.TVObject;
>>>>>>> Stashed changes

public class AssetSetter {
    private final GameController controller;
    private final UtilityTool uTool = new UtilityTool();

    public AssetSetter(GameController controller) {
        this.controller = controller;
    }

    /**
     * Mengisi array objek interaktif di dalam Model untuk map yang sedang aktif.
     * Objek yang dibuat secara dinamis (seperti hasil cangkulan) tidak akan tertimpa
     * jika metode ini hanya men-set objek-objek statis/awal peta.
     */
    public void setInteractableObject() {
        Farm farmModel = controller.getFarmModel();
        if (farmModel == null) {
            System.err.println("AssetSetter Error: farmModel is null!");
            return;
        }

        InteractableObject[][] allObjects = farmModel.getAllObjects();
        int tileSize = controller.getTileSize();
        if (tileSize <= 0) {
            System.err.println("AssetSetter Error: tileSize is invalid (" + tileSize + ")!");
            return;
        }

        int currentMapIndex = farmModel.getCurrentMap(); // Dapatkan map yang sedang aktif

        // Bersihkan objek untuk peta saat ini sebelum men-set yang baru
        // Ini penting jika Anda ingin objek peta selalu "fresh" saat peta dimuat ulang.
        // Namun, ini juga akan menghapus objek dinamis seperti UnplantedTileObject hasil cangkulan.
        // Pertimbangkan apakah ini perilaku yang diinginkan.
        // farmModel.clearObjects(currentMapIndex); // OPSIONAL, tergantung kebutuhan

        System.out.println("AssetSetter: Setting objects for map index " + currentMapIndex);


        if (currentMapIndex == 0) { // --- Map 0 (Farm) ---
            InteractableObject[] farmObjects = {
                new DoorObject(), 
                new PondObject(), 
                new ShippingBinObject(), new MountainLakeObject() 
            };
            // Sesuaikan farmPositions jika jumlah objek berkurang
            int[][] farmPositions = {
                {5, 7}, 
                {3, 22}, 

                {11, 8}, {29, 10} 
            };

            for (int i = 0; i < farmObjects.length; i++) {
                if (farmObjects[i] == null) continue; 
                
                if (farmObjects[i].image != null) {
                    farmObjects[i].image = uTool.scaleImage(farmObjects[i].image, tileSize, tileSize);
                }
                farmObjects[i].worldX = farmPositions[i][0] * tileSize;
                farmObjects[i].worldY = farmPositions[i][1] * tileSize;


                if (i < allObjects[currentMapIndex].length) {
                    allObjects[currentMapIndex][i] = farmObjects[i];
                } else {
                    System.err.println("AssetSetter Error: Index " + i + " out of bounds for mapIndex " + currentMapIndex);
                }
            }
        } else if (currentMapIndex == 3) { // --- Map 3 (Town) ---
            // Objek untuk Town
            InteractableObject[] townInteractables = {
                new CarolineHouse(), new PerryHouse(), new MayorHouse(),
                new EmilyStore(), new DascoHouse(), new AbigailHouse()
            };
            int[][] townPositions = {
                {5, 17}, {5, 28}, {7, 7}, {23, 7}, {26, 17}, {25, 28}
            };

            for (int i = 0; i < townInteractables.length; i++) {
                if (townInteractables[i] == null) continue;

                if (townInteractables[i].image != null) {
                    townInteractables[i].image = uTool.scaleImage(townInteractables[i].image, tileSize, tileSize);
                }
                townInteractables[i].worldX = townPositions[i][0] * tileSize;
                townInteractables[i].worldY = townPositions[i][1] * tileSize;
                if (i < allObjects[currentMapIndex].length) {
                    allObjects[currentMapIndex][i] = townInteractables[i];
                } else {
                     System.err.println("AssetSetter Error: Index " + i + " out of bounds for mapIndex " + currentMapIndex + " (Town)");
                }
            }
        } else if (currentMapIndex == 2) { // --- Map 2 (River) ---
            RiverObject river = new RiverObject();
            if (river.image != null) river.image = uTool.scaleImage(river.image, tileSize, tileSize);
            river.worldX = 22 * tileSize;
            river.worldY = 7 * tileSize;
            if (0 < allObjects[currentMapIndex].length) { 
                allObjects[currentMapIndex][0] = river; 
            } else {
                System.err.println("AssetSetter Error: No available slot for RiverObject in mapIndex " + currentMapIndex);
            }
        } else if (currentMapIndex == 1) { // --- Map 1 (Ocean) ---
            OceanObject ocean = new OceanObject();
            if (ocean.image != null) ocean.image = uTool.scaleImage(ocean.image, tileSize, tileSize);
            ocean.worldX = 10 * tileSize;
            ocean.worldY = 21 * tileSize;
            if (0 < allObjects[currentMapIndex].length) {
                allObjects[currentMapIndex][0] = ocean;
            } else {
                 System.err.println("AssetSetter Error: No available slot for OceanObject in mapIndex " + currentMapIndex);
            }
        } else if (currentMapIndex == 4) { // --- Map 4 (House) ---
            // Definisikan semua objek untuk rumah
            InteractableObject[] houseObjects = {
                new StoveObject(),
                new BedObject(), 
                new TVObject()
            };
        
            // Tentukan posisi untuk setiap objek
            int[][] housePositions = {
<<<<<<< Updated upstream
                {6, 3},  // Posisi untuk StoveObject
                {9, 10}  // Posisi untuk BedObject
=======
                {6, 3},  // stove
                {9, 3},  // bed
                {3, 3}   // TV
>>>>>>> Stashed changes
            };
        
            // Loop untuk menempatkan semua objek ke dalam daftar objek utama
            for (int i = 0; i < houseObjects.length; i++) {
                if (houseObjects[i] == null) continue; // Lewati jika objek tidak ada
            
    
        
                // Scale gambar jika ada
                if (houseObjects[i].image != null) {
                    houseObjects[i].image = uTool.scaleImage(houseObjects[i].image, tileSize, tileSize);
                }
                houseObjects[i].worldX = housePositions[i][0] * tileSize;
                houseObjects[i].worldY = housePositions[i][1] * tileSize;
        
                // --- INI BAGIAN YANG HILANG ---
                // Masukkan objek ke dalam array allObjects agar terdeteksi oleh game
                if (i < allObjects[currentMapIndex].length) {
                    allObjects[currentMapIndex][i] = houseObjects[i];
                } else {
                    System.err.println("AssetSetter Error: Slot tidak cukup untuk objek di peta rumah.");
                }
            }
        }
    }
}
