package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;


public class DoorObject extends InteractableObject {
    GamePanel gp;
    public DoorObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Door";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Assuming you want to scale the image to 48x48 pixels
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        // Logic for interacting with the door, e.g., opening it
        System.out.println("You opened the door.");
    }
}
