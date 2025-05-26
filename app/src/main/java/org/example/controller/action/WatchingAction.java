// Lokasi: src/main/java/org/example/controller/action/WatchingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.GameClock;
import org.example.model.enums.LocationType; // Pastikan ini diimport
import org.example.model.enums.Weather;

public class WatchingAction implements Action {

    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 15;

    public WatchingAction() {
        // Konstruktor bisa kosong
    }

    @Override
    public String getActionName() {
        return "Watch TV";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();

        if (player == null) {
            System.out.println("LOG: Player not found.");
            return false;
        }

        // 1. Cek Lokasi: Pemain harus berada di dalam rumahnya
        // Menggunakan LocationType.RUMAH_PLAYER yang baru Anda tambahkan
        if (player.getCurrentLocationType() != LocationType.RUMAH_PLAYER) {
            System.out.println("LOG: You can only watch TV inside your house (current location: " + player.getCurrentLocationType() + ").");
            return false;
        }

        // 2. Cek Energi Pemain
        if (player.getEnergy() < ENERGY_COST) {
            System.out.println("LOG: Not enough energy to watch TV. Need " + ENERGY_COST +
                               ", has " + player.getEnergy() + ".");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        // Asumsi canExecute() sudah dipanggil dan true
        Player player = farm.getPlayer();
        GameClock gameClock = farm.getGameClock();

        // 1. Kurangi Energi Pemain
        player.decreaseEnergy(ENERGY_COST);

        // 2. Majukan Waktu Game
        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_MINUTES);
        }

        // 3. Tampilkan informasi cuaca
        Weather currentWeather = gameClock.getTodayWeather(); // Asumsi getTodayWeather() ada di GameClock
        System.out.println(player.getName() + " watched TV for " + TIME_COST_MINUTES + " minutes.");
        System.out.println("The weather forecast for today is: " + currentWeather.toString().substring(0,1).toUpperCase() + currentWeather.toString().substring(1).toLowerCase()); // Format output cuaca
        System.out.println("- Energy consumed: " + ENERGY_COST);
        System.out.println("- Time advanced by " + TIME_COST_MINUTES + " minutes.");
    }
}