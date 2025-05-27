package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class ShippingBinObject extends InteractableObject {

    GamePanel gp;
    public ShippingBinObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Shipping Bin";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/ShippingBin.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); // Assuming you want to scale the image to 48x48 pixels
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
