package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class EnergyWarningDialog extends JDialog {
    private Font pixelFont;

    public EnergyWarningDialog(JFrame parentFrame) {
        super(parentFrame, "Tidak Bisa Ngobrol", true);
        setUndecorated(true);
        setSize(420, 200);
        setLocationRelativeTo(parentFrame);
        setLayout(null);
        getContentPane().setBackground(new Color(174, 102, 47)); // Coklat muda
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(90, 60, 30), 5)); // Border box classic

        loadFont();

        JLabel messageLine1 = new JLabel("Energi kamu tidak cukup", SwingConstants.CENTER);
        messageLine1.setForeground(Color.WHITE);
        messageLine1.setFont(pixelFont.deriveFont(12f));
        messageLine1.setBounds(30, 50, 360, 30);
        add(messageLine1);

        JLabel messageLine2 = new JLabel("untuk ngobrol", SwingConstants.CENTER);
        messageLine2.setForeground(Color.WHITE);
        messageLine2.setFont(pixelFont.deriveFont(12f));
        messageLine2.setBounds(30, 70, 360, 30);
        add(messageLine2);

        JButton okButton = new JButton("OK");
        okButton.setFont(pixelFont.deriveFont(12f));
        okButton.setBackground(new Color(122, 72, 30));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBounds(160, 120, 100, 30);
        okButton.addActionListener(e -> dispose());
        add(okButton);
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            pixelFont = baseFont;
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(baseFont);
        } catch (Exception e) {
            pixelFont = new Font("Arial", Font.PLAIN, 13);
        }
    }
}
