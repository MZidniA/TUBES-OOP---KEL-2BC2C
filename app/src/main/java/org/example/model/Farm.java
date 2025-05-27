package org.example.model;

import org.example.view.InteractableObject.InteractableObject;
// Tidak perlu import PlayerView lagi di sini

public class Farm {
    private final Player playerModel;
    // private final PlayerView playerView; // DIHAPUS: Farm tidak boleh tahu tentang PlayerView
    private final InteractableObject[][] objects = new InteractableObject[6][20]; // maxMap, maxObjects
    private int currentMap = 0;

    public Farm(String farmName, Player playerModel) { // Parameter hanya Player model
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName);
        
        // Inisialisasi PlayerView TIDAK LAGI dilakukan di sini
        // this.playerView = new PlayerView(playerModel, ???); // DIHAPUS
    }
    
    // Getters
    public Player getPlayerModel() { return playerModel; }
    // public PlayerView getPlayerView() { return playerView; } // DIHAPUS

    public int getCurrentMap() { return currentMap; }
    public InteractableObject[][] getAllObjects() { return objects; }
    public InteractableObject[] getObjectsForCurrentMap() { return objects[currentMap]; }

    // Setters
    public void setCurrentMap(int map) { this.currentMap = map; }

    public String getMapPathFor(int mapIndex) {
        return switch (mapIndex) {
            case 0 -> "/maps/map.txt";
            case 1 -> "/maps/beachmap.txt";
            case 2 -> "/maps/rivermap.txt";
            case 3 -> "/maps/townmap.txt";   // Disesuaikan dengan pemanggilan loadMap di TileManager
            case 4 -> "/maps/housemap.txt";
            // ... dst, pastikan semua peta terdaftar di sini jika digunakan
            default -> "/maps/map.txt"; // Default jika mapIndex tidak valid
        };
    }
    
    public void clearObjects(int mapIndex) {
        if (mapIndex >= 0 && mapIndex < objects.length) {
            for(int i = 0; i < objects[mapIndex].length; i++) {
                objects[mapIndex][i] = null;
            }
        }
    }
}