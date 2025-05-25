package org.example.controller;

import org.example.view.entitas.Entity;
// Hapus import GamePanel jika tidak terpakai secara eksplisit di sini, karena sudah ada via gp
// import org.example.controller.GamePanel; 
import org.example.view.InteractableObject.InteractableObject;


public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // Hitung batas-batas solidArea entitas di dunia
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Hitung kolom dan baris yang disentuh oleh solidArea entitas
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                // Prediksi baris berikutnya ke atas
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                // ---- PENGECEKAN BATAS ----
                if (entityTopRow < 0 || entityTopRow >= gp.maxWorldRow ||
                    entityLeftCol < 0 || entityLeftCol >= gp.maxWorldCol ||
                    entityRightCol < 0 || entityRightCol >= gp.maxWorldCol) {
                    entity.collisionOn = true; // Anggap collision jika keluar batas
                    return;
                }
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                // Prediksi baris berikutnya ke bawah
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                // ---- PENGECEKAN BATAS ----
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
                // Prediksi kolom berikutnya ke kiri
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                // ---- PENGECEKAN BATAS ----
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
                // Prediksi kolom berikutnya ke kanan
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                // ---- PENGECEKAN BATAS ----
                 if (entityRightCol < 0 || entityRightCol >= gp.maxWorldCol ||
                    entityTopRow < 0 || entityTopRow >= gp.maxWorldRow ||
                    entityBottomRow < 0 || entityBottomRow >= gp.maxWorldRow) {
                    entity.collisionOn = true;
                    return;
                }
                // Anda sebelumnya menggunakan entityRightCol - 1 di sini, itu mungkin salah.
                // Jika entityRightCol adalah kolom yang akan dituju, gunakan langsung.
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    // Metode checkObject Anda (pastikan sudah menggunakan prediksi seperti diskusi sebelumnya)
    public int checkObject(Entity entity, InteractableObject[][] target, int currenMap) {
        // ... (kode checkObject dengan prediksi yang sudah kita bahas) ...
        // Pastikan di sini juga ada boundary check jika Anda mengakses mapTileNum
        // atau pastikan posisi objek selalu valid.
        // Untuk checkObject, biasanya boundary check lebih pada apakah worldX/Y objek itu sendiri
        // valid saat penempatan, bukan saat pengecekan kolisi dinamis terhadap tepi peta.
        // Pengecekan utama di sini adalah interseksi rectangle.
        int index = 999;

        for (int i = 0; i < target[1].length; i++) {
            if (target[gp.currentMap][i] != null) {
                if (entity.solidArea == null || target[gp.currentMap][i].solidArea == null) {
                    continue;
                }

                int originalEntitySolidX = entity.solidArea.x;
                int originalEntitySolidY = entity.solidArea.y;

                entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x; // Gunakan offset target
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }

                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if (target[gp.currentMap][i].collision) {
                        entity.collisionOn = true;
                    }
                    index = i;
                }

                

                entity.solidArea.x = originalEntitySolidX;
                entity.solidArea.y = originalEntitySolidY;

                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].solidArea.x - target[gp.currentMap][i].worldX; // Kembali ke offset
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].solidArea.y - target[gp.currentMap][i].worldY;
            }
        }
        return index;
    }
}