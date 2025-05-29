package org.example.model.Items;

import java.util.EnumSet;
import javax.imageio.ImageIO; 
import java.io.IOException;   
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
    private GameTime time;
    private int energyRestored; // Tambahkan field ini

    public Fish(String name, int sellprice, int buyprice, EnumSet<Season> season, EnumSet<FishType> fishType, EnumSet<Weather> weather, EnumSet<LocationType> locationTypeParam, GameTime time, int energyRestored) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.fishType = fishType;
        this.weather = weather;
        this.locationType = locationTypeParam;
        this.time = time;
        this.energyRestored = energyRestored; 
        loadImage(); 
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
        return locationType;
    }
    public void setLocationType(EnumSet<LocationType> locationType) {
        this.locationType = locationType;
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

    @Override
    public void loadImage() { // Implementasi metode loadImage yang diwarisi dari Items
        String path = null;
        UtilityTool uTool = new UtilityTool(); // Jika Anda ingin scaling seperti di Equipment

        // Menggunakan getName() dari superclass Items dan toLowerCase() untuk konsistensi
        switch (getName().toLowerCase()) {
            // Common Fish
            case "bullhead":
                path = "/fish/Bullhead.png";
                break;
            case "carp":
                path = "/fish/Carp.png";
                break;
            case "chub":
                path = "/fish/Chub.png";
                break;
            // Regular Fish
            case "largemouth bass": // Nama item dengan spasi
                path = "/fish/LargemouthBass.png"; // Nama file tanpa spasi, CamelCase
                break;
            case "rainbow trout":
                path = "/fish/RainbowTrout.png";
                break;
            case "sturgeon":
                path = "/fish/Sturgeon.png";
                break;
            case "midnight carp":
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
            case "super cucumber":
                path = "/fish/SuperCucumber.png";
                break;
            case "catfish":
                path = "/fish/Catfish.png";
                break;
            case "salmon":
                path = "/fish/Salmon.png";
                break;
            // Legendary Fish
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
                System.err.println("Fish.loadImage(): No image path defined for fish: " + getName());
                this.image = null; // Atau set gambar placeholder default jika ada
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Fish.loadImage(): Failed to load image from path: " + path + " for " + getName());
            } else {
                // Opsional: Scaling gambar jika diperlukan, konsisten dengan Equipment
                this.image = uTool.scaleImage(this.image, 32, 32); // Sesuaikan ukuran target (misal 32x32)
                // System.out.println("Successfully loaded and scaled image for Fish: " + getName()); // Untuk debug
            }
        } catch (IOException e) {
            System.err.println("Error (IOException) loading image for Fish " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            // Ini sering terjadi jika path resource tidak benar atau resource tidak ditemukan
            System.err.println("Error (IllegalArgumentException - likely invalid path or resource not found) loading image for Fish " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }
}