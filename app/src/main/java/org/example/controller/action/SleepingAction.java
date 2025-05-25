// Lokasi: src/main/java/org/example/controller/action/SleepingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;

public class SleepingAction implements Action {
    // Tidak ada energy cost, malah memulihkan energi
    // Time cost adalah signifikan (skip ke pagi berikutnya)

    @Override
    public String getActionName() {
        return "Tidur (Sleep)";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player p
        sPlayerNearBed(player)) {
        //     System.out.println("LOG: Hanya bisa tidur di tempat tidur.");
        //     return false;
        // }
        // Pemain selalu bisa tidur jika di tempat yang benar
        return true; // Placeholder, logika lokasi tidur perlu implementasi
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayer();
        // Logika pemulihan energi
        int energyBeforeSleep = player.getEnergy();
        // Sesuai spesifikasi:
        // - Isi ulang energi penuh.
        // - Namun, jika tidur di saat energi sudah menipis (misal < 0 atau kondisi tertentu),
        //   maka akan dikenai penalti di mana energi yang terisi ulang hanya setengah penuh.
        // - Jika waktu telah mencapai pukul 02.00 dan Player belum tidur, maka akan segera otomatis pergi tidur.

        // Penalti jika energi habis saat bekerja (energi < -20)
        // (Ini lebih ke kondisi game over atau auto-sleep, bukan pilihan tidur biasa)
        // Jika waktu >= 02:00 AM, otomatis tidur (ini juga auto-sleep)

        // Untuk aksi tidur manual:
        boolean receivedPenalty = false;
        // Asumsikan ada threshold energi "menipis", misal Player.LOW_ENERGY_THRESHOLD = 10
        // Atau jika energi player.getEnergy() < (0.10 * Player.MAX_ENERGY)
        if (player.getEnergy() < (player.getMaxEnergy() * 0.10) && player.getEnergy() < 0) { // Contoh threshold energi menipis
             // atau kondisi dari spesifikasi "jika tidur di saat energi sudah menipis"
            receivedPenalty = true;
        }

        if (receivedPenalty) {
            player.setEnergy(player.getMaxEnergy() / 2);
            System.out.println("LOG: " + player.getName() + " tidur dengan energi menipis, energi pulih setengah.");
        } else {
            player.setEnergy(player.getMaxEnergy()); // Pulih penuh
             System.out.println("LOG: " + player.getName() + " tidur nyenyak, energi pulih penuh.");
        }


        // Logika untuk skip waktu ke pagi berikutnya
        farm.getGameClock().advanceToNextMorning(); // Method ini perlu diimplementasikan di GameClock
        // Ini juga akan memicu event akhir hari (penjualan dari shipping bin, save game, dll.)

        System.out.println("LOG: Hari baru telah dimulai.");
    }
}