// // Buat file baru: org/example/view/InteractableObject/TeleportPointObject.java
// package org.example.view.InteractableObject;

// import org.example.controller.GameController;
// import org.example.controller.GameState; // Untuk mengakses nilai state
// import org.example.view.GameStateUI;

// import javax.imageio.ImageIO;
// import java.io.IOException;

// public class TeleportPointObject extends InteractableObject {

//     public TeleportPointObject() {
//         // super("Teleporter"); // Atau "Warp Point", "Portal", dll.
//         // this.collision = false; // Biasanya pemain bisa berdiri di atas teleporter
//         //                         // Sesuaikan jika Anda ingin collision true dan interaksi dari samping
//         // loadImage();
//         // // Anda bisa menambahkan deskripsi jika ingin ditampilkan saat dekat
//         // // this.description = "Use to travel to other areas.";
//     }

//     @Override
//     protected void loadImage() {
//         // try {
//         //     // Anda perlu gambar untuk teleporter ini
//         //     image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/teleporter.png")); // GANTI DENGAN PATH GAMBAR ANDA
//         //     if (image == null) {
//         //         System.err.println("Gambar untuk TeleportPointObject tidak ditemukan di path: /InteractableObject/teleporter.png");
//         //     }
//         // } catch (IOException e) {
//         //     System.err.println("Gagal memuat gambar untuk TeleportPointObject: " + e.getMessage());
//         // } catch (IllegalArgumentException e) {
//         //     System.err.println("Error loading image for TeleportPointObject: Path issue or resource not found. " + e.getMessage());
//         // }
//     }

//     @Override
//     public void interact(GameController gc) {
//         if (gc == null) {
//             System.err.println("TeleportPointObject Error: GameController null saat interaksi.");
//             return;
//         }

//         GameState gameStateManager = gc.getGameState();
//         GameStateUI ui = gc.getGameStateUI();

//         if (gameStateManager == null || ui == null) {
//             System.err.println("TeleportPointObject Error: GameState atau GameStateUI null.");
//             return;
//         }

//         System.out.println("TeleportPointObject: Interacted. Opening map selection menu.");
//         // Anda perlu mendefinisikan konstanta/nilai untuk state map_selection di kelas GameState
//         // Misalnya, jika gameState.map_selection adalah field integer:
//         gameStateManager.setGameState(gameStateManager.map_selection); // GANTI DENGAN KONSTANTA STATE ANDA
//         ui.resetMapSelectionMenuState(); // Kita akan buat metode ini di GameStateUI
//     }
// }