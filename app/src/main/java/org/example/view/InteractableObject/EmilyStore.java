package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class EmilyStore extends InteractableObject{
    GamePanel gp;
    public EmilyStore(GamePanel gp) {
        this.gp = gp;
        this.name = "Store";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/EmilyStore.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        System.out.println("You are visiting Store.");
    }
}
