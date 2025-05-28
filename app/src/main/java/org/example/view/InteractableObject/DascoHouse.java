package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class DascoHouse extends InteractableObject {

    public DascoHouse() { 
        super("Dasco House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/DascoHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading DascoHouse.png for DascoHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are visiting Dasco's House.");
    }
}