package org.example.view.entitas;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.example.controller.CollisionChecker;
import org.example.controller.UtilityTool;
import org.example.model.Player; 
import org.example.model.Items.Items;
import org.example.view.GamePanel; 

public class PlayerView extends Entity {
    private final Player playerModel; 
    public final int screenX;
    public final int screenY;
    GamePanel gp;

    public PlayerView(Player playerModel, GamePanel gp) { 
        this.playerModel = playerModel;


        this.screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        this.screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        
        this.solidArea = new Rectangle(8, 16, 24, 24); 
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
        
        setDefaultValues(gp.tileSize); 
        getPlayerImage(gp.tileSize); 
    }

    public GamePanel getGamePanel(){
        return  this.gp;
    }

    public void setDefaultValues(int tileSize) { 
        worldX = 4 * tileSize; 
        worldY = 9 * tileSize;
        speed = 4;
        direction = "down";
    }
    
    public void getPlayerImage(int tileSize) { 
        up1 = setup("/player/boy_up_1", tileSize);
        up2 = setup("/player/boy_up_2", tileSize);
        down1 = setup("/player/boy_down_1", tileSize);
        down2 = setup("/player/boy_down_2", tileSize);
        left1 = setup("/player/boy_left_1", tileSize);
        left2 = setup("/player/boy_left_2", tileSize);
        right1 = setup("/player/boy_right_1", tileSize);
        right2 = setup("/player/boy_right_2", tileSize);
    }

    private BufferedImage setup(String imagePath, int tileSize) { 
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            if (image != null) {
                image = uTool.scaleImage(image, tileSize, tileSize);
            } else {
                System.err.println("Gambar tidak ditemukan: " + imagePath + ".png");
            }
        } catch (IOException e) { 
            e.printStackTrace(); 
        } catch (IllegalArgumentException e) {
            System.err.println("Error saat memuat gambar (mungkin path salah atau file tidak ada): " + imagePath + ".png");
            e.printStackTrace();
        }
        return image;
    }

    public void update(Map<String, Boolean> movementState, CollisionChecker cChecker) {
        boolean isMoving = movementState.values().stream().anyMatch(b -> b);

        if (isMoving) {
            if (Boolean.TRUE.equals(movementState.get("up"))) direction = "up";
            else if (Boolean.TRUE.equals(movementState.get("down"))) direction = "down";
            else if (Boolean.TRUE.equals(movementState.get("left"))) direction = "left";
            else if (Boolean.TRUE.equals(movementState.get("right"))) direction = "right";

            collisionOn = false;
            cChecker.checkTile(this);
            cChecker.checkObject(this); 

            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) { 
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        } else {
             spriteNum = 1; 
        }
    }

    public void draw(Graphics2D g2, GamePanel gp,  int screenX, int screenY) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
            default: 
                image = down1; 
                break;
        }
        

        if (image != null) {
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            System.err.println(" Gambar untuk arah " + direction + " adalah null.");
        }

        Items heldItem = playerModel.getCurrentHeldItem();
        if (heldItem != null && heldItem.getImage() != null) {
            BufferedImage itemImage = heldItem.getImage(); 
            int itemDisplaySize = gp.tileSize / 2; 


            int itemDrawX = screenX;
            int itemDrawY = screenY;

            if ("down".equals(direction)) {
                itemDrawX = screenX + (gp.tileSize / 3); 
                itemDrawY = screenY + (gp.tileSize / 2); 
            } else if ("up".equals(direction)) {
                itemDrawX = screenX + (gp.tileSize / 3); 
                itemDrawY = screenY + (gp.tileSize / 4) - itemDisplaySize; 
            } else if ("left".equals(direction)) {
                itemDrawX = screenX - (itemDisplaySize / 2) + (gp.tileSize / 4); 
                itemDrawY = screenY + (gp.tileSize / 2);
            } else if ("right".equals(direction)) {
                itemDrawX = screenX + (gp.tileSize / 2) ; 
                itemDrawY = screenY + (gp.tileSize / 2);
            }
            
            g2.drawImage(itemImage, itemDrawX, itemDrawY, itemDisplaySize, itemDisplaySize, null);
        }
    }


    public Player getPlayerModel() {
        return playerModel;
    }
}