package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.view.entitas.PlayerView;

public class DoorObject extends InteractableObject {

    public DoorObject() {
        super("Door"); 
        this.collision = true;
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Door.png for DoorObject");
        }
    }

    @Override
    public void interact(GameController gc) {
        if (gc == null) return;

        Farm farmModel = gc.getFarmModel();
        PlayerView playerView = gc.getPlayerViewInstance();
        int tileSize = gc.getTileSize();

        if (farmModel == null || playerView == null) return;

        int currentMap = farmModel.getCurrentMap();

        if (currentMap == 0) { // Jika saat ini di FARM (map 0)
            // Asumsikan pintu ini adalah pintu rumah di Farm
            // Cek apakah pemain berinteraksi dengan pintu yang benar (misalnya berdasarkan posisi pintu)
            // Untuk contoh, kita anggap ini pintu utama ke rumah
            int doorTileX_Farm = 5; // GANTI DENGAN TILE X PINTU RUMAH DI FARM
            int doorTileY_Farm = 7; // GANTI DENGAN TILE Y PINTU RUMAH DI FARM

            // Periksa apakah interaksi terjadi pada tile pintu rumah
            // Anda mungkin perlu logika yang lebih baik untuk mencocokkan this DoorObject
            // dengan pintu yang benar jika ada banyak DoorObject di satu map.
            if (this.worldX/tileSize == doorTileX_Farm && this.worldY/tileSize == doorTileY_Farm) {
                System.out.println("DoorObject: Entering House from Farm.");
                // Teleport ke House (map 4) pada koordinat spawn di dalam House
                int houseSpawnTileX = 3; // GANTI DENGAN TILE X SPAWN DI DALAM HOUSE (depan pintu)
                int houseSpawnTileY = 10; // GANTI DENGAN TILE Y SPAWN DI DALAM HOUSE (depan pintu)
                gc.teleportPlayer(4, houseSpawnTileX * tileSize, houseSpawnTileY * tileSize);
            }

        } else if (currentMap == 4) { // Jika saat ini di HOUSE (map 4)
            // Asumsikan pintu ini adalah pintu keluar dari House ke Farm
            int doorTileX_House = 3;  // GANTI DENGAN TILE X PINTU KELUAR DI HOUSE
            int doorTileY_House = 10; // GANTI DENGAN TILE Y PINTU KELUAR DI HOUSE

            if (this.worldX/tileSize == doorTileX_House && this.worldY/tileSize == doorTileY_House) {
                System.out.println("DoorObject: Exiting House to Farm.");
                // Teleport ke Farm (map 0) pada koordinat spawn di Farm (depan pintu rumah)
                int farmSpawnTileX = 5;  // GANTI DENGAN TILE X SPAWN DI FARM (depan pintu rumah)
                int farmSpawnTileY = 8;  // GANTI DENGAN TILE Y SPAWN DI FARM (depan pintu rumah)
                                        // Pastikan ini sedikit berbeda dari tile pintu itu sendiri untuk menghindari loop
                gc.teleportPlayer(0, farmSpawnTileX * tileSize, farmSpawnTileY * tileSize);
            }
        }
    }
}