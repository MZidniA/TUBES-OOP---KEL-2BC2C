// Lokasi: src/main/java/org/example/controller/action/TillingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase;

// Asumsi ada kelas Tile dan enum TileType atau konstanta untuk jenis tile
// import org.example.model.tile.Tile;
// import org.example.model.tile.TileType;


public class TillingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5; // Dari spesifikasi "5 menit dalam game / tile"

    @Override
    public String getActionName() {
        return "Membajak Tanah (Till)";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        // Cek energi
        if (player.getEnergy() <= (ENERGY_COST - 21)) { // Mengikuti pola FishingAction untuk batas energi
            // System.out.println("LOG: Energi tidak cukup untuk membajak.");
            return false;
        }
        // Cek apakah punya Hoe
        if (!player.getInventory().hasItem(ItemDatabase.getItem("Hoe"), 1)) {
            // System.out.println("LOG: Tidak punya Hoe untuk membajak.");
            return false;
        }
            // Cek apakah tile di depan/di bawah pemain adalah Tillable Land
            // Ini memerlukan logika untuk mendapatkan tile target
            // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
            // if (targetTile == null || targetTile.getType() != TileType.TILLABLE_LAND) {
            //     System.out.println("LOG: Tidak bisa membajak tile ini.");
            //     return false;
            // }
            return true; // Placeholder, logika tile target perlu implementasi
        }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayer();
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        // Logika untuk mengubah tile menjadi Tilled Land
        // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
        // farm.getFarmMap().changeTile(targetTile.getX(), targetTile.getY(), TileType.TILLED_LAND);

        System.out.println("LOG: " + player.getName() + " membajak tanah.");
    }
}