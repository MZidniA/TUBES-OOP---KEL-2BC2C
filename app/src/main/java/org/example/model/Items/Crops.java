package org.example.model.Items;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.UtilityTool;

public class Crops extends Items implements EdibleItem {
    private int jumlahcropperpanen;
    private int energyRestored; 

    public Crops(String name, int sellprice, int buyprice, int jumlahcropperpanen, int energyRestored) {
        super(name, sellprice, buyprice);
        this.jumlahcropperpanen = jumlahcropperpanen;
        this.energyRestored = energyRestored;
    }

    public int getJumlahcropperpanen() {
        return jumlahcropperpanen;
    }

    public void setJumlahcropperpanen(int jumlahcropperpanen) {
        this.jumlahcropperpanen = jumlahcropperpanen;
    }

    public void loadImage() {
        String path = null;
        String lowerName = getName().toLowerCase().replace(" ", "");

        switch (lowerName) {
            case "parsnip":
                path = "/crops/Parsnip.png";
                break;
            case "cauliflower":
                path = "/crops/Cauliflower.png";
                break;
            case "potato":
                path = "/crops/Potato.png";
                break;
            case "wheat":
                path = "/crops/Wheat.png";
                break;
            case "blueberry":
                path = "/crops/Blueberry.png";
                break;
            case "tomato":
                path = "/crops/Tomato.png";
                break;
            case "hotpepper":
                path = "/crops/Hot_Pepper.png";
                break;
            case "melon":
                path = "/crops/Melon.png";
                break;
            case "cranberry":
                path = "/crops/Cranberry.png";
                break;
            case "pumpkin":
                path = "/crops/Pumpkin.png";
                break;
            case "grape":
                path = "/crops/Grape.png";
                break;
            default:
                this.image = null;
                System.err.println("No image available for crop: " + getName());
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

    @Override
    public String getName() {
        return super.getName();
    }
}