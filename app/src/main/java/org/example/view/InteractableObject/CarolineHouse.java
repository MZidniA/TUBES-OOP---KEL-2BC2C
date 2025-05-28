package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class CarolineHouse extends InteractableObject {

    public CarolineHouse() { 
        super("Caroline House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/CarolineHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading CarolineHouse.png for CarolineHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are visiting Caroline's House.");
    }
}