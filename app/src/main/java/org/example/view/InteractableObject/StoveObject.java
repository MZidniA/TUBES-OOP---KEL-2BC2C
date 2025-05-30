package org.example.view.InteractableObject;

import java.io.IOException;
import javax.imageio.ImageIO;
import org.example.controller.GameController;
import org.example.controller.GameState; // Diperlukan untuk mengakses field instance state
import org.example.model.CookingInProgress;
import org.example.model.Farm;
import org.example.view.GameStateUI; // Diperlukan untuk reset dan dialog

public class StoveObject extends InteractableObject {

    public StoveObject() {
        super("Stove");
        this.collision = true;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png")); // Pastikan path ini benar
            if (this.image == null) {
                 System.err.println("Error loading Stove.png for StoveObject: Resource not found at /InteractableObject/Stove.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Stove.png for StoveObject: " + e.getMessage());
        }  catch (IllegalArgumentException e) {
            // Tangani jika getResourceAsStream mengembalikan null karena path tidak valid
            System.err.println("Error loading Stove.png for StoveObject: Path issue or resource not found. " + e.getMessage());
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("StoveObject: Interacting with Stove...");
        if (controller == null) {
            System.err.println("StoveObject ERROR: GameController is null.");
            return;
        }
        
        Farm farm = controller.getFarmModel();
        GameState gameStateManager = controller.getGameState(); // Dapatkan objek GameState
        GameStateUI ui = controller.getGameStateUI();

        if (farm == null || gameStateManager == null || ui == null) {
            System.err.println("StoveObject ERROR: Farm, GameState, or GameStateUI is null.");
            return;
        }

        // Cek apakah ada masakan yang sudah siap diklaim DULU
        // Farm.updateCookingProgress() seharusnya sudah menangani penambahan otomatis ke inventory.
        // Bagian ini lebih bersifat notifikasi tambahan jika pemain berinteraksi tepat saat ada yang matang.
        boolean foundReadyDishNotified = false;
        if (farm.getActiveCookings() != null && farm.getGameClock() != null) {
             // Kita cari yang readyToClaim tapi belum diproses oleh updateCookingProgress di frame ini
            for (CookingInProgress task : farm.getActiveCookings()) {
                if (task != null && task.isReadyToClaim(farm.getCurrentTime()) && !task.isClaimed()) {
                    // Jika updateCookingProgress berjalan dengan baik, task ini seharusnya sudah diklaim & dihapus.
                    // Tapi jika pemain berinteraksi tepat sebelum updateCookingProgress berjalan, pesan ini bisa muncul.
                    ui.setDialogue("Something just finished cooking at the stove!"); // Pesan notifikasi
                    foundReadyDishNotified = true;
                    break; // Cukup satu notifikasi
                }
            }
        }
        
        // Tetap buka menu masak meskipun ada notifikasi (desain saat ini)
        // atau Anda bisa return di sini jika ingin pemain mengurus notifikasi dulu.
        
        gameStateManager.setGameState(gameStateManager.cooking_menu); // <-- MENGGUNAKAN FIELD INSTANCE
        ui.resetCookingMenuState(); 
        System.out.println("StoveObject: Changed game state to COOKING_MENU (" + gameStateManager.cooking_menu + ")");
    }
}