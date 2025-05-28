package org.example.view.InteractableObject;

import java.io.IOException; // Lebih spesifik untuk ImageIO
import javax.imageio.ImageIO;
import org.example.controller.GameController;
// Tidak perlu import org.example.view.GamePanel;

public class PondObject extends InteractableObject {
    // HAPUS: GamePanel gp;

    public PondObject() { // Konstruktor tanpa parameter
        super("Pond"); // Panggil konstruktor superclass dengan nama objek
        this.collision = true; // Atur jika kolam solid atau tidak (biasanya solid)
        loadImage(); // Panggil metode untuk memuat gambar spesifik
    }

    @Override
    protected void loadImage() {
        try {
            // Hanya memuat gambar, tidak melakukan scaling di sini.
            // Scaling akan dilakukan oleh AssetSetter.
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Pond.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Pond.png for PondObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        // Definisikan logika interaksi spesifik untuk kolam di sini jika ada.
        // Misalnya, jika pemain bisa memancing di kolam ini, Anda bisa
        // memanggil metode di controller untuk memulai aksi memancing.
        System.out.println("You are interacting with the pond.");
        // Contoh: if(controller.getPlayerHasFishingRod()) { controller.startFishing(); }
    }
}