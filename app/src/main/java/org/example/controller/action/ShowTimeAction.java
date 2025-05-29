// // Lokasi: src/main/java/org/example/controller/action/ShowTimeAction.java
// package org.example.controller.action;

// import org.example.model.Farm;
// import org.example.model.enums.Season;
// import org.example.model.enums.Weather;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;

// public class ShowTimeAction implements Action {

//     @Override
//     public boolean canExecute(Farm farm) {
//         // Aksi Show Time selalu bisa dilakukan tanpa prasyarat energi atau lokasi.
//         return true;
//     }

//     @Override
//     public void execute(Farm farm) {
//         // Mendapatkan informasi waktu dari objek Farm
//         int day = farm.getCurrentDay();
//         Season season = farm.getCurrentSeason();
//         Weather weather = farm.getCurrentWeather();
//         LocalTime time = farm.getCurrentTime();

//         // Format waktu untuk tampilan yang lebih baik
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm"); // Format jam dan menit

//         System.out.println("--- Waktu Permainan ---");
//         System.out.println("Hari ke-: " + day);
//         System.out.println("Musim: " + season.toString()); // Menggunakan toString() dari enum
//         System.out.println("Cuaca: " + weather.toString()); // Menggunakan toString() dari enum
//         System.out.println("Waktu: " + time.format(formatter));
//         System.out.println("-----------------------");
// }

//     @Override
//     public String getActionName() {
//         return "Show Time";
//     }
// }