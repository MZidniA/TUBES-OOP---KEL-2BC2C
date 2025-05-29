// package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.enums.LocationType;
// import org.example.model.Player;

// public class VisitingAction implements Action{
//     private static final int ENERGY_COST = 10;
//     private static final int TIME_COST_MINUTES = 15;

//     private LocationType destination;

//     public VisitingAction(LocationType destination) {
//         this.destination = destination;
//     }

//     @Override
//     public String getActionName() {
//         return "Visiting" + destination.name().replace("_", " ");
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayerModel();

//         // Tidak boleh ke lokasi yang sama (tidak perlu visiting ke tempat yang sudah ditempati)
//         if (player.getCurrentLocationType() == destination) {
//             System.out.println("Kamu sudah berada di " + destination + ".");
//             return false;
//         }

//         // Cek energi
//         if (player.getEnergy() < ENERGY_COST) {
//             System.out.println("Energi kamu tidak cukup untuk mengunjungi " + destination + ".");
//             return false;
//         }

//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayerModel();

//         if (!canExecute(farm)) return;

//         // Kurangi energi
//         player.decreaseEnergy(ENERGY_COST);

//         // Tambahkan waktu
//         farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

//         // Update lokasi
//         player.setCurrentLocationType(destination);

//         // Log info
//         System.out.println("Kamu pergi mengunjungi " + destination.name().replace("_", " ") + ".");
//         System.out.println("Energi berkurang 10. Waktu maju 15 menit.");
//     }
// }