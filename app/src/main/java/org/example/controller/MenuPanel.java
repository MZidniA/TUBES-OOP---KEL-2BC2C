package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MenuPanel extends JPanel implements ActionListener {
    private Image backgroundImage;
    private JButton startButton, quitButton;
    private JFrame frame;
    private Font customFont;
    private ImageIcon buttonIcon;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        loadImage();
        loadCustomFont();
        loadButtonImage();

        setPreferredSize(new Dimension(640, 576));

        startButton = createPixelButton("New Game");
        quitButton = createPixelButton("Quit");

        startButton.setBounds(240, 300, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        quitButton.setBounds(240, 360, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        add(startButton);
        add(quitButton);
    }

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/menu/menu.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            if (is == null) {
                System.out.println("Font kustom TIDAK ditemukan!");
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 14f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            System.out.println("Font kustom berhasil dimuat di MenuPanel.");
        } catch (Exception e) {
            System.out.println("Font kustom tidak ditemukan, menggunakan Arial.");
            customFont = new Font("Arial", Font.PLAIN, 14);
        }
    }

    private void loadButtonImage() {
        try {
            buttonIcon = new ImageIcon(getClass().getResource("/button/button.png"));
        } catch (Exception e) {
            System.out.println("Gagal memuat gambar tombol /button/button.png");
            buttonIcon = null;
        }
    }

    private JButton createPixelButton(String text) {
        JButton button = new JButton(text, buttonIcon);
        button.setFont(customFont);
        button.setForeground(Color.BLACK);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);   
        button.setFocusPainted(false); 
        button.setOpaque(false);
        return button;
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
            // ⬇️ GANTI ke TransitionPanel dulu
            frame.getContentPane().removeAll();
            TransitionPanel transitionPanel = new TransitionPanel(frame);
            frame.setContentPane(transitionPanel);
            frame.revalidate();
            frame.repaint();
            SwingUtilities.invokeLater(transitionPanel::requestFocusInWindow);
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }
}
