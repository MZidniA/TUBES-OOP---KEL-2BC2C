package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class CarolineHouse extends InteractableObject {
    GamePanel gp;
    public CarolineHouse(GamePanel gp) {
        this.gp = gp;
        this.name = "Caroline House";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/CarolineHouse.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        System.out.println("You are visiting Caroline's House.");
    }
}
