package org.example.view.InteractableObject;

import java.io.IOException;
import javax.imageio.ImageIO;
import org.example.controller.GameController;
import org.example.controller.GameState; // Pastikan GameState diimpor
import org.example.model.CookingInProgress;
import org.example.model.Farm;

public class StoveObject extends InteractableObject {

    public StoveObject() {
        super("Stove");
        this.collision = true;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            // Ganti dengan path yang benar jika berbeda
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png"));
            if (this.image == null) {
                 System.err.println("Error loading Stove.png for StoveObject: Resource not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Stove.png for StoveObject: " + e.getMessage());
        }  catch (IllegalArgumentException e) {
            System.err.println("Error loading Stove.png for StoveObject: Input is null (check path). " + e.getMessage());
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("Interacting with the Stove. Opening cooking menu...");
        // Ubah game state untuk menampilkan UI memasak
        if (controller != null && controller.getGameState() != null) {
            // Sebelum membuka menu, cek apakah ada masakan yang sudah matang & bisa diklaim
            Farm farm = controller.getFarmModel();
            if (farm != null && farm.getActiveCookings() != null) {
                CookingInProgress readyToClaimTask = farm.getActiveCookings().stream()
                    .filter(task -> task != null && task.isReadyToClaim(farm.getGameClock().getCurrentTime()) && !task.isClaimed())
                    .findFirst().orElse(null);

                if (readyToClaimTask != null) {
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
        }
    }
}