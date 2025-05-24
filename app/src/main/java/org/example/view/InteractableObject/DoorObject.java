package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

public class DoorObject extends InteractableObject {

    public DoorObject() {
        this.name = "Door";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
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
