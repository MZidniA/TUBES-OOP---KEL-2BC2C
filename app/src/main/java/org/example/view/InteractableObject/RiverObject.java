package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class RiverObject extends InteractableObject {
GamePanel gp;
    public RiverObject(GamePanel gp) {
        this.gp = gp;
        this.name = "River";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/River.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        System.out.println("You are interacting with the River.");
    }
}
