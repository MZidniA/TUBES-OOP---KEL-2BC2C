package org.example.model;

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

    // Atribut baru untuk menyimpan state waktu, hari, musim, dan cuaca saat ini
    private int currentDay;
    private Season currentSeason;
    private Weather currentWeather;
    private LocalTime currentTime; // Waktu aktual saat ini dalam game

    public Farm(String farmName, Player player, List<NPC> npclist) {
        this.farmName = farmName;
        this.player = player;

        if (this.player != null) {
            this.player.setFarmname(this.farmName);
        }

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
