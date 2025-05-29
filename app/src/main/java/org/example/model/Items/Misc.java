package org.example.model.Items;

import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.UtilityTool;  // Pastikan kelas ini ada dan berfungsi

public class Misc extends Items {
    private boolean isOutsideCategory; // Anda mungkin ingin memberi nama yang lebih deskriptif untuk atribut ini

    public Misc(String name, int sellprice, int buyprice) {
        super(name, sellprice, buyprice);
        loadImage();
        this.isOutsideCategory = true;
    }


    @Override
    public void loadImage() {
        String path = null;
        UtilityTool uTool = new UtilityTool(); // Jika Anda menggunakan scaling

        switch (getName().toLowerCase()) {
            case "coal":
                path = "/misc/Coal.png"; // Path sesuai informasi Anda
                this.isOutsideCategory = false; // Contoh: Coal bukan 'di luar kategori' tapi bahan bakar/crafting
                break;
            case "firewood":
                path = "/misc/Firewood.png"; // Path sesuai informasi Anda
                this.isOutsideCategory = false; // Contoh: Firewood juga bahan bakar/crafting
                break;
            case "proposal ring":
                path = "/misc/Coal.png"; // Path sesuai informasi Anda
                this.isOutsideCategory = false; // Contoh: Coal bukan 'di luar kategori' tapi bahan bakar/crafting
                break;
            case "wood":
                path = "/misc/Firewood.png"; // Path sesuai informasi Anda
                this.isOutsideCategory = false; // Contoh: Firewood juga bahan bakar/crafting
                break;

            default:
                System.out.println("Misc.loadImage(): No specific image path defined for misc item: " + getName() + ". Image will be null.");
                this.image = null; // Atau set gambar placeholder default
                this.isOutsideCategory = true; // Default jika tidak terdefinisi secara spesifik
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Misc.loadImage(): Failed to load image from path: " + path + " for " + getName());
            } else {
                // Opsional: Scaling gambar jika diperlukan
                this.image = uTool.scaleImage(this.image, 32, 32); // Sesuaikan ukuran target
                // System.out.println("Successfully loaded and scaled image for Misc: " + getName()); // Untuk debug
            }
        } catch (IOException e) {
            System.err.println("Error (IOException) loading image for Misc " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            // Sering terjadi jika path resource salah atau resource tidak ditemukan
            System.err.println("Error (IllegalArgumentException - likely invalid path or resource not found) loading image for Misc " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }
}