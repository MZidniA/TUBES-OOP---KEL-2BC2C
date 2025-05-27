// Lokasi: src/main/java/org/example/controller/action/HarvestingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Tile;
import org.example.model.Map.Plantedland;

public class HarvestingAction implements Action {
    private static final int ENERGY_COST = 3;
    private static final int TIME_COST_MINUTES = 5;

    public HarvestingAction() {}

    @Override
    public String getActionName() {
        return "Panen Tanaman (Harvest)";
    }

    // Ambil tile Plantedland di posisi player (atau sesuaikan dengan logika tile target)
    private Plantedland getTargetPlantedLand(Farm farm) {
        Player player = farm.getPlayer();
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null || player == null) {
            return null;
        }
        // Misal: player berdiri di tile tertentu (butuh getter posisi tile di Player)
        int tileX = player.getTileX(); // Pastikan ada getter ini di Player
        int tileY = player.getTileY();
        Tile currentTile = farmMap.getTile(tileX, tileY);
        if (currentTile instanceof Plantedland) {
            return (Plantedland) currentTile;
        }
        return null;
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayer();
        if (player.getEnergy() <= (ENERGY_COST - (player.getMinEnergyOperational() + 1))) {
            return false;
        }
        Plantedland targetPlant = getTargetPlantedLand(farm);
        if (targetPlant == null) {
            return false;
        }
        if (!targetPlant.isHarvestable()) {
            return false;
        }
        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayer();
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null) {
            System.err.println("Error: FarmMap tidak tersedia di HarvestingAction.execute");
            return;
        }
        Plantedland targetPlant = getTargetPlantedLand(farm);
        if (targetPlant == null || !targetPlant.isHarvestable()) {
            System.out.println("LOG: Gagal memanen, kondisi tidak terpenuhi saat eksekusi.");
            return;
        }
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        // Ambil hasil panen
        // Crops harvestedCrop = targetPlant.harvest(ItemDatabase.getInstance());
        // if (harvestedCrop != null) {
        //     player.getInventory().addInventory(harvestedCrop, harvestedCrop.getJumlahcropperpanen());
        //     System.out.println("LOG: " + player.getName() + " memanen " + harvestedCrop.getJumlahcropperpanen() +
        //                        " " + harvestedCrop.getName() + " dari (" + targetPlant.getX() + "," + targetPlant.getY() + ").");
        //     // Reset tile setelah panen
        //     farmMap.setTile(targetPlant.getX(), targetPlant.getY(), new Tilledland(targetPlant.getX(), targetPlant.getY()));
        // } else {
        //     System.out.println("LOG: Gagal memanen dari (" + targetPlant.getX() + "," + targetPlant.getY() + ") (tanaman tidak valid atau error).");
        // }
    }
}