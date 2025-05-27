package org.example.view.InteractableObject;

import javax.imageio.ImageIO;
import org.example.controller.GameController;
import java.io.IOException;

public class DoorObject extends InteractableObject {

    public DoorObject() {
        super("Door"); // Panggil konstruktor superclass dengan nama objek
        this.collision = true;
        loadImage(); // Panggil metode untuk memuat gambar spesifik DoorObject
    }

    @Override
    protected void loadImage() {
        try {
            // Hanya memuat gambar, tidak melakukan scaling di sini.
            // Scaling akan dilakukan oleh AssetSetter.
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Door.png for DoorObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        int targetTileSize = controller.getTileSize();
        // Koordinat target teleportasi menggunakan tileSize dari controller
        controller.teleportPlayer(4, 6 * targetTileSize, 6 * targetTileSize);
    }
}