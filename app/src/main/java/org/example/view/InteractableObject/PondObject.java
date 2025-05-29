package org.example.view.InteractableObject;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.GameController;
import org.example.model.Items.ItemDatabase;
import org.example.model.Player;
import org.example.model.enums.LocationType;

public class PondObject extends InteractableObject {

    public PondObject() {
        super("Pond");
        this.collision = false;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Pond.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact(GameController controller) {
        System.out.println("You are interacting with the Pond.");
        Player player = controller.getFarmModel().getPlayerModel();

        player.setCurrentLocationType(LocationType.POND);

        if (player.getInventory().hasItem(ItemDatabase.getItem("Fishing Rod"), 1)) {
            controller.openFishingPanel();
        } else {
            System.out.println("You need a Fishing Rod to fish.");
        }
    }
}
