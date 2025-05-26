package org.example.controller;

import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.Entity;


public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // Batas SolidArea
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Kolom dan Baris
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                if (entityTopRow < 0 || entityTopRow >= gp.maxWorldRow ||
                    entityLeftCol < 0 || entityLeftCol >= gp.maxWorldCol ||
                    entityRightCol < 0 || entityRightCol >= gp.maxWorldCol) {
                    entity.collisionOn = true; 
                    return;
                }
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                if (entityBottomRow < 0 || entityBottomRow >= gp.maxWorldRow ||
                    entityLeftCol < 0 || entityLeftCol >= gp.maxWorldCol ||
                    entityRightCol < 0 || entityRightCol >= gp.maxWorldCol) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                if (entityLeftCol < 0 || entityLeftCol >= gp.maxWorldCol ||
                    entityTopRow < 0 || entityTopRow >= gp.maxWorldRow ||
                    entityBottomRow < 0 || entityBottomRow >= gp.maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                 if (entityRightCol < 0 || entityRightCol >= gp.maxWorldCol ||
                    entityTopRow < 0 || entityTopRow >= gp.maxWorldRow ||
                    entityBottomRow < 0 || entityBottomRow >= gp.maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }


    public int checkObject(Entity entity, InteractableObject[][] target, int currenMap) {
        int index = 999;

        for (int i = 0; i < target[gp.currentMap].length; i++) {
            if (target[gp.currentMap][i] != null) {


                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;


                switch (entity.direction) {
                    case "up": entity.solidArea.y -= (entity.speed + 1); break;
                    case "down": entity.solidArea.y += (entity.speed + 1); break;
                    case "left": entity.solidArea.x -= (entity.speed + 1); break;
                    case "right": entity.solidArea.x += (entity.speed + 1); break;
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if (target[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    index = i;
                }

                // Reset posisi solidArea kembali ke offset default
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }
}