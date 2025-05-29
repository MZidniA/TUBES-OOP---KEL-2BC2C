package org.example.view.InteractableObject;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.example.controller.GameController;
import org.example.controller.UtilityTool;
import org.example.view.GamePanel;
import org.example.view.entitas.PlayerView;

public abstract class InteractableObject {
    public BufferedImage image; 
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); 
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    protected static final UtilityTool uTool = new UtilityTool();

    /**
     * Konstruktor dasar untuk objek interaktif.
     * @param name Nama objek.
     */
    public InteractableObject(String name) {
        this.name = name;
    }

    /**
     * Metode abstrak yang harus diimplementasikan oleh subclass
     * untuk memuat gambar spesifik mereka (belum di-scale).
     */
    protected abstract void loadImage();

    /**
     * Menggambar objek ke layar.
     * @param g2 Graphics context.
     * @param gp GamePanel untuk mendapatkan konstanta seperti tileSize dan dimensi layar.
     * @param playerView PlayerView untuk mendapatkan posisi kamera.
     */
    public void draw(Graphics2D g2, GamePanel gp, PlayerView playerView) {
        if (playerView == null || gp == null) return; 
    
        int worldWidth = gp.maxWorldCol * gp.tileSize;
        int worldHeight = gp.maxWorldRow * gp.tileSize;
        
        // Logika kamera yang sama dengan TileManager
        double cameraX = playerView.worldX - (gp.screenWidth / 2.0);
        double cameraY = playerView.worldY - (gp.screenHeight / 2.0);
        
        cameraX = Math.max(0, Math.min(cameraX, worldWidth - gp.screenWidth));
        cameraY = Math.max(0, Math.min(cameraY, worldHeight - gp.screenHeight));
        
        int screenX = (int) (this.worldX - cameraX);
        int screenY = (int) (this.worldY - cameraY);
        
        // Culling dan gambar objek
        if (screenX > -gp.tileSize && screenX < gp.screenWidth && 
            screenY > -gp.tileSize && screenY < gp.screenHeight) {
            
            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
        }
    }

    /**
     * Logika interaksi default. Bisa di-override oleh subclass.
     * @param controller GameController untuk memungkinkan objek memicu aksi game.
     */
    public void interact(GameController controller) {
        System.out.println("Interacted with " + name);
    }

    public int getWorldX() {
        return this.worldX;
    }

    public int getWorldY() {
        return this.worldY;
    }
}