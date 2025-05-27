package org.example.view.InteractableObject;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.example.controller.GameController;
import org.example.controller.UtilityTool;
import org.example.view.GamePanel;
import org.example.view.entitas.PlayerView;

public abstract class InteractableObject {
    public BufferedImage image; // Akan di-load oleh subclass, di-scale oleh AssetSetter
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // Default tileSize 48x48
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;
    protected static final UtilityTool uTool = new UtilityTool(); // Untuk keperluan lain jika ada

    /**
     * Konstruktor dasar untuk objek interaktif.
     * @param name Nama objek.
     */
    public InteractableObject(String name) {
        this.name = name;
        // Subclass akan memanggil loadImage() di konstruktornya.
        // Scaling akan dilakukan oleh AssetSetter.
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
        if (playerView == null || gp == null) return; // Guard clause

        // Pemain selalu di tengah layar, jadi screenX dan screenY pemain bisa dianggap
        // sebagai titik referensi untuk menghitung posisi objek di layar.
        int playerScreenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        int playerScreenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        int screenX = worldX - playerView.worldX + playerScreenX;
        int screenY = worldY - playerView.worldY + playerScreenY;

        // Culling, hanya gambar objek yang terlihat di layar
        if (worldX + gp.tileSize > playerView.worldX - playerScreenX &&
            worldX - gp.tileSize < playerView.worldX + playerScreenX + gp.tileSize && // Sedikit penyesuaian culling
            worldY + gp.tileSize > playerView.worldY - playerScreenY &&
            worldY - gp.tileSize < playerView.worldY + playerScreenY + gp.tileSize) {

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
}