package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class StatisticsPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;

    public StatisticsPanel(JFrame parentFrame) {
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        setOpaque(false);

        loadFont();
        loadBackground();

        JLabel titleLabel = new JLabel("STATISTICS");
        titleLabel.setFont(pixelFont.deriveFont(14f));
        titleLabel.setBounds(200, 40, 300, 30);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JLabel msg = new JLabel("Belum bisa akses statistik");
        msg.setFont(pixelFont.deriveFont(10f));
        msg.setBounds(100, 120, 500, 20);
        msg.setForeground(Color.WHITE);
        add(msg);

        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(270, 500, 100, 30);
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
