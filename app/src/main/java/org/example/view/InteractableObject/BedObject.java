package org.example.view.InteractableObject;
import java.io.IOException; 

import javax.imageio.ImageIO;

import org.example.controller.GameController;
import org.example.controller.action.SleepingAction;

public class BedObject extends InteractableObject {
    public BedObject() { 
        super("Bed"); 
        this.collision = true; 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/Bed.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Bed.png for BedObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        SleepingAction sleepingAction = new SleepingAction(controller);
        sleepingAction.execute(controller.getFarm());
    }
}

