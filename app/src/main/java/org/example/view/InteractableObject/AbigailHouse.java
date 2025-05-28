package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class AbigailHouse extends InteractableObject {

    public AbigailHouse() { 
        super("Abigail House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/AbigailHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading AbigailHouse.png for AbigailHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are visiting Abigail's House.");
    }
}