package org.example.model;

import java.util.EnumSet;
 
import org.example.model.Items.Items;
import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private final int MAX_ENERGY = 100;
    private final int MIN_ENERGY_OPERATIONAL = -20;
    private boolean passedOut = false;
    private boolean forceSleepByTime = false; 
    private String farmname;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private LocationType currentLocationType;
    private Items currentHeldItem;
    private PlayerStats playerStats = new PlayerStats();

    private int tileX = 0;
    private int tileY = 0;

    public Player(String name, String gender, String farmname) {
        this.name = name;
        this.gender = gender;
        this.energy = MAX_ENERGY;
        this.farmname = farmname;
        this.gold = 500;
        this.inventory = new Inventory(); 
        this.currentLocationType = LocationType.FARM; 
        this.currentHeldItem = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energy <= MIN_ENERGY_OPERATIONAL) { 
            this.energy = MIN_ENERGY_OPERATIONAL;

            this.passedOut = true; 
            System.out.println("LOG: " + this.name + " pingsan karena kelelahan!");
        } else {
            this.energy = energy;
        }
    }

    public boolean isPassedOut() {
        return this.passedOut;
    }

    public void setPassedOut(boolean status) {
        this.passedOut = status;
    }
    public boolean isForceSleepByTime() {
        return this.forceSleepByTime;
    }

    public void setForceSleepByTime(boolean status) {
        this.forceSleepByTime = status;
    }

    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
     public int getMinEnergyOperational() {
        return MIN_ENERGY_OPERATIONAL;
    }


    public String getFarmname() { return farmname; }
    public void setFarmname(String farmname) { this.farmname = farmname; }
    public NPC getPartner() { return partner; }
    public void setPartner(NPC partner) { this.partner = partner; }
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public LocationType getCurrentLocationType() { 
        return currentLocationType;
    }

    public void setCurrentLocationType(LocationType locationType) { 
        if (locationType == null || !EnumSet.allOf(LocationType.class).contains(locationType)) {
            throw new IllegalArgumentException("Invalid LocationType: " + locationType);
        }
        this.currentLocationType = locationType;
    }

    public void setLocation(LocationType locationType) {
        if (locationType == null || !EnumSet.allOf(LocationType.class).contains(locationType)) {
            throw new IllegalArgumentException("Invalid LocationType: " + locationType);
        }
        this.currentLocationType = locationType;
    }

    public void decreaseEnergy(int amount) {
        if (amount < 0) return; 
        setEnergy(this.energy - amount); 
    }

    public void increaseEnergy(int amount) {
        if (amount < 0) return;
        setEnergy(this.energy + amount);
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }
    public void setTilePosition(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
    }
    public Items getCurrentHeldItem() {
        return currentHeldItem;
    }
    public void setCurrentHeldItem(Items currentHeldItem) {
        this.currentHeldItem = currentHeldItem;
        if (currentHeldItem != null) {
            System.out.println("Now holding: " + currentHeldItem.getName());
        } else {
            System.out.println("Hands are empty.");
        }
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(PlayerStats stats) {
        this.playerStats = stats;
    }
}