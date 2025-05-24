package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

public class ShippingBinObject extends InteractableObject {

    public ShippingBinObject() {
        this.name = "Shipping Bin";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/ShippingBin.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        // Implement interaction logic here
        System.out.println("Interacting with the Shipping Bin");
    }
}
