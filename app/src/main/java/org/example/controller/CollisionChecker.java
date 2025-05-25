package org.example.controller;

import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol ][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol ][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol ][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol ][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }

    }

    public int checkObject(Entity entity, InteractableObject[] target) {
        int index = 999;
    
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                if (entity.solidArea == null || target[i].solidArea == null) {
                    continue; 
                }
    
                int entityLeft = entity.worldX + entity.solidArea.x;
                int entityTop = entity.worldY + entity.solidArea.y;
                int entityRight = entityLeft + entity.solidArea.width;
                int entityBottom = entityTop + entity.solidArea.height;
    
                int targetLeft = target[i].worldX + target[i].solidArea.x;
                int targetTop = target[i].worldY + target[i].solidArea.y;
                int targetRight = targetLeft + target[i].solidArea.width;
                int targetBottom = targetTop + target[i].solidArea.height;
    
                if (entityRight > targetLeft && entityLeft < targetRight &&
                    entityBottom > targetTop && entityTop < targetBottom) {
                    if (target[i].collision) {
                        entity.collisionOn = true;
                    }
                    index = i; 
                }
            }
        }
        return index; 
    }
    
}

