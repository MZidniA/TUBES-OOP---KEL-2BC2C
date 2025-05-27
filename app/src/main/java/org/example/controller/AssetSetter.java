package org.example.controller;

import org.example.model.Farm;
import org.example.view.InteractableObject.DoorObject;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.PondObject;
import org.example.view.InteractableObject.ShippingBinObject;
import org.example.view.InteractableObject.UnplantedTileObject;
import org.example.view.InteractableObject.InteractableObject;

public class AssetSetter {
    private final GameController controller;
    private final UtilityTool uTool = new UtilityTool(); // Buat instance UtilityTool di sini

    public AssetSetter(GameController controller) {
        this.controller = controller;
    }

    /**
     * Mengisi array objek interaktif di dalam Model dan melakukan scaling gambar.
     */
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
             return; // Hindari error jika tileSize tidak valid
        }

        int mapIndex = 0; // Fokus pada map 0 (FARM) untuk contoh ini

        // --- Map 0 (FARM) ---
        // Setiap objek dibuat, lalu gambarnya di-scale jika ada, baru dimasukkan ke array.

        DoorObject door = new DoorObject();
        if (door.image != null) {
            door.image = uTool.scaleImage(door.image, tileSize, tileSize);
        }
        door.worldX = 5 * tileSize;
        door.worldY = 7 * tileSize;
        objects[mapIndex][0] = door;

        PlantedTileObject planted1 = new PlantedTileObject();
        if (planted1.image != null) {
            planted1.image = uTool.scaleImage(planted1.image, tileSize, tileSize);
        }
        planted1.worldX = 15 * tileSize;
        planted1.worldY = 18 * tileSize;
        objects[mapIndex][1] = planted1;
        
        PlantedTileObject planted2 = new PlantedTileObject();
        if (planted2.image != null) {
            planted2.image = uTool.scaleImage(planted2.image, tileSize, tileSize);
        }
        planted2.worldX = 16 * tileSize;
        planted2.worldY = 18 * tileSize;
        objects[mapIndex][2] = planted2;

        PlantedTileObject planted3 = new PlantedTileObject();
        if (planted3.image != null) {
            planted3.image = uTool.scaleImage(planted3.image, tileSize, tileSize);
        }
        planted3.worldX = 17 * tileSize;
        planted3.worldY = 18 * tileSize;
        objects[mapIndex][3] = planted3;

        PlantedTileObject planted4 = new PlantedTileObject();
        if (planted4.image != null) {
            planted4.image = uTool.scaleImage(planted4.image, tileSize, tileSize);
        }
        planted4.worldX = 18 * tileSize;
        planted4.worldY = 18 * tileSize;
        objects[mapIndex][4] = planted4;

        PlantedTileObject planted5 = new PlantedTileObject();
        if (planted5.image != null) {
            planted5.image = uTool.scaleImage(planted5.image, tileSize, tileSize);
        }
        planted5.worldX = 19 * tileSize;
        planted5.worldY = 18 * tileSize;
        objects[mapIndex][5] = planted5;
        
        PondObject pond = new PondObject();
        if (pond.image != null) {
            pond.image = uTool.scaleImage(pond.image, tileSize, tileSize);
        }
        pond.worldX = 3 * tileSize;
        pond.worldY = 22 * tileSize;
        objects[mapIndex][6] = pond;

        UnplantedTileObject unplanted1 = new UnplantedTileObject();
        if (unplanted1.image != null) {
            unplanted1.image = uTool.scaleImage(unplanted1.image, tileSize, tileSize);
        }
        unplanted1.worldX = 22 * tileSize;
        unplanted1.worldY = 18 * tileSize;
        objects[mapIndex][7] = unplanted1;
        
        UnplantedTileObject unplanted2 = new UnplantedTileObject();
        if (unplanted2.image != null) {
            unplanted2.image = uTool.scaleImage(unplanted2.image, tileSize, tileSize);
        }
        unplanted2.worldX = 23 * tileSize;
        unplanted2.worldY = 18 * tileSize;
        objects[mapIndex][8] = unplanted2;

        UnplantedTileObject unplanted3 = new UnplantedTileObject();
        if (unplanted3.image != null) {
            unplanted3.image = uTool.scaleImage(unplanted3.image, tileSize, tileSize);
        }
        unplanted3.worldX = 24 * tileSize;
        unplanted3.worldY = 18 * tileSize;
        objects[mapIndex][9] = unplanted3;

        UnplantedTileObject unplanted4 = new UnplantedTileObject();
        if (unplanted4.image != null) {
            unplanted4.image = uTool.scaleImage(unplanted4.image, tileSize, tileSize);
        }
        unplanted4.worldX = 25 * tileSize;
        unplanted4.worldY = 18 * tileSize;
        objects[mapIndex][10] = unplanted4;

        UnplantedTileObject unplanted5 = new UnplantedTileObject();
        if (unplanted5.image != null) {
            unplanted5.image = uTool.scaleImage(unplanted5.image, tileSize, tileSize);
        }
        unplanted5.worldX = 26 * tileSize;
        unplanted5.worldY = 18 * tileSize;
        objects[mapIndex][11] = unplanted5;

        ShippingBinObject shippingBin = new ShippingBinObject();
        if (shippingBin.image != null) {
            shippingBin.image = uTool.scaleImage(shippingBin.image, tileSize, tileSize);
        }
        shippingBin.worldX = 11 * tileSize;
        shippingBin.worldY = 8 * tileSize;
        objects[mapIndex][12] = shippingBin;

        // --- Map 1 (BEACH), etc. ---
        // mapIndex = 1; 
        // Lakukan hal yang sama untuk peta lain jika ada objeknya
        // Contoh:
        // if (objects.length > 1 && objects[1] != null) { // Pastikan array untuk map 1 ada
        //    // objects[1][0] = new SomeBeachObject();
        //    // if (objects[1][0].image != null) {
        //    //     objects[1][0].image = uTool.scaleImage(objects[1][0].image, tileSize, tileSize);
        //    // }
        //    // objects[1][0].worldX = ...;
        //    // objects[1][0].worldY = ...;
        // }
    }
}