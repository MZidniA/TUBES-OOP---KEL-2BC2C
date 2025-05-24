package org.example.view.InteractableObject;
import javax.imageio.ImageIO;

public class PlantedTileObject extends InteractableObject {
    public PlantedTileObject() {
        this.name = "Planted Tile";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PlantedTile.png"));
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
