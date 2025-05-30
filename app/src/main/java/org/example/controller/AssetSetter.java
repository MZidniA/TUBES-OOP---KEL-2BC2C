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
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.RiverObject;
import org.example.view.InteractableObject.ShippingBinObject;
import org.example.view.InteractableObject.StoveObject;
import org.example.view.InteractableObject.TVObject;
import org.example.view.InteractableObject.TeleportPointObject;

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

        InteractableObject[][] allObjects = farmModel.getAllObjects();
        int tileSize = controller.getTileSize();
        if (tileSize <= 0) {
            System.err.println("AssetSetter Error: tileSize is invalid (" + tileSize + ")!");
            return;
        }

        // Membersihkan objek lama sebelum menempatkan yang baru untuk map saat ini
        // Ini penting jika pemain berpindah map dan method ini dipanggil lagi.
        // Atau, pastikan method ini hanya dipanggil sekali per load map.
        // Jika hanya dipanggil sekali saat inisialisasi game untuk semua map,
        // maka loop di bawah yang berdasarkan currentMapIndex sudah cukup.
        // Jika dipanggil setiap kali ganti map, maka perlu membersihkan array map spesifik:
        // if (allObjects[farmModel.getCurrentMap()] != null) {
        //    Arrays.fill(allObjects[farmModel.getCurrentMap()], null);
        // }


        // Iterasi melalui SEMUA map untuk menempatkan objeknya.
        // Ini lebih baik daripada hanya mengandalkan currentMapIndex,
        // karena objek harus diset untuk semua map saat game dimulai atau dimuat.
        for (int mapIdx = 0; mapIdx < allObjects.length; mapIdx++) {
            // Kosongkan dulu array objek untuk map ini sebelum menempatkan yang baru
            if (allObjects[mapIdx] != null) {
                for(int i = 0; i < allObjects[mapIdx].length; i++) {
                    allObjects[mapIdx][i] = null;
                }
            } else {
                // Jika array untuk map ini belum diinisialisasi di Farm.java, ini masalah.
                // Asumsikan sudah diinisialisasi dengan ukuran tertentu.
                System.err.println("AssetSetter Warning: allObjects[" + mapIdx + "] is null. Objects for this map cannot be set.");
                continue;
            }

            System.out.println("AssetSetter: Setting objects for map index " + mapIdx);

            if (mapIdx == 0) { // FARM (Map Index 0)
                InteractableObject[] objects = {
                    new DoorObject(),   // 0: Ke Rumah (map 4)
                    new PondObject(),               // 1:
                    new ShippingBinObject(),        // 2:
                    new TeleportPointObject()       // 3: Teleporter Utama ke World Map
                };
                int[][] positions = {
                    {5, 7},     // Pintu Rumah (TILE X, TILE Y)
                    {3, 22},    // Pond
                    {11, 8},    // Shipping Bin
                    {29, 10}    // GANTI DENGAN POSISI TELEPORTER UTAMA DI FARM
                };
                placeObjectsOnMap(mapIdx, objects, positions, tileSize, allObjects);

            } else if (mapIdx == 1) { // OCEAN (Map Index 1)
                InteractableObject[] objects = {
                    new TeleportPointObject()       // 0: Teleporter
                    // Tambahkan objek lain spesifik Ocean di sini jika ada
                };
                int[][] positions = {
                    {30, 2}      // GANTI DENGAN POSISI TELEPORTER DI OCEAN
                };
                placeObjectsOnMap(mapIdx, objects, positions, tileSize, allObjects);

            } else if (mapIdx == 2) { // FOREST RIVER (Map Index 2)
                InteractableObject[] objects = {
                    new TeleportPointObject()       // 0: Teleporter
                    // Tambahkan objek lain spesifik Forest River di sini jika ada
                };
                int[][] positions = {
                    {28, 0}     // GANTI DENGAN POSISI TELEPORTER DI FOREST RIVER
                };
                placeObjectsOnMap(mapIdx, objects, positions, tileSize, allObjects);

            } else if (mapIdx == 3) { // TOWN (Map Index 3)
                InteractableObject[] objects = {
                    new CarolineHouse(),            // 0
                    new PerryHouse(),               // 1
                    new MayorHouse(),               // 2
                    new EmilyStore(),               // 3
                    new DascoHouse(),               // 4
                    new AbigailHouse(),             // 5
                    new TeleportPointObject()       // 6: Teleporter
                };
                int[][] positions = {
                    {5, 17}, {5, 28}, {7, 7}, {23, 7}, {26, 17}, {25, 28},
                    {16, 30}      // GANTI DENGAN POSISI TELEPORTER DI TOWN
                };
                placeObjectsOnMap(mapIdx, objects, positions, tileSize, allObjects);

            } else if (mapIdx == 4) { // HOUSE (Map Index 4)
                InteractableObject[] objects = {
                    new StoveObject(),              // 0
                    new BedObject(),                // 1
                    new TVObject(),                 // 2
                    new DoorObject()     // 3: Pintu kembali ke Farm (map 0)
                };
                int[][] positions = {
                    {6, 3},     // Stove
                    {9, 10},    // Bed
                    {4, 3},     // TV
                    {3, 11}     // GANTI DENGAN POSISI PINTU DI DALAM RUMAH MENUJU FARM
                };
                placeObjectsOnMap(mapIdx, objects, positions, tileSize, allObjects);
            }
            // Tambahkan else if untuk map lain jika ada
        }
    }

    /**
     * Helper method untuk menempatkan sekumpulan objek pada map tertentu.
     * @param mapIndex Indeks map tujuan
     * @param objectsToPlace Array objek yang akan ditempatkan
     * @param positions Array 2D berisi koordinat [tileX, tileY] untuk setiap objek
     * @param tileSize Ukuran tile
     * @param allObjects Referensi ke array utama semua objek game
     */
    private void placeObjectsOnMap(int mapIndex, InteractableObject[] objectsToPlace, int[][] positions, int tileSize, InteractableObject[][] allObjects) {
        if (objectsToPlace.length != positions.length) {
            System.err.println("AssetSetter Error for map " + mapIndex + ": Jumlah objek tidak sama dengan jumlah posisi.");
            return;
        }
        if (allObjects[mapIndex] == null) {
             System.err.println("AssetSetter Error for map " + mapIndex + ": Array objek untuk map ini null.");
            return;
        }

        for (int i = 0; i < objectsToPlace.length; i++) {
            InteractableObject obj = objectsToPlace[i];
            if (obj == null) {
                System.err.println("AssetSetter Warning for map " + mapIndex + ": Objek pada indeks " + i + " adalah null.");
                continue;
            }

            // Scale gambar jika ada
            if (obj.image != null) {
                obj.image = uTool.scaleImage(obj.image, tileSize, tileSize);
            } else {
                 // Ini bisa terjadi jika loadImage() di dalam objek gagal atau tidak ada gambar
                // System.out.println("AssetSetter Info for map " + mapIndex + ": Objek '" + obj.name + "' tidak memiliki gambar.");
            }

            // Set posisi dunia
            obj.worldX = positions[i][0] * tileSize;
            obj.worldY = positions[i][1] * tileSize;

            // Tempatkan objek ke dalam array utama, pastikan tidak out of bounds
            if (i < allObjects[mapIndex].length) {
                allObjects[mapIndex][i] = obj;
            } else {
                System.err.println("AssetSetter Error for map " + mapIndex + ": Slot tidak cukup untuk objek '" + obj.name + "' pada indeks " + i + ". Ukuran array: " + allObjects[mapIndex].length);
                // Berhenti menempatkan objek untuk map ini jika array sudah penuh untuk mencegah error lebih lanjut
                break; 
            }
        }
    }
}