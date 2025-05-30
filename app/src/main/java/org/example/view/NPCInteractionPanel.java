package org.example.view;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.example.controller.GameController;
import org.example.controller.action.ChattingAction;
import org.example.model.Farm;
import org.example.model.Items.Items;
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
        setPreferredSize(new Dimension(360, 360));
        setOpaque(false);

        loadFont();
        loadBackground();

        int panelWidth = 360;
        int buttonWidth = 140;
        int buttonHeight = 30;
        int startY = 60;
        int gap = 10;

        NPC npc = controller.getFarm().getNPCByName(npcName);

        String[] actions = {"Chatting", "Gifting", "Proposing", "Marrying"};
        for (int i = 0; i < actions.length; i++) {
            String actionName = actions[i];
            JButton btn = createPixelButton(actionName);
            btn.setBounds((panelWidth - buttonWidth) / 2, startY + i * (buttonHeight + gap), buttonWidth, buttonHeight);
            add(btn);

            switch (actionName) {
                case "Chatting":
                    btn.addActionListener(e -> {
                        List<String> dialogLines = Arrays.asList(
                            "Hai, senang bertemu denganmu!",
                            "Aku sedang bersantai hari ini.",
                            "Cuaca sangat bagus, ya?",
                            "Sampai jumpa lagi!"
                        );

                        ChattingAction action = new ChattingAction(npc, npc.getLocation());
                        if (!action.canExecute(farm)) {
                            showStyledMessage("Tidak Cukup Energi", "Kamu tidak punya\nenergi cukup untuk ngobrol.");
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
                    break;

                case "Gifting":
                    btn.addActionListener(e -> {
                        if (npc.hasGiftedToday()) {
                            showStyledMessage("Sudah Dikasih", "Kamu sudah memberikan\nhadiah hari ini!");
                            return;
                        }

                        GiftingDialogPanel giftPanel = new GiftingDialogPanel(parentFrame, controller, npc, farm);

                        JDialog giftDialog = new JDialog(parentFrame, "Pilih Hadiah untuk " + npc.getName(), true);
                        giftDialog.setUndecorated(true);
                        giftDialog.setContentPane(giftPanel);
                        giftDialog.pack();
                        giftDialog.setLocationRelativeTo(parentFrame);
                        giftDialog.setVisible(true);
                    });
                    break;

                case "Proposing":
                    btn.addActionListener(e -> showStyledMessage("Proposing", "Kamu mencoba\nmelamar " + npc.getName() + "."));
                    break;

                case "Marrying":
                    btn.addActionListener(e -> showStyledMessage("Marrying", "Kamu mencoba\nmenikah dengan " + npc.getName() + "."));
                    break;
            }
        }

        JButton backButton = createPixelButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        int backY = startY + actions.length * (buttonHeight + gap) + 10;
        backButton.setBounds((panelWidth - (buttonWidth - 20)) / 2, backY, buttonWidth - 20, buttonHeight - 10);
        backButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        add(backButton);

        infoLabel = new JLabel();
        infoLabel.setFont(pixelFont.deriveFont(9f));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBounds(0, backY + 35, panelWidth, 15);
        add(infoLabel);

        updateNPCInfo(npc);
    }

    private void showStyledMessage(String title, String message) {
        UIManager.put("OptionPane.background", new Color(255, 228, 196)); // Light brown
        UIManager.put("Panel.background", new Color(255, 228, 196));
        UIManager.put("OptionPane.messageFont", pixelFont.deriveFont(12f));
        UIManager.put("Button.background", new Color(102, 51, 51)); // Dark brown
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", pixelFont.deriveFont(10f));

        String formatted = "<html><center>" + message.replace("\n", "<br>") + "</center></html>";
        JOptionPane.showMessageDialog(parentFrame, formatted, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateNPCInfo(NPC npc) {
        int heartPoints = Math.min(npc.getHeartPoints(), 150);
        String info = "Heart: " + heartPoints + " / 150    Status: " + npc.getRelationshipsStatus();
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
