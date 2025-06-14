package org.example.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Map;

import org.example.controller.GameController;
import org.example.controller.action.GiftingAction;
import org.example.model.Farm;
import org.example.model.Items.Items;
import org.example.model.NPC.NPC;

public class GiftingDialogPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;
    private final int COLUMNS = 5;

    public GiftingDialogPanel(JFrame frame, GameController controller, NPC npc, Farm farm) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(420, 350));
        setOpaque(false);

        loadFont();
        loadBackground();

        Map<Items, Integer> inventory = farm.getPlayerModel().getInventory().getInventory();

        JPanel gridPanel = new JPanel(new GridLayout(0, COLUMNS, 12, 12));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        for (Items item : inventory.keySet()) {
            if (!item.isGiftable()) continue;

            int qty = inventory.get(item);
            JButton btn = new JButton("<html><center>" + item.getName() + "<br>x" + qty + "</center></html>");
            btn.setFont(pixelFont.deriveFont(9f));
            btn.setPreferredSize(new Dimension(60, 60));
            btn.setBackground(new Color(210, 180, 140));
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(BorderFactory.createLineBorder(new Color(100, 60, 40), 2));
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> {
                GiftingAction action = new GiftingAction(npc, item);
                if (action.canExecute(farm)) {
                    action.execute(farm);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Tidak bisa memberikan hadiah.\nPastikan kamu punya energi dan item yang cukup.",
                        "Gagal", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Tampilkan pesan sukses
                UIManager.put("OptionPane.background", new Color(255, 228, 196));
                UIManager.put("Panel.background", new Color(255, 228, 196));
                UIManager.put("OptionPane.messageFont", pixelFont.deriveFont(12f));
                UIManager.put("Button.background", new Color(139, 69, 19));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("Button.font", pixelFont.deriveFont(10f));

                JOptionPane.showMessageDialog(frame,
                    "Kamu memberikan\n" + item.getName() + " kepada " + npc.getName(),
                    "Gift Result", JOptionPane.INFORMATION_MESSAGE);

                SwingUtilities.getWindowAncestor(this).dispose(); 
            });

            gridPanel.add(btn);
        }


        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(76, 38, 38));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(60, 30, 30), 2));
        backButton.setFocusPainted(false);
        backButton.setOpaque(true);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            pixelFont = baseFont.deriveFont(Font.PLAIN, 14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.PLAIN, 14);
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/box/box.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
