package org.example.model.Items;

import java.io.IOException;
import java.util.EnumSet;

import javax.imageio.ImageIO;

import org.example.controller.UtilityTool;
import org.example.model.enums.Season;

public class Seeds extends Items {
    private EnumSet<Season> season;
    private int daysToHarvest; 


    public Seeds(String name, int sellprice, int buyprice, EnumSet<Season> season, int daysToHarvest) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.daysToHarvest = daysToHarvest;
        loadImage();
    }

    public EnumSet<Season> getSeason() {
        return season;
    }
    public void setSeason(EnumSet<Season> season) {
        this.season = season;
    }


    public int getDaysToHarvest() {
        return daysToHarvest;
    }


    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    public boolean isPlantableInSeason(Season season) {
        return this.season.contains(season);
    }


    public void loadImage() {
        String path = null;
        switch (getName().toLowerCase()) {
            case "parsnip seeds":
                path = "/seeds/ParsnipSeeds.png"; 
                break;
            case "cauliflower seeds":
                path = "/seeds/CauliflowerSeeds.png";
                break;
            case "potato seeds":
                path = "/seeds/PotatoSeeds.png"; 
                break;
            case "wheat seeds":
                path = "/seeds/WheatSeeds.png";
                break;
            case "blueberry seeds":
                path = "/seeds/BlueberrySeeds.png";
                break;
            case "tomato seeds":
                path = "/seeds/TomatoSeeds.png";
                break;
            case "hot pepper seeds":
                path = "/seeds/Seeds.png";
                break;
            case "melon seeds":
                path = "/seeds/MelonSeeds.png";
                break;
            case "cranberry seeds":
                path = "/seeds/CranberrySeeds.png";
                break;
            case "pumpkin seeds":
                path = "/seeds/PumpkinSeeds.png";
                break;
            case "grape seeds":
                path = "/seeds/GrapeSeeds.png";
                break;
            default:
                this.image = null;
                return;
        }

        try {
            if (path != null) {
                this.image = ImageIO.read(getClass().getResourceAsStream(path));
                if (this.image == null) {
                     System.err.println("Gagal ngeload gambar " + path + " dari " + getName());
                }
                UtilityTool uTool = new UtilityTool();
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) { 
            System.err.println("Error (likely invalid path) loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }
 

}