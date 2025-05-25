package org.example.model.Items;

import org.example.model.enums.Season;
import java.util.Set; // Untuk mendukung beberapa musim tanam

public class Seeds extends Items {
    private Set<Season> plantableSeasons; // Musim-musim di mana bibit ini bisa ditanam
    private int daysToHarvest;        // Nama variabel konsisten dengan Java convention (camelCase)
    private String yieldCropName;     // Nama dari Crops yang akan dihasilkan
    private int quantityPerHarvest;  // Jumlah Crops yang dihasilkan per panen
    private boolean requiresDailyWatering; // Apakah perlu disiram tiap hari

    // Constructor yang diperbarui
    public Seeds(String name, int sellPrice, int buyPrice,
                 Set<Season> plantableSeasons, int daysToHarvest,
                 String yieldCropName, int quantityPerHarvest,
                 boolean requiresDailyWatering) {
        super(name, sellPrice, buyPrice); // Harga jual bibit biasanya setengah harga beli (sesuai spesifikasi)
        if (plantableSeasons == null || plantableSeasons.isEmpty()) {
            throw new IllegalArgumentException("Plantable seasons cannot be null or empty for " + name);
        }
        if (daysToHarvest <= 0) {
            throw new IllegalArgumentException("Days to harvest must be positive for " + name);
        }
        if (yieldCropName == null || yieldCropName.trim().isEmpty()) {
            throw new IllegalArgumentException("Yield crop name cannot be null or empty for " + name);
        }
        if (quantityPerHarvest <= 0) {
            throw new IllegalArgumentException("Quantity per harvest must be positive for " + name);
        }

        this.plantableSeasons = plantableSeasons;
        this.daysToHarvest = daysToHarvest;
        this.yieldCropName = yieldCropName;
        this.quantityPerHarvest = quantityPerHarvest;
        this.requiresDailyWatering = requiresDailyWatering;
    }

    // Getter
    public Set<Season> getPlantableSeasons() {
        return plantableSeasons;
    }

    public int getDaysToHarvest() { // Nama method konsisten
        return daysToHarvest;
    }

    public String getYieldCropName() {
        return yieldCropName;
    }

    public int getQuantityPerHarvest() {
        return quantityPerHarvest;
    }

    public boolean requiresDailyWatering() {
        return requiresDailyWatering;
    }

    /**
     * Mengecek apakah bibit ini bisa ditanam pada musim tertentu.
     * @param currentSeason Musim saat ini.
     * @return true jika bisa ditanam, false jika tidak.
     */
    public boolean canPlantInSeason(Season currentSeason) {
        return plantableSeasons.contains(currentSeason);
    }

    /**
     * Menghasilkan objek Crops baru berdasarkan definisi bibit ini.
     * Ini akan dipanggil saat panen.
     * Objek Crops yang dikembalikan harus dibuat berdasarkan data dari ItemDatabase.
     * @param itemDatabase Referensi ke ItemDatabase untuk mendapatkan template Crops.
     * @return Objek Crops yang baru, atau null jika yieldCropName tidak ditemukan.
     */
    public Crops generateYield(ItemDatabase itemDatabase) {
        if (itemDatabase == null) {
            System.err.println("Error: ItemDatabase is null in Seeds.generateYield()");
            return null;
        }
        // Dapatkan template Crops dari ItemDatabase berdasarkan yieldCropName
        Items cropTemplate = itemDatabase.getItem(this.yieldCropName);

        if (cropTemplate instanceof Crops) {
            Crops template = (Crops) cropTemplate;
            // Buat instance baru dari Crops dengan kuantitas yang benar
            return new Crops(
                template.getName(),
                template.getSellprice(), // Harga jual hasil panen
                0, // Harga beli hasil panen (biasanya tidak dibeli langsung)
                this.quantityPerHarvest, // Jumlah yang dihasilkan sesuai definisi bibit
                template.getEnergyRestored()
            );
        } else {
            System.err.println("Error: Yield crop named '" + this.yieldCropName + "' not found or is not a Crop in ItemDatabase.");
            return null;
        }
    }

    // Setter mungkin tidak terlalu dibutuhkan jika Seeds adalah objek immutable setelah dibuat
    // kecuali Anda punya mekanisme untuk mengubah properti bibit (jarang)
    // public void setPlantableSeasons(Set<Season> plantableSeasons) { this.plantableSeasons = plantableSeasons; }
    // public void setDaysToHarvest(int daysToHarvest) { this.daysToHarvest = daysToHarvest; }
}