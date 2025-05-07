package org.example.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeasonManager {
    private final Map<Season, List<Crop>> seasonCrops = new HashMap<>();
    private final Map<Season, List<Fish>> seasonFish = new HashMap<>();

    public SeasonManager() {
        // add crops
        seasonCrops.put(Season.SPRING, Arrays.asList(new Crop("Strawberry", Season.SPRING), new Crop("Tomato", Season.SPRING)));
        seasonCrops.put(Season.SUMMER, Arrays.asList(new Crop("Corn", Season.SUMMER), new Crop("Watermelon", Season.SUMMER)));
        seasonCrops.put(Season.FALL, Arrays.asList(new Crop("Pumpkin", Season.FALL), new Crop("Carrot", Season.FALL)));
        seasonCrops.put(Season.WINTER, Arrays.asList(new Crop("Cabbage", Season.WINTER)));
        
        // add fish
        seasonFish.put(Season.SPRING, Arrays.asList(new Fish("Trout", Season.SPRING), new Fish("Salmon", Season.SPRING)));
        seasonFish.put(Season.SUMMER, Arrays.asList(new Fish("Bass", Season.SUMMER), new Fish("Catfish", Season.SUMMER)));
        seasonFish.put(Season.FALL, Arrays.asList(new Fish("Salmon", Season.FALL), new Fish("Tuna", Season.FALL)));
        seasonFish.put(Season.WINTER, Arrays.asList(new Fish("Perch", Season.WINTER), new Fish("Icefish", Season.WINTER)));
    }

    public List<Crop> getCropsForSeason(Season season) {
        return seasonCrops.getOrDefault(season, Collections.emptyList());
    }

    public List<Fish> getFishForSeason(Season season) {
        return seasonFish.getOrDefault(season, Collections.emptyList());
    }
}
