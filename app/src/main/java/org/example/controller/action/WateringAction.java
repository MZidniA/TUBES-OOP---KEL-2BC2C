// // Lokasi: src/main/java/org/example/controller/action/WateringAction.java
// package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.Player;
// import org.example.model.Items.ItemDatabase;

// public class WateringAction implements Action {
//     private static final int ENERGY_COST = 5; // Per tile/soil
//     private static final int TIME_COST_MINUTES = 5; // Per tile/soil

//     @Override
//     public String getActionName() {
//         return "Menyiram Tanaman (Water)";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayer();
//         if (player.getEnergy() <= (ENERGY_COST - 21)) {
//             return false;
//         }
//         // Cek apakah punya Watering Can
//         if (!player.getInventory().hasItem(ItemDatabase.getItem("Watering Can"), 1)) {
//             // System.out.println("LOG: Tidak punya Watering Can.");
//             return false;
//         }
//         // Cek apakah tile target adalah Tilled Land atau Planted Land yang perlu disiram
//         // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
//         // if (targetTile == null || !(targetTile.getType() == TileType.TILLED_LAND || targetTile.getType() == TileType.PLANTED_LAND)) {
//         //     System.out.println("LOG: Tidak ada yang bisa disiram di sini.");
//         //     return false;
//         // }
//         // if (targetTile.isWateredToday()) { // Asumsi tile punya status sudah disiram
//         //     System.out.println("LOG: Tile ini sudah disiram hari ini.");
//         //     return false;
//         // }
//         return true; // Placeholder
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayer();
//         player.decreaseEnergy(ENERGY_COST);
//         farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

//         // Logika untuk menandai tile sebagai sudah disiram
//         // Tile targetTile = farm.getTileAt(player.getFacingX(), player.getFacingY());
//         // targetTile.setWatered(true);
//         // farm.getFarmMap().updateTile(targetTile); // Jika perlu update visual tile

//         System.out.println("LOG: " + player.getName() + " menyiram tanaman.");
//     }
// }