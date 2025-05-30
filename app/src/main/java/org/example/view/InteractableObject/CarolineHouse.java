package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.controller.GameController;
import org.example.view.NPCInteractionPanel;

public class CarolineHouse extends InteractableObject {

    public CarolineHouse() { 
        super("Caroline House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/CarolineHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading CarolineHouse.png for CarolineHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(controller.getGamePanel());

        JDialog dialog = new JDialog(frame, "Interaction", true);
        dialog.setUndecorated(true);
        String playerName = controller.getFarm().getPlayerModel().getName();
        NPCInteractionPanel panel = new NPCInteractionPanel(frame, controller, controller.getFarm(), "Caroline", playerName);
        dialog.setContentPane(panel); 
        dialog.pack(); 
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}