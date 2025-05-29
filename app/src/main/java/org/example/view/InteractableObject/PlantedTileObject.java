package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class PlantedTileObject extends InteractableObject {
    public PlantedTileObject() { 
        super("Planted Tile"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PlantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading PlantedTile.png for PlantedTileObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You have interacted with a Planted Tile.");
    }
}