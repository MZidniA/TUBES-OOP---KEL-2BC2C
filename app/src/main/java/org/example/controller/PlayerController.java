package org.example.controller;

import org.example.model.Player;
import org.example.model.Items.Items;
import org.example.model.Farm; // Diperlukan jika ingin akses data farm
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.UnplantedTileObject;
import org.example.view.entitas.PlayerView;
import org.example.view.GameStateUI; // Untuk akses slotCol/Row

import java.util.ArrayList;
import java.util.Map;

public class PlayerController {

    // GamePanel gp; // DIHAPUS
    Player playerModel;
    PlayerView playerView;
    // KeyHandler keyH; // DIHAPUS, logika input sekarang di GameController

    GameController gameController; // Tambahkan referensi ke GameController utama

    // Konstruktor diubah untuk menerima GameController
    public PlayerController(GameController gameController, Player playerModel, PlayerView playerView) {
        this.gameController = gameController;
        this.playerModel = playerModel;
        this.playerView = playerView;
        // this.keyH = gp.keyH; // DIHAPUS
    }

    public void update() {
        // Logika ini perlu dipikirkan ulang bagaimana dan kapan dipanggil oleh GameController
        // handleInteraction(); // Mungkin dipanggil dari GameController.handleInteraction()
        // handleHotbarSelection(); // Mungkin dipicu oleh event angka dari KeyHandler -> GameController
    }

    // Dipanggil dari KeyHandler saat ENTER di inventaris
    // GameStateUI sekarang dimiliki oleh GamePanel, jadi GameController perlu menyediakan akses
    public void selectItemFromInventory() {
        if (gameController == null) return;
        GameStateUI ui = gameController.getGameStateUI();
        if (ui == null) {
            System.err.println("PlayerController Error: GameStateUI is null via GameController!");
            return;
        }
        int slotCol = ui.slotCol;
        int slotRow = ui.slotRow;
        int slotsPerRow = 12;

        int slotIndex = slotCol + (slotRow * slotsPerRow);

        ArrayList<Map.Entry<Items, Integer>> inventoryList =
            new ArrayList<>(playerModel.getInventory().getInventory().entrySet());

        if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
            Items selectedItem = inventoryList.get(slotIndex).getKey();
            playerModel.setCurrentHeldItem(selectedItem);
        } else {
            playerModel.setCurrentHeldItem(null);
        }
    }

    // Logika ini perlu diubah total karena KeyHandler tidak lagi punya flag boolean
    private void handleHotbarSelection() {
        // if (gameController.getGameState().getGameState() != gameController.getGameState().play) {
        //     return;
        // }
        // Implementasi baru akan bergantung pada bagaimana GameController meneruskan event penekanan tombol angka
        // Misalnya: gameController.handleHotbarKeyPress(int slotNumber);
        System.out.println("PlayerController: handleHotbarSelection() perlu diimplementasikan ulang.");
    }

    // Logika ini perlu diubah total
    private void handleInteraction() {
        // if (/* kondisi tombol interaksi ditekan dari GameController */) {
        //     Items heldItem = playerModel.getCurrentHeldItem();
        //     Farm farmModel = gameController.getFarmModel();
        //     int currentMap = farmModel.getCurrentMap();
        //     InteractableObject[][] allObjects = farmModel.getAllObjects();

        //     // Logika checkObject sudah ada di CollisionChecker, dipanggil oleh GameController
        //     // int objIndex = gameController.getCChecker().checkObject(playerView);
        //     // if (objIndex != 999) {
        //     // if (allObjects[currentMap][objIndex] != null) {
        //     // allObjects[currentMap][objIndex].interact(gameController);
        //     // return;
        //     // }
        //     // }

        //     if (heldItem != null) {
        //         handleTileInteraction(heldItem);
        //     }
        // }
        System.out.println("PlayerController: handleInteraction() perlu diimplementasikan ulang atau diintegrasikan ke GameController.");
    }

    private void handleTileInteraction(Items heldItem) {
        if (playerModel == null || playerView == null || gameController == null) return;
        Farm farmModel = gameController.getFarmModel();
        if (farmModel == null) return;

        int tileSize = gameController.getTileSize();
        int maxWorldCol = gameController.getMaxWorldCol();
        int maxWorldRow = gameController.getMaxWorldRow();
        int currentMap = farmModel.getCurrentMap();
        InteractableObject[][] allObjects = farmModel.getAllObjects();
        // TileManager tileM = gameController.getTileManager(); // Jika TileManager ada di GameController

        if (heldItem.getName().equalsIgnoreCase("Hoe")) {
            int targetCol = playerView.worldX / tileSize;
            int targetRow = playerView.worldY / tileSize;

            switch (playerView.direction) {
                case "up":    targetRow = (playerView.worldY + playerView.solidArea.y - tileSize) / tileSize; targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width/2) / tileSize; break;
                case "down":  targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height) / tileSize; targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width/2) / tileSize; break;
                case "left":  targetCol = (playerView.worldX + playerView.solidArea.x - tileSize) / tileSize; targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height/2) / tileSize; break;
                case "right": targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width) / tileSize; targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height/2) / tileSize; break;
            }

            if (targetCol < 0 || targetCol >= maxWorldCol || targetRow < 0 || targetRow >= maxWorldRow) {
                return;
            }

            for(int i=0; i < allObjects[currentMap].length; i++){
                if(allObjects[currentMap][i] != null &&
                   allObjects[currentMap][i].worldX / tileSize == targetCol &&
                   allObjects[currentMap][i].worldY / tileSize == targetRow){
                    System.out.println("Sudah ada objek di tile target, tidak bisa dicangkul.");
                    return;
                }
            }
            
            // Akses ke tileM.mapTileNum dan tileM.tile juga perlu melalui GameController jika TileManager di sana
            // Untuk sekarang, ini akan error jika tileM tidak bisa diakses
            // int tileNum = tileM.mapTileNum[currentMap][targetCol][targetRow];
            // if (tileM.tile[tileNum] != null && !tileM.tile[tileNum].collision) {
            //     for (int i = 0; i < allObjects[currentMap].length; i++) {
            //         if (allObjects[currentMap][i] == null) {
            //             allObjects[currentMap][i] = new UnplantedTileObject(); // Konstruktor sudah diubah
            //             allObjects[currentMap][i].worldX = targetCol * tileSize;
            //             allObjects[currentMap][i].worldY = targetRow * tileSize;
            //             // Scaling gambar untuk UnplantedTileObject juga perlu ditangani oleh AssetSetter
            //             System.out.println("Tanah berhasil dicangkul di (" + targetCol + ", " + targetRow + ")!");
            //             break;
            //         }
            //     }
            // } else {
            //     System.out.println("Tidak bisa mencangkul tile ini: ("+ targetCol + ", "+ targetRow +")");
            // }
            System.out.println("PlayerController: Logika mencangkul dengan Hoe perlu penyesuaian akses ke TileManager.");
        }
    }
}