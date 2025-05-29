package org.example.model;

import java.time.LocalTime;
import java.util.List;

import org.example.model.Map.FarmMap;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.InteractableObject.InteractableObject;
// Tidak perlu import PlayerView lagi di sini

public class Farm {
    private final Player playerModel;
    private int currentDay;
    // private final PlayerView playerView; // DIHAPUS: Farm tidak boleh tahu tentang PlayerView
    private final InteractableObject[][] objects = new InteractableObject[6][300]; // maxMap, maxObjects
    private int currentMap = 0;
    // Tambahkan list untuk menyimpan CookingInProgress jika belum ada
    private List<CookingInProgress> activeCookings = new java.util.ArrayList<>();
    private final PlayerStats playerStats;
    private final GameClock gameClock;
    private final FarmMap farmMap = new FarmMap(); // Tambahkan deklarasi dan inisialisasi FarmMap
    private Weather currentWeather; // Tambahkan untuk menyimpan cuaca saat ini
    private Season currentSeason; // Tambahkan untuk menyimpan musim saat ini   

    public Farm(String farmName, Player playerModel) { // Parameter hanya Player model
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName);
        this.playerStats = new PlayerStats(); // Atau sesuai konstruktor PlayerStats yang tersedia
        this.gameClock = new GameClock(); // Atau sesuai konstruktor GameClock
        // Inisialisasi PlayerView TIDAK LAGI dilakukan di sini
        // this.playerView = new PlayerView(playerModel, ???); // DIHAPUS
        this.currentDay = 1; // Inisialisasi hari pertama
        this.currentWeather = Weather.SUNNY; // Inisialisasi cuaca awal
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

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public GameClock getGameClock() {
        return gameClock;
    }

        // Method untuk menambahkan CookingInProgress ke list
    public void addActiveCooking(CookingInProgress cookingTask) {
        if (cookingTask != null) {
            activeCookings.add(cookingTask);
        }
    }

        // (Pastikan ada getter jika dibutuhkan di tempat lain)
    public List<CookingInProgress> getActiveCookings() {
        return activeCookings;
    }

    // Add this method to provide access to the FarmMap
    public FarmMap getFarmMap() {
        return this.farmMap;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public LocalTime getCurrentTime() {
        return gameClock.getCurrentTime();
    }

    public void setCurrentTime(LocalTime newTime) {
        gameClock.setCurrentTime(newTime);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentSeason(Season nextSeason) {
        this.currentSeason = nextSeason;
    }

    public void setCurrentWeather(Weather nextWeather) {
        this.currentWeather = nextWeather;
    }


    public InteractableObject getObjectAtTile(int mapIndex, int col, int row, int tileSize) {
        if (mapIndex < 0 || mapIndex >= objects.length) return null;
        for (InteractableObject obj : objects[mapIndex]) {
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == col && objRow == row) {
                    return obj;
                }
            }
        }
        return null;
    }

   
    public boolean removeObjectAtTile(int mapIndex, int col, int row, int tileSize) {
        if (mapIndex < 0 || mapIndex >= objects.length) return false;
        for (int i = 0; i < objects[mapIndex].length; i++) {
            InteractableObject obj = objects[mapIndex][i];
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == col && objRow == row) {
                    objects[mapIndex][i] = null; 
                    return true;
                }
            }
        }
        return false;
    }
    
}
