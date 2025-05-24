package org.example.view.InteractableObject;
import javax.imageio.ImageIO;

public class UnplantedTileObject extends InteractableObject {

    public UnplantedTileObject() {
        this.name = "Unplanted Tile";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/UnplantedTile.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        // Implement interaction logic here
        System.out.println("Interacting with the Unplanted Tile");
    }
}
