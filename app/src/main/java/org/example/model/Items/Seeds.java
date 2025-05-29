// Lokasi: org.example.model.Items.Seeds.java
package org.example.model.Items;

import org.example.model.enums.Season;
import javax.imageio.ImageIO; // Import yang mungkin dibutuhkan
import java.io.IOException;   // Import yang mungkin dibutuhkan
import org.example.controller.UtilityTool; // Jika ingin scaling

public class Seeds extends Items {
    private Season plantableSeason; // Mengganti nama variabel agar lebih jelas
    private int daysToHarvest;
    private String producesCropName; // Nama crop yang dihasilkan (String)

    public Seeds(String name, int sellPrice, int buyPrice, Season plantableSeason, int daysToHarvest, String producesCropName) {
        super(name, sellPrice, buyPrice);
        this.plantableSeason = plantableSeason;
        this.daysToHarvest = daysToHarvest;
        this.producesCropName = producesCropName;
        loadImage(); // Panggil loadImage di konstruktor
    }

    @Override
    public void loadImage() {
        // String path = null;
        // // Contoh: jika nama file seed sama dengan nama itemnya (setelah normalisasi)
        // // String imageName = getName().replaceAll("\\s+", "") + "_Seeds.png"; // Misal "Parsnip Seeds" -> "Parsnip_Seeds.png"
        // // path = "/seeds/" + imageName; // Asumsi ada folder /seeds/

        // // Karena tidak ada path spesifik, kita set null atau gambar placeholder
        // // Contoh switch jika Anda punya beberapa gambar seed:
        // UtilityTool uTool = new UtilityTool();
        // switch (getName().toLowerCase()) {
        //     case "parsnip seeds":
        //         path = "/seeds/ParsnipSeeds.png"; // Ganti dengan path aktual jika ada
        //         break;
        //     case "cauliflower seeds":
        //         path = "/seeds/CauliflowerSeeds.png";
        //         break;
        //     // Tambahkan case untuk seed lain
        //     default:
        //         System.out.println("Seeds.loadImage(): No specific image path for " + getName() + ". Image will be null.");
        //         this.image = null; // Tidak ada gambar spesifik
        //         return;
        // }

        // try {
        //     this.image = ImageIO.read(getClass().getResourceAsStream(path));
        //     if (this.image == null) {
        //         System.err.println("Seeds.loadImage(): Failed to load image from path: " + path + " for " + getName());
        //     } else {
        //         this.image = uTool.scaleImage(this.image, 32, 32); // Sesuaikan ukuran target
        //     }
        // } catch (IOException e) {
        //     System.err.println("Error (IOException) loading image for Seed " + getName() + " from path " + path + ": " + e.getMessage());
        //     this.image = null;
        // } catch (IllegalArgumentException e) {
        //     System.err.println("Error (IllegalArgumentException - likely invalid path) loading image for Seed " + getName() + " from path " + path + ": " + e.getMessage());
        //     this.image = null;
        // }
    }

    public Season getPlantableSeason() { // Nama getter disesuaikan
        return plantableSeason;
    }

    public void setPlantableSeason(Season plantableSeason) { // Nama setter disesuaikan
        this.plantableSeason = plantableSeason;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    public String getProducesCropName() {
        return producesCropName;
    }

    public void setProducesCropName(String producesCropName) {
        this.producesCropName = producesCropName;
    }

    public boolean canPlantInSeason(Season currentSeason) {
        return this.plantableSeason == Season.ALL || this.plantableSeason == currentSeason;
    }
}