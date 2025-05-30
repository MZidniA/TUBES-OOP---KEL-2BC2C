package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;
import java.awt.Rectangle;

import org.example.controller.GameController;
import org.example.controller.action.PlantingAction;
import org.example.controller.action.RecoverLandAction;
import org.example.model.Items.Items;
import org.example.model.Items.Seeds;
import org.example.model.Map.*;
import org.example.model.Farm;
import org.example.model.Player;


public class UnplantedTileObject extends InteractableObject {

    public UnplantedTileObject() { 
        super("Unplanted Tile");
        this.collision = false;
        this.solidArea = new Rectangle(0, 0, 10, 10);
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/UnplantedTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interact(GameController controller) {
        Farm farm = controller.getFarmModel();
        Player player = controller.getFarmModel().getPlayerModel();
        Items heldItem  = player.getCurrentHeldItem();

        int tileX = this.worldX / controller.getTileSize();
        int tileY = this.worldY / controller.getTileSize();


        if (heldItem instanceof Seeds) {
            Seeds seedToPlant = (Seeds) heldItem; 
            

            PlantingAction plantingAction = new PlantingAction(controller, seedToPlant, tileX, tileY);
            if (plantingAction.canExecute(controller.getFarmModel())) {
                plantingAction.execute(controller.getFarmModel());
            } else {
                System.out.println("UnplantedTileObject: Tidak bisa menanam " + seedToPlant.getName() + " di sini/saat ini.");
            }
        } else if (heldItem != null && heldItem.getName().equalsIgnoreCase("Pickaxe")) {
            System.out.println("PlantedTileObject: Mencoba mengembalikan tanah di (" + tileX + "," + tileY + ")");
            RecoverLandAction recoverLandAction = new RecoverLandAction(controller, tileX, tileY);
            if (recoverLandAction.canExecute(farm)) {
                recoverLandAction.execute(farm);
            } else {
                System.out.println("PlantedTileObject: Tidak bisa mengembalikan tanah saat ini (mungkin energi kurang).");
            }
        } else {
            System.out.println("UnplantedTileObject: Tidak ada tindakan yang bisa dilakukan di sini.");
        }  
    }
}