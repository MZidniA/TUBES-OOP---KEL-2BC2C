package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class EmilyStore extends InteractableObject {

    public EmilyStore() { 
        super("Store"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/EmilyStore.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading EmilyStore.png for EmilyStore");
        }
    }

    @Override
    public void interact(GameController controller) {
        int tileSize = controller.getTileSize();
        int currentMap = controller.getFarmModel().getCurrentMap();
        controller.visitingAction(5, 9 * tileSize, 25 * tileSize);
    }
}