package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class PerryHouse extends InteractableObject {

    public PerryHouse() { 
        super("Perry House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PerryHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MayorHouse.png for PerryHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are visiting Perry's House.");
    }
}