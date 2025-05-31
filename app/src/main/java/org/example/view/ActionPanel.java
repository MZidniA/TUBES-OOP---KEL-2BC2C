package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ActionPanel extends JPanel {
    private Font pixelFont;
    private Image backgroundImage;
    private JFrame parentFrame;

    public ActionPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        setOpaque(false);

        loadFont();
        loadBackground();

        JLabel titleLabel = new JLabel("ALL PLAYER ACTIONS");
        titleLabel.setFont(pixelFont.deriveFont(16f));
        titleLabel.setBounds(160, 20, 400, 30);
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        // Panel isi action
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setOpaque(false);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Tambah margin kiri/kanan

        List<String> actions = Arrays.asList(
                "1. Move (WASD or Arrow Keys)",
                "2. Interact (F)",
                "3. Eat (E)",
                "4. Watering Crops",
                "5. Tilling Land",
                "6. Planting Seeds",
                "7. Harvesting Crops",
                "8. Fishing",
                "9. Cooking",
                "10. Shipping Items",
                "11. Chatting with NPC",
                "12. Gifting NPC",
                "13. Proposing",
                "14. Marrying",
                "15. Sleep",
                "16. Open Inventory (I)",
                "17. Navigate Menus",
                "18. View Player Info (G)",
                "19. Navigate Cooking Menu",
                "20. Interact with Shops / Store Doors"
        );

        for (String action : actions) {
            JLabel label = new JLabel(action);
            label.setFont(pixelFont.deriveFont(11f)); // lebih kecil biar pas
            label.setForeground(Color.WHITE);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            actionsPanel.add(label);
            actionsPanel.add(Box.createVerticalStrut(5)); // spasi antar baris
        }

        JScrollPane scrollPane = new JScrollPane(actionsPanel);
        scrollPane.setBounds(80, 60, 480, 370);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane);

        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(270, 460, 100, 30);
        backButton.setBackground(new Color(102, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
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
            pixelFont = baseFont.deriveFont(Font.PLAIN, 10f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.PLAIN, 10);
        }
    }

    private void loadBackground() {
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/box/box.png")).getImage();
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
