package org.example.view.InteractableObject;

import java.io.IOException; // Lebih spesifik untuk ImageIO
import javax.imageio.ImageIO;
import org.example.controller.GameController;
// Tidak perlu import org.example.view.GamePanel;

public class UnplantedTileObject extends InteractableObject {
    // HAPUS: GamePanel gp;

    public UnplantedTileObject() { // Konstruktor tanpa parameter
        super("Unplanted Tile"); // Panggil konstruktor superclass dengan nama objek
        // this.collision = false; // Tile yang belum ditanami biasanya tidak solid (bisa dilewati)
        loadImage(); // Panggil metode untuk memuat gambar spesifik
    }

    @Override
    protected void loadImage() {
        try {
            // Hanya memuat gambar, tidak melakukan scaling di sini.
            // Scaling akan dilakukan oleh AssetSetter.
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/UnplantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading UnplantedTile.png for UnplantedTileObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        // Implementasikan logika interaksi spesifik untuk tile yang belum ditanami.
        // Misalnya, jika pemain memegang cangkul, tile ini bisa diubah menjadi TilledLand.
        // Jika pemain memegang bibit, bibit bisa ditanam di sini (jika sudah dicangkul).
        System.out.println("Interacting with the Unplanted Tile");
        // Contoh:
        // Items heldItem = controller.getFarmModel().getPlayerModel().getCurrentHeldItem();
        // if (heldItem != null && heldItem.getName().equals("Hoe")) {
        //    controller.tillSoil(this.worldX, this.worldY);
        // } else if (heldItem instanceof Seeds) {
        //    controller.plantSeed((Seeds)heldItem, this.worldX, this.worldY);
        // }
    }
}