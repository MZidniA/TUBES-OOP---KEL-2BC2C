package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class OceanObject extends InteractableObject {

    public OceanObject() { 
        super("Ocean"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Ocean.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Ocean.png for OceanObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are interacting with the Ocean.");
    }
}