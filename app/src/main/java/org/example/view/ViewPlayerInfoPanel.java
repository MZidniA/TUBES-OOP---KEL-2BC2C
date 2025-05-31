package org.example.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import org.example.model.Player;

public class ViewPlayerInfoPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;

    public ViewPlayerInfoPanel(JFrame parentFrame, Player player) {
        setLayout(null);
        setPreferredSize(new Dimension(400, 300));
        setOpaque(false);

        loadFont();
        loadBackground();

        JLabel titleLabel = new JLabel("PLAYER INFO");
        titleLabel.setFont(pixelFont.deriveFont(16f));
        titleLabel.setBounds(120, 20, 200, 30);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        // Informasi player
        add(createLabel("Name: " + player.getName(), 70));
        add(createLabel("Energy: " + player.getEnergy(), 100));
        add(createLabel("Gold: " + player.getGold(), 130));
        add(createLabel("Location: " + player.getCurrentLocationType(), 160));
        add(createLabel("Partner: " + (player.getPartner() != null ? player.getPartner().getName() : "None"), 190));
        add(createLabel("Tile: (" + player.getTileX() + ", " + player.getTileY() + ")", 220));

        // Tombol BACK
        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(150, 250, 100, 25);
        backButton.setBackground(new Color(102, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        backButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        add(backButton);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(pixelFont.deriveFont(12f));
        label.setForeground(Color.WHITE);
        label.setBounds(40, y, 320, 20);
        return label;
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
