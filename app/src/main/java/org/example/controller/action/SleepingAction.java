// // Lokasi: src/main/java/org/example/controller/action/SleepingAction.java
// package org.example.controller.action;

// import java.time.LocalTime;

// import org.example.model.Farm;
// import org.example.model.Player;
// import org.example.model.Items.ItemDatabase;

// public class SleepingAction implements Action {
//     // Tidak ada energy cost, malah memulihkan energi
//     // Time cost adalah signifikan (skip ke pagi berikutnya)

//     private void skipToMorning(Farm farm) {
//         LocalTime now = farm.getGameClock().getCurrentTime();
//         int nowMinutes = now.getHour() * 60 + now.getMinute();
//         int morningMinutes = 6 * 60;

//         int minutesToSkip;
//         if (nowMinutes >= morningMinutes) {
//             minutesToSkip = (24 * 60 - nowMinutes) + morningMinutes;
//         } else {
//             minutesToSkip = morningMinutes - nowMinutes;
//         }

//         farm.getGameClock().advanceTimeMinutes(minutesToSkip);
//     }
    
//     @Override
//     public String getActionName() {
//         return "Tidur (Sleep)";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         Player player = farm.getPlayer();

//         boolean hasQueenBed = player.getInventory().hasItem(ItemDatabase.getItem("Queen Bed"), 1);
//         boolean hasKingBed = player.getInventory().hasItem(ItemDatabase.getItem("King Bed"), 1);
//         if (!hasQueenBed && !hasKingBed) {
//             System.out.println("Butuh Bed untuk tidur");
//             return false;
//         }

//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayer();

//         if (!canExecute(farm)) return;

//         int maxEnergy = player.getMaxEnergy();
//         int currentEnergy = player.getEnergy();

//         // Cek tipe kasur untuk efek bonus
//         boolean hasKingBed = player.getInventory().hasItem(ItemDatabase.getItem("King Bed"), 1);

//         System.out.println("Kamu memutuskan untuk tidur...");

//         // Logika pemulihan energi
//         if (currentEnergy == 0) {
//             player.setEnergy(10);
//             System.out.println("Energi habis total. Tidur hanya memulihkan 10 poin.");
//         } else if (currentEnergy < (0.1 * maxEnergy)) {
//             player.setEnergy(maxEnergy / 2);
//             System.out.println("Energi terlalu rendah. Hanya terisi setengah.");
//         } else if (hasKingBed) {
//             player.setEnergy((int) (maxEnergy * 1.1));
//             System.out.println("idur super nyenyak di King Bed. Energi jadi 110%!");
//         } else {
//             player.setEnergy(maxEnergy);
//             System.out.println("Tidur nyenyak. Energi pulih sepenuhnya.");
//         }

//         // Time skip ke jam 06.00
//         skipToMorning(farm);

//         // Lanjut ke hari berikutnya
//         farm.getGameClock().nextDay(null);
//         System.out.println("Selamat pagi! Hari ke-" + farm.getGameClock().getDay()
//                 + ", pukul " + farm.getGameClock().getCurrentTime());
//     }
// }
