package org.example.model.Items;

import org.example.model.enums.Season; // Ensure the Season class exists in this package or update the package path
import org.example.model.GameTime;
import org.example.model.Location;
import org.example.model.enums.FishType;
import org.example.model.enums.Weather;


public class Fish extends Items {
    private Season season;
    private FishType fishType;
    private Weather weather;
    private Location location;
    private GameTime time;

    public Fish(String name, int sellprice, int buyprice, Season season, FishType fishType, Weather weather, Location location, GameTime time) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.fishType = fishType;
        this.weather = weather;
        this.location = location;
        this.time = time;
    }

   
    public Season getSeason() {
        return season;
    }
    public void setSeason(Season season) {
        
        this.season = season;
    }
    public FishType getFishType() {
        return fishType;
    }
    public void setFishType(FishType fishType) {
        
        this.fishType = fishType;
    }
    public Weather getWeather() {
        return weather;
    }
    public void setWeather(Weather weather) {
        
        this.weather = weather;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        
        this.location = location;
    }
    public GameTime getTime() {
        return time;
    }
    public void setTime(GameTime time) {
        
        this.time = time;
    }
}