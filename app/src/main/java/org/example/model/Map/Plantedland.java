// Lokasi: src/main/java/org/example/model/Map/Plantedland.java
package org.example.model.Map; // Pastikan package ini benar

import org.example.model.Items.Crops;
import org.example.model.Items.ItemDatabase; // Diperlukan untuk generateYield
import org.example.model.Items.Seeds;    // Kelas Seeds yang sudah kita diskusikan
import org.example.model.enums.Season;     // Enum Season

public class Plantedland extends Tile { // Nama kelas Anda Plantedland (huruf kecil 'l')
    private Seeds plantedSeed;
    private int daysSincePlanted;
    private boolean wateredToday;
    private int currentGrowthStage; // 0 = bibit, ..., N = siap panen
    private boolean isDead;

    public Plantedland(int x, int y, Seeds seed) {
        super(x, y, true, 's'); // Simbol awal 's' untuk seedling
        if (seed == null) {
            throw new IllegalArgumentException("Cannot initialize Plantedland: seed cannot be null.");
        }
        this.plantedSeed = seed;
        this.daysSincePlanted = 0;
        this.wateredToday = false;
        this.currentGrowthStage = 0; // Baru ditanam
        this.isDead = false;
        // System.out.println("Planted " + seed.getName() + " at (" + x + "," + y + ")");
    }

    // Getter
    public Seeds getPlantedSeed() { return plantedSeed; }
    public boolean isWateredToday() { return wateredToday; }
    public boolean isDead() { return isDead; }
    public int getCurrentGrowthStage() { return currentGrowthStage; }

    public void setWatered(boolean watered) {
        this.wateredToday = watered;
    }

    /**
     * Dipanggil setiap akhir hari untuk memproses pertumbuhan tanaman.
     * @param currentSeason Musim saat ini.
     */
    public void dailyGrow(Season currentSeason) {
        if (isDead || plantedSeed == null) {
            return;
        }

        if (!plantedSeed.canPlantInSeason(currentSeason)) {
            this.isDead = true;
            // System.out.println(plantedSeed.getName() + " at (" + getX() + "," + getY() + ") died (out of season).");
            updateSymbolByGrowth();
            return;
        }

        boolean canGrowToday = true;
        if (plantedSeed.requiresDailyWatering() && !wateredToday) {
            canGrowToday = false;
            // Implementasi tanaman mati jika tidak disiram beberapa hari bisa ditambahkan di sini
            // (misalnya, dengan counter `daysWithoutWater`)
            // System.out.println(plantedSeed.getName() + " at (" + getX() + "," + getY() + ") was not watered.");
        }

        if (canGrowToday && !isHarvestable()) {
            daysSincePlanted++;
            updateGrowthStage();
            // System.out.println(plantedSeed.getName() + " at ("+getX()+","+getY()+") grew. Days: " + daysSincePlanted + ", Stage: " + currentGrowthStage);
        }
        wateredToday = false; // Reset untuk hari berikutnya
        updateSymbolByGrowth();
    }

    private void updateGrowthStage() {
        if (plantedSeed == null || isDead) return;

        int totalDaysToGrow = plantedSeed.getDaysToHarvest();
        if (totalDaysToGrow <= 0) { // Bibit yang tidak tumbuh atau siap instan
            this.currentGrowthStage = isHarvestable() ? 4 : 0; // Asumsi 4 stage visual, 4 = siap panen
            return;
        }

        double progress = (double) daysSincePlanted / totalDaysToGrow;
        // Contoh pembagian menjadi 4 tahap visual sebelum siap panen (total 5 stage termasuk bibit)
        // Stage 0: 0% (bibit)
        // Stage 1: >0% - 33%
        // Stage 2: >33% - 66%
        // Stage 3: >66% - <100%
        // Stage 4: 100% (siap panen)
        if (progress >= 1.0) {
            this.currentGrowthStage = 4; // Siap panen
        } else if (progress > 0.66) {
            this.currentGrowthStage = 3;
        } else if (progress > 0.33) {
            this.currentGrowthStage = 2;
        } else if (progress > 0) { // Sudah melewati tahap bibit awal
            this.currentGrowthStage = 1;
        } else {
            this.currentGrowthStage = 0; // Baru ditanam (seedling)
        }
    }


    public boolean isHarvestable() {
        if (isDead || plantedSeed == null) {
            return false;
        }
        return daysSincePlanted >= plantedSeed.getDaysToHarvest();
    }

    /**
     * Memanen tanaman dari tile ini.
     * @param itemDb Referensi ke ItemDatabase untuk membuat objek Crops yang benar.
     * @return Objek Crops hasil panen, atau null jika tidak ada yang bisa dipanen/error.
     */
    public Crops harvest(ItemDatabase itemDb) {
        if (isHarvestable() && plantedSeed != null) {
            Crops cropYield = plantedSeed.generateYield(itemDb); // Menggunakan ItemDatabase
            // Reset state tanaman di tile ini tidak dilakukan di sini, tapi oleh FarmMap
            return cropYield;
        }
        return null;
    }

    // Override getSymbol untuk merepresentasikan tahap pertumbuhan secara visual
    @Override
    public char getSymbol() {
        if (isDead) return 'x'; // Tanaman mati
        if (plantedSeed == null) return '?'; // Error state

        if (isHarvestable()) return 'H'; // Siap Panen

        // Simbol berdasarkan tahap pertumbuhan (contoh)
        switch (currentGrowthStage) {
            case 0: return 's'; // Seedling (bibit kecil)
            case 1: return 'v'; // Vegetative small (tumbuh kecil)
            case 2: return 'V'; // Vegetative medium (tumbuh sedang)
            case 3: return 'F'; // Fruiting/Flowering (hampir siap)
            default: return 'l'; // Simbol default jika tidak ada stage lain
        }
    }

    // Panggil ini setelah setiap dailyGrow atau perubahan state signifikan
    public void updateSymbolByGrowth() {
        // Method ini bisa dipanggil dari dailyGrow untuk setSymbol(),
        // atau biarkan getSymbol() yang selalu menghitungnya.
        // Untuk efisiensi, lebih baik setSymbol() saat state berubah.
        // Namun, getSymbol() dinamis lebih mudah dikelola.
        // Untuk saat ini, kita biarkan getSymbol() yang menentukannya.
    }

    @Override
    public List<Action> getActions(Player player) {
        return new ArrayList<>();
    }
}