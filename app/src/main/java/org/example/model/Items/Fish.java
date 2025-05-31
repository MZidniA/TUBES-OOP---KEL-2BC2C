package org.example.model.Items;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import javax.imageio.ImageIO;

import org.example.controller.UtilityTool;
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

    public void loadImage() {
        String path = null;
        String lowerName = getName().toLowerCase().replace(" ", "");

        switch (lowerName) {
            case "bullhead":
                path = "/fish/Bullhead.png";
                break;
            case "carp":
                path = "/fish/Carp.png";
                break;
            case "chub":
                path = "/fish/Chub.png";
                break;
            case "largemouthbass":
                path = "/fish/LargemouthBass.png";
                break;
            case "rainbowtrout":
                path = "/fish/RainbowTrout.png";
                break;
            case "sturgeon":
                path = "/fish/Sturgeon.png";
                break;
            case "midnightcarp":
                path = "/fish/MidnightCarp.png";
                break;
            case "flounder":
                path = "/fish/Flounder.png";
                break;
            case "halibut":
                path = "/fish/Halibut.png";
                break;
            case "octopus":
                path = "/fish/Octopus.png";
                break;
            case "pufferfish":
                path = "/fish/Pufferfish.png";
                break;
            case "sardine":
                path = "/fish/Sardine.png";
                break;
            case "supercucumber":
                path = "/fish/SuperCucumber.png";
                break;
            case "catfish":
                path = "/fish/Catfish.png";
                break;
            case "salmon":
                path = "/fish/Salmon.png";
                break;
            case "angler":
                path = "/fish/Angler.png";
                break;
            case "crimsonfish":
                path = "/fish/Crimsonfish.png";
                break;
            case "glacierfish":
                path = "/fish/Glacierfish.png";
                break;
            case "legend":
                path = "/fish/Legend.png";
                break;
            default:
                this.image = null;
                System.err.println("No image available for fish: " + getName());
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Failed to load image for " + getName() + " at path: " + path);
            } else {
                UtilityTool uTool = new UtilityTool();
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image path for " + getName() + ": " + e.getMessage());
            this.image = null;
        }
    }

    @Override
    public int getEnergyRestored() {
        return energyRestored;
    }
}