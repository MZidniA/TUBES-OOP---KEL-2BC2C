package org.example.model.Items;

import java.io.IOException;
import javax.imageio.ImageIO;
import org.example.controller.UtilityTool;

public class Food extends Items implements EdibleItem {
<<<<<<< Updated upstream
    private int recoverenergy;
    private boolean fish; // Tambahkan field ini

    public Food(String name, int sellprice, int buyprice, int recoverenergy, boolean fish) {
        super(name, sellprice, buyprice);
        this.recoverenergy = recoverenergy;
        this.fish = fish; // Set dari konstruktor
    }

    // Konstruktor lama (jika ada) tetap, default fish = false
    public Food(String name, int sellprice, int buyprice, int recoverenergy) {
        this(name, sellprice, buyprice, recoverenergy, false);
=======
    private int recoverEnergy; // Mengganti nama variabel agar konsisten dengan getter/setter

    public Food(String name, int sellPrice, int buyPrice, int recoverEnergy) {
        super(name, sellPrice, buyPrice);
        this.recoverEnergy = recoverEnergy;
        loadImage();
>>>>>>> Stashed changes
    }

    public int getRecoverEnergy() { // Nama metode disesuaikan
        return recoverEnergy;
    }

    public void setRecoverEnergy(int recoverEnergy) { // Nama metode disesuaikan
        this.recoverEnergy = recoverEnergy;
    }

    @Override
    public int getEnergyRestored() {
        return recoverEnergy;
    }

    // getName() sudah diwarisi dari Items, override ini tidak menambahkan fungsionalitas baru
    // @Override
    // public String getName() {
    //     return super.getName();
    // }

    @Override
    public void loadImage() { // Ubah ke protected agar konsisten dengan Items.java
        String path = null;
        UtilityTool uTool = new UtilityTool();

        switch (getName().toLowerCase()) {
            case "fish n’ chips":
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
            case "pumpkin pie":
                path = "/food/Pumpkin_Pie.png";
                break;
            case "veggie soup":
                path = "/food/Veggie_Soup.png";
                break;
            case "fish stew":
                path = "/food/Fish_Stew.png";
                break;
            case "spakbor salad":
                path = "/food/Spakbor_Salad.png";
                break;
            case "fish sandwich":
                path = "/food/Fish_Sandwich.png";
                break;
            case "the legends of spakbor":
                path = "/food/Fish_Sandwich.png"; // PASTIKAN NAMA FILE INI BENAR
                break;
            case "cooked pig’s head":
                path = "/food/Pighead.png"; 
                break;
            case "egg":
                path = "/food/Egg.png";
                break;
            default:
                System.err.println("Food.loadImage(): No image path defined for food: " + getName());
                this.image = null;
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Food.loadImage(): Failed to load image from path: " + path + " for " + getName());
            } else {
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error (IOException) loading image for Food " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Error (IllegalArgumentException - likely invalid path) loading image for Food " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }

<<<<<<< Updated upstream
    public void initialize() {
        // Optional: implementasi jika diperlukan
    }

    // IMPLEMENTASI isFish
    public boolean isFish() {
        return fish;
    }

    // Setter jika ingin mengubah status fish
    public void setFish(boolean fish) {
        this.fish = fish;
    }
}
=======
    // Metode initialize() ini tampaknya tidak digunakan dan bisa dihapus jika tidak ada implementasi khusus.
    // public void initialize() {
    // }
}
>>>>>>> Stashed changes
