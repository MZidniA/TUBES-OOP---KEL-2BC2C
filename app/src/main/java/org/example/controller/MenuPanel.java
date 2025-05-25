package org.example.controller;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;


public class MenuPanel extends JPanel implements ActionListener {
    private Image backgroundImage;
    private JButton startButton, quitButton;
    private JFrame frame;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        loadImage();

        Font customFont = loadFont("/fonts/Stardew Valley ALL CAPS.ttf", 18f);

        startButton = new JButton("New Game");
        quitButton = new JButton("Quit");

        startButton.setBounds(240, 300, 180, 45);
        quitButton.setBounds(240, 360, 180, 45);

        if (customFont != null){
            startButton.setFont(customFont);
            quitButton.setFont(customFont);
        }

        styleButton(startButton);
        styleButton(quitButton);

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        add(startButton);
        add(quitButton);
    }

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/gui/menu.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Font loadFont(String path, float size) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            return font;
        } catch (Exception e) {
            System.err.println("Gagal load font: " + e.getMessage());
            return null;
        }
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(java.awt.Color.WHITE);

        // 🎨 Warna biru tua elegan
        java.awt.Color darkBlue = new java.awt.Color(40, 70, 150); // deep blue
        java.awt.Color darkBlueHover = new java.awt.Color(60, 90, 180); // hover glow

        button.setBackground(darkBlue);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 18f));

        // 🔁 Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkBlueHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(darkBlue);
            }
        });
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            frame.getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel();
            frame.getContentPane().add(gamePanel);

            frame.revalidate();
            frame.repaint();

            gamePanel.paintImmediately(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

            SwingUtilities.invokeLater(() -> {
                gamePanel.requestFocus(); // ini wajib banget
                gamePanel.startGameThread();
            });
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }

}
