package org.example.view.InteractableObject;


import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.GameController;

import java.awt.Rectangle;

public class PlantedTileObject extends InteractableObject {
    public PlantedTileObject(String seedName, int worldX, int worldY, GameController controller) {
        super("Tanaman " + seedName); 
        this.worldX = worldX;
        this.worldY = worldY;
        this.collision = false; 
        this.solidArea = new Rectangle(0, 0,5,5);
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