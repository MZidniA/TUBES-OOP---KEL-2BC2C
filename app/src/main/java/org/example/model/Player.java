package org.example.model;
import java.util.EnumSet;
import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;

public class Player {
    private String name; 
    private String gender;
    private int energy;
    private String farmname;
    private NPC partner;
    private int gold;
    private Inventory inventory;
    private LocationType locationtype;

    public Player(String name, String gender, String farmname, NPC partner, Inventory inventory, LocationType location) {
        this.name = name;
        this.gender = gender;
        this.energy = 100; 
        this.farmname = farmname;
        this.partner = partner;
        this.gold = 0; 
        this.inventory = inventory;
        this.locationtype = LocationType.FARM; 
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
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
        this.energy = energy;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
    }

    public NPC getPartner() {
        return partner;
    }

    public void setPartner(NPC partner) {
        this.partner = partner;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public LocationType getLocation() {
        return locationtype;
    }

    public void setLocation(LocationType locationtype) {
        if (locationtype == null || !EnumSet.allOf(LocationType.class).contains(locationtype)) {
            throw new IllegalArgumentException("Invalid LocationType");
        }
        this.locationtype = locationtype;
    }

    public void decreaseEnergy(int amount) {
        this.energy = Math.max(0, this.energy - amount);
    }

}