// Lokasi: src/main/java/org/example/controller/action/OpenInventoryAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Inventory;
import org.example.model.Player;
import org.example.model.Items.Items; // Import kelas Items

import java.util.Map; // Import Map

public class OpenInventoryAction implements Action {

    @Override
    public boolean canExecute(Farm farm) {
        // Aksi membuka inventory selalu bisa dilakukan, tidak ada batasan energi atau lokasi spesifik.
        // Namun, bisa ditambahkan validasi jika ada kondisi tertentu di masa depan.
        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayer(); // Mendapatkan objek Player dari Farm
        Inventory inventory = player.getInventory(); // Mendapatkan objek Inventory dari Player

        System.out.println("--- Isi Inventaris " + player.getName() + " ---"); // Output ke konsol untuk CLI
        Map<Items, Integer> itemsInInventory = inventory.getInventory();

        if (itemsInInventory.isEmpty()) {
            System.out.println("Inventaris kosong.");
        } else {
            int i = 1;
            for (Map.Entry<Items, Integer> entry : itemsInInventory.entrySet()) {
                Items item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println(i + ". " + item.getName() + " (Jumlah: " + quantity + ")");
                i++;
            }
        }
        System.out.println("------------------------------------");

        // Karena ini aplikasi GUI, idealnya Anda akan memicu pembaruan pada View di sini.
        // Misalnya, memanggil metode di GamePanel atau ViewManager untuk menampilkan pop-up inventaris.
        // Contoh placeholder (akan diimplementasikan di layer View):
        // GamePanel.getInstance().showInventoryView(inventory);
        // Atau jika ada ViewManager:
        // ViewManager.getInstance().displayInventory(inventory);
        // Karena kita belum melihat kelas ViewManager atau implementasi GUI, kita akan output ke konsol dulu.
        // Spesifikasi tugas besar menyebutkan CLI sebagai dasar, jadi output konsol tetap relevan.
    }

    @Override
    public String getActionName() {
        return "Open Inventory";
    }
}   