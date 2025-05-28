package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class MayorHouse extends InteractableObject {

    public MayorHouse() { 
        super("Mayor Tadi House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/MayorHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MayorHouse.png for MayorHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are visiting Mayor Tadi's House.");
    }
}
