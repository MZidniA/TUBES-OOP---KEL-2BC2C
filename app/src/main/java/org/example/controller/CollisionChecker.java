package org.example.controller;

import org.example.model.Farm;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.tile.TileManager;
import org.example.view.entitas.Entity;
import org.example.view.entitas.PlayerView;

public class CollisionChecker {
    private final GameController controller;

    public CollisionChecker(GameController controller) {
        this.controller = controller;
    }

    public void checkTile(Entity entity) {
        // Mengambil semua data yang dibutuhkan dari controller
        int tileSize = controller.getTileSize();
        int maxWorldCol = controller.getMaxWorldCol();
        int maxWorldRow = controller.getMaxWorldRow();
        Farm farmModel = controller.getFarmModel();
        TileManager tileM = controller.getTileManager();
        int currentMap = farmModel.getCurrentMap();

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / tileSize;
        int entityRightCol = entityRightWorldX / tileSize;
        int entityTopRow = entityTopWorldY / tileSize;
        int entityBottomRow = entityBottomWorldY / tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / tileSize;
                if (entityTopRow < 0 || entityTopRow >= maxWorldRow || entityLeftCol < 0 || entityLeftCol >= maxWorldCol || entityRightCol < 0 || entityRightCol >= maxWorldCol) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = tileM.mapTileNum[currentMap][entityLeftCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[currentMap][entityRightCol][entityTopRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / tileSize;
                if (entityBottomRow < 0 || entityBottomRow >= maxWorldRow || entityLeftCol < 0 || entityLeftCol >= maxWorldCol || entityRightCol < 0 || entityRightCol >= maxWorldCol) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = tileM.mapTileNum[currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = tileM.mapTileNum[currentMap][entityRightCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / tileSize;
                if (entityLeftCol < 0 || entityLeftCol >= maxWorldCol || entityTopRow < 0 || entityTopRow >= maxWorldRow || entityBottomRow < 0 || entityBottomRow >= maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = tileM.mapTileNum[currentMap][entityLeftCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[currentMap][entityLeftCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / tileSize;
                if (entityRightCol < 0 || entityRightCol >= maxWorldCol || entityTopRow < 0 || entityTopRow >= maxWorldRow || entityBottomRow < 0 || entityBottomRow >= maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = tileM.mapTileNum[currentMap][entityRightCol][entityTopRow];
                tileNum2 = tileM.mapTileNum[currentMap][entityRightCol][entityBottomRow];
                if (tileM.tile[tileNum1].collision || tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity) {
        int index = 999;

        // Ambil data objek dan peta saat ini dari Model melalui Controller
        Farm farmModel = controller.getFarmModel();
        InteractableObject[][] allObjects = farmModel.getAllObjects();
        int currentMap = farmModel.getCurrentMap();
        
        // Loop melalui objek hanya untuk peta saat ini
        for (int i = 0; i < allObjects[currentMap].length; i++) {
            if (allObjects[currentMap][i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                
                allObjects[currentMap][i].solidArea.x = allObjects[currentMap][i].worldX + allObjects[currentMap][i].solidArea.x;
                allObjects[currentMap][i].solidArea.y = allObjects[currentMap][i].worldY + allObjects[currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= (entity.speed + 1); break;
                    case "down": entity.solidArea.y += (entity.speed + 1); break;
                    case "left": entity.solidArea.x -= (entity.speed + 1); break;
                    case "right": entity.solidArea.x += (entity.speed + 1); break;
                }

                if (entity.solidArea.intersects(allObjects[currentMap][i].solidArea)) {
                    if (allObjects[currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    if (entity instanceof PlayerView) { // Hanya player yang bisa dapat index untuk interaksi
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                allObjects[currentMap][i].solidArea.x = allObjects[currentMap][i].solidAreaDefaultX;
                allObjects[currentMap][i].solidArea.y = allObjects[currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
}