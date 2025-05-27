package org.example.model.Items;

import org.example.model.enums.Season;
import org.example.model.enums.FishType;
import org.example.model.enums.Weather;
import org.example.model.enums.LocationType;
import org.example.model.GameTime;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class FishFactory {
    public static Map<String, Fish> createFish() {
        Map<String, Fish> fish = new HashMap<>();

        
        fish.put("Angler", new Fish(
            "Angler", 800, 0,
            EnumSet.of(Season.FALL), EnumSet.of(FishType.LEGENDARY),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.POND),
            new GameTime("08:00", "20:00"), 1
        ));

        fish.put("Crimsonfish", new Fish(
            "Crimsonfish", 800, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.LEGENDARY),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("08:00", "20:00"),1
        ));

        fish.put("Glacierfish", new Fish(
            "Glacierfish", 800, 0,
            EnumSet.of(Season.WINTER), EnumSet.of(FishType.LEGENDARY),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.FOREST_RIVER),
            new GameTime("08:00", "20:00"),1
        ));

        fish.put("Legend", new Fish(
            "Legend", 1600, 0,
            EnumSet.of(Season.SPRING), EnumSet.of(FishType.LEGENDARY),
            EnumSet.of(Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("08:00", "20:00"),1
        ));

       
        fish.put("Largemouth Bass", new Fish(
            "Largemouth Bass", 40, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("06:00", "18:00"),1
        ));

        fish.put("Rainbow Trout", new Fish(
            "Rainbow Trout", 160, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY), EnumSet.of(LocationType.FOREST_RIVER),
            new GameTime("06:00", "18:00"),1
        ));

        fish.put("Sturgeon", new Fish(
            "Sturgeon", 40, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("06:00", "18:00"),1
        ));

        fish.put("Midnight Carp", new Fish(
            "Midnight Carp", 80, 0,
            EnumSet.of(Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("20:00", "02:00"),1
        ));

        fish.put("Flounder", new Fish(
            "Flounder", 60, 0,
            EnumSet.of(Season.SPRING), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("06:00", "22:00"),1
        ));

        fish.put("Halibut", new Fish(
            "Halibut", 40, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("06:00", "11:00"),1
        ));

        fish.put("Octopus", new Fish(
            "Octopus", 120, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("06:00", "22:00"),1
        ));

        fish.put("Pufferfish", new Fish(
            "Pufferfish", 240, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY), EnumSet.of(LocationType.OCEAN),
            new GameTime("00:00", "16:00"),1
        ));

        fish.put("Sardine", new Fish(
            "Sardine", 40, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("06:00", "18:00"),1
        ));

        fish.put("Super Cucumber", new Fish(
            "Super Cucumber", 80, 0,
            EnumSet.of(Season.SUMMER), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.OCEAN),
            new GameTime("18:00", "02:00"),1
        ));

        fish.put("Catfish", new Fish(
            "Catfish", 40, 0,
            EnumSet.of(Season.SPRING), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.RAINY), EnumSet.of(LocationType.FOREST_RIVER),
            new GameTime("06:00", "22:00"),1
        ));

        fish.put("Salmon", new Fish(
            "Salmon", 160, 0,
            EnumSet.of(Season.FALL), EnumSet.of(FishType.REGULAR),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.FOREST_RIVER),
            new GameTime("06:00", "18:00"),1
        ));

        
        fish.put("Bullhead", new Fish(
            "Bullhead", 40, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.COMMON),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("Any", "Any"),1
        ));

        fish.put("Carp", new Fish(
            "Carp", 20, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.COMMON),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.MOUNTAIN_LAKE),
            new GameTime("Any", "Any"),1
        ));

        fish.put("Chub", new Fish(
            "Chub", 20, 0,
            EnumSet.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), EnumSet.of(FishType.COMMON),
            EnumSet.of(Weather.SUNNY, Weather.RAINY), EnumSet.of(LocationType.FOREST_RIVER),
            new GameTime("Any", "Any"),1
        ));

        return fish;
    }
}
