package org.example.model.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.Items.Crops;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Items.Seeds;
import org.example.model.enums.Season;

public class Store {
    private final Map<String, Crops> cropCatalog;
    private final Map<String, Integer> cropSoldCount;
    private final Map<String, Double> demandMultipliers;
    private final Season currentSeason;

    public Store(Season season) {
        this.currentSeason = season;
        this.cropCatalog = new HashMap<>();
        this.cropSoldCount = new HashMap<>();
        this.demandMultipliers = new HashMap<>();
        loadInitialCrops();
    }

    private void loadInitialCrops() {
        Map<String, Items> allItems = ItemDatabase.getAllItems();
        for (Items item : allItems.values()) {
            if (item instanceof Crops crop) {
                addCrop(crop);
            }
        }
    }

    private void addCrop(Crops crop) {
        cropCatalog.put(crop.getName(), crop);
        cropSoldCount.put(crop.getName(), 0);
        demandMultipliers.put(crop.getName(), 1.0);
    }

    public List<Crops> getAvailableCrops() {
        return new ArrayList<>(cropCatalog.values());
    }

    public int getCropBuyPrice(String name) {
        Crops crop = cropCatalog.get(name);
        return (crop != null) ? crop.getBuyprice() : 0;
    }

    public Crops getCropByName(String name) {
        return cropCatalog.get(name);
    }

    public void recordSale(String name, int qty) {
        cropSoldCount.put(name, cropSoldCount.getOrDefault(name, 0) + qty);
    }

    public Season getSeason() {
        return this.currentSeason;
    }

    public List<Items> getAvailableSeeds() {
        List<Items> allSeeds = ItemDatabase.getItemsByCategory("Seeds");
        List<Items> filteredSeeds = new ArrayList<>();
        for (Items item : allSeeds) {
            if (item instanceof Seeds seed && seed.isPlantableInSeason(currentSeason)) {
                filteredSeeds.add(seed);
            }
        }
        return filteredSeeds;
    }
}