package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;

public class ShippingBinObject extends InteractableObject {

    public ShippingBinObject() { 
        super("Shipping Bin"); 
        this.collision = true; 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/ShippingBin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ShippingBin.png for ShippingBinObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("Interacting with the Shipping Bin");
    }
}