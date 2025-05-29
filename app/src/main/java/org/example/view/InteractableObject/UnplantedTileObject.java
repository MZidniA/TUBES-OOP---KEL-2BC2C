package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;
import java.awt.Rectangle;

import org.example.controller.GameController;

public class UnplantedTileObject extends InteractableObject {

    public UnplantedTileObject() { 
        super("Unplanted Tile");
        this.collision = false;
        this.solidArea = new Rectangle(0, 0, 5, 5);
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