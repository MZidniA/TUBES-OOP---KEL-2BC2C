package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class MayorHouse extends InteractableObject {
      GamePanel gp;
    public MayorHouse(GamePanel gp) {
        this.gp = gp;
        this.name = "Mayor Tadi House";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/MayorHouse.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void interact() {
        System.out.println("You are visiting Mayor Tadi's House.");
    }  
}
