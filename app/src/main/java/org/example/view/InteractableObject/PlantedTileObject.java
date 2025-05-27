package org.example.view.InteractableObject;
import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class PlantedTileObject extends InteractableObject {
    GamePanel gp;
    public PlantedTileObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Planted Tile";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PlantedTile.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        // Define interaction logic for the PlantedTileObject
        System.out.println("You have interacted with a Planted Tile.");
    }
}
