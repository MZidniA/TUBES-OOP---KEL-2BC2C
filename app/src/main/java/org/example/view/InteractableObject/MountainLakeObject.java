package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class MountainLakeObject extends InteractableObject {
    
    public MountainLakeObject() {
        super("Mountain Lake");
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/MountainLake.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MountainLake.png for MountainLakeObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are interacting with the Mountain Lake.");
    }
}