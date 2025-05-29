package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.InputStream;

import javax.swing.*;

public class NPCInteractionPanel extends JPanel {
    private Image backgroundImage;
    private Font pixelFont;
    private JFrame parentFrame;

    public NPCInteractionPanel(JFrame parentFrame, String npcName) {
        this.parentFrame = parentFrame;
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

            btn.addActionListener(e -> {
                JOptionPane.showMessageDialog(parentFrame,
                        "Kamu memilih aksi: " + actionName,
                        "Interaksi dengan " + npcName,
                        JOptionPane.INFORMATION_MESSAGE);
            });

            add(btn);
        }

        // Tambah tombol Back di paling bawah
        JButton backButton = new JButton("Back");
        backButton.setFont(pixelFont);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(153, 102, 102)); // warna normal
        backButton.setOpaque(true);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(90, 60, 60), 2));

        // Hover effect
        backButton.getModel().addChangeListener(e -> {
            if (backButton.getModel().isRollover()) {
                backButton.setBackground(new Color(119, 68, 68)); // warna hover
            } else {
                backButton.setBackground(new Color(153, 102, 102)); // warna default
            }
        });

        int backButtonWidth = 100;
        int backButtonHeight = 25;
        int totalHeight = startY + actions.length * (buttonHeight + gap) + 10;
        backButton.setBounds((300 - backButtonWidth) / 2, totalHeight, backButtonWidth, backButtonHeight);

        backButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
        });
        add(backButton);
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(10f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = new Font("Arial", Font.PLAIN, 10);
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/box/box.png")).getImage();
        } catch (Exception e) {
            System.err.println("Gagal load box.png");
        }
    }

    private JButton createPixelButton(String text) {
        JButton button = new JButton(text);
        button.setFont(pixelFont);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBackground(new Color(204, 153, 102));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 60), 2));

        button.getModel().addChangeListener(e -> {
            if (button.getModel().isRollover()) {
                button.setBackground(new Color(190, 130, 90));
            } else {
                button.setBackground(new Color(204, 153, 102));
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
