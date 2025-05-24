package org.example.view.InteractableObject;
import javax.imageio.ImageIO;

public class PondObject extends InteractableObject {

    public PondObject() {
        this.name = "Pond";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Pond.png"));
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
