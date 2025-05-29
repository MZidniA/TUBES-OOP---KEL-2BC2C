package org.example.model;

import java.util.EnumSet;
// import org.example.model.items.Items; // Import the Items class
// Ensure the Items class exists in the correct package or update the import path
import org.example.model.Items.Items; 
import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private final int MAX_ENERGY = 100; // Tambahkan konstanta untuk energi maksimum
    private final int MIN_ENERGY_OPERATIONAL = -20;
    private boolean passedOut = false; // Energi minimum sebelum pingsan/auto-sleep
    private boolean forceSleepByTime = false; // Flag untuk memaksa tidur
    private String farmname;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private LocationType currentLocationType;// Ganti nama agar lebih jelas
    private Items currentHeldItem;// Ganti nama agar lebih jelas

    // Tambahkan field posisi tile player
    private int tileX = 0;
    private int tileY = 0;

    // Constructor
    public Player(String name, String gender, String farmname /*, NPC partner, Inventory inventory */) {
        this.name = name;
        this.gender = gender;
        this.energy = MAX_ENERGY; // Energi awal penuh
        this.farmname = farmname;
        // this.partner = partner; // Bisa di-set nanti
        this.gold = 500; // Modal awal, sesuaikan
        this.inventory = new Inventory(); // Inisialisasi inventory kosong atau default
        this.currentLocationType = LocationType.FARM; 
        this.currentHeldItem = null;// Lokasi awal
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

    // --- TAMBAHKAN GETTER DAN SETTER UNTUK FLAG ---
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

    public int getMaxEnergy() { // Getter untuk MAX_ENERGY
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
}