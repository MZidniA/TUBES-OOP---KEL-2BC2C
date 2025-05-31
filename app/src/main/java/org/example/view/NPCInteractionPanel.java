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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.controller.GameController;
import org.example.controller.action.ChattingAction;
import org.example.model.Farm;
import org.example.model.Map.Store;
import org.example.model.NPC.NPC;

public class NPCInteractionPanel extends JPanel {
    private Image backgroundImage;
    private Font pixelFont;
    private JFrame parentFrame;
    private GameController controller;
    private Farm farm;
    private String playerName;
    private JLabel infoLabel;

    public NPCInteractionPanel(JFrame parentFrame, GameController controller, Farm farm, String npcName, String playerName) {
        this.parentFrame = parentFrame;
        this.controller = controller;
        this.farm = farm;
        this.playerName = playerName;

        setLayout(null);
        setPreferredSize(new Dimension(300, 320));
        setOpaque(false);

        loadFont();
        loadBackground();

        int buttonWidth = 140;
        int buttonHeight = 30;
        int startY = 40;
        int gap = 10;

        NPC npc = controller.getFarm().getNPCByName(npcName);
        String[] actions = {"Chatting", "Gifting", "Proposing", "Marrying"};
        int buttonIndex = 0;

        for (String actionName : actions) {
            JButton btn = createPixelButton(actionName);
            btn.setBounds(80, startY + buttonIndex * (buttonHeight + gap), buttonWidth, buttonHeight);
            add(btn);

            if (actionName.equals("Chatting")) {
                btn.addActionListener(e -> {
                    List<String> dialogLines = Arrays.asList(
                        "Hai, senang bertemu denganmu!",
                        "Aku sedang bersantai hari ini.",
                        "Cuaca sangat bagus, ya?",
                        "Sampai jumpa lagi!"
                    );

                    ChattingAction action = new ChattingAction(npc, npc.getLocation());
                    if (!action.canExecute(farm)) {
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

                    updateNPCInfo(npc);
                });
            } else {
                btn.addActionListener(e -> {
                    JOptionPane.showMessageDialog(parentFrame,
                        "Kamu memilih aksi: " + actionName,
                        "Interaksi dengan " + npcName,
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
            buttonIndex++;
        }

        if (npc.getName().equalsIgnoreCase("Emily")) {
            JButton storeButton = createPixelButton("Store");
            storeButton.setBounds(80, startY + buttonIndex * (buttonHeight + gap), buttonWidth, buttonHeight);
            storeButton.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                Store store = new Store(farm.getCurrentSeason());
                StorePanel storePanel = new StorePanel(parentFrame, store, farm.getPlayerModel(), controller, farm, npcName);
                parentFrame.setContentPane(storePanel);
                parentFrame.revalidate();
                parentFrame.repaint();
            });
            add(storeButton);
            buttonIndex++;
        }

        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(76, 38, 38));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(60, 30, 30), 2));
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        int backY = startY + buttonIndex * (buttonHeight + gap) + 10;
        backButton.setBounds(90, backY, buttonWidth - 20, buttonHeight - 10);
        backButton.addActionListener(e -> {
            controller.getMainFrame().setContentPane(controller.getGamePanel());
            controller.getMainFrame().revalidate();
            controller.getMainFrame().repaint();
            controller.getGamePanel().requestFocusInWindow();
            controller.getGameState().setGameState(controller.getGameState().play);
        });
        add(backButton);

        infoLabel = new JLabel();
        infoLabel.setFont(pixelFont.deriveFont(9f));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBounds(40, backY + 50, 300, 15);
        add(infoLabel);

        updateNPCInfo(npc);
    }

    private void updateNPCInfo(NPC npc) {
        String info = "Heart: " + npc.getHeartPoints() + "    Status: " + npc.getRelationshipStatus();
        infoLabel.setText(info);
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
        button.setFont(pixelFont.deriveFont(12f));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(102, 51, 51));
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