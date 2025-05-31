package org.example.model.Items;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.UtilityTool;

public class Food extends Items implements EdibleItem {
    private int recoverenergy;

    public Food(String name, int sellprice, int buyprice, int recoverenergy) {
        super(name, sellprice, buyprice);
        this.recoverenergy = recoverenergy;
    }

    public int getrecoverenergy() {
        return recoverenergy;
    }

    public void setrecoverenergy(int recoverenergy) {
        this.recoverenergy = recoverenergy;
    }

    @Override
    public int getEnergyRestored() {
        return recoverenergy;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void initialize() {
        
    }

    public void loadImage() {
        String path = null;
        String normalizedName = getName().toLowerCase()
                                .replace(" ", "")
                                .replace("'", "")
                                .replace("’", ""); // hilangkan spasi dan tanda kutip

        switch (normalizedName) {
            case "fishnchips":
                path = "/food/Fishnchips.png";
                break;
            case "baguette":
                path = "/food/Baguette.png";
                break;
            case "sashimi":
                path = "/food/Sashimi.png";
                break;
            case "fugu":
                path = "/food/Fugu.png";
                break;
            case "wine":
                path = "/food/Wine.png";
                break;
            case "pumpkinpie":
                path = "/food/Pumpkin_Pie.png";
                break;
            case "veggiesoup":
                path = "/food/Veggie_Soup.png";
                break;
            case "fishstew":
                path = "/food/Fish_Stew.png";
                break;
            case "spakborsalad":
                path = "/food/Spakbor_Salad.png";
                break;
            case "fishsandwich":
                path = "/food/Fish_Sandwich.png";
                break;
            case "thelegendsofspakbor":
                path = "/food/TheLegendsOfSpakbor.png";
                break;
            case "cookedpig'shead":
            case "cookedpigheads":
                path = "/food/Pighead.png";
                break;
            default:
                this.image = null;
                System.err.println("No image available for food: " + getName());
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
}
