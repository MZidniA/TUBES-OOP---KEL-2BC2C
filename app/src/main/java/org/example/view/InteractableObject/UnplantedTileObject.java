package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class UnplantedTileObject extends InteractableObject {

    public UnplantedTileObject() { 
        super("Unplanted Tile"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/UnplantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading UnplantedTile.png for UnplantedTileObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("Interacting with the Unplanted Tile");
    }
}