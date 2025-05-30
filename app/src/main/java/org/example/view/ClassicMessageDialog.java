package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class ClassicMessageDialog extends JDialog {
    private Font pixelFont;

    public ClassicMessageDialog(JFrame parent, String message, String title) {
        super(parent, title, true);
        setUndecorated(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(340, 120));
        getContentPane().setBackground(new Color(255, 235, 200)); // coklat muda

        loadFont();

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(pixelFont.deriveFont(10f));
        label.setForeground(new Color(50, 20, 20));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton okButton = new JButton("OK");
        okButton.setFont(pixelFont.deriveFont(9f));
        okButton.setBackground(new Color(102, 51, 51)); // coklat tua
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        add(label, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
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

    public static void showMessage(JFrame parent, String message, String title) {
        ClassicMessageDialog dialog = new ClassicMessageDialog(parent, message, title);
        dialog.setVisible(true);
    }
}
