package org.example.view;

import org.example.controller.GamePanel;
import org.example.model.Inventory;
import org.example.model.Items.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.BasicStroke; // Untuk bingkai
import java.io.InputStream; // Untuk memuat font
import java.util.ArrayList;
import java.util.Map;

public class GameStateUI {
    GamePanel gp;
    Graphics2D g2;
    Font stardewFont_40, stardewFont_30, stardewFont_20; // Ganti nama font jika Anda punya
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;

    // Warna tema Stardew Valley (perkiraan)
    Color woodBrown = new Color(139, 69, 19); // Cokelat kayu
    Color lightYellow = new Color(255, 253, 208); // Kuning krem untuk teks
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140, 255); // Bayangan teks cokelat tua

    public GameStateUI(GamePanel gp) {
        this.gp = gp;

        // Coba muat font pixelated
        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Sesuaikan path ke font Anda
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                stardewFont_40 = baseFont.deriveFont(40f);
                stardewFont_30 = baseFont.deriveFont(30f);
                stardewFont_20 = baseFont.deriveFont(20f); 
            } else {
                // Fallback ke Arial jika font tidak ditemukan
                System.err.println("Font kustom tidak ditemukan, menggunakan Arial.");
                stardewFont_40 = new Font("Arial", Font.BOLD, 40);
                stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
                stardewFont_20 = new Font("Arial", Font.PLAIN, 20); // Tambahkan font untuk inventory
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback jika ada error saat memuat font
            stardewFont_40 = new Font("Arial", Font.BOLD, 40);
            stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
            stardewFont_20 = new Font("Arial", Font.PLAIN, 20); // Tambahkan font untuk inventory
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow); 

        if (gp.gameState.getGameState() == gp.gameState.pause) {
            drawPauseScreen();
        } else if (gp.gameState.getGameState() == gp.gameState.inventory) { // BARU
            drawInventory();
        }
    }

<<<<<<< Updated upstream
=======
    private void drawTimeInfo() {
        // 1. Persiapan Dasar
        if (gp == null || g2 == null) { // Guard clause jika GamePanel atau Graphics2D belum siap
            System.err.println("GameStateUI.drawTimeInfo: GamePanel atau Graphics2D null!");
            return;
        }

        // 2. Tentukan Ukuran dan Posisi Ikon yang Lebih Besar
        int iconSize = (int) (gp.tileSize * 2.5); // Buat ikon lebih besar, misal 2.5x ukuran tile
        int paddingKanan = 15; // Jarak dari tepi kanan layar
        int paddingAtas = 15;  // Jarak dari tepi atas layar

        int iconX = gp.screenWidth - paddingKanan - iconSize;
        int iconY = paddingAtas;

        // 3. Gambar Ikon Terlebih Dahulu
        if (timeIcon != null) {
            g2.drawImage(timeIcon, iconX, iconY, iconSize, iconSize, null);
        } else {
            // Jika ikon tidak ada, mungkin gambar kotak placeholder agar tata letak teks tetap terlihat
            // g2.setColor(Color.DARK_GRAY);
            // g2.fillRect(iconX, iconY, iconSize, iconSize);
            System.err.println("GameStateUI.drawTimeInfo: timeIcon is null, tidak digambar.");
        }

        // 4. Siapkan Teks yang Akan Ditampilkan
        String seasonText = (currentSeason != null) ? currentSeason.toString() : "Musim?";
        String dayText = "Hari " + currentDay;
        String timeText = (currentTime != null && timeFormatter != null) ? currentTime.format(timeFormatter) : "00:00";

        // 5. Tentukan Font dan Warna untuk Teks di Atas Ikon
        Font fontDiAtasIkon = (timeFont != null) ? timeFont.deriveFont(Font.BOLD, (float) (iconSize / 5.5)) : new Font("Arial", Font.BOLD, 10);
        g2.setFont(fontDiAtasIkon); // PENTING: Set font PADA g2 SEBELUM mendapatkan FontMetrics

        // DAPATKAN FontMetrics DARI g2 SETELAH FONT DI-SET
        FontMetrics fm = g2.getFontMetrics(); // Tidak perlu membuat FontMetrics.java

        int textHeightAscent = fm.getAscent(); // Tinggi dari baseline ke atas
        int lineSpacing = fm.getDescent() / 2; // Contoh spasi antar baris

        // Hitung total tinggi blok teks (3 baris)
        int totalTextBlockHeight = (3 * textHeightAscent) + (2 * lineSpacing);

        // 6. Hitung Posisi Y Awal untuk Baris Pertama
        int startYText = iconY + (iconSize - totalTextBlockHeight) / 2 + textHeightAscent;

        // 7. Gambar Setiap Baris Teks, Tengahkan Horizontal

        // Line 1: Season
        int textWidthSeason = fm.stringWidth(seasonText); // Gunakan fm untuk mendapatkan lebar string
        int textXSeason = iconX + (iconSize - textWidthSeason) / 2;
        g2.setColor(darkTextShadow);
        g2.drawString(seasonText, textXSeason + 1, startYText + 1);
        g2.setColor(lightYellow);
        g2.drawString(seasonText, textXSeason, startYText);

        // Line 2: Day
        int currentY = startYText + textHeightAscent + lineSpacing; // Pindah ke baris berikutnya
        int textWidthDay = fm.stringWidth(dayText); // Gunakan fm
        int textXDay = iconX + (iconSize - textWidthDay) / 2;
        g2.setColor(darkTextShadow);
        g2.drawString(dayText, textXDay + 1, currentY + 1);
        g2.setColor(lightYellow);
        g2.drawString(dayText, textXDay, currentY);

        // Line 3: Time
        currentY += textHeightAscent + lineSpacing; // Pindah ke baris berikutnya
        int textWidthTime = fm.stringWidth(timeText); // Gunakan fm
        int textXTime = iconX + (iconSize - textWidthTime) / 2;
        g2.setColor(darkTextShadow);
        g2.drawString(timeText, textXTime + 1, currentY + 1);
        g2.setColor(lightYellow);
        g2.drawString(timeText, textXTime, currentY);
        }

    // Metode drawPauseScreen, drawInventory, drawSubWindow, getXforCenteredText tetap sama seperti sebelumnya
    // ... (Pastikan untuk menyalin metode-metode ini dari versi GameStateUI Anda sebelumnya) ...
>>>>>>> Stashed changes
    private void drawPauseScreen() {
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.screenWidth - (gp.tileSize * 8);
        int frameHeight = gp.tileSize * 6; 

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);
        String text = "Paused";
        int x = getXforCenteredText(text);
        int y = frameY + gp.tileSize + 10; 
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);

        g2.setFont(stardewFont_30);


        text = "Continue";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;

        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);

        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawImage(null, x - gp.tileSize, y - gp.tileSize + 10, gp.tileSize, gp.tileSize, null); // Ganti null dengan gambar panah jika ada
            g2.drawString(">", x - gp.tileSize + 5, y); 
        }


        text = "Exit Game";
        x = getXforCenteredText(text);
        y += gp.tileSize + 10; 
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawImage(null, x - gp.tileSize, y - gp.tileSize + 10, gp.tileSize, gp.tileSize, null); // Ganti null dengan gambar panah
            g2.drawString(">", x - gp.tileSize + 5, y);
        }
    }

    public void drawInventory() {
        // 1. Definisikan ukuran frame yang lebih kecil dan posisikan di tengah layar
        final int frameWidth = gp.tileSize * 11;  // Lebar sekitar 11 tile
        final int frameHeight = gp.tileSize * 8; // Tinggi sekitar 8 tile
        final int frameX = (gp.screenWidth / 2) - (frameWidth / 2);
        final int frameY = (gp.screenHeight / 2) - (frameHeight / 2);
    
        // Gambar background window
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
    
        // Judul "Inventory"
        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);
        String text = "Inventory";
        // Menengahkan teks judul di dalam frame baru
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = frameX + (frameWidth - textLength) / 2;
        int y = frameY + gp.tileSize;
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
    
        // Cek jika inventory kosong
        Inventory inventory = gp.player.getInventory();
        if (inventory == null || inventory.getInventory().isEmpty()) {
            g2.setFont(stardewFont_30);
            g2.setColor(lightYellow);
            String msg = "Inventory is empty.";
            int msgLength = (int) g2.getFontMetrics().getStringBounds(msg, g2).getWidth();
            int msgX = frameX + (frameWidth - msgLength) / 2;
            int msgY = frameY + frameHeight / 2;
            g2.drawString(msg, msgX, msgY);
            return;
        }
    
        // 2. Pengaturan Slot untuk 20 item (5x4 grid)
        final int slotsPerRow = 5;
        final int slotRows = 4;
        final int inventoryCapacity = slotsPerRow * slotRows; // Total 20 slot
    
        final int slotSize = gp.tileSize + 10;
        final int slotGap = 8;
    
        // Kalkulasi untuk menengahkan grid slot di dalam frame
        final int gridWidth = (slotsPerRow * slotSize) + ((slotsPerRow - 1) * slotGap);
        final int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        final int slotYStart = frameY + gp.tileSize + 40; // Posisi Y di bawah judul
    
        int slotX = slotXStart;
        int slotY = slotYStart;
    
        // Menggambar item di dalam slot
        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());
        for (int i = 0; i < inventoryList.size(); i++) {
            if (i >= inventoryCapacity) {
                break; // Hanya gambar item sesuai kapasitas UI (20)
            }
    
            Map.Entry<Items, Integer> entry = inventoryList.get(i);
            Items item = entry.getKey();
            Integer quantity = entry.getValue();
    
            // Gambar kotak slot
            g2.setColor(new Color(139, 69, 19, 150));
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
    
            // Gambar nama item (dengan logika pemotongan teks)
            g2.setFont(stardewFont_20.deriveFont(12F));
            g2.setColor(lightYellow);
            String itemName = item.getName();
            if (g2.getFontMetrics().stringWidth(itemName) > slotSize - 10) {
                while(g2.getFontMetrics().stringWidth(itemName + "...") > slotSize - 10 && itemName.length() > 1){
                    itemName = itemName.substring(0, itemName.length() - 1);
                }
                itemName += "...";
            }
            g2.drawString(itemName, slotX + 5, slotY + 20);
    
            // Gambar kuantitas item
            if (quantity > 1) {
                g2.setFont(stardewFont_20.deriveFont(15F));
                String qtyText = String.valueOf(quantity);
                int qtyX = slotX + slotSize - g2.getFontMetrics().stringWidth(qtyText) - 5;
                int qtyY = slotY + slotSize - 5;
                g2.setColor(darkTextShadow);
                g2.drawString(qtyText, qtyX + 1, qtyY + 1);
                g2.setColor(lightYellow);
                g2.drawString(qtyText, qtyX, qtyY);
            }
    
            // 3. Pindah ke slot berikutnya dalam grid 5x4
            slotX += slotSize + slotGap;
            if ((i + 1) % slotsPerRow == 0) {
                slotX = slotXStart;
                slotY += slotSize + slotGap;
            }
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color windowBackgroundColor = new Color(101, 67, 33, 220); // Cokelat tua semi-transparan (seperti Stardew)
        g2.setColor(windowBackgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35); // Kotak dengan sudut membulat
        Color borderColor = new Color(210, 180, 140, 255); // Cokelat muda (Tan)
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(5)); // Ketebalan bingkai
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }


    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}