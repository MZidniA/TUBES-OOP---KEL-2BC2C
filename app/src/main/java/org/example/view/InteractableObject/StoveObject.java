package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class StoveObject extends InteractableObject {

    public StoveObject() {
        super("Stove");
        this.collision = true;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Stove.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Stove.png for StoveObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("Interacting with the Stove");
    }
}