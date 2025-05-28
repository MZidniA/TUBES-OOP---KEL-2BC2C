// package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.NPC.NPC;
// import org.example.model.enums.LocationType;
// import org.example.model.enums.RelationshipStats;
// import org.example.model.Player;
// import org.example.model.Items.ItemDatabase;

// public class MarryAction implements Action{
//     private static final int ENERGY_COST = 80;

//     private void skipTo22(Farm farm) {
//         int now = farm.getGameClock().getCurrentTime().getHour() * 60 +
//                   farm.getGameClock().getCurrentTime().getMinute();
//         int target = 22 * 60;
//         int minutesToSkip = Math.max(0, target - now);
//         farm.getGameClock().advanceTimeMinutes(minutesToSkip);
//     }

//     @Override
//     public String getActionName() {
//         return "Marry";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayer();
//         NPC partner = player.getPartner();

//         if (partner == null) {
//             System.out.println("Kamu belum punya tunangan.");
//             return false;
//         }

//         if (partner.getRelationshipsStatus() != RelationshipStats.FIANCE) {
//             System.out.println(partner.getName() + " belum menjadi tunanganmu.");
//             return false;
//         }

//         if (!player.getInventory().hasItem(ItemDatabase.getItem("Proposal Ring"), 1)) {
//             System.out.println("Kamu membutuhkan Proposal Ring.");
//             return false;
//         }

//         if (player.getEnergy() < ENERGY_COST) {
//             System.out.println("Energi kamu tidak cukup untuk menikah (butuh 80).");
//             return false;
//         }

//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayer();
//         NPC partner = player.getPartner();

//         if (!canExecute(farm)) return;

//         player.decreaseEnergy(ENERGY_COST);
//         partner.setRelationshipsStatus(RelationshipStats.MARRIED);

//         skipTo22(farm);
//         player.setCurrentLocationType(LocationType.FARM);

//         System.out.println("Selamat! Kamu dan " + partner.getName() + " resmi menikah!");
//         System.out.println("Kamu menghabiskan hari bersama pasanganmu dan kembali ke rumah.");
//         System.out.println("Waktu sekarang: " + farm.getGameClock().getCurrentTime());
//     }
// }
