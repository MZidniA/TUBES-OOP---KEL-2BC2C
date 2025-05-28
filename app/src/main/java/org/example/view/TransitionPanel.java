package org.example.view; // Atau org.example.controller jika Anda meletakkannya di sana

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

// Import kelas-kelas MVC yang baru
import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.Player;
// GamePanel juga di-import karena kita akan membuat instance-nya
// import org.example.view.GamePanel; // Tidak perlu jika GamePanel di package yang sama

public class TransitionPanel extends JPanel {
    private JFrame frame;
    private Image backgroundImage;
    private Image fieldBg;
    private Font pixelFont;

    private JTextField nameField;
    private JTextField farmNameField;
    private JComboBox<String> genderBox;

    public TransitionPanel(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        setPreferredSize(new Dimension(640, 576)); // Sesuaikan dengan ukuran frame Anda
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            if (is == null) {
                is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Fallback
                 if (is == null) {
                    System.err.println("Font PressStart2P.ttf atau slkscr.ttf tidak ditemukan!");
                    pixelFont = new Font("Monospaced", Font.PLAIN, 10); // Font bawaan jika gagal
                 } else {
                    pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(10f);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
                 }
            } else {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(10f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
            }
            System.out.println("Font untuk TransitionPanel: " + pixelFont.getFontName());
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.PLAIN, 12); // Fallback jika ada error lain
            System.err.println("Font gagal dimuat di TransitionPanel, pakai Monospaced sementara. Error: " + e.getMessage());
            e.printStackTrace();
        }

        setBackground(Color.BLACK);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/menu/transition_background.png"));
            fieldBg = ImageIO.read(getClass().getResource("/button/button.png"));
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar background/field di TransitionPanel.");
            e.printStackTrace();
        }

        JLabel nameLabel = createLabel("Name:", 180);
        nameField = createField(180);

        JLabel genderLabel = createLabel("Gender:", 230);
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderBox.setBounds(300, 230, 180, 36);
        genderBox.setFont(pixelFont.deriveFont(10f));
        add(genderBox);

        JLabel farmLabel = createLabel("Farm Name:", 280);
        farmNameField = createField(280);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(pixelFont.deriveFont(10f));
        startButton.setBounds(250, 380, 180, 40);
        startButton.setFocusPainted(false);
        startButton.addActionListener(this::startGame);
        add(startButton);
    }

    private JLabel createLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setFont(pixelFont);
        label.setBounds(180, y, 110, 30); // Lebar disesuaikan agar tidak terpotong
        label.setForeground(Color.WHITE);
        add(label);
        return label;
    }

    private JTextField createField(int y) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                if (fieldBg != null) {
                    g.drawImage(fieldBg, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(getBackground());
                    g.fillRect(0,0,getWidth(),getHeight());
                }
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setForeground(Color.BLACK);
        field.setFont(pixelFont.deriveFont(9f));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBounds(300, y, 180, 36);
        add(field);
        return field;
    }

    private void startGame(ActionEvent e) {
        String playerName = nameField.getText().trim();
        String playerGender = (String) genderBox.getSelectedItem();
        String farmNameInput = farmNameField.getText().trim();

        if (playerName.isEmpty() || playerGender == null || farmNameInput.isEmpty()) {
            showStardewAlert("Semua field harus diisi ya!");
            return;
        }
        if (!playerName.matches("^[a-zA-Z]+$")) {
            showStardewAlert("Name hanya boleh berisi huruf tanpa spasi yaa!");
            return;
        }
        if (!farmNameInput.matches("^[a-zA-Z0-9 ]+$")) { // Izinkan spasi untuk nama farm
            showStardewAlert("Farm Name hanya boleh huruf/angka yaa!");
            return;
        }

        // 1. Buat Model Inti (Player dan Farm)
        Player playerModel = new Player(playerName, playerGender, farmNameInput);
        Farm farmModel = new Farm(farmNameInput, playerModel); // Farm hanya butuh Player model

        // 2. Buat View Utama (GamePanel)
        GamePanel newGamePanel = new GamePanel(); // Konstruktor GamePanel MVC baru (tanpa param)

        // 3. Buat Controller Utama dan hubungkan Model & View
        GameController gameController = new GameController(newGamePanel, farmModel);

        // Beri tahu GamePanel siapa controllernya
        newGamePanel.setController(gameController);
        // GameController akan memasang KeyListener ke newGamePanel di dalam konstruktornya.

        // Ganti konten frame dengan GamePanel yang baru
        frame.getContentPane().removeAll();
        frame.setContentPane(newGamePanel);
        frame.revalidate();
        frame.repaint();

        // Fokus pada panel baru dan mulai game thread dari controller
        SwingUtilities.invokeLater(() -> {
            newGamePanel.requestFocusInWindow();
            gameController.startGameThread();
        });
    }

    private void showStardewAlert(String message) {
        JDialog dialog = new JDialog(frame, "Oops!", true);
        dialog.setUndecorated(true);
        dialog.setSize(460, 160);
        dialog.setLocationRelativeTo(this.frame);
        dialog.setLayout(null);

        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image woodBg = null;
                try {
                    woodBg = ImageIO.read(getClass().getResource("/button/message.png"));
                } catch (IOException ex) {
                    g.setColor(new Color(204, 153, 102));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    System.err.println("Gagal memuat background dialog /button/message.png");
                }
                if (woodBg != null) {
                    g.drawImage(woodBg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(null);
        bgPanel.setBounds(0, 0, dialog.getWidth(), dialog.getHeight());

        JLabel msgLabel = new JLabel(
            "<html><div style='text-align:center; padding-top:4px; line-height:1.5;'>"
            + message +
            "</div></html>",
            SwingConstants.CENTER
        );
        msgLabel.setFont(pixelFont.deriveFont(9f));
        msgLabel.setForeground(Color.BLACK);
        msgLabel.setBounds(30, 25, bgPanel.getWidth() - 60, 60);

        JButton okBtn = new JButton("OK");
        okBtn.setFont(pixelFont.deriveFont(10f));
        okBtn.setBounds((bgPanel.getWidth() - 100) / 2, 90, 100, 30);
        okBtn.addActionListener(event -> dialog.dispose()); // Lambda lebih ringkas

        bgPanel.add(msgLabel);
        bgPanel.add(okBtn);
        dialog.add(bgPanel);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}