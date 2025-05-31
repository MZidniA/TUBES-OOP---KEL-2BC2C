package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class HelpPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;

    public HelpPanel(JFrame frame) {
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        setOpaque(false);

        loadFont();
        loadBackground();

        // Judul
        JLabel titleLabel = new JLabel("Welcome to Spakbor Hills!");
        titleLabel.setFont(pixelFont.deriveFont(14f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(120, 40, 500, 30);
        add(titleLabel);

        // List kontrol
        String[] lines = {
            "Kontrol Dasar:",
            "",
            "  - W / A / S / D : Gerak",
            "  - F : Interaksi",
            "  - I : Inventory",
            "  - E : Makan",
            "  - G : Player Info",
            "  - ESC : Pause"
        };

        int y = 90;
        for (String line : lines) {
            JLabel label = new JLabel(line);
            label.setFont(pixelFont.deriveFont(12f));
            label.setForeground(Color.WHITE);
            label.setBounds(100, y, 500, 25); // panjang teks max 500 px
            add(label);
            y += 30; // jarak antar barisnya dibikin lega
        }

        // Tombol BACK
        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(270, 480, 100, 30);
        backButton.setBackground(new Color(102, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        backButton.addActionListener(e -> {
            frame.setContentPane(new MenuPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        add(backButton);
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            pixelFont = baseFont.deriveFont(Font.PLAIN, 14f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(baseFont);
        } catch (Exception e) {
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
