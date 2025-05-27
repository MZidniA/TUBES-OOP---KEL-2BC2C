package org.example.controller; // Pastikan package controller

import org.example.model.Player;
import org.example.model.Items.Items;
import org.example.view.InteractableObject.UnplantedTileObject; // Untuk aksi Hoe
import org.example.view.entitas.PlayerView;
import java.util.ArrayList;
import java.util.Map;

public class PlayerController {

    GamePanel gp;
    Player playerModel;     // Referensi ke model Player
    PlayerView playerView;  // Referensi ke view Player
    KeyHandler keyH;

    public PlayerController(GamePanel gp, Player playerModel, PlayerView playerView) {
        this.gp = gp;
        this.playerModel = playerModel;
        this.playerView = playerView;
        this.keyH = gp.keyH;
    }

    public void update() {
        handleInteraction();
        handleHotbarSelection();
    }

    // Dipanggil dari KeyHandler saat ENTER di inventaris
    public void selectItemFromInventory() {
        int slotCol = gp.gameStateUI.slotCol;
        int slotRow = gp.gameStateUI.slotRow;
        int slotsPerRow = 12; // Sesuai desain UI inventaris baru kita

        int slotIndex = slotCol + (slotRow * slotsPerRow);

        ArrayList<Map.Entry<Items, Integer>> inventoryList =
            new ArrayList<>(playerModel.getInventory().getInventory().entrySet());

        if (slotIndex >= 0 && slotIndex < inventoryList.size()) {
            Items selectedItem = inventoryList.get(slotIndex).getKey();
            playerModel.setCurrentHeldItem(selectedItem);
        } else {
            playerModel.setCurrentHeldItem(null); // Jika slot kosong atau di luar batas
        }
    }

    private void handleHotbarSelection() {
        // Hanya berjalan jika game state adalah play
        if (gp.gameState.getGameState() != gp.gameState.play) {
            return;
        }

        int hotbarSlot = -1;
        if (keyH.num1Pressed) hotbarSlot = 0;
        else if (keyH.num2Pressed) hotbarSlot = 1;
        else if (keyH.num3Pressed) hotbarSlot = 2;
        else if (keyH.num4Pressed) hotbarSlot = 3;
        else if (keyH.num5Pressed) hotbarSlot = 4;
        else if (keyH.num6Pressed) hotbarSlot = 5;
        else if (keyH.num7Pressed) hotbarSlot = 6;
        else if (keyH.num8Pressed) hotbarSlot = 7;
        else if (keyH.num9Pressed) hotbarSlot = 8;
        else if (keyH.num0Pressed) hotbarSlot = 9;
        else if (keyH.minusPressed) hotbarSlot = 10;
        else if (keyH.equalsPressed) hotbarSlot = 11;


        if (hotbarSlot != -1) {
            ArrayList<Map.Entry<Items, Integer>> inventoryList =
                new ArrayList<>(playerModel.getInventory().getInventory().entrySet());
            if (hotbarSlot < inventoryList.size()) {
                Items selectedItem = inventoryList.get(hotbarSlot).getKey();
                playerModel.setCurrentHeldItem(selectedItem);
            } else {
                playerModel.setCurrentHeldItem(null);
            }
            // Reset flag tombol angka agar tidak terpilih terus menerus
            keyH.resetHotbarKeys();
        }
    }

    private void handleInteraction() {
        if (keyH.interactPressed) {
            Items heldItem = playerModel.getCurrentHeldItem();

            // Interaksi dengan OBJEK di depan pemain
            int objIndex = gp.cChecker.checkObject(playerView, gp.obj, gp.currentMap);
            if (objIndex != 999) {
                if (gp.obj[gp.currentMap][objIndex] != null) {
                    gp.obj[gp.currentMap][objIndex].interact();
                    keyH.interactPressed = false; // Interaksi ditangani
                    return; // Keluar setelah interaksi objek
                }
            }

            // Jika tidak ada objek, dan pemain memegang sesuatu, coba interaksi TILE
            if (heldItem != null) {
                handleTileInteraction(heldItem);
            }
            keyH.interactPressed = false; // Reset flag
        }
    }

    private void handleTileInteraction(Items heldItem) {
        if (heldItem.getName().equalsIgnoreCase("Hoe")) {
            // Tentukan koordinat tile yang dihadapi pemain
            int targetCol = playerView.worldX / gp.tileSize; // Awalnya di bawah pemain
            int targetRow = playerView.worldY / gp.tileSize;

            // Sesuaikan target berdasarkan arah pemain dan jangkauan solidArea
            // Asumsi solidArea.y adalah offset dari atas, solidArea.x dari kiri
            // dan solidArea.height/width adalah ukurannya. Kita targetkan pusat dari "jangkauan" solidArea.
            switch (playerView.direction) {
                case "up":    targetRow = (playerView.worldY + playerView.solidArea.y - gp.tileSize) / gp.tileSize; targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width/2) / gp.tileSize; break;
                case "down":  targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height) / gp.tileSize; targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width/2) / gp.tileSize; break;
                case "left":  targetCol = (playerView.worldX + playerView.solidArea.x - gp.tileSize) / gp.tileSize; targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height/2) / gp.tileSize; break;
                case "right": targetCol = (playerView.worldX + playerView.solidArea.x + playerView.solidArea.width) / gp.tileSize; targetRow = (playerView.worldY + playerView.solidArea.y + playerView.solidArea.height/2) / gp.tileSize; break;
            }

            if (targetCol < 0 || targetCol >= gp.maxWorldCol || targetRow < 0 || targetRow >= gp.maxWorldRow) {
                return; // Target di luar peta
            }

            // Cek apakah sudah ada objek Interactable di tile target
            for(int i=0; i < gp.obj[gp.currentMap].length; i++){
                if(gp.obj[gp.currentMap][i] != null && 
                   gp.obj[gp.currentMap][i].worldX / gp.tileSize == targetCol &&
                   gp.obj[gp.currentMap][i].worldY / gp.tileSize == targetRow){
                    System.out.println("Sudah ada objek di tile target, tidak bisa dicangkul.");
                    return; // Jangan cangkul jika sudah ada objek
                }
            }

            int tileNum = gp.tileM.mapTileNum[gp.currentMap][targetCol][targetRow];
            // Asumsi tile 0 (RumputSummer) atau tile lain yang bisa dicangkul
            if (gp.tileM.tile[tileNum] != null && !gp.tileM.tile[tileNum].collision) {
                // Cari slot kosong di array gp.obj untuk menaruh UnplantedTileObject baru
                for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
                    if (gp.obj[gp.currentMap][i] == null) {
                        gp.obj[gp.currentMap][i] = new UnplantedTileObject(gp); // Pastikan path import benar
                        gp.obj[gp.currentMap][i].worldX = targetCol * gp.tileSize;
                        gp.obj[gp.currentMap][i].worldY = targetRow * gp.tileSize;
                        System.out.println("Tanah berhasil dicangkul di (" + targetCol + ", " + targetRow + ")!");
                        break; 
                    }
                }
            } else {
                System.out.println("Tidak bisa mencangkul tile ini: ("+ targetCol + ", "+ targetRow +")");
            }
        }
        // Tambahkan 'else if' untuk alat lain (Watering Can, Seeds, Pickaxe, dll.)
    }
}