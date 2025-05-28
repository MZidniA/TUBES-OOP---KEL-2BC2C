package org.example.controller.action;

import org.example.controller.GameController;
import org.example.view.tile.TileManager;
import org.example.controller.UtilityTool; // Import UtilityTool
import org.example.model.Farm;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Player;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.UnplantedTileObject; // Pastikan diimport

public class TillingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5;

    private final GameController gameController;
    private final int targetCol;
    private final int targetRow;

    public TillingAction(GameController controller, int targetCol, int targetRow) {
        this.gameController = controller;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    @Override
    public String getActionName() {
        return "Membajak Tanah (Till)";
    }

    @Override
    public boolean canExecute(Farm farm) {
        // --- Logika canExecute() tetap sama seperti versi terakhir yang memvalidasi: ---
        // 1. Energi pemain
        // 2. Pemain memegang Hoe dan ada di inventory
        // 3. Target koordinat valid
        // 4. Target berada dalam area cangkul yang diizinkan (untuk map 0)
        // 5. Tile target adalah TILE_ID_BISA_DICANGKUL_UTAMA (ID 90)
        // 6. Tidak ada objek lain (terutama yang solid) di atas tile target

        if (farm == null || gameController == null) return false;
        Player player = farm.getPlayerModel();
        if (player == null) return false;

        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            System.out.println("TillingAction: Energi terlalu rendah.");
            return false;
        }
        Items heldItem = player.getCurrentHeldItem();
        if (heldItem == null || !heldItem.getName().equalsIgnoreCase("Hoe")) {
            System.out.println("TillingAction: Bukan Hoe yang dipegang.");
            return false;
        }
        if (!player.getInventory().hasItem(ItemDatabase.getItem("Hoe"), 1)) {
            System.out.println("TillingAction: Hoe tidak ada di inventory.");
            return false;
        }
        if (targetCol < 0 || targetCol >= gameController.getMaxWorldCol() || 
            targetRow < 0 || targetRow >= gameController.getMaxWorldRow()) {
            System.out.println("LOG: Target cangkul di luar peta.");
            return false; 
        }
        int currentMap = farm.getCurrentMap();
        if (currentMap == 0) {
            final int MIN_COL = gameController.getTillableAreaMinCol(currentMap);
            final int MAX_COL = gameController.getTillableAreaMaxCol(currentMap);
            final int MIN_ROW = gameController.getTillableAreaMinRow(currentMap);
            final int MAX_ROW = gameController.getTillableAreaMaxRow(currentMap);
            if (!(targetCol >= MIN_COL && targetCol <= MAX_COL && targetRow >= MIN_ROW && targetRow <= MAX_ROW)) {
                System.out.println("DEBUG: Tilling target ("+targetCol+","+targetRow+") OUTSIDE defined area ["+MIN_COL+"-"+MAX_COL+", "+MIN_ROW+"-"+MAX_ROW+"]");
                return false;
            }
        } else { 
            // System.out.println("TillingAction: Mencangkul hanya diizinkan di Farm (map 0).");
            return false; 
        }
        TileManager tileM = gameController.getTileManager();
        if (tileM == null) return false;
        int tileIDTarget = tileM.mapTileNum[currentMap][targetCol][targetRow];
        
        InteractableObject[] objectsOnMap = farm.getObjectsForCurrentMap();
        if (objectsOnMap != null) {
            for (InteractableObject objCheck : objectsOnMap) {
                if (objCheck != null &&
                    (objCheck.worldX / gameController.getTileSize() == targetCol) && 
                    (objCheck.worldY / gameController.getTileSize() == targetRow)) {
                    // System.out.println("TillingAction: Sudah ada objek (" + objCheck.name + ") di tile target.");
                    return false;
                }
            }
        }
        return true; // Jika semua pengecekan lolos
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            return;
        }

        Player player = farm.getPlayerModel();
        int currentMap = farm.getCurrentMap();
        int tileSize = gameController.getTileSize();

        player.decreaseEnergy(ENERGY_COST);
        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            player.setPassedOut(true); // Jika energi habis, lakukan sleeping action
            return; 
        }

        InteractableObject[][] allFarmObjects = farm.getAllObjects();
        int emptySlotIndex = -1;
        for (int i = 0; i < allFarmObjects[currentMap].length; i++) {
            if (allFarmObjects[currentMap][i] == null) {
                emptySlotIndex = i;
                break;
            }
        }

        if (emptySlotIndex != -1) {
            UnplantedTileObject tilledObject = new UnplantedTileObject(); // Membuat objek baru
            
            if (tilledObject.image != null) { // Gambar sudah di-load oleh konstruktor UnplantedTileObject
                UtilityTool uTool = new UtilityTool();
                // Lakukan scaling gambar UnplantedTileObject di sini
                tilledObject.image = uTool.scaleImage(tilledObject.image, tileSize, tileSize);
            } else {
                System.err.println("TillingAction: Gambar untuk UnplantedTileObject gagal dimuat!");
            }
            
            tilledObject.worldX = targetCol * tileSize;
            tilledObject.worldY = targetRow * tileSize;
            tilledObject.collision = false; // Tanah tercangkul biasanya tidak solid

            allFarmObjects[currentMap][emptySlotIndex] = tilledObject; // Tambahkan objek ke Model

            // Opsional: Mengubah tile di bawah UnplantedTileObject (di TileManager) menjadi tanah dasar
            // TileManager tileM = gameController.getTileManager();
            // if (tileM != null && tileM.TILE_ID_TANAH_DASAR_SETELAH_CANGKUL > 0) { // Pastikan ID valid
            //     tileM.setTile(currentMap, targetCol, targetRow, tileM.TILE_ID_TANAH_DASAR_SETELAH_CANGKUL);
            // }

            if (farm.getGameClock() != null) {
                farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
            }
            System.out.println(player.getName() + " berhasil membuat UnplantedTileObject di (" + targetCol + ", " + targetRow + ")");
        } else {
            System.err.println("TillingAction: Tidak ada slot objek kosong di map " + currentMap);
            player.increaseEnergy(ENERGY_COST); 
        }
    }
}