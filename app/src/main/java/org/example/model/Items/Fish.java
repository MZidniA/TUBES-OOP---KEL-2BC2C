package org.example.model.Items;

import java.util.EnumSet;
import java.util.List;

import org.example.model.GameTime;
import org.example.model.enums.FishType;
import org.example.model.enums.LocationType;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;

public class Fish extends Items implements EdibleItem {
    private EnumSet<Season> season;
    private EnumSet<FishType> fishType;
    private EnumSet<Weather> weather;
    private EnumSet<LocationType> locationType;
    private List<GameTime> timeRanges; 
    private int energyRestored;

    public Fish(String name, int sellprice, int buyprice,
                EnumSet<Season> season, EnumSet<FishType> fishType,
                EnumSet<Weather> weather, EnumSet<LocationType> locationType,
                List<GameTime> timeRanges, int energyRestored) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.fishType = fishType;
        this.weather = weather;
        this.locationType = locationType;
        this.timeRanges = timeRanges;
        this.energyRestored = energyRestored;
    }

    public Fish(String name, int sellprice, int buyprice,
                EnumSet<Season> season, EnumSet<FishType> fishType,
                EnumSet<Weather> weather, EnumSet<LocationType> locationType,
                GameTime time, int energyRestored) {
        this(name, sellprice, buyprice, season, fishType, weather, locationType, List.of(time), energyRestored);
    }

    public boolean isAvailableAt(java.time.LocalTime currentTime) {
        return timeRanges.stream().anyMatch(t -> t.isWithin(currentTime));
    }

    public EnumSet<Season> getSeason() {
        return season;
    }

    public EnumSet<FishType> getFishType() {
        return fishType;
    }

    public EnumSet<Weather> getWeather() {
        return weather;
    }

    public EnumSet<LocationType> getLocationType() {
        return locationType;
    }

    public List<GameTime> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(List<GameTime> timeRanges) {
        this.timeRanges = timeRanges;
    }

    @Override
    public int getEnergyRestored() {
        return energyRestored;
    }
}