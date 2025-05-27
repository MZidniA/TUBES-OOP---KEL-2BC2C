package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class TransitionPanel extends JPanel {
    private JFrame frame;
    private Image backgroundImage;
    private Image fieldBg;
    private Font pixelFont;

    private JTextField nameField, genderField, farmNameField;
    private JComboBox<String> genderBox;

    public TransitionPanel(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(10f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            pixelFont = new Font("Arial", Font.PLAIN, 12);
            System.err.println("Font gagal dimuat, pakai Arial sementara.");
}

        setBackground(Color.BLACK);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/menu/transition_background.png")); 
            fieldBg = ImageIO.read(getClass().getResource("/button/button.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel nameLabel = createLabel("Name:", 180);
        nameField = createField(180);

        JLabel genderLabel = createLabel("Gender:", 230);
        genderBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderBox.setBounds(300, 230, 180, 36);
        genderBox.setFont(pixelFont.deriveFont(10f));
        add(genderBox);

        JLabel farmLabel = createLabel("Farm Name:", 280);
        farmNameField = createField(280);

        JButton startButton = new JButton("Start Game");
        startButton.setBounds(250, 380, 180, 40);
        startButton.setFont(pixelFont.deriveFont(10f));
        startButton.setBackground(new Color(204, 153, 102));
        startButton.setFocusPainted(false);
        startButton.addActionListener(this::startGame);
        add(startButton);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(pixelFont);
        label.setBounds(180, y, 130, 30);
        label.setForeground(Color.WHITE);
        return addAndReturn(label);
    }

    private JTextField createField(int y) {
        JTextField field = new JTextField() {
            protected void paintComponent(Graphics g) {
                g.drawImage(fieldBg, 0, 0, getWidth(), getHeight(), this);
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.BLACK);
        field.setFont(pixelFont);
        field.setBounds(300, y, 180, 36);
        return addAndReturn(field);
    }

    private <T extends JComponent> T addAndReturn(T comp) {
        add(comp);
        return comp;
    }

    private void startGame(ActionEvent e) {
        String name = nameField.getText().trim();
        String gender = (String) genderBox.getSelectedItem();
        String farm = farmNameField.getText().trim();

        // Validasi karakter
        if (name.isEmpty() || gender == null || farm.isEmpty()) {
            showStardewAlert("Semua field harus diisi ya!");
            return;
        }

        if (!name.matches("^[a-zA-Z]+$")) {
            showStardewAlert("Name hanya boleh berisi huruf tanpa spasi yaa!");
            return;
        }

        if (!farm.matches("^[a-zA-Z0-9]+$")) {
            showStardewAlert("Farm Name hanya boleh huruf/angka tanpa spasi yaa!");
            return;
        }


        // Setup GamePanel
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(frame);
        gamePanel.player.getPlayer().setName(name);
        gamePanel.player.getPlayer().setGender(gender);
        gamePanel.player.getPlayer().setFarmname(farm);

        frame.setContentPane(gamePanel);
        gamePanel.setupGame();
        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocus();
            gamePanel.startGameThread();
        });
    }

    private void showStardewAlert(String message) {
        JDialog dialog = new JDialog(frame, "Oops!", true);
        dialog.setUndecorated(true);
        dialog.setSize(460, 160);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JPanel bgPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Image woodBg;
                try {
                    woodBg = ImageIO.read(getClass().getResource("/button/message.png"));
                } catch (IOException e) {
                    g.setColor(new Color(204, 153, 102));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    return;
                }
                g.drawImage(woodBg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setBounds(0, 0, 460, 160);

        JLabel msg = new JLabel(
            "<html><div style='text-align:center; padding-top:4px; line-height:1.5;'>"
            + message +
            "</div></html>",
            SwingConstants.CENTER
        );
        msg.setFont(pixelFont.deriveFont(9f));
        msg.setForeground(Color.BLACK);
        msg.setBounds(30, 25, 400, 60);

        JButton okBtn = new JButton("OK");
        okBtn.setFont(pixelFont.deriveFont(10f));
        okBtn.setBounds(180, 90, 100, 30);
        okBtn.addActionListener(e -> dialog.dispose());

        bgPanel.add(msg);
        bgPanel.add(okBtn);
        dialog.add(bgPanel);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
