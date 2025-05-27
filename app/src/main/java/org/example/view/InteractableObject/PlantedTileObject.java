package org.example.view.InteractableObject;

import java.io.IOException; // Lebih spesifik untuk ImageIO
import javax.imageio.ImageIO;
import org.example.controller.GameController;
// Tidak perlu import org.example.view.GamePanel;

public class PlantedTileObject extends InteractableObject {
    // HAPUS: GamePanel gp;

    public PlantedTileObject() { // Konstruktor tanpa parameter
        super("Planted Tile"); // Panggil konstruktor superclass dengan nama objek
        // this.collision = true; // Atur jika tile ini solid atau tidak
        loadImage(); // Panggil metode untuk memuat gambar spesifik
    }

    @Override
    protected void loadImage() {
        try {
            // Hanya memuat gambar, tidak melakukan scaling di sini.
            // Scaling akan dilakukan oleh AssetSetter.
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PlantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading PlantedTile.png for PlantedTileObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        // Definisikan logika interaksi spesifik di sini jika ada.
        // Misalnya, jika ini adalah tanaman yang bisa dipanen, Anda bisa memanggil
        // metode di controller untuk memproses panen.
        System.out.println("You have interacted with a Planted Tile.");
        // Contoh: controller.harvestPlant(this.worldX, this.worldY);
    }
}