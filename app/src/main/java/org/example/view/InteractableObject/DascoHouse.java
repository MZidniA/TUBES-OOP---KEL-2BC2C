package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class DascoHouse extends InteractableObject {
    GamePanel gp;
    public DascoHouse(GamePanel gp) {
        this.gp = gp;
        this.name = "Dasco House";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/DascoHouse.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        System.out.println("You are visiting Dasco's House.");
    }
}
