// // Lokasi: src/main/java/org/example/controller/action/RecoverLandAction.java
// package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.Player;
// import org.example.model.Map.FarmMap;
// import org.example.model.Map.Tile;
// import org.example.model.Map.Tilledland;
// import org.example.model.Map.Tillableland;
// import org.example.model.Items.ItemDatabase;
// import org.example.model.Items.Items;
// import org.example.model.GameClock; // Pastikan GameClock bisa diakses dari Farm

// public class RecoverLandAction implements Action {

//     private static final int ENERGY_COST_PER_TILE = 5;
//     private static final int TIME_COST_PER_TILE_MINUTES = 5;
//     private static final Items REQUIRED_ITEM = ItemDatabase.getItem("Pickaxe"); // Ambil instance Pickaxe

//     @Override
//     public String getActionName() {
//         return "Recover Land";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayer();
//         FarmMap farmMap = farm.getFarmMap(); // Asumsi Farm punya getFarmMap()

//         if (player == null || farmMap == null) {
//             // Handle case where player or map is not initialized (shouldn't happen in normal flow)
//             return false;
//         }

//         // 1. Cek Energi Player
//         if (player.getEnergy() < ENERGY_COST_PER_TILE) {
//             System.out.println("LOG: Not enough energy to recover land.");
//             return false;
//         }

//         // 2. Cek Player berada di atas Tile yang tepat (TilledLand)
//         int playerX = player.getTileX();
//         int playerY = player.getTileY();
//         Tile currentTile = farmMap.getTile(playerX, playerY);

//         if (currentTile == null) {
//             System.out.println("LOG: Cannot recover land on a null tile."); // Should not happen
//             return false;
//         }

//         // Aksi Recover Land hanya bisa dilakukan PADA TILE yang sudah di-tilled
//         if (!(currentTile instanceof Tilledland)) {
//             System.out.println("LOG: Player is not on a tilled land tile.");
//             return false;
//         }

//         // 3. Cek Inventory Player memiliki Pickaxe
//         if (REQUIRED_ITEM == null) {
//              System.out.println("ERROR: Pickaxe item definition not found in ItemDatabase.");
//              return false; // Safety check
//         }

//         if (!player.getInventory().hasItem(REQUIRED_ITEM, 1)) {
//             System.out.println("LOG: You need a Pickaxe to recover land.");
//             return false;
//         }

//         // Jika semua cek lolos
//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         // canExecute should be called before execute, but re-validating minimal things is safer
//         if (!canExecute(farm)) {
//             System.out.println("LOG: Failed to execute Recover Land action due to unmet requirements.");
//             // Mungkin tambahkan pesan spesifik kenapa gagal jika mau
//             return;
//         }

//         Player player = farm.getPlayer();
//         FarmMap farmMap = farm.getFarmMap();
//         GameClock gameClock = farm.getGameClock(); // Asumsi Farm punya getGameClock()

//         int playerX = player.getTileX();
//         int playerY = player.getTileY();
//         Tile currentTile = farmMap.getTile(playerX, playerY); // Sudah dipastikan TilledLand oleh canExecute

//         // 1. Kurangi Energi Player
//         player.decreaseEnergy(ENERGY_COST_PER_TILE);
//         System.out.println(player.getName() + " used " + ENERGY_COST_PER_TILE + " energy to recover land.");

//         // 2. Ubah Tile dari TilledLand menjadi TillableLand
//         // Ciptakan Tile baru (TillableLand) dengan koordinat yang sama
//         Tile newTillableTile = new Tillableland(playerX, playerY);
//         farmMap.setTile(playerX, playerY, newTillableTile);
//         System.out.println("Land recovered at (" + playerX + ", " + playerY + ").");

//         // 3. Majukan waktu game
//         if (gameClock != null) {
//             gameClock.advanceTimeMinutes(TIME_COST_PER_TILE_MINUTES);
//             System.out.println("Game time advanced by " + TIME_COST_PER_TILE_MINUTES + " minutes.");
//         } else {
//             System.out.println("WARNING: GameClock not available to advance time.");
//         }

//         // Pickaxe (Equipment) tidak dikonsumsi, hanya dibutuhkan untuk melakukan aksi

//         System.out.println("Action 'Recover Land' executed successfully.");
//     }
// }