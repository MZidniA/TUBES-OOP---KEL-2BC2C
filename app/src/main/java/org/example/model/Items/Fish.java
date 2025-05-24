package org.example.model.Items;

import org.example.model.enums.Season; // Ensure the Season class exists in this package or update the package path

import java.util.EnumSet;

import org.example.model.GameTime;
import org.example.model.enums.LocationType; // Update the package path to the correct one
import org.example.model.enums.FishType;
import org.example.model.enums.Weather;


public class Fish extends Items implements EdibleItem {
    private EnumSet<Season> season;
    private EnumSet<FishType> fishType;
    private EnumSet<Weather> weather;
    private EnumSet<LocationType> LocationType;
    private GameTime time;
    private int energyRestored; // Tambahkan field ini

    public Fish(String name, int sellprice, int buyprice, EnumSet<Season> season, EnumSet<FishType> fishType, EnumSet<Weather> weather, EnumSet<LocationType> LocationType, GameTime time, int energyRestored) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.fishType = fishType;
        this.weather = weather;
        this.LocationType = LocationType;
        this.time = time;
        this.energyRestored = energyRestored; // Set nilai energi
    }

   
    public EnumSet<Season> getSeason() {
        return season;
    }
    public void setSeason(EnumSet<Season> season) {
        this.season = season;
    }
    public EnumSet<FishType> getFishType() {
        return fishType;
    }
    public void setFishType(EnumSet<FishType> fishType) {
        this.fishType = fishType;
    }
    public EnumSet<Weather> getWeather() {
        return weather;
    }
    public void setWeather(EnumSet<Weather> weather) {
        this.weather = weather;
    }
    public EnumSet<LocationType> getLocationType() {
        return LocationType;
    }
    public void setLocationType(EnumSet<LocationType> LocationType) {
        this.LocationType = LocationType;
    }
    public GameTime getTime() {
        return time;
    }
    public void setTime(GameTime time) { 
        this.time = time;
    }

    @Override
    public int getEnergyRestored() {
        return energyRestored;
    }
}