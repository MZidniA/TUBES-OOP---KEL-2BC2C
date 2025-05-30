package org.example.view.InteractableObject;

import java.io.IOException;
import javax.imageio.ImageIO;
import org.example.controller.GameController;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import org.example.controller.GameState; // Pastikan GameState diimpor
=======
import org.example.controller.GameState; // Import GameState
>>>>>>> Stashed changes
import org.example.model.CookingInProgress;
import org.example.model.Farm;
=======
import org.example.controller.GameState; // Penting untuk mengakses instance field
import org.example.model.CookingInProgress;
import org.example.model.Farm;
import org.example.view.GameStateUI; // Untuk mereset menu dan menampilkan dialog
>>>>>>> Stashed changes

public class StoveObject extends InteractableObject {

    public StoveObject() { // Konstruktor tanpa GameController
        super("Stove");
        this.collision = true;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            // Ganti dengan path yang benar jika berbeda
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png"));
            if (this.image == null) {
                 System.err.println("Error loading Stove.png for StoveObject: Resource not found.");
=======
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png")); // Pastikan path ini benar
            if (this.image == null) {
                System.err.println("Error loading Stove.png for StoveObject: Resource not found at /InteractableObject/Stove.png");
>>>>>>> Stashed changes
=======
            // Pastikan path ke gambar kompor benar
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png"));
            if (this.image == null) {
                System.err.println("Error loading Stove.png for StoveObject: Resource not found at /InteractableObject/Stove.png");
>>>>>>> Stashed changes
            }
        } catch (IOException e) {
            System.err.println("Error loading Stove.png for StoveObject: " + e.getMessage());
            e.printStackTrace();
<<<<<<< Updated upstream
<<<<<<< Updated upstream
            System.err.println("Error loading Stove.png for StoveObject: " + e.getMessage());
        }  catch (IllegalArgumentException e) {
=======
        } catch (IllegalArgumentException e) {
>>>>>>> Stashed changes
=======
            System.err.println("Error loading Stove.png for StoveObject: " + e.getMessage());
        } catch (IllegalArgumentException e) {
>>>>>>> Stashed changes
            System.err.println("Error loading Stove.png for StoveObject: Input is null (check path). " + e.getMessage());
        }
    }

    @Override
    public void interact(GameController controller) {
<<<<<<< Updated upstream
        System.out.println("Interacting with the Stove. Opening cooking menu...");
<<<<<<< Updated upstream
        // Ubah game state untuk menampilkan UI memasak
        if (controller != null && controller.getGameState() != null) {
            // Sebelum membuka menu, cek apakah ada masakan yang sudah matang & bisa diklaim
            Farm farm = controller.getFarmModel();
            if (farm != null && farm.getActiveCookings() != null) {
=======
        if (controller != null && controller.getGameState() != null) {
            Farm farm = controller.getFarmModel();
            GameState gameStateManager = controller.getGameState(); // <-- Dapatkan objek GameState

            // Cek apakah ada masakan yang sudah matang & bisa diklaim
            if (farm != null && farm.getActiveCookings() != null && farm.getGameClock() != null) {
>>>>>>> Stashed changes
                CookingInProgress readyToClaimTask = farm.getActiveCookings().stream()
                    .filter(task -> task != null && task.isReadyToClaim(farm.getGameClock().getCurrentTime()) && !task.isClaimed())
                    .findFirst().orElse(null);

                if (readyToClaimTask != null) {
<<<<<<< Updated upstream
                    // Tampilkan pesan atau UI kecil untuk klaim dulu
                    // Ini bisa dihandle oleh GameStateUI atau dialog khusus
                    controller.getGameStateUI().setDialogue("You have something ready to claim at the stove!");
                    // Untuk sekarang, kita bisa langsung klaim jika ada, lalu buka menu masak
                    // Atau, bisa buat state khusus DIALOGUE_STOVE_CLAIM
                    // player.claimFinishedCooking(farm); // Anda perlu metode ini di Player atau Farm
                    // System.out.println(readyToClaimTask.getCookedDish().getName() + " has been claimed!");
                    // farm.removeCompletedCookingTask(readyToClaimTask);
                }
            }

            controller.getGameState().setGameState(GameState.COOKING_MENU);
            // Anda mungkin ingin mereset state UI memasak di GameStateUI di sini
            if (controller.getGameStateUI() != null) {
                controller.getGameStateUI().resetCookingMenuState(); // Buat metode ini
            }
        } else {
            System.err.println("ERROR: GameController or GameState is null in StoveObject.interact().");
=======
                    if (controller.getGameStateUI() != null) {
                        // Menampilkan pesan bahwa ada makanan yang bisa diklaim.
                        // Anda bisa membuat logika di GameStateUI untuk menampilkan ini sebagai dialog yang lebih interaktif
                        // atau pemain harus mengklaimnya dulu sebelum bisa membuka menu masak baru.
                        // Untuk sekarang, kita hanya tampilkan pesan.
                        controller.getGameStateUI().setDialogue(readyToClaimTask.getCookedDish().getName() + " is ready to claim!");
                        // Pertimbangkan untuk tidak langsung membuka menu masak jika ada yang bisa diklaim,
                        // mungkin pemain harus berinteraksi lagi untuk klaim atau menu masak.
                        // Namun, sesuai kode Anda, kita tetap lanjut membuka menu masak.
                    }
                }
            }

            // Mengatur game state ke mode memasak menggunakan field instance dari gameStateManager
            gameStateManager.setGameState(gameStateManager.cooking_menu); // <-- PERUBAHAN DI SINI

            // Mereset state UI memasak di GameStateUI
            if (controller.getGameStateUI() != null) {
                controller.getGameStateUI().resetCookingMenuState(); // Pastikan metode ini ada di GameStateUI
            }
        } else {
            System.err.println("ERROR: GameController or GameState object is null in StoveObject.interact().");
>>>>>>> Stashed changes
        }
=======
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

        boolean hasReadyDish = farm.getActiveCookings().stream()
            .anyMatch(task -> task != null && task.isCompleted(farm.getCurrentTime()) && !task.isClaimed());

        if (hasReadyDish) {
            if (farm.getActiveCookings().stream().anyMatch(task -> task != null && task.isCompleted(farm.getCurrentTime()) && !task.isClaimed())) {
                 ui.setDialogue("Masih ada masakan yang siap diklaim!");
                 // Mungkin tetap ingin buka menu masak atau tidak, tergantung desain Anda.
            }
        }
        
        gameStateManager.setGameState(gameStateManager.cooking_menu); // Menggunakan field instance
        ui.resetCookingMenuState(); // Persiapkan UI untuk menu memasak yang baru
        System.out.println("StoveObject: Changed game state to COOKING_MENU (" + gameStateManager.cooking_menu + ")");
>>>>>>> Stashed changes
    }
}