package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class DoorObject extends InteractableObject {

    public DoorObject() {
        super("Door"); 
        this.collision = true;
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Door.png for DoorObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        int targetTileSize = controller.getTileSize();
        controller.visitingAction(4, 6 * targetTileSize, 6 * targetTileSize);
    }
}