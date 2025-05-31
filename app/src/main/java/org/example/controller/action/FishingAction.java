package org.example.controller.action;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.example.model.Farm;
import org.example.model.Items.Fish;
import org.example.model.Items.ItemDatabase;
import org.example.model.Player;
import org.example.model.enums.FishType;
import org.example.model.enums.LocationType;
;

public class FishingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST = 15;

    private int generateTargetNumber(FishType type) {
        Random rand = new Random();
        return switch (type) {
            case COMMON -> rand.nextInt(10) + 1;
            case REGULAR -> rand.nextInt(100) + 1;
            case LEGENDARY -> rand.nextInt(500) + 1;
        };
    }

    private int getMaxAttempts(FishType type) {
        return switch (type) {
            case COMMON, REGULAR -> 10;
            case LEGENDARY -> 7;
        };
    }

    private String getRangeText(FishType type) {
        switch (type) {
            case COMMON: return "1–10";
            case REGULAR: return "1–100";
            case LEGENDARY: return "1–500";
            default: return "unknown";
        }
    }


    @Override
    public String getActionName() {
        return "Memancing";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        LocationType location = player.getCurrentLocationType();
        boolean validLocation = location == LocationType.POND
                             || location == LocationType.OCEAN
                             || location == LocationType.MOUNTAIN_LAKE
                             || location == LocationType.FOREST_RIVER;

        return player.getEnergy() >= ENERGY_COST &&
               player.getInventory().hasItem(ItemDatabase.getItem("Fishing Rod"), 1) &&
               validLocation;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (!canExecute(farm)) {
            System.out.println("Tidak bisa memancing");
            return;
        }

        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST);
        System.out.println("Kamu mulai memancing di " + player.getCurrentLocationType() + "...");

        // Info waktu, cuaca, season, lokasi
        LocalTime now = farm.getGameClock().getCurrentTime();
        var season = farm.getGameClock().getCurrentSeason();
        var weather = farm.getGameClock().getTodayWeather();
        var location = player.getCurrentLocationType();

        // Filter ikan berdasarkan kondisi sekarang
        List<Fish> validFish = ItemDatabase.getItemsByCategory("Fish").stream()
            .filter(item -> item instanceof Fish)
            .map(item -> (Fish) item)
            .filter(f -> f.getSeason().contains(season))
            .filter(f -> f.getWeather().contains(weather))
            .filter(f -> f.getLocationType().contains(location))
            .filter(f -> f.getTimeRanges().stream().anyMatch(timeRange -> timeRange.isWithin(now)))
            .collect(Collectors.toList());
        
        if (validFish.isEmpty()) {
            System.out.println("Tdak ada ikan yang tersedia saat ini.");
            return;
        }

        // Random 1 ikan dari validFish
        Fish selectedFish = validFish.get(new Random().nextInt(validFish.size()));
        FishType type = selectedFish.getFishType().iterator().next(); // Ambil salah satu tipe aja

        int targetNumber = generateTargetNumber(type);
        int maxAttempts = getMaxAttempts(type);

        System.out.println("Jenis ikan: " + selectedFish.getName() + " (" + type + ")");
        System.out.println("Tebak angka antara " + getRangeText(type) + ". Kamu punya " + maxAttempts + " percobaan.");

        Scanner scanner = new Scanner(System.in);
        boolean success = false;

        for (int i = 1; i <= maxAttempts; i++) {
            System.out.print("Tebakan ke-" + i + ": ");
            int guess;
            try {
                guess = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka ya!");
                continue;
            }

            if (guess == targetNumber) {
                System.out.println("Berhasil! Kamu menangkap " + selectedFish.getName() + "!");
                player.getInventory().addInventory(selectedFish,1);
                success = true;
                FishType typeToRecord = selectedFish.getFishType().iterator().next();
                player.getPlayerStats().recordFishCaught(typeToRecord, selectedFish.getName()); // << TAMBAHKAN INI
                break;
            } else if (guess < targetNumber) {
                System.out.println("Terlalu kecil!");
            } else {
                System.out.println("Terlalu besar");
            }

        }

        if (!success) {
            System.out.println("Sayang sekali, ikan kabur...");
        }
        System.out.println("Waktu sekarang: " + farm.getGameClock().getCurrentTime());
        scanner.close();
    }
}