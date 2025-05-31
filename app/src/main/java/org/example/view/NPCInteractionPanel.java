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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.example.controller.GameController;
import org.example.controller.action.ChattingAction;
import org.example.controller.action.MarryingAction;
import org.example.controller.action.ProposingAction;
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
    private boolean drawBackground = true;

    public NPCInteractionPanel(JFrame parentFrame, GameController controller, Farm farm, String npcName, String playerName) {
        this(parentFrame, controller, farm, npcName, playerName, true);
    }

    public NPCInteractionPanel(JFrame parentFrame, GameController controller, Farm farm, String npcName, String playerName, boolean drawBackground) {
        this.parentFrame = parentFrame;
        this.controller = controller;
        this.farm = farm;
        this.playerName = playerName;
        this.drawBackground = drawBackground;

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
        int buttonIndex = 0;

        for (String actionName : actions) {
            JButton btn = createPixelButton(actionName);
            btn.setBounds((panelWidth - buttonWidth) / 2, startY + buttonIndex * (buttonHeight + gap), buttonWidth, buttonHeight);
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
                    break;

                case "Gifting":
                    btn.addActionListener(e -> {
                        GiftingDialogPanel giftPanel = new GiftingDialogPanel(parentFrame, controller, npc, farm);

                        JDialog giftDialog = new JDialog(parentFrame, "Pilih Hadiah untuk " + npc.getName(), true);
                        giftDialog.setUndecorated(true);
                        giftDialog.setContentPane(giftPanel);
                        giftDialog.pack();
                        giftDialog.setLocationRelativeTo(parentFrame);
                        giftDialog.setVisible(true);
                        updateNPCInfo(npc);
                    });
                    break;

                case "Proposing":
                    btn.addActionListener(e -> {
                        ProposingAction action = new ProposingAction(npc, farm);
                        action.execute(farm);

                        if (action.isAlreadySpouse()) {
                            showStyledMessage("Sudah Menikah", npc.getName() + " sudah menjadi pasangan hidupmu.");
                        } else if (action.isAlreadyFiance()) {
                            showStyledMessage("Sudah Bertunangan", "Kamu sudah bertunangan dengan " + npc.getName());
                        } else if (action.isNoRing()) {
                            showStyledMessage("Tidak Ada Proposal Ring", "Kamu tidak memiliki Proposal Ring!");
                        } else if (action.isSuccess()) {
                            showStyledMessage("Lamaran Berhasil 💍", npc.getName() + " menerima lamaranmu!");
                        } else if (action.isRejected()) {
                            showStyledMessage("Lamaran Ditolak", "Lamaranmu ditolak. Bangun relasi lebih kuat dulu ya");
                        }
                        updateNPCInfo(npc);
                    });
                    break;

                case "Marrying":
                    btn.addActionListener(e -> {
                        MarryingAction action = new MarryingAction(npc);

                        if (action.canExecute(farm)) {
                            action.execute(farm);
                            showStyledMessage("Pernikahan Berhasil 💍", npc.getName() + " kini menjadi pasangan hidupmu!");
                        } else if (action.isNoRing()) {
                            showStyledMessage("Tidak Ada Proposal Ring", "Kamu tidak memiliki Proposal Ring!");
                        } else if (action.isFailedNotFiance()) {
                            showStyledMessage("Gagal Menikah", npc.getName() + " belum menjadi tunanganmu.");
                        } else if (action.isFailedSameDay()) {
                            showStyledMessage("Belum Waktunya", "Kamu baru bertunangan hari ini. Menikah bisa dilakukan mulai besok.");
                        } else {
                            showStyledMessage("Gagal Menikah", "Syarat pernikahan belum terpenuhi.");
                        }
                        updateNPCInfo(npc);
                    });
                    break;
            }
            buttonIndex++;
        }

        if (npc.getName().equalsIgnoreCase("Emily")) {
            JButton storeButton = createPixelButton("Store");
            storeButton.setBounds((panelWidth - buttonWidth) / 2, startY + buttonIndex * (buttonHeight + gap), buttonWidth, buttonHeight);
            storeButton.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();

                Store store = new Store(farm.getCurrentSeason());
                StorePanel storePanel = new StorePanel(parentFrame, store, farm.getPlayerModel(), controller, farm, npc.getName());
                parentFrame.setContentPane(storePanel);
                parentFrame.setSize(640, 576);
                parentFrame.setLocationRelativeTo(null);
                parentFrame.revalidate();
                parentFrame.repaint();
            });
            add(storeButton);
            buttonIndex++;
        }

        JButton backButton = createPixelButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        int backY = startY + buttonIndex * (buttonHeight + gap) + 10;
        backButton.setBounds((panelWidth - (buttonWidth - 20)) / 2, backY, buttonWidth - 20, buttonHeight - 10);
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
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setBounds(0, backY + 35, panelWidth, 15);
        add(infoLabel);

        updateNPCInfo(npc);
    }

    private void showStyledMessage(String title, String message) {
        UIManager.put("OptionPane.background", new Color(255, 228, 196));
        UIManager.put("Panel.background", new Color(255, 228, 196));
        UIManager.put("OptionPane.messageFont", pixelFont.deriveFont(12f));
        UIManager.put("Button.background", new Color(102, 51, 51));
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
        if (drawBackground && backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}