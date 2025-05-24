// Lokasi: src/main/java/org/example/controller/action/PlantingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.Seeds;

public class PlantingAction implements Action {
    private static final int ENERGY_COST = 5; // Per seed
    private static final int TIME_COST_MINUTES = 5; // Per seed

    private Seeds seedToPlant; // Bibit yang akan ditanam, perlu diset sebelum execute

    // Constructor bisa dimodifikasi untuk menerima bibit yang akan ditanam
    public PlantingAction(Seeds seedToPlant) {
        this.seedToPlant = seedToPlant;
    }
     public PlantingAction() {
        this.seedToPlant = null; // Default, mungkin perlu diset dari UI/Controller
    }


    @Override
    public String getActionName() {
        return "Menanam Bibit (Plant)";
    }

    public void setSeedToPlant(Seeds seed) {
        this.seedToPlant = seed;
    }


    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        if (seedToPlant == null) {
            // System.out.println("LOG: Tidak ada bibit yang dipilih untuk ditanam.");
            return false;
        }
        if (player.getEnergy() <= (ENERGY_COST - 21)) {
            // System.out.println("LOG: Energi tidak cukup untuk menanam.");
            return false;
        }
        // Cek apakah punya bibit yang dipilih di inventory
        if (!player.getInventory().hasItem(seedToPlant, 1)) {
            // System.out.println("LOG: Tidak punya bibit " + seedToPlant.getName() + ".");
            return false;
        }
        // Cek apakah tile target adalah Tilled Land
        // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
        // if (targetTile == null || targetTile.getType() != TileType.TILLED_LAND) {
        //     System.out.println("LOG: Hanya bisa menanam di tanah yang sudah dibajak.");
        //     return false;
        // }
        // Cek apakah bibit sesuai dengan musim
        // if (!seedToPlant.canBePlantedIn(farm.getCurrentSeason())) {
        //     System.out.println("LOG: Bibit " + seedToPlant.getName() + " tidak bisa ditanam di musim ini.");
        //     return false;
        // }
        return true; // Placeholder
    }

    @Override
    public void execute(Farm farm) {
        if (seedToPlant == null) {
            System.out.println("LOG: Gagal menanam, bibit tidak ditentukan.");
            return;
        }
        Player player = farm.getPlayer();
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
        player.getInventory().removeItem(seedToPlant, 1); // Kurangi bibit dari inventory

        // Logika untuk mengubah tile menjadi Planted Land dengan bibit yang sesuai
        // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
        // farm.getFarmMap().plantSeedOnTile(targetTile.getX(), targetTile.getY(), seedToPlant);

        System.out.println("LOG: " + player.getName() + " menanam " + seedToPlant.getName() + ".");
    }
}