// // Lokasi: src/main/java/org/example/controller/action/ShowLocationAction.java
// package org.example.controller.action;

// import org.example.model.Farm; // Diperlukan untuk mendapatkan Player
// import org.example.model.Player; // Diperlukan untuk mendapatkan lokasi Player
// import org.example.model.enums.LocationType; // Diperlukan untuk tipe lokasi

// public class ShowLocationAction implements Action {

//     @Override
//     public boolean canExecute(Farm farm) {
//         // Aksi Show Location selalu bisa dilakukan tanpa prasyarat energi atau lokasi.
//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         Player player = farm.getPlayer(); // Mendapatkan objek Player dari Farm
//         LocationType currentLocation = player.getCurrentLocationType(); // Mendapatkan tipe lokasi pemain
//         int tileX = player.getTileX(); // Mendapatkan koordinat X pemain
//         int tileY = player.getTileY(); // Mendapatkan koordinat Y pemain

//         System.out.println("--- Lokasi Saat Ini ---");
//         System.out.println("Anda berada di: " + currentLocation.toString()); // Menampilkan tipe lokasi
        
//         // Jika pemain berada di FARM, tampilkan juga koordinat tile
//         if (currentLocation == LocationType.FARM) {
//             System.out.println("Koordinat Tile: (" + tileX + ", " + tileY + ")");
//         }
//         System.out.println("-----------------------");

//         // Dalam implementasi GUI (MVC), Anda akan memicu pembaruan pada View di sini.
//         // Misalnya, memanggil metode di GamePanel atau ViewManager untuk menampilkan informasi lokasi.
//         // Contoh placeholder untuk GUI (Anda perlu mengimplementasikan ini di layer View):
//         // GamePanel.getInstance().displayLocationInfo(currentLocation, tileX, tileY);
//         // Atau jika ada ViewManager:
//         // ViewManager.getInstance().displayLocation(currentLocation, tileX, tileY);
//     }

//     @Override
//     public String getActionName() {
//         return "Show Location";
//     }
// }