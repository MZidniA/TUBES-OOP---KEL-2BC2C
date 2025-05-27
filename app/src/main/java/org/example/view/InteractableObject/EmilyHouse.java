package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class EmilyHouse extends InteractableObject {
       GamePanel gp;
    public EmilyHouse(GamePanel gp) {
        this.gp = gp;
        this.name = "Emily House";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/EmilyHouse.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        System.out.println("You are visiting Emily's House.");
    } 
}
