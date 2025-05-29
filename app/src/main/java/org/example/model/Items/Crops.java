package org.example.model.Items;

import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.UtilityTool;

public class Crops extends Items implements EdibleItem {
    private int jumlahcropperpanen;
    private int energyRestored; // Tambahkan field untuk energi yang dipulihkan

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

    @Override
    public int getEnergyRestored() {
        return energyRestored;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void loadImage() {
        String path = null;
        UtilityTool uTool = new UtilityTool();

        switch (getName().toLowerCase()) {
            case "parsnip":
                path = "/crops/Parsnip.png"; //
                break;
            case "cauliflower":
                path = "/crops/Cauliflower.png"; //
                break;
            case "potato":
                path = "/crops/Potato.png"; //
                break;
            case "wheat":
                path = "/crops/Wheat.png"; //
                break;
            case "blueberry":
                path = "/crops/Blueberry.png"; //
                break;
            case "tomato":
                path = "/crops/Tomato.png"; //
                break;
            case "hot_pepper": // Nama item dengan spasi
                path = "/crops/Hot_Pepper.png"; // Sesuaikan dengan nama file Anda, atau HotPepper.png
                break;
            case "melon":
                path = "/crops/Melon.png"; //
                break;
            case "cranberries":
                path = "/crops/Cranberries.png"; //
                break;
            case "pumpkin":
                path = "/crops/Pumpkin.png"; //
                break;
            case "grape": // atau "grapes"
                path = "/crops/Grape.png"; //
                break;

            default:
                System.err.println("Crops.loadImage(): No image path defined for crop: " + getName());
                this.image = null;
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Crops.loadImage(): Failed to load image from path: " + path + " for " + getName());
            } else {
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error (IOException) loading image for Crop " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error (IllegalArgumentException - likely invalid path) loading image for Crop " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }

}