package org.example.controller;

import java.util.ArrayList;
import java.util.Map;

import org.example.model.Farm;
import org.example.model.Items.Items;
import org.example.model.Player;
import org.example.view.GameStateUI;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;

public class PlayerController {

    Player playerModel;
    PlayerView playerView;
    GameController gameController;

    public PlayerController(GameController gameController, Player playerModel, PlayerView playerView) {
        this.gameController = gameController;
        this.playerModel = playerModel;
        this.playerView = playerView;
    }

    public void update() {
    }

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

    private void handleHotbarSelection() {
        System.out.println("PlayerController: handleHotbarSelection() perlu diimplementasikan ulang.");
    }

    private void handleInteraction() {
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

            System.out.println("PlayerController: Logika mencangkul dengan Hoe perlu penyesuaian akses ke TileManager.");
        }
    }
}