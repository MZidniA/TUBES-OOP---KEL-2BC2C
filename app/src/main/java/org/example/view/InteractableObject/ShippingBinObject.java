package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;
import org.example.controller.GameState;
import org.example.view.GameStateUI; 

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
        GameState gs = controller.getGameState();
        GameStateUI ui = controller.getGameStateUI();

        if (gs != null &&  ui != null) {
            gs.setGameState(gs.shipping_bin);
            ui.slotCol = 0; 
            ui.slotRow = 0; 
            return;
        }
    }
}