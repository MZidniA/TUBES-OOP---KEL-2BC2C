package org.example.model;

import java.util.EnumSet;

import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private final int MAX_ENERGY = 100; // Tambahkan konstanta untuk energi maksimum
    private final int MIN_ENERGY_OPERATIONAL = -20; // Energi minimum sebelum pingsan/auto-sleep
    private String farmname;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private LocationType currentLocationType; // Ganti nama agar lebih jelas

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
        this.currentLocationType = LocationType.FARM; // Lokasi awal
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
        // Batasi energi antara MIN_ENERGY_OPERATIONAL dan MAX_ENERGY
        if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energy < MIN_ENERGY_OPERATIONAL) {
            this.energy = MIN_ENERGY_OPERATIONAL;
            // Di sini bisa ada logika tambahan jika energi mencapai batas minimum (misalnya, pingsan)
            // System.out.println("LOG: " + this.name + " pingsan karena kelelahan!");
            // (Biasanya auto-sleep akan ditangani oleh game loop/controller)
        } else {
            this.energy = energy;
        }
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

    public LocationType getCurrentLocationType() { // Ganti nama getter
        return currentLocationType;
    }

    public void setCurrentLocationType(LocationType locationType) { // Ganti nama setter
        if (locationType == null || !EnumSet.allOf(LocationType.class).contains(locationType)) {
            throw new IllegalArgumentException("Invalid LocationType: " + locationType);
        }
        this.currentLocationType = locationType;
    }

    // Tambahkan method setLocation agar sesuai dengan pemanggilan di Farm.java
    public void setLocation(LocationType locationType) {
        if (locationType == null || !EnumSet.allOf(LocationType.class).contains(locationType)) {
            throw new IllegalArgumentException("Invalid LocationType: " + locationType);
        }
        this.currentLocationType = locationType;
    }

    public void decreaseEnergy(int amount) {
        if (amount < 0) return; // Tidak bisa mengurangi dengan nilai negatif
        setEnergy(this.energy - amount); // Gunakan setEnergy agar ada validasi batas
        // System.out.println("LOG: Energi " + this.name + " berkurang " + amount + ", sisa: " + this.energy);
    }

    public void increaseEnergy(int amount) {
        if (amount < 0) return;
        setEnergy(this.energy + amount);
        // System.out.println("LOG: Energi " + this.name + " bertambah " + amount + ", menjadi: " + this.energy);
    }

    // Getter dan Setter untuk posisi tile
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
}