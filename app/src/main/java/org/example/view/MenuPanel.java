package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel implements ActionListener {
    private Image backgroundImage;
    private JButton startBtn, statsBtn, actionsBtn, creditsBtn, helpBtn, quitBtn;
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

        // Button Texts
        startBtn = createPixelButton("New Game");
        statsBtn = createPixelButton("Stats");
        actionsBtn = createPixelButton("Actions");
        creditsBtn = createPixelButton("Credits");
        helpBtn = createPixelButton("Help");
        quitBtn = createPixelButton("Quit");

        // Button Positions
        int leftX = 120;
        int rightX = 360;
        int startY = 300;
        int spacing = 60;

        startBtn.setBounds(leftX, startY, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        statsBtn.setBounds(leftX, startY + spacing, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        actionsBtn.setBounds(leftX, startY + spacing * 2, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());

        creditsBtn.setBounds(rightX, startY, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        helpBtn.setBounds(rightX, startY + spacing, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
        quitBtn.setBounds(rightX, startY + spacing * 2, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());

        // Listeners
        startBtn.addActionListener(this);
        statsBtn.addActionListener(this);
        actionsBtn.addActionListener(this);
        creditsBtn.addActionListener(this);
        helpBtn.addActionListener(this);
        quitBtn.addActionListener(this);

        // Add buttons to panel
        add(startBtn);
        add(statsBtn);
        add(actionsBtn);
        add(creditsBtn);
        add(helpBtn);
        add(quitBtn);
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
            if (is == null) return;
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 12f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("Arial", Font.PLAIN, 12);
        }
    }

    private void loadButtonImage() {
        try {
            buttonIcon = new ImageIcon(getClass().getResource("/button/button.png"));
        } catch (Exception e) {
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
        Object src = e.getSource();
        if (src == startBtn) {
            frame.setContentPane(new TransitionPanel(frame));
        } else if (src == statsBtn) {
            frame.setContentPane(new StatisticsPanel(frame, null)); 
        } else if (src == actionsBtn) {
            frame.setContentPane(new ActionPanel(frame));
        } else if (src == creditsBtn) {
            frame.setContentPane(new CreditsPanel(frame));
        } else if (src == helpBtn) {
            frame.setContentPane(new HelpPanel(frame));
        } else if (src == quitBtn) {
            System.exit(0);
        }
        frame.revalidate();
        frame.repaint();
    }
}
