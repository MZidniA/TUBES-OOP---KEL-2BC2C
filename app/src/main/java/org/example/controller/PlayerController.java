// // package org.example.controller;

// import java.util.ArrayList;
// import java.util.Map;

// import org.example.model.Farm;
// import org.example.model.Items.Items;
// import org.example.model.Player;
// import org.example.view.GameStateUI;
// import org.example.view.InteractableObject.InteractableObject;
// import org.example.view.entitas.PlayerView;

// public class PlayerController {

//     Player playerModel;
//     PlayerView playerView;
//     GameController gameController;

//     public PlayerController(GameController gameController, Player playerModel, PlayerView playerView) {
//         this.gameController = gameController;
//         this.playerModel = playerModel;
//         this.playerView = playerView;
//     }

//     public void update() {
//     }

//     public void selectItemFromInventory() {
//         if (gameController == null) return;
//         GameStateUI ui = gameController.getGameStateUI();
//         if (ui == null) {
//             System.err.println("PlayerController Error: GameStateUI is null via GameController!");
//             return;
//         }
//         int slotCol = ui.slotCol;
//         int slotRow = ui.slotRow;
//         int slotsPerRow = 12;

// //     public void update() {
// //         // Logika ini perlu dipikirkan ulang bagaimana dan kapan dipanggil oleh GameController
// //         // handleInteraction(); // Mungkin dipanggil dari GameController.handleInteraction()
// //         // handleHotbarSelection(); // Mungkin dipicu oleh event angka dari KeyHandler -> GameController
// //     }

// //     // Dipanggil dari KeyHandler saat ENTER di inventaris
// //     // GameStateUI sekarang dimiliki oleh GamePanel, jadi GameController perlu menyediakan akses
// //     public void selectItemFromInventory() {
// //         if (gameController == null) return;
// //         GameStateUI ui = gameController.getGameStateUI();
// //         if (ui == null) {
// //             System.err.println("PlayerController Error: GameStateUI is null via GameController!");
// //             return;
// //         }
// //         int slotCol = ui.slotCol;
// //         int slotRow = ui.slotRow;
// //         int slotsPerRow = 12;

// //         int slotIndex = slotCol + (slotRow * slotsPerRow);

//     private void handleHotbarSelection() {
//         System.out.println("PlayerController: handleHotbarSelection() perlu diimplementasikan ulang.");
//     }

//     private void handleInteraction() {
//         System.out.println("PlayerController: handleInteraction() perlu diimplementasikan ulang atau diintegrasikan ke GameController.");
//     }

// //         //     // Logika checkObject sudah ada di CollisionChecker, dipanggil oleh GameController
// //         //     // int objIndex = gameController.getCChecker().checkObject(playerView);
// //         //     // if (objIndex != 999) {
// //         //     // if (allObjects[currentMap][objIndex] != null) {
// //         //     // allObjects[currentMap][objIndex].interact(gameController);
// //         //     // return;
// //         //     // }
// //         //     // }

//         int tileSize = gameController.getTileSize();
//         int maxWorldCol = gameController.getMaxWorldCol();
//         int maxWorldRow = gameController.getMaxWorldRow();
//         int currentMap = farmModel.getCurrentMap();
//         InteractableObject[][] allObjects = farmModel.getAllObjects();

// //     private void handleTileInteraction(Items heldItem) {
// //         if (playerModel == null || playerView == null || gameController == null) return;
// //         Farm farmModel = gameController.getFarmModel();
// //         if (farmModel == null) return;

// //         int tileSize = gameController.getTileSize();
// //         int maxWorldCol = gameController.getMaxWorldCol();
// //         int maxWorldRow = gameController.getMaxWorldRow();
// //         int currentMap = farmModel.getCurrentMap();
// //         InteractableObject[][] allObjects = farmModel.getAllObjects();
// //         // TileManager tileM = gameController.getTileManager(); // Jika TileManager ada di GameController

// //         if (heldItem.getName().equalsIgnoreCase("Hoe")) {
// //             int targetCol = playerView.worldX / tileSize;
// //             int targetRow = playerView.worldY / tileSize;

//             for(int i=0; i < allObjects[currentMap].length; i++){
//                 if(allObjects[currentMap][i] != null &&
//                    allObjects[currentMap][i].worldX / tileSize == targetCol &&
//                    allObjects[currentMap][i].worldY / tileSize == targetRow){
//                     System.out.println("Sudah ada objek di tile target, tidak bisa dicangkul.");
//                     return;
//                 }
//             }

//             System.out.println("PlayerController: Logika mencangkul dengan Hoe perlu penyesuaian akses ke TileManager.");
//         }
//     }
// }
