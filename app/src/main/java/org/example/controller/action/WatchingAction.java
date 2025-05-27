//     package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.Player;
// import org.example.model.Items.ItemDatabase;
// import org.example.model.enums.LocationType;

// public class WatchingAction implements Action{

//     private static final int ENERGY_COST = 5;
//     private static final int TIME_COST_MINUTES = 15;

//     @Override
//     public String getActionName() {
//         return "Watching";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayer();

//         // Cek lokasi harus di rumah (FARM)
//         if (player.getCurrentLocationType() != LocationType.FARM) {
//             System.out.println("Menonton TV hanya bisa dilakukan di rumah.");
//             return false;
//         }

//         // Cek punya TV
//         boolean hasTV = player.getInventory().hasItem(ItemDatabase.getItem("TV"), 1);

//         if (!hasTV) {
//             System.out.println("Kamu membutuhkan TV untuk menonton.");
//             return false;
//         }

//         // Cek energi cukup
//         if (player.getEnergy() < ENERGY_COST) {
//             System.out.println("Energi tidak cukup untuk menonton TV.");
//             return false;
//         }

//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayer();

//         if (!canExecute(farm)) return;

//         // Kurangi energi
//         player.decreaseEnergy(ENERGY_COST);

//         // Tambah waktu
//         farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

//         // Tampilkan pesan
//         System.out.println("Kamu menonton acara favoritmu di TV.");
//         System.out.println("Energi berkurang 5 poin. Waktu maju 15 menit.");
//     }
// }
    

