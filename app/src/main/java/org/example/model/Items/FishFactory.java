package org.example.model.Items;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.GameTime;
import org.example.model.enums.FishType;
import org.example.model.enums.LocationType;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;

public class FishFactory {
    public static Map<String, Fish> createFish() {
        Map<String, Fish> fish = new HashMap<>();

        // === COMMON FISH ===
        fish.put("Bullhead", new Fish("Bullhead", 40, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.COMMON),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("Any", "Any")), 1));

        fish.put("Carp", new Fish("Carp", 80, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.COMMON),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.MOUNTAIN_LAKE, LocationType.POND),
            List.of(new GameTime("Any", "Any")), 1));

        fish.put("Chub", new Fish("Chub", 80, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.COMMON),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.FOREST_RIVER, LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("Any", "Any")), 1));

        // === REGULAR FISH ===
        fish.put("LargemouthBass", new Fish("LargemouthBass", 40, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("06:00", "18:00")), 1));

        fish.put("RainbowTrout", new Fish("RainbowTrout", 20, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY), EnumSet.of(LocationType.FOREST_RIVER, LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("06:00", "18:00")), 1));

        fish.put("Sturgeon", new Fish("Sturgeon", 40, 0,
            EnumSet.of(Season.SUMMER, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("06:00", "18:00")), 1));

        fish.put("MidnightCarp", new Fish("MidnightCarp", 40, 0,
            EnumSet.of(Season.FALL, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.MOUNTAIN_LAKE, LocationType.POND),
            List.of(new GameTime("20:00", "02:00")), 1));

        fish.put("Flounder", new Fish("Flounder", 30, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("06:00", "22:00")), 1));

        fish.put("Halibut", new Fish("Halibut", 40, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(
                new GameTime("06:00", "11:00"),
                new GameTime("19:00", "02:00")
            ), 1));

        fish.put("Octopus", new Fish("Octopus", 20, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("06:00", "22:00")), 1));

        fish.put("Pufferfish", new Fish("Pufferfish", 10, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("00:00", "16:00")), 1));

        fish.put("Sardine", new Fish("Sardine", 40, 0,
            EnumSet.allOf(Season.class), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("06:00", "18:00")), 1));

        fish.put("SuperCucumber", new Fish("SuperCucumber", 15, 0,
            EnumSet.of(Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("18:00", "02:00")), 1));

        fish.put("Catfish", new Fish("Catfish", 20, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.RAINY), EnumSet.of(LocationType.FOREST_RIVER, LocationType.POND),
            List.of(new GameTime("06:00", "22:00")), 1));

        fish.put("Salmon", new Fish("Salmon", 40, 0,
            EnumSet.of(Season.FALL), EnumSet.of(FishType.REGULAR),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.FOREST_RIVER),
            List.of(new GameTime("06:00", "18:00")), 1));

        // === LEGENDARY FISH ===
        fish.put("Angler", new Fish("Angler", 100, 0,
            EnumSet.of(Season.FALL), EnumSet.of(FishType.LEGENDARY),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.POND),
            List.of(new GameTime("08:00", "20:00")), 1));

        fish.put("Crimsonfish", new Fish("Crimsonfish", 100, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.LEGENDARY),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.OCEAN),
            List.of(new GameTime("08:00", "20:00")), 1));

        fish.put("Glacierfish", new Fish("Glacierfish", 100, 0,
            EnumSet.of(Season.WINTER), EnumSet.of(FishType.LEGENDARY),
            EnumSet.allOf(Weather.class), EnumSet.of(LocationType.FOREST_RIVER),
            List.of(new GameTime("08:00", "20:00")), 1));

        fish.put("Legend", new Fish("Legend", 100, 0,
            EnumSet.of(Season.SPRING), EnumSet.of(FishType.LEGENDARY),
            EnumSet.of(Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            List.of(new GameTime("08:00", "20:00")), 1));

        return fish;
    }
}