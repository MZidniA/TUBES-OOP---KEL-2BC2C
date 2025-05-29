package org.example.model.Map;

import java.util.ArrayList;
import java.util.List;

import org.example.controller.action.Action;
import org.example.model.Items.Seeds;
import org.example.model.Player;
import org.example.model.enums.Season;
import org.example.model.enums.Weather; 

public class Plantedland extends Tile {
    private Seeds plantedSeed;
    private int daysSincePlanted;
    private boolean wateredToday;
    private int currentGrowthStage;
    private boolean isDead;
    private int daysSinceLastWatering; 

    public Plantedland(int x, int y, Seeds seed) {
        super(x, y, true, 's'); 
        if (seed == null) { 
            throw new IllegalArgumentException("Seed cannot be null");
        }
        this.plantedSeed = seed;
        this.daysSincePlanted = 0;
        this.wateredToday = false; 
        this.currentGrowthStage = 0;
        this.isDead = false;
        this.daysSinceLastWatering = 0; 
    }


    public boolean isWateredToday() { return wateredToday; }
    public Seeds getPlantedSeed() { return plantedSeed; }
    public boolean isDead() { return isDead; }
    public int getCurrentGrowthStage() { return currentGrowthStage; }
    public int getDaysSincePlanted() { return daysSincePlanted; }
    public int getDaysSinceLastWatering() { return daysSinceLastWatering; }


    public void setWatered(boolean watered) {
        this.wateredToday = watered;
        if (watered) {
            this.daysSinceLastWatering = 0; 
        }
    }

  
    public void dailyGrow(Season currentSeason, Weather currentWeather) { 
        if (isDead || plantedSeed == null) {
            return;
        }

        if (!plantedSeed.isPlantableInSeason(currentSeason)) {
            this.isDead = true;
            System.out.println(plantedSeed.getName() + " di (" + getX() + "," + getY() + ") mati (di luar musim).");
            return; 
        }

        boolean canGrowToday = true;

        if (currentWeather == Weather.SUNNY) {
            if (!wateredToday) {
                daysSinceLastWatering++;
                System.out.println("Tanaman di ("+getX()+","+getY()+") tidak disiram hari ini (Sunny). daysSinceLastWatering: " + daysSinceLastWatering);
            }
            if (daysSinceLastWatering >= 2) {
                canGrowToday = false;
                System.out.println(plantedSeed.getName() + " di (" + getX() + "," + getY() + ") tidak tumbuh (kurang air - Sunny).");
            }
        } else if (currentWeather == Weather.RAINY) {

            this.daysSinceLastWatering = 0;
            this.wateredToday = true; 
            System.out.println("Tanaman di ("+getX()+","+getY()+") tersiram oleh hujan.");
        }


        if (canGrowToday && !isHarvestable()) {
            daysSincePlanted++;
            updateGrowthStage(); 
            System.out.println(plantedSeed.getName() + " di ("+getX()+","+getY()+") tumbuh. Total hari: " + daysSincePlanted + ", Stage: " + currentGrowthStage);
        }
        
        this.wateredToday = false;
    }

    private void updateGrowthStage() {
        if (plantedSeed == null || isDead) return;
        int totalDaysToGrow = plantedSeed.getDaysToHarvest();
        if (totalDaysToGrow <= 0) {
            this.currentGrowthStage = (daysSincePlanted >= totalDaysToGrow) ? 4 : 0; 
            return;
        }
        double progress = (double) daysSincePlanted / totalDaysToGrow;
        if (progress >= 1.0) this.currentGrowthStage = 4;
        else if (progress > 0.66) this.currentGrowthStage = 3;
        else if (progress > 0.33) this.currentGrowthStage = 2;
        else if (progress > 0) this.currentGrowthStage = 1;
        else this.currentGrowthStage = 0;
    }

    public boolean isHarvestable() {
        if (isDead || plantedSeed == null) return false;
        return daysSincePlanted >= plantedSeed.getDaysToHarvest() && currentGrowthStage == 4; 
    }

    @Override
    public List<Action> getActions(Player player) {
        return new ArrayList<>();
    }
}