package org.example.model;

import org.example.model.Map.FarmMap;
import org.example.model.NPC.NPC;
import org.example.model.enums.Season; // Import Season enum
import org.example.model.enums.Weather; // Import Weather enum
import java.time.LocalTime; // Import LocalTime

import java.util.List;

public class Farm {
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
}