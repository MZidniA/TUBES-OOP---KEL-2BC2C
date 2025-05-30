    package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.LocationType;

public class WatchingAction implements Action{

    public static final int ENERGY_COST = 5;
    public static final int TIME_COST_MINUTES = 15;

    @Override
    public String getActionName() {
        return "Watching";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        // Asumsikan interaksi TVObject sudah memastikan pemain di rumah.
        // Jika tidak, tambahkan pengecekan lokasi di sini, misal:
        // if (player.getCurrentLocationType() != LocationType.HOUSE) { // Ganti FARM ke HOUSE jika perlu
        //     System.out.println("Menonton TV hanya bisa dilakukan di rumah."); // Pesan ini bisa dihapus jika TVObject sudah menangani
        //     return false;
        // }

        // Hapus pengecekan kepemilikan item TV di inventory
        // boolean hasTV = player.getInventory().hasItem(ItemDatabase.getItem("TV"), 1);
        // if (!hasTV) {
        //     System.out.println("Kamu membutuhkan TV untuk menonton.");
        //     return false;
        // }

        if (player.getEnergy() < ENERGY_COST) {
            // Pesan ini bisa juga ditampilkan oleh TVObject sebelum memanggil action
            // System.out.println("Energi tidak cukup untuk menonton TV.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        // Pengecekan canExecute bisa di-skip jika sudah dilakukan oleh TVObject sebelum memanggil execute
        // if (!canExecute(farm)) return; 

        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        // Pesan ke konsol bisa dihapus, karena TVObject akan menampilkan di UI
        // System.out.println("Kamu menonton acara favoritmu di TV.");
        // System.out.println("Energi berkurang 5 poin. Waktu maju 15 menit.");
    }
}
    

