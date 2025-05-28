package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class RiverObject extends InteractableObject {

    public RiverObject() { 
        super("River");
        this.collision = true; 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/River.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading River.png for RiverObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are interacting with the River.");
    }
}