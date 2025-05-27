package org.example.view;

import org.example.controller.GamePanel;
import org.example.model.Inventory;
import org.example.model.Items.Items;
import org.example.model.enums.Season;
import java.time.LocalTime;
import org.example.model.enums.Weather;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke; // Untuk bingkai
import java.io.InputStream; // Untuk memuat font
import java.util.ArrayList;
import java.util.Map;

public class GameStateUI {
    GamePanel gp;
    Graphics2D g2;
    Font stardewFont_40, stardewFont_30, stardewFont_20, defaultFont; // Ganti nama font jika Anda punya
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    private int currentDay;
    private Season currentSeason;
    private LocalTime currentTime;


    // Warna tema Stardew Valley (perkiraan)
    Color woodBrown = new Color(139, 69, 19); // Cokelat kayu
    Color lightYellow = new Color(255, 253, 208); // Kuning krem untuk teks
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140); // Dibuat tidak transparan agar lebih solid

    public GameStateUI(GamePanel gp) {
        this.gp = gp;
        defaultFont = new Font("Arial", Font.PLAIN, 12); // Inisialisasi defaultFont

        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Path ke font Anda
            if (is == null) {
                throw new Exception("File font tidak ditemukan di resources: /font/slkscr.ttf");
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            stardewFont_40 = baseFont.deriveFont(40f);
            stardewFont_30 = baseFont.deriveFont(30f);
            stardewFont_20 = baseFont.deriveFont(20f);
            System.out.println("Font kustom 'slkscr.ttf' berhasil dimuat.");
        } catch (Exception e) {
            System.err.println("Gagal memuat font kustom, menggunakan Arial. Error: " + e.getMessage());
            stardewFont_40 = new Font("Arial", Font.BOLD, 40);
            stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
            stardewFont_20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);

        // Selalu tampilkan info waktu di pojok kanan atas
        drawTimeInfo();

        if (gp.gameState.getGameState() == gp.gameState.pause) {
            drawPauseScreen();
        } else if (gp.gameState.getGameState() == gp.gameState.inventory) {
            drawInventoryScreen(); // Menggunakan nama metode yang konsisten
        }
    }


    private void drawPauseScreen() {
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.screenWidth - (gp.tileSize * 8);
        int frameHeight = gp.tileSize * 6;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(0, 0, 0, 210)); // Background pause lebih gelap

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(40F));
        Font optionFont = (stardewFont_30 != null ? stardewFont_30 : defaultFont.deriveFont(30F));

        String text = "Paused";
        // Menggunakan getXforCenteredTextInWindow agar terpusat di dalam frame pause
        int x = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int y = frameY + gp.tileSize + gp.tileSize/2; // Posisi Y judul disesuaikan
        drawTextWithShadow(text, x, y, titleFont);

        g2.setFont(optionFont);
        text = "Continue";
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize * 2;
        drawTextWithShadow(text, x, y, optionFont);
        if (commandNum == 0) {
            g2.setColor(lightYellow); // Pastikan warna untuk panah
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Exit Game";
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize + 10;
        drawTextWithShadow(text, x, y, optionFont);
        if (commandNum == 1) {
            g2.setColor(lightYellow);
            g2.drawString(">", x - gp.tileSize, y);
        }
    }
    public void onTimeUpdate(int day, Season season, Weather weather, LocalTime time) {
        this.currentDay = day;
        this.currentSeason = season;
        // this.currentWeather = weather; // Jika ingin data cuaca
        this.currentTime = time;
        // System.out.println("GameStateUI onTimeUpdate: Day=" + day + ", Season=" + season + ", Time=" + (time != null ? time.format(timeFormatter) : "null"));
    }


    private void drawTimeInfo() {
        // Guard clause
        if (gp == null || g2 == null) {
            return;
        }

        // 1. Persiapan Teks
        String seasonText = "Musim?";
        if (currentSeason != null) {
            seasonText = currentSeason.toString();
        }

        String dayText = "Hari " + currentDay;

        String timeText = "--:--";
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
        if (currentTime != null) {
            timeText = currentTime.format(timeFormatter);
        }

        // 2. Tentukan Font dan Warna
        // Anda bisa menggunakan font yang sudah ada seperti stardewFont_20
        Font fontUntukWaktu = (stardewFont_20 != null) ? stardewFont_20.deriveFont(16f) : new Font("Arial", Font.PLAIN, 16);
        // Ukuran 16f adalah contoh, sesuaikan agar terlihat bagus.
        g2.setFont(fontUntukWaktu);

        // 3. Tentukan Posisi X dan Y (Tanpa FontMetrics)
        // Karena kita tidak tahu lebar teks, kita set X agar teks mulai dari posisi tertentu di kanan.
        // Anda perlu menyesuaikan nilai '150' atau '200' ini agar pas.
        int xPosisiTeks = gp.screenWidth - 150; // Mulai menggambar 150px dari tepi kanan
        int yPosisiAwal = 30;                 // Jarak dari atas untuk baris pertama (baseline)
        int spasiAntarBaris = 20;             // Perkiraan spasi vertikal antar baseline teks (sesuaikan)

        // 4. Gambar Tiga Baris Teks

        // Line 1: Season
        g2.setColor(darkTextShadow);
        g2.drawString(seasonText, xPosisiTeks + 1, yPosisiAwal + 1); // Bayangan
        g2.setColor(lightYellow);
        g2.drawString(seasonText, xPosisiTeks, yPosisiAwal);         // Teks utama

        // Line 2: Day
        int yPosisiBaris2 = yPosisiAwal + spasiAntarBaris;
        g2.setColor(darkTextShadow);
        g2.drawString(dayText, xPosisiTeks + 1, yPosisiBaris2 + 1);   // Bayangan
        g2.setColor(lightYellow);
        g2.drawString(dayText, xPosisiTeks, yPosisiBaris2);         // Teks utama

        // Line 3: Time
        int yPosisiBaris3 = yPosisiBaris2 + spasiAntarBaris;
        g2.setColor(darkTextShadow);
        g2.drawString(timeText, xPosisiTeks + 1, yPosisiBaris3 + 1);    // Bayangan
        g2.setColor(lightYellow);
        g2.drawString(timeText, xPosisiTeks, yPosisiBaris3);          // Teks utama

    }
    
    private void drawInventoryScreen() {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        final int frameHeight = gp.tileSize * 8;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(101, 67, 33, 230)); // Warna inventory

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(40F));
        g2.setColor(lightYellow); // Set warna sebelum menggambar teks
        String text = "Inventory";
        int titleX = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(text, titleX, titleY, titleFont);

        final int slotsPerRow = 12;
        final int totalDisplayRows = 3;
        final int slotSize = gp.tileSize + 6;
        final int slotGap = 4;
        final int maxSlotsToDisplay = slotsPerRow * totalDisplayRows;

        final int gridWidth = (slotsPerRow * slotSize) + ((slotsPerRow - 1) * slotGap);
        final int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        final int slotYStart = titleY + gp.tileSize + (gp.tileSize / 2) + 5; // Beri ruang untuk nomor hotbar

        int currentSlotX = slotXStart;
        int currentSlotY = slotYStart;

        Inventory inventory = gp.p.getInventory();
        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());

        Font itemPlaceholderFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(9F) : defaultFont.deriveFont(9F));
        Font quantityFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(12F) : defaultFont.deriveFont(12F));
        Font hotkeyFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(10F) : defaultFont.deriveFont(10F));

        for (int i = 0; i < maxSlotsToDisplay; i++) {
            g2.setColor(new Color(80, 40, 0, 200)); // Warna slot
            g2.fillRoundRect(currentSlotX, currentSlotY, slotSize, slotSize, 8, 8);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(currentSlotX, currentSlotY, slotSize, slotSize, 8, 8);

            if (i < inventoryList.size()) {
                Items item = inventoryList.get(i).getKey();
                Integer quantity = inventoryList.get(i).getValue();

                g2.setFont(itemPlaceholderFont);
                g2.setColor(lightYellow);
                String itemName = item.getName();
                int maxNameLengthInSlot = 6; // Sesuaikan jika perlu
                if (itemName.length() > maxNameLengthInSlot) {
                    itemName = itemName.substring(0, Math.min(itemName.length(), maxNameLengthInSlot - 2)) + "..";
                }
                int textWidth = g2.getFontMetrics().stringWidth(itemName);
                int textX = currentSlotX + (slotSize - textWidth) / 2;
                int textY = currentSlotY + (slotSize / 2) + (g2.getFontMetrics().getAscent() / 3); // Penyesuaian Y
                g2.drawString(itemName, textX, textY);

                if (quantity > 1) {
                    g2.setFont(quantityFont); // Set font sebelum getFontMetrics
                    String qtyText = String.valueOf(quantity);
                    int qtyTextWidth = g2.getFontMetrics().stringWidth(qtyText);
                    int qtyX = currentSlotX + slotSize - qtyTextWidth - 4;
                    int qtyY = currentSlotY + slotSize - 4;
                    drawTextWithShadow(qtyText, qtyX, qtyY, quantityFont);
                }
            }

            if (i < slotsPerRow) { // Nomor untuk hotbar
                String hotkeyNum = "";
                if (i < 9) hotkeyNum = String.valueOf(i + 1);
                else if (i == 9) hotkeyNum = "0";
                else if (i == 10) hotkeyNum = "-";
                else if (i == 11) hotkeyNum = "=";
                
                g2.setFont(hotkeyFont); // Set font sebelum getFontMetrics
                g2.setColor(new Color(230, 230, 230, 200));
                int hotkeyTextWidth = g2.getFontMetrics().stringWidth(hotkeyNum);
                g2.drawString(hotkeyNum, currentSlotX + (slotSize - hotkeyTextWidth) / 2, currentSlotY - 4);
            }

            currentSlotX += slotSize + slotGap;
            if ((i + 1) % slotsPerRow == 0) {
                currentSlotX = slotXStart;
                currentSlotY += slotSize + slotGap;
            }
        }
        
        int cursorX = slotXStart + (slotSize + slotGap) * slotCol;
        int cursorY = slotYStart + (slotSize + slotGap) * slotRow;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX - 2, cursorY - 2, slotSize + 4, slotSize + 4, 10, 10);
    }

    // Helper method untuk menggambar sub-window (latar belakang UI)
    public void drawSubWindow(int x, int y, int width, int height, Color backgroundColor) {
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    // Helper method untuk menengahkan teks di seluruh layar
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    // Helper method untuk menengahkan teks di dalam sebuah window/frame tertentu
    public int getXforCenteredTextInWindow(String text, int windowX, int windowWidth, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont);
        return windowX + (windowWidth - length) / 2;
    }
     // Overload jika font sudah di-set di g2
    public int getXforCenteredTextInWindow(String text, int windowX, int windowWidth) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return windowX + (windowWidth - length) / 2;
    }


    // Helper method untuk menggambar teks dengan bayangan
    // Overload ini menggunakan font yang sudah di-set pada g2
    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2); // Sesuaikan offset bayangan jika perlu
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
    }
    
    // Overload ini menerima font spesifik
    private void drawTextWithShadow(String text, int x, int y, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2); // Sesuaikan offset bayangan
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (font != null) g2.setFont(originalFont); // Kembalikan font
    }
}