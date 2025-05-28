package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class PondObject extends InteractableObject {

    public PondObject() { 
        super("Pond"); 
        this.collision = true; 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Pond.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Pond.png for PondObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are interacting with the pond.");
    }
}
