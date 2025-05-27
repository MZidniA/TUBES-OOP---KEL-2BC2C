package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class OceanObject extends InteractableObject {
  GamePanel gp;
    public OceanObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Ocean";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Ocean.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        System.out.println("You are interacting with the Ocean.");
    }  
}
