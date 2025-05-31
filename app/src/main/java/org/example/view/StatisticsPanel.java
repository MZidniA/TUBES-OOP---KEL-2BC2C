package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D; // Import Graphics2D
import java.awt.BasicStroke; // Import BasicStroke
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StatisticsPanel extends JPanel implements ActionListener {
    private Image backgroundImage;
    private JButton startButton, viewStatsButton, quitButton; // Tambah tombol viewStatsButton
    private JFrame frame;
    private Font customFont, titleFontStats, headerFontStats, statFontStats, npcStatFontStats, continueFontStats; // Font untuk statistik
    private ImageIcon buttonIcon;
    private MenuPanel menuPanelInstance;
    private Font pixelFont = new Font("Press Start 2P", Font.PLAIN, 8); 


    private boolean showStatisticsPlaceholder = false; // Flag untuk menampilkan statistik

    // Warna yang dibutuhkan untuk statistik (bisa disesuaikan)
    Color woodBrown = new Color(139, 69, 19);
    Color lightYellow = new Color(255, 253, 208);
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140);


    public StatisticsPanel (JFrame frame, MenuPanel menuPanel) {
        this.frame = frame;
        this.menuPanelInstance = menuPanel;
        setLayout(null); // Tetap null jika Anda ingin posisi manual untuk tombol
        loadImage();
        loadCustomFont(); // Font untuk tombol
        loadStatisticFonts(); // Font khusus untuk layar statistik
        loadButtonImage();

        setPreferredSize(new Dimension(640, 576)); // Sesuaikan dengan ukuran game Anda

        startButton = createPixelButton("New Game");
        viewStatsButton = createPixelButton("View Stats"); // Tombol baru
        quitButton = createPixelButton("Quit");

        // Sesuaikan posisi tombol
        int buttonWidth = (buttonIcon != null) ? buttonIcon.getIconWidth() : 150;
        int buttonHeight = (buttonIcon != null) ? buttonIcon.getIconHeight() : 40;
        int centerX = (getWidth() > 0 ? getWidth() : 640) / 2 - buttonWidth / 2; // Menyesuaikan dengan lebar panel

        startButton.setBounds(centerX, 300, buttonWidth, buttonHeight);
        viewStatsButton.setBounds(centerX, 360, buttonWidth, buttonHeight);
        quitButton.setBounds(centerX, 420, buttonWidth, buttonHeight);


        startButton.addActionListener(this);
        viewStatsButton.addActionListener(this); // Tambahkan listener
        quitButton.addActionListener(this);
        JButton backButton = new JButton("BACK");
        backButton.setFont(pixelFont.deriveFont(10f));
        backButton.setBounds(270, 500, 100, 30);
        backButton.setBackground(new Color(102, 51, 51));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(80, 40, 40), 2));
        backButton.addActionListener(e -> {
            frame.setContentPane(new MenuPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        add(backButton);

        add(startButton);
        add(viewStatsButton); // Tambahkan tombol ke panel
        add(quitButton);
    }

    private void loadImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/menu/menu.png")); // Pastikan path ini benar
        } catch (Exception e) {
            System.err.println("MenuPanel: Failed to load background image. " + e.getMessage());
            backgroundImage = null;
        }
    }

    private void loadCustomFont() { // Font untuk tombol
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf"); // Font untuk tombol
            if (is == null) {
                System.err.println("MenuPanel: Font kustom untuk tombol (/font/PressStart2P.ttf) tidak ditemukan!");
                customFont = new Font("Arial", Font.BOLD, 8); // Fallback, dikecilkan dari 12
                return;
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 8f); // Ukuran dikecilkan dari 10f
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            System.err.println("MenuPanel: Gagal memuat font kustom untuk tombol, menggunakan Arial. Error: " + e.getMessage());
            customFont = new Font("Arial", Font.BOLD, 8); // Dikecilkan dari 12
        }
    }
    
    private void loadStatisticFonts() { // Font yang akan digunakan di layar statistik
        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Atau font lain yang Anda gunakan di GameStateUI
             if (is == null) {
                is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
                 if (is == null) {
                     throw new Exception("File font statistik tidak ditemukan: /font/slkscr.ttf atau /font/PressStart2P.ttf");
                 }
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            // Ukuran font statistik dikecilkan
            titleFontStats = baseFont.deriveFont(16f);         
            headerFontStats = baseFont.deriveFont(Font.BOLD, 11f); 
            statFontStats = baseFont.deriveFont(9f);         
            npcStatFontStats = baseFont.deriveFont(8f);       
            continueFontStats = baseFont.deriveFont(Font.ITALIC, 10f); 
        } catch (Exception e) {
            titleFontStats = new Font("Arial", Font.BOLD, 16);
            headerFontStats = new Font("Arial", Font.BOLD, 11);
            statFontStats = new Font("Arial", Font.PLAIN, 9);
            npcStatFontStats = new Font("Arial", Font.PLAIN, 8);
            continueFontStats = new Font("Arial", Font.ITALIC, 10);
        }
    }


    private void loadButtonImage() {
        try {
            buttonIcon = new ImageIcon(getClass().getResource("/button/button.png")); // Pastikan path ini benar
        } catch (Exception e) {
            System.err.println("MenuPanel: Failed to load button icon. " + e.getMessage());
            buttonIcon = new ImageIcon(new java.awt.image.BufferedImage(150, 40, java.awt.image.BufferedImage.TYPE_INT_ARGB)); // Placeholder icon
        }
    }

    private JButton createPixelButton(String text) {
        JButton button = new JButton(text); 
        if (buttonIcon != null && buttonIcon.getImage() != null) {
            button.setIcon(buttonIcon);
        }
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
        Graphics2D g2 = (Graphics2D) g; // Cast ke Graphics2D

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(Color.DARK_GRAY); // Fallback background jika gambar gagal load
            g2.fillRect(0,0, getWidth(), getHeight());
        }

        if (showStatisticsPlaceholder) {
            // Sembunyikan tombol-tombol menu utama saat statistik ditampilkan
            startButton.setVisible(false);
            viewStatsButton.setVisible(false);
            quitButton.setVisible(false);
            drawStatisticsPlaceholderScreen(g2);
        } else {
            // Tampilkan tombol-tombol menu utama jika tidak menampilkan statistik
            startButton.setVisible(true);
            viewStatsButton.setVisible(true);
            quitButton.setVisible(true);
            // Anda bisa menggambar judul game atau elemen menu lain di sini jika perlu
        }
    }

    // Metode helper untuk menggambar statistik (adaptasi dari GameStateUI)
    private void drawSubWindow(Graphics2D g2, int x, int y, int width, int height, Color backgroundColor) {
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(borderColor); // Anda perlu mendefinisikan borderColor di MenuPanel
        g2.setStroke(new BasicStroke(3)); // Stroke dikecilkan dari 5
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private int getXforCenteredTextInWindow(Graphics2D g2, String text, int windowX, int windowWidth, Font font) {
        if (g2 == null || text == null) return windowX;
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont); // Kembalikan font asli setelah mengukur
        return windowX + (windowWidth - length) / 2;
    }

    private void drawTextWithShadow(Graphics2D g2, String text, int x, int y, Font font, Color textColor, Color shadowColor) {
        if (g2 == null || text == null) return;
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);

        g2.setColor(shadowColor);
        g2.drawString(text, x + 1, y + 1); // Offset bayangan tetap
        g2.setColor(textColor);
        g2.drawString(text, x, y);
        if (font != null) g2.setFont(originalFont); // Kembalikan font asli
    }

    private void drawStatisticsPlaceholderScreen(Graphics2D g2) {
        // Dimensi default jika gp tidak tersedia (sesuaikan dengan ukuran game Anda)
        int screenWidth = getWidth(); // Mengambil lebar MenuPanel saat ini
        int screenHeight = getHeight(); // Mengambil tinggi MenuPanel saat ini
        int tileSize = 36; // Ukuran tile dikecilkan dari 48 untuk menyesuaikan font yang lebih kecil

        // 1. Latar Belakang Gelap
        g2.setColor(new Color(0, 0, 0, 230));
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // 2. Frame Utama
        int frameX = tileSize;
        int frameY = tileSize;
        int frameWidth = screenWidth - (tileSize * 2);
        int frameHeight = screenHeight - (tileSize * 2);
        drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight, woodBrown);

        // 3. Font (gunakan yang sudah di-load di MenuPanel)
        // Font titleFontStats, headerFontStats, statFontStats, npcStatFontStats, continueFontStats;
        // sudah didefinisikan sebagai field dan di-load di loadStatisticFonts()

        // 4. Posisi Awal Teks
        int currentX = frameX + tileSize / 2;
        int currentY = frameY + tileSize;
        // Line height disesuaikan karena font lebih kecil
        int lineHeightLarge = (int)(tileSize * 0.8);    // Dari 0.9
        int lineHeightMedium = (int)(tileSize * 0.6);  // Dari 0.7
        int lineHeightSmall = (int)(tileSize * 0.5);   // Dari 0.6
        int npcDetailIndent = tileSize; // Indentasi untuk detail NPC
        int secondColumnX = frameX + frameWidth / 2 + tileSize / 4;

        // 5. Judul
        String title = "Game Statistics";
        drawTextWithShadow(g2, title, getXforCenteredTextInWindow(g2, title, frameX, frameWidth, titleFontStats), currentY, titleFontStats, Color.YELLOW, darkTextShadow);
        currentY += lineHeightLarge * 1.2; // Spasi disesuaikan sedikit

        // --- Kolom Kiri ---
        int leftColumnY = currentY;
        drawTextWithShadow(g2, "Total Income: 0g", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightMedium;
        drawTextWithShadow(g2, "Total Expenditure: 0g", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightMedium;
        drawTextWithShadow(g2, "Avg. Season Income: 0g", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightMedium;
        drawTextWithShadow(g2, "Avg. Season Expenditure: 0g", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightMedium;
        drawTextWithShadow(g2, "Total Days Played: 0", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightLarge; // Spasi lebih besar sebelum kategori baru
        drawTextWithShadow(g2, "Crops Harvested: 0", currentX, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightLarge; // Spasi lebih besar
        drawTextWithShadow(g2, "Fish Caught:", currentX, leftColumnY, headerFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightMedium;
        drawTextWithShadow(g2, "  Total: 0", currentX + tileSize/2, leftColumnY, statFontStats, lightYellow, darkTextShadow);
        leftColumnY += lineHeightSmall;
        drawTextWithShadow(g2, "    Common: 0", currentX + npcDetailIndent, leftColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow); // Menggunakan npcDetailIndent
        leftColumnY += lineHeightSmall;
        drawTextWithShadow(g2, "    Regular: 0", currentX + npcDetailIndent, leftColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow); // Menggunakan npcDetailIndent
        leftColumnY += lineHeightSmall;
        drawTextWithShadow(g2, "    Legendary: 0", currentX + npcDetailIndent, leftColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow); // Menggunakan npcDetailIndent

        // --- Kolom Kanan ---
        int rightColumnY = currentY;
        drawTextWithShadow(g2, "NPC Status:", secondColumnX, rightColumnY, headerFontStats, lightYellow, darkTextShadow);
        rightColumnY += lineHeightMedium;
        
        String[] placeholderNpcNames = {"NPC A", "NPC B", "NPC C"}; // Contoh nama NPC
        for (int i=0; i<3; i++) { 
            // Cek batas bawah sebelum menggambar nama NPC
            if (rightColumnY + lineHeightMedium + (lineHeightSmall * 3) > frameY + frameHeight - tileSize * 1.5) break; 
            
            drawTextWithShadow(g2, "  " + placeholderNpcNames[i % placeholderNpcNames.length] + ": SINGLE", secondColumnX + tileSize/2, rightColumnY, statFontStats, lightYellow, darkTextShadow);
            rightColumnY += lineHeightMedium; // Beri spasi setelah nama NPC sebelum detail
            
            // Detail NPC masing-masing satu baris
            drawTextWithShadow(g2, "    Chats: 0", secondColumnX + npcDetailIndent, rightColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow);
            rightColumnY += lineHeightSmall;
            
            // Cek batas bawah sebelum menggambar "Gifts"
            if (rightColumnY + lineHeightSmall > frameY + frameHeight - tileSize * 1.5) break;
            drawTextWithShadow(g2, "    Gifts: 0", secondColumnX + npcDetailIndent, rightColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow);
            rightColumnY += lineHeightSmall;

            // Cek batas bawah sebelum menggambar "Visits"
            if (rightColumnY + lineHeightSmall > frameY + frameHeight - tileSize * 1.5) break;
            drawTextWithShadow(g2, "    Visits: 0", secondColumnX + npcDetailIndent, rightColumnY, npcStatFontStats, Color.LIGHT_GRAY, darkTextShadow);
            rightColumnY += lineHeightMedium; // Beri spasi lebih setelah detail NPC terakhir sebelum NPC berikutnya
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            if (showStatisticsPlaceholder) { // Jika statistik sedang tampil, sembunyikan dulu
                showStatisticsPlaceholder = false;
                startButton.setVisible(true);
                viewStatsButton.setVisible(true);
                quitButton.setVisible(true);
                viewStatsButton.setText("View Stats"); // Pastikan teks tombol kembali normal
                repaint(); // Perlu repaint untuk menghilangkan statistik dan menampilkan tombol
                // Kemudian lanjutkan ke transisi
            }
            // Lanjutkan ke TransitionPanel
            frame.getContentPane().removeAll();
            TransitionPanel transitionPanel = new TransitionPanel(frame);
            frame.setContentPane(transitionPanel);
            frame.revalidate();
            frame.repaint();
            SwingUtilities.invokeLater(transitionPanel::requestFocusInWindow);

        } else if (e.getSource() == viewStatsButton) {
            showStatisticsPlaceholder = !showStatisticsPlaceholder; // Toggle tampilan statistik
            if (showStatisticsPlaceholder) {
                System.out.println("MenuPanel: Showing statistics placeholder.");
                startButton.setVisible(false);
                viewStatsButton.setText("Hide Stats"); // Ubah teks tombol
                quitButton.setVisible(false);
            } else {
                System.out.println("MenuPanel: Hiding statistics placeholder.");
                startButton.setVisible(true);
                viewStatsButton.setText("View Stats"); // Kembalikan teks tombol
                quitButton.setVisible(true);
            }
            repaint(); // Minta panel untuk menggambar ulang dirinya sendiri
        } else if (e.getSource() == quitButton) {
             if (showStatisticsPlaceholder) { // Jika statistik sedang tampil, sembunyikan dulu
                showStatisticsPlaceholder = false;
                startButton.setVisible(true);
                viewStatsButton.setVisible(true);
                quitButton.setVisible(true);
                viewStatsButton.setText("View Stats"); // Pastikan teks tombol kembali normal
                repaint();
            } else {
                System.exit(0);
            }
        }
    }
}