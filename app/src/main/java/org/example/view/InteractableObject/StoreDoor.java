package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class StoreDoor extends InteractableObject {

    public StoreDoor() { 
        super("Store Door"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/StoreDoor.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading StoreDoor.png for Store");
        }
    }

    @Override
    public void interact(GameController controller) {
        int tileSize = controller.getTileSize();
        int currentMap = controller.getFarmModel().getCurrentMap();
        controller.teleportPlayer(3, 23 * tileSize, 8 * tileSize);
    }
}
