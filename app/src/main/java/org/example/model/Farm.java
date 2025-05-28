package org.example.model;

<<<<<<< HEAD
import org.example.model.Map.FarmMap;
import org.example.model.NPC.NPC;
import org.example.model.enums.Season; // Import Season enum
import org.example.model.enums.Weather; // Import Weather enum

import java.time.LocalTime; // Import LocalTime
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator; // Untuk menghapus dengan aman saat iterasi

public class Farm {
    private List<CookingInProgress> activeCookings;
    private String farmName;
    private Player player;
    private FarmMap farmMap;
    private GameClock gameClock; // Ini masih mengacu pada GameTime.java yang Anda berikan
    private List<NPC> npcList;
    private PlayerStats playerStats;
=======
import org.example.view.InteractableObject.InteractableObject;
// Tidak perlu import PlayerView lagi di sini

public class Farm {
    private final Player playerModel;
    // private final PlayerView playerView; // DIHAPUS: Farm tidak boleh tahu tentang PlayerView
    private final InteractableObject[][] objects = new InteractableObject[6][20]; // maxMap, maxObjects
    private int currentMap = 0;
>>>>>>> main

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
<<<<<<< HEAD

        this.farmMap = new FarmMap();
        this.gameClock = new GameClock(); // Ini masih mengacu pada GameTime.java
        this.playerStats = new PlayerStats();
        this.npcList = npclist;
        if (this.player != null) {
            this.player.setLocation(this.farmMap.getFarmLocation());
        }

        // Inisialisasi nilai awal untuk atribut baru
        this.currentDay = 1; // Permainan dimulai dari hari ke-1
        this.currentSeason = Season.SPRING; // Permainan dimulai dari musim semi
        this.currentWeather = Weather.SUNNY; // Cuaca awal cerah
        this.currentTime = LocalTime.of(6, 0); // Waktu awal 06.00
        this.activeCookings = new ArrayList<>(); // Inisialisasi
    }

    public String getFarmName() {
        return farmName;
    }

    public Player getPlayer() {
        return player;
    }

    public FarmMap getFarmMap() {
        return farmMap;
    }

    public GameClock getGameClock() { // Perlu diperhatikan bahwa ini mengembalikan GameTime, bukan current game time
        return gameClock;
    }

    public List<NPC> getNpcList() {
        return npcList;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    // --- Getter untuk atribut waktu, hari, musim, dan cuaca saat ini ---
    public int getCurrentDay() {
        return currentDay;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    // --- Setter untuk atribut waktu, hari, musim, dan cuaca saat ini (jika diperlukan untuk update game loop) ---
    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public void setCurrentSeason(Season currentSeason) {
        this.currentSeason = currentSeason;
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void setCurrentTime(LocalTime currentTime) {
        this.currentTime = currentTime;
    }


    public void addActiveCooking(CookingInProgress cookingTask) {
        this.activeCookings.add(cookingTask);
        System.out.println("LOG: " + cookingTask.getResultingDish().getName() + " mulai dimasak, selesai pukul " + cookingTask.getCompletionGameTime());
    }

    public List<CookingInProgress> getActiveCookings() {
        return activeCookings;
    }

    // Metode ini perlu dipanggil secara berkala oleh game loop utama atau TimeManager
    public void checkCompletedCookings(Inventory playerInventory, LocalTime currentGameTime) {
        if (playerInventory == null) return;

        Iterator<CookingInProgress> iterator = activeCookings.iterator();
        while (iterator.hasNext()) {
            CookingInProgress task = iterator.next();
            if (!task.isClaimed() && task.isCompleted(currentGameTime)) {
                playerInventory.addInventory(task.getResultingDish(), task.getQuantity());
                System.out.println("LOG: " + task.getQuantity() + "x " + task.getResultingDish().getName() + " selesai dimasak dan masuk inventory!");
                // Opsi 1: Tandai sudah diklaim (jika ingin ada notifikasi sebelum benar-benar hilang)
                task.setClaimed(true); 
                // Opsi 2: Langsung hapus setelah masuk inventory
                // iterator.remove(); 
            }
        }
        // Jika menggunakan opsi 1, perlu mekanisme lain untuk menghapus task yang sudah isClaimed()
        // Misalnya, hapus semua yang isClaimed true:
        activeCookings.removeIf(CookingInProgress::isClaimed);
    }
}
=======
    }
}
>>>>>>> main
