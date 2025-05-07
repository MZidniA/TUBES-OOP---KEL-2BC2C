package org.example.model;

public class Fish extends Items {
    private Season season;
    private FishType fishType;
    private Weather weather;
    private Location location;
    private GameTime time;

   
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
