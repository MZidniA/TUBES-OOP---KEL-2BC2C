package org.example.view.InteractableObject;

import javax.imageio.ImageIO;

import org.example.controller.GamePanel;


public class DoorObject extends InteractableObject {
    GamePanel gp;
    public DoorObject(GamePanel gp) {
        this.gp = gp;
        this.name = "Door";
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Door.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact() {
        gp.teleportPlayer(4, 6 * gp.tileSize, 6 * gp.tileSize);
    }
}
