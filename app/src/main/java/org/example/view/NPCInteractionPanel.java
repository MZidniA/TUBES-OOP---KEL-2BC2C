package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NPCInteractionPanel extends  JPanel {
    private Image backgroundImage;
    private Font pixelFont;
    private JFrame parentFrame;

    public NPCInteractionPanel(JFrame parentFrame, String npcName) {
        this.parentFrame = parentFrame;
        setLayout(null);
        setPreferredSize(new Dimension(300, 200));
        setOpaque(false);

        loadFont();
        loadBackground();

        int buttonWidth = 140;
        int buttonHeight = 30;
        int startY = 30;
        int gap = 10;

        String[] actions = {"Chatting", "Gifting", "Proposing", "Marrying"};
        for (int i = 0; i < actions.length; i++) {
            JButton btn = createPixelButton(actions[i]);
            btn.setBounds(80, startY + i * (buttonHeight + gap), buttonWidth, buttonHeight);
            add(btn);
        }
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
        button.setBackground(new Color(204, 153, 102)); // warna coklat klasik Stardew
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 90, 60), 2));

        // Efek hover
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
            g.setColor(new Color(240, 240, 240)); // fallback abu
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
