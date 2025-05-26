package org.example.model.Items;

import org.example.model.enums.Season;

public class Seeds extends Items {
    private Season season;
    private int daysToHarvest; // Mengganti DaystoHarvest menjadi daysToHarvest untuk konsistensi penamaan


    public Seeds(String name, int sellprice, int buyprice, Season season, int daysToHarvest) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.daysToHarvest = daysToHarvest;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    // Mengubah nama method agar konsisten dengan panggilan di Plantedland
    public int getDaysToHarvest() {
        return daysToHarvest;
    }

    // Mengubah nama method agar konsisten dengan panggilan di Plantedland
    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }

    /**
     * Memeriksa apakah bibit dapat ditanam di musim saat ini.
     * @param currentSeason Musim saat ini.
     * @return true jika bibit dapat ditanam di musim tersebut, false jika tidak.
     */
    public boolean canPlantInSeason(Season currentSeason) {
        // Jika bibit dapat ditanam di SEMUA musim atau musimnya cocok
        return this.season == Season.ALL || this.season == currentSeason;
    }

    /**
    /**
     * Menghasilkan objek Crops yang sesuai dari bibit ini.
     * @param itemDb Referensi ke ItemDatabase untuk mendapatkan objek Crops yang benar.
     * @return Objek Crops yang dihasilkan, atau null jika tidak ditemukan.
     */

}