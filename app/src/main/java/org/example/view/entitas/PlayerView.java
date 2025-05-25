package org.example.view.entitas;

import java.io.IOException;
import org.example.view.entitas.Entity; // Ensure Entity is imported

import org.example.controller.GamePanel;
import org.example.controller.KeyHandler;
import org.example.controller.UtilityTool;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

public class PlayerView extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public PlayerView(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 24;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 4;
        worldY = gp.tileSize * 9;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imagePath + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true
                || keyH.rightPressed == true) {
            if (keyH.upPressed) {
                direction = "up";
            }
            if (keyH.downPressed) {
                direction = "down";
            }
            if (keyH.leftPressed) {
                direction = "left";
            }
            if (keyH.rightPressed) {
                direction = "right";
            }

            // check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, gp.obj, gp.currentMap);

            if (objIndex != 999 && gp.keyH.interactPressed) {
                gp.obj[gp.currentMap][objIndex].interact();
            }

            if (keyH.interactPressed) {
                boolean interactionHandled = false; // Flag untuk menandai apakah interaksi sudah ditangani

                if (objIndex != 999) { // Jika ada objek interaktif di depan
                    if (gp.obj[gp.currentMap][objIndex] != null) {
                        gp.obj[gp.currentMap][objIndex].interact(); 
                        interactionHandled = true;
                    }
                } else {
                    // Jika TIDAK ada objek interaktif, cek TILE TELEPORT
                    int playerCol = (worldX + solidArea.x + solidArea.width / 2) / gp.tileSize; // Tengah solidArea pemain
                    int playerRow = (worldY + solidArea.y + solidArea.height / 2) / gp.tileSize; // Tengah solidArea pemain

                    // Pastikan tidak keluar batas peta
                    if (playerCol >= 0 && playerCol < gp.maxWorldCol && playerRow >= 0 && playerRow < gp.maxWorldRow) {
                        int tileNumUnderPlayer = gp.tileM.mapTileNum[gp.currentMap][playerCol][playerRow];

                        // ---- LOGIKA TELEPORTASI DARI TILE 69 ----
                        if (gp.currentMap == 0 && tileNumUnderPlayer == 69) {
                            System.out.println("Berdiri di tile 69 pada map 0, teleportasi!");
                            // Tentukan koordinat tujuan di map 1 (misalnya kolom 5, baris 5)
                            int destinationMapIndex = 1;
                            int destinationCol = 5; // Kolom tujuan di map baru
                            int destinationRow = 5; // Baris tujuan di map baru
                            gp.teleportPlayer(destinationMapIndex, destinationCol * gp.tileSize, destinationRow * gp.tileSize);
                            interactionHandled = true; // Interaksi sudah ditangani
                        }
                        // Anda bisa menambahkan 'else if' untuk tile teleport lain di sini
                        // Misalnya:
                        // else if (gp.currentMap == 1 && tileNumUnderPlayer == XX) { // XX adalah ID tile teleport kembali
                        //     gp.teleportPlayer(0, kolomTujuanDiMap0 * gp.tileSize, barisTujuanDiMap0 * gp.tileSize);
                        //     interactionHandled = true;
                        // }
                    }
                }
                keyH.interactPressed = false; // Reset flag interaksi setelah diproses
            }


            // if collision is false, player can move
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null); // Ensure gp.tileSize is properly defined in GamePanel
    }
}

