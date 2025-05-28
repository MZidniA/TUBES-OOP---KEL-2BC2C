package org.example.view.InteractableObject;

import java.io.IOException; 

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.controller.GameController;
import org.example.view.NPCInteractionPanel;

public class PerryHouse extends InteractableObject {

    public PerryHouse() { 
        super("Perry House"); 
        loadImage(); 
    }

    @Override
    protected void loadImage() {
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/PerryHouse.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading MayorHouse.png for PerryHouse");
        }
    }

    @Override
    public void interact(GameController controller) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(controller.getGamePanel());

        JDialog dialog = new JDialog(frame, "Interaction", true);
        dialog.setUndecorated(true); // Biar latar belakang keliatan
        NPCInteractionPanel panel = new NPCInteractionPanel(frame, "Perry");
        dialog.setContentPane(panel); // Ganti isi dialog dengan panel yang punya latar
        dialog.pack(); // Biarkan ukurannya ikut ukuran panel
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}