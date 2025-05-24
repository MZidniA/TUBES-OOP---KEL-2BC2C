package org.example.controller;

import org.example.view.entitas.Entity;
import org.example.controller.GamePanel;
import org.example.view.InteractableObject.InteractableObject;

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
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionOn = true;
                }
                break;
        }

    }

    // Tambahkan metode ini di dalam kelas CollisionChecker
    public int checkObject(Entity entity, InteractableObject[] target) {
        int index = 999; // Nilai default jika tidak ada kolisi
    
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // Validasi solidArea
                if (entity.solidArea == null || target[i].solidArea == null) {
                    continue; // Lewati jika solidArea tidak diinisialisasi
                }
    
                // Hitung posisi absolut solidArea dari entity
                int entityLeft = entity.worldX + entity.solidArea.x;
                int entityTop = entity.worldY + entity.solidArea.y;
                int entityRight = entityLeft + entity.solidArea.width;
                int entityBottom = entityTop + entity.solidArea.height;
    
                // Hitung posisi absolut solidArea dari objek
                int targetLeft = target[i].worldX + target[i].solidArea.x;
                int targetTop = target[i].worldY + target[i].solidArea.y;
                int targetRight = targetLeft + target[i].solidArea.width;
                int targetBottom = targetTop + target[i].solidArea.height;
    
                // Periksa apakah solidArea entity dan objek saling bertabrakan
                if (entityRight > targetLeft && entityLeft < targetRight &&
                    entityBottom > targetTop && entityTop < targetBottom) {
                    if (target[i].collision) {
                        entity.collisionOn = true;
                    }
                    index = i; // Simpan index dari objek yang ditabrak
                }
            }
        }
    
        return index; // Kembalikan index dari objek yang ditabrak, atau 999 jika tidak ada
    }
    
}

