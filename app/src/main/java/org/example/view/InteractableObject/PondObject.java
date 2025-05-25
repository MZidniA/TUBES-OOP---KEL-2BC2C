package org.example.view.InteractableObject;
import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class PondObject extends InteractableObject {
    GamePanel gp;
    public PondObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Pond";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Pond.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        // Define interaction logic for the pond here
        System.out.println("You are interacting with the pond.");
    }
}
