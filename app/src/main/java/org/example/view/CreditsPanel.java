package org.example.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class CreditsPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;
    private JFrame parentFrame;

    public CreditsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        setOpaque(false);

        loadFont();
        loadBackground();

        int baseY = 150; // <- GESER KE BAWAH LEBIH DEKAT TENGAH
        int spacing = 35;

        JLabel titleLabel = new JLabel("CREDITS", SwingConstants.CENTER);
        titleLabel.setFont(pixelFont.deriveFont(16f));
        titleLabel.setBounds(0, baseY, 640, 30);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        String[] creators = {
            "Sarah Alwa Neguita Surbakti - 18223023",
            "Ferro Arka Berlian - 18223027",
            "Inggried Amelia Deswanty - 18223035",
            "Muhammad Zidni Alkindi - 18223071"
        };

        int y = baseY + spacing;
        for (String name : creators) {
            JLabel label = new JLabel(name, SwingConstants.CENTER);
            label.setFont(pixelFont.deriveFont(10f));
            label.setForeground(Color.WHITE);
            label.setBounds(0, y, 640, 25);
            add(label);
            y += spacing;
        }

        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(270, y + 10, 100, 30);
        backButton.setBackground(new Color(102, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        backButton.addActionListener(e -> {
            parentFrame.setContentPane(new MenuPanel(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        add(backButton);
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
