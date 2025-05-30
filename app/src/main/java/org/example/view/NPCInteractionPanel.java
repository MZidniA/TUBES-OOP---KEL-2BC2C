package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.example.controller.GameController;
import org.example.controller.action.ChattingAction;
import org.example.model.Farm;
import org.example.model.NPC.AbigailNPC;
import org.example.model.NPC.CarolineNPC;
import org.example.model.NPC.DascoNPC;
import org.example.model.NPC.EmilyNPC;
import org.example.model.NPC.MayorTadiNPC;
import org.example.model.NPC.NPC;
import org.example.model.NPC.PerryNPC;

public class NPCInteractionPanel extends JPanel {
    private Image backgroundImage;
    private Font pixelFont;
    private JFrame parentFrame;
    private GameController controller;
    private Farm farm;
    private String playerName;

    public NPCInteractionPanel(JFrame parentFrame, GameController controller, Farm farm, String npcName, String playerName) {
        this.parentFrame = parentFrame;
        this.controller = controller;
        this.farm = farm;
        this.playerName = playerName;

        setLayout(null);
        setPreferredSize(new Dimension(300, 250));
        setOpaque(false);

        loadFont();
        loadBackground();

        int buttonWidth = 140;
        int buttonHeight = 30;
        int startY = 20;
        int gap = 10;

        String[] actions = {"Chatting", "Gifting", "Proposing", "Marrying"};
        for (int i = 0; i < actions.length; i++) {
            String actionName = actions[i];
            JButton btn = createPixelButton(actionName);
            btn.setBounds(80, startY + i * (buttonHeight + gap), buttonWidth, buttonHeight);
            add(btn);

            if (actionName.equals("Chatting")) {
                btn.addActionListener(e -> {
                    List<String> dialogLines = Arrays.asList(
                        "Hai, senang bertemu denganmu!",
                        "Aku sedang bersantai hari ini.",
                        "Cuaca sangat bagus, ya?",
                        "Sampai jumpa lagi!"
                    );

                    NPC npc;
                    switch (npcName) {
                        case "Abigail":
                            npc = new AbigailNPC();
                            break;
                        case "Caroline":
                            npc = new CarolineNPC();
                            break;
                        case "Perry":
                            npc = new PerryNPC();
                            break;
                        case "Emily":
                            npc = new EmilyNPC();
                            break;
                        case "Dasco":
                            npc = new DascoNPC();
                            break;
                        case "Mayor Tadi":
                            npc = new MayorTadiNPC();
                            break;
                        default:
                            throw new IllegalArgumentException("NPC tidak dikenali: " + npcName);
                    }

                    ChattingAction action = new ChattingAction(npc, npc.getLocation());
                    if (!action.canExecute(farm)){
                        EnergyWarningDialog warning = new EnergyWarningDialog(parentFrame);
                        warning.setVisible(true);
                        return;
                    }
                    ChattingDialogPanel dialogPanel = new ChattingDialogPanel(parentFrame, dialogLines, npcName, playerName, action, farm);

                    JDialog dialog = new JDialog(parentFrame, "Chatting with " + npcName, true);
                    dialog.setUndecorated(true);
                    dialog.setContentPane(dialogPanel);
                    dialog.pack();
                    dialog.setLocationRelativeTo(parentFrame);
                    dialog.setVisible(true);
                });
            } else {
                btn.addActionListener(e -> {
                    JOptionPane.showMessageDialog(parentFrame,
                        "Kamu memilih aksi: " + actionName,
                        "Interaksi dengan " + npcName,
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
        }

        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(76, 38, 38)); // Coklat lebih gelap
        backButton.setBorder(BorderFactory.createLineBorder(new Color(60, 30, 30), 2));
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setBounds(90, startY + actions.length * (buttonHeight + gap) + 10, buttonWidth - 20, buttonHeight - 10);
        backButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        add(backButton);

    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            pixelFont = baseFont.deriveFont(Font.PLAIN, 16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.PLAIN, 16);
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/box/box.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createPixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(12f)); // Ukuran font lebih kecil dari sebelumnya
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(102, 51, 51)); // Coklat biasa
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}