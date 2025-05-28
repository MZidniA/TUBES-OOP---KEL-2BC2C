// Lokasi: src/main/java/org/example/view/GameStateUI.java
package org.example.view;

import org.example.view.GamePanel; // Tetap dibutuhkan untuk konstanta layout
import org.example.controller.GameState; // Import GameState dari controller
import org.example.model.Inventory;
<<<<<<< HEAD
import org.example.model.Items.Items; // Pastikan import ini benar
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
=======
import org.example.model.Items.Items;
import org.example.model.enums.Season;
import java.time.LocalTime;
import org.example.model.enums.Weather; // Meskipun tidak digunakan di drawTimeInfo, mungkin berguna nanti
>>>>>>> main

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
<<<<<<< HEAD
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
=======
import java.io.InputStream;
>>>>>>> main
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;

<<<<<<< HEAD
public class GameStateUI implements TimeObserver { // IMPLEMENTASIKAN TimeObserver
    GamePanel gp;
    Graphics2D g2;
    Font stardewFont_40, stardewFont_30, stardewFont_20;
    public Font timeFont;
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;

    Color woodBrown = new Color(139, 69, 19);
    public Color lightYellow = new Color(255, 253, 208);
    public Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140, 255);

    // Variabel untuk menyimpan data waktu dari TimeManager
    private LocalTime currentTime;
    private int currentDay;
    private Season currentSeason;
    // private Weather currentWeather; // Uncomment jika ingin menampilkan cuaca juga
    private BufferedImage timeIcon;
    private DateTimeFormatter timeFormatter;
=======
public class GameStateUI implements TimeObserver { // Pastikan implement TimeObserver
    GamePanel gp; // Untuk konstanta layout dan dimensi
    Graphics2D g2; // Diset di metode draw utama
    Font stardewFont_40, stardewFont_30, stardewFont_20, defaultFont;
    public int commandNum = 0; // State internal untuk navigasi menu pause
    public int slotCol = 0;    // State internal untuk navigasi inventory
    public int slotRow = 0;

    // State untuk TimeInfo, diperbarui oleh onTimeUpdate
    private int currentDay = 1;
    private Season currentSeason = Season.SPRING;
    private LocalTime currentTime = LocalTime.of(6,0);
    private java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");


    // Warna tema
    Color woodBrown = new Color(139, 69, 19);
    Color lightYellow = new Color(255, 253, 208);
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140);
>>>>>>> main

    public GameStateUI(GamePanel gp) {
        this.gp = gp;
        defaultFont = new Font("Arial", Font.PLAIN, 12); // Fallback font

<<<<<<< HEAD
        // Inisialisasi nilai waktu awal (akan diupdate oleh TimeManager)
        this.currentTime = LocalTime.of(6, 0); // Default
        this.currentDay = 1;
        this.currentSeason = Season.SPRING; // Default
        // this.currentWeather = Weather.SUNNY; // Default
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            // Lokasi aset Time.png dikonfirmasi: resources/Time.png
            InputStream timeIconStream = getClass().getResourceAsStream("/Time.png");
            if (timeIconStream != null) {
                timeIcon = ImageIO.read(timeIconStream);
                System.out.println("Time.png berhasil dimuat.");
            } else {
                System.err.println("Time.png tidak ditemukan di root folder resources. Pastikan path sudah benar.");
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat Time.png: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Sesuaikan path jika perlu
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                stardewFont_40 = baseFont.deriveFont(40f);
                stardewFont_30 = baseFont.deriveFont(30f);
                stardewFont_20 = baseFont.deriveFont(20f);
                timeFont = baseFont.deriveFont(18f); // Font untuk waktu
                System.out.println("Font kustom slkscr.ttf berhasil dimuat.");
            } else {
                System.err.println("Font kustom slkscr.ttf tidak ditemukan, menggunakan Arial.");
                fallbackToArial();
=======
        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Ganti dengan path font Anda
            if (is == null) {
                is = getClass().getResourceAsStream("/font/PressStart2P.ttf"); // Fallback ke font lain jika ada
                 if (is == null) {
                    throw new Exception("File font tidak ditemukan: /font/slkscr.ttf atau /font/PressStart2P.ttf");
                 }
>>>>>>> main
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            stardewFont_40 = baseFont.deriveFont(40f);
            stardewFont_30 = baseFont.deriveFont(30f);
            stardewFont_20 = baseFont.deriveFont(20f);
            System.out.println("Font kustom berhasil dimuat.");
        } catch (Exception e) {
<<<<<<< HEAD
            System.err.println("Error memuat font kustom: " + e.getMessage());
            fallbackToArial();
        }
    }

    private void fallbackToArial() {
        stardewFont_40 = new Font("Arial", Font.BOLD, 40);
        stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
        stardewFont_20 = new Font("Arial", Font.PLAIN, 20);
        timeFont = new Font("Arial", Font.PLAIN, 18);
    }

    @Override
    public void onTimeUpdate(int day, Season season, Weather weather, LocalTime time) {
        this.currentDay = day;
        this.currentSeason = season;
        // this.currentWeather = weather; // Uncomment jika perlu
        this.currentTime = time;
        // System.out.println("GameStateUI onTimeUpdate: Day=" + day + ", Season=" + season + ", Time=" + time.format(timeFormatter)); // Untuk debug
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // Gambar informasi waktu jika game sedang berjalan atau pause
        if (gp.gameState.getGameState() == gp.gameState.play ||
            gp.gameState.getGameState() == gp.gameState.pause ||
            gp.gameState.getGameState() == gp.gameState.inventory) { // Juga tampilkan saat di inventory
            drawTimeInfo();
        }

        // Gambar UI spesifik berdasarkan state
        if (gp.gameState.getGameState() == gp.gameState.pause) {
            drawPauseScreen();
        } else if (gp.gameState.getGameState() == gp.gameState.inventory) {
            drawInventory();
=======
            System.err.println("Gagal memuat font kustom, menggunakan Arial. Error: " + e.getMessage());
            stardewFont_40 = new Font("Arial", Font.BOLD, 40);
            stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
            stardewFont_20 = new Font("Arial", Font.PLAIN, 20);
        }
    }

    /**
     * Metode utama untuk menggambar semua elemen UI.
     * @param g2 Graphics context.
     * @param currentGameState State game saat ini (misalnya, PLAY, PAUSE, INVENTORY).
     * @param playerInventory Inventory pemain (hanya dibutuhkan jika state adalah INVENTORY).
     */
    public void draw(Graphics2D g2, GameState currentGameState, Inventory playerInventory) {
        this.g2 = g2; // Simpan graphics context untuk helper methods

        // Selalu tampilkan info waktu di pojok kanan atas
        drawTimeInfo();

        // Tampilkan UI spesifik berdasarkan state game
        if (currentGameState.getGameState() == currentGameState.pause) {
            drawPauseScreen();
        } else if (currentGameState.getGameState() == currentGameState.inventory) {
            drawInventoryScreen(playerInventory); // Kirim inventory ke metode ini
>>>>>>> main
        }
        // Tambahkan state lain jika ada (misalnya, DIALOGUE, SHOP_MENU, dll.)
    }

    @Override
    public void onTimeUpdate(int day, Season season, Weather weather, LocalTime time) {
        this.currentDay = day;
        this.currentSeason = season;
        // this.currentWeather = weather; // Anda bisa uncomment jika ingin menampilkan cuaca juga
        this.currentTime = time;
    }

    private void drawTimeInfo() {
        if (g2 == null || gp == null) return;

        String seasonText = (currentSeason != null) ? currentSeason.toString() : "Musim?";
        String dayText = "Hari " + currentDay;
        String timeText = (currentTime != null) ? currentTime.format(timeFormatter) : "--:--";

        Font fontUntukWaktu = (stardewFont_20 != null) ? stardewFont_20.deriveFont(16f) : new Font("Arial", Font.PLAIN, 16);
        g2.setFont(fontUntukWaktu);

        int xPosisiTeks = gp.screenWidth - 150; // Sesuaikan agar pas
        int yPosisiAwal = 30;
        int spasiAntarBaris = 20;

        // Menggambar teks dengan bayangan untuk keterbacaan
        drawTextWithShadow(seasonText, xPosisiTeks, yPosisiAwal);
        drawTextWithShadow(dayText, xPosisiTeks, yPosisiAwal + spasiAntarBaris);
        drawTextWithShadow(timeText, xPosisiTeks, yPosisiAwal + (spasiAntarBaris * 2));
    }

    private void drawTimeInfo() {
        g2.setFont(timeFont != null ? timeFont : stardewFont_20);
        g2.setColor(lightYellow);

        int x = gp.screenWidth - gp.tileSize * 6; // Posisi agak ke kiri sedikit
        int yBase = gp.tileSize / 2 + 15; // Posisi Y dasar

        String timeString = "Time Error";
        if (currentTime != null) {
            timeString = currentTime.format(timeFormatter);
        } else {
             System.err.println("GameStateUI: currentTime is null in drawTimeInfo!");
        }


        String dayString = "Day " + currentDay;
        String seasonString = (currentSeason != null) ? currentSeason.toString() : "Season Error";

        String fullTimeString = String.format("%s - %s - %s", seasonString, dayString, timeString);

        // Bayangan teks
        g2.setColor(darkTextShadow);
        g2.drawString(fullTimeString, x + gp.tileSize + 2, yBase + 2); // +2 untuk shadow offset
        // Teks utama
        g2.setColor(lightYellow);
        g2.drawString(fullTimeString, x + gp.tileSize, yBase);

        // Gambar ikon waktu jika ada
        if (timeIcon != null) {
            g2.drawImage(timeIcon, x + gp.tileSize/2 -10 , yBase - gp.tileSize / 2 , gp.tileSize , gp.tileSize, null);
        } else {
            // System.err.println("GameStateUI: timeIcon is null, tidak bisa digambar."); // Hanya untuk debug jika perlu
        }
    }

    // Metode drawPauseScreen, drawInventory, drawSubWindow, getXforCenteredText tetap sama seperti sebelumnya
    // ... (Pastikan untuk menyalin metode-metode ini dari versi GameStateUI Anda sebelumnya) ...
    private void drawPauseScreen() {
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.screenWidth - (gp.tileSize * 8);
        int frameHeight = gp.tileSize * 6;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(0, 0, 0, 210));

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(Font.BOLD, 40F));
        Font optionFont = (stardewFont_30 != null ? stardewFont_30 : defaultFont.deriveFont(Font.PLAIN, 30F));

        String text = "Paused";
<<<<<<< HEAD
        int x_text = getXforCenteredText(text, frameX, frameWidth); // Menggunakan helper baru
        int y_text = frameY + gp.tileSize + 10;
        g2.setColor(darkTextShadow);
        g2.drawString(text, x_text + 2, y_text + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x_text, y_text);

        g2.setFont(stardewFont_30);

        text = "Continue";
        x_text = getXforCenteredText(text, frameX, frameWidth);
        y_text += gp.tileSize * 2;
        g2.setColor(darkTextShadow);
        g2.drawString(text, x_text + 2, y_text + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x_text, y_text);
        if (commandNum == 0) {
            g2.drawString(">", x_text - gp.tileSize + 5, y_text);
        }

        text = "Exit Game";
        x_text = getXforCenteredText(text, frameX, frameWidth);
        y_text += gp.tileSize + 10;
        g2.setColor(darkTextShadow);
        g2.drawString(text, x_text + 2, y_text + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x_text, y_text);
        if (commandNum == 1) {
            g2.drawString(">", x_text - gp.tileSize + 5, y_text);
        }
    }

    public void drawInventory() {
        final int frameWidth = gp.tileSize * 11;
        final int frameHeight = gp.tileSize * 8;
        final int frameX = (gp.screenWidth / 2) - (frameWidth / 2);
        final int frameY = (gp.screenHeight / 2) - (frameHeight / 2);

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);
        String text = "Inventory";
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = frameX + (frameWidth - textLength) / 2;
        int y = frameY + gp.tileSize;
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);

        // Perlu akses ke model Player untuk inventory, bukan PlayerView
        Inventory inventory = null;
        if (gp.farm != null && gp.farm.getPlayer() != null) {
            inventory = gp.farm.getPlayer().getInventory();
        }


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

        final int slotsPerRow = 5;
        final int slotRows = 4;
        final int inventoryCapacity = slotsPerRow * slotRows;

        final int slotSize = gp.tileSize + 10;
        final int slotGap = 8;

        final int gridWidth = (slotsPerRow * slotSize) + ((slotsPerRow - 1) * slotGap);
        final int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        final int slotYStart = frameY + gp.tileSize + 40;

        int slotX = slotXStart;
        int slotY = slotYStart;

        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());
        for (int i = 0; i < inventoryList.size(); i++) {
            if (i >= inventoryCapacity) break;

            Map.Entry<Items, Integer> entry = inventoryList.get(i);
            Items item = entry.getKey();
            Integer quantity = entry.getValue();

            g2.setColor(new Color(139, 69, 19, 150));
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);

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

            slotX += slotSize + slotGap;
            if ((i + 1) % slotsPerRow == 0) {
                slotX = slotXStart;
                slotY += slotSize + slotGap;
=======
        int x = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int y = frameY + gp.tileSize + gp.tileSize / 2;
        drawTextWithShadow(text, x, y, titleFont);

        g2.setFont(optionFont); // Set font untuk opsi
        text = "Continue";
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize * 2;
        drawTextWithShadow(text, x, y, optionFont);
        if (commandNum == 0) {
            drawTextWithShadow(">", x - gp.tileSize, y, optionFont);
        }

        text = "Exit Game";
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize + 10;
        drawTextWithShadow(text, x, y, optionFont);
        if (commandNum == 1) {
            drawTextWithShadow(">", x - gp.tileSize, y, optionFont);
        }
    }

    private void drawInventoryScreen(Inventory inventory) {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        final int frameHeight = gp.tileSize * 8; // Sesuaikan tinggi jika perlu

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(101, 67, 33, 230));

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(Font.BOLD, 40F));
        String text = "Inventory";
        int titleX = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(text, titleX, titleY, titleFont);

        final int slotsPerRow = 12;
        final int totalDisplayRows = 3; // Jumlah baris slot yang ingin ditampilkan
        final int slotSize = gp.tileSize + 6;
        final int slotGap = 4;
        final int maxSlotsToDisplay = slotsPerRow * totalDisplayRows;

        final int gridWidth = (slotsPerRow * slotSize) + ((slotsPerRow - 1) * slotGap);
        final int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        final int slotYStart = titleY + gp.tileSize + (gp.tileSize / 2); // Sedikit ruang setelah judul

        // Mengambil daftar item dari inventory yang diberikan
        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());

        Font itemPlaceholderFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(10F) : defaultFont.deriveFont(10F));
        Font quantityFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(Font.BOLD, 12F) : defaultFont.deriveFont(Font.BOLD, 12F));
        Font hotkeyFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(10F) : defaultFont.deriveFont(10F));

        for (int i = 0; i < maxSlotsToDisplay; i++) {
            int col = i % slotsPerRow;
            int row = i / slotsPerRow;

            int currentSlotX = slotXStart + col * (slotSize + slotGap);
            int currentSlotY = slotYStart + row * (slotSize + slotGap);
            
            // Gambar nomor hotkey di atas slot baris pertama
            if (row == 0 && i < 12) { // Hanya untuk 12 slot pertama (hotbar)
                 String hotkeyNum = "";
                 if (i < 9) hotkeyNum = String.valueOf(i + 1);
                 else if (i == 9) hotkeyNum = "0";
                 else if (i == 10) hotkeyNum = "-";
                 else if (i == 11) hotkeyNum = "=";
                
                 g2.setFont(hotkeyFont);
                 g2.setColor(new Color(230, 230, 230, 200));
                 int hotkeyTextWidth = g2.getFontMetrics().stringWidth(hotkeyNum);
                 g2.drawString(hotkeyNum, currentSlotX + (slotSize - hotkeyTextWidth) / 2, currentSlotY - 5);
            }


            g2.setColor(new Color(80, 40, 0, 200)); // Warna dasar slot
            g2.fillRoundRect(currentSlotX, currentSlotY, slotSize, slotSize, 8, 8);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(currentSlotX, currentSlotY, slotSize, slotSize, 8, 8);

            if (i < inventoryList.size()) {
                Items item = inventoryList.get(i).getKey();
                Integer quantity = inventoryList.get(i).getValue();

                // Di sini idealnya Anda akan menggambar ikon item jika ada.
                // Untuk sekarang, kita gambar nama item (dipotong jika terlalu panjang).
                g2.setFont(itemPlaceholderFont);
                g2.setColor(lightYellow);
                String itemName = item.getName();
                int maxNameLengthInSlot = 7; // Sesuaikan agar muat
                if (itemName.length() > maxNameLengthInSlot) {
                    itemName = itemName.substring(0, Math.min(itemName.length(), maxNameLengthInSlot - 2)) + "..";
                }
                int textWidth = g2.getFontMetrics().stringWidth(itemName);
                int textX = currentSlotX + (slotSize - textWidth) / 2;
                int textY = currentSlotY + (slotSize / 2) + (g2.getFontMetrics().getAscent() / 3); // Pusatkan vertikal
                g2.drawString(itemName, textX, textY);

                if (quantity > 1) {
                    g2.setFont(quantityFont);
                    String qtyText = String.valueOf(quantity);
                    int qtyTextWidth = g2.getFontMetrics().stringWidth(qtyText);
                    // Posisi kanan bawah di dalam slot
                    int qtyX = currentSlotX + slotSize - qtyTextWidth - 4;
                    int qtyY = currentSlotY + slotSize - 4;
                    drawTextWithShadow(qtyText, qtyX, qtyY, quantityFont); // Gunakan helper untuk shadow
                }
>>>>>>> main
            }
        }
        
        // Gambar kursor seleksi
        int cursorX = slotXStart + (slotSize + slotGap) * slotCol;
        int cursorY = slotYStart + (slotSize + slotGap) * slotRow;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX - 2, cursorY - 2, slotSize + 4, slotSize + 4, 10, 10);
    }

<<<<<<< HEAD
    public void drawSubWindow(int x, int y, int width, int height) {
        Color windowBackgroundColor = new Color(101, 67, 33, 220);
        g2.setColor(windowBackgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        Color currentBorderColor = new Color(210, 180, 140, 255); // Diganti namanya agar tidak konflik
        g2.setColor(currentBorderColor);
=======
    public void drawSubWindow(int x, int y, int width, int height, Color backgroundColor) {
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(borderColor);
>>>>>>> main
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

<<<<<<< HEAD
    public int getXforCenteredText(String text, int frameX, int frameWidth) { // Untuk subwindow
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return frameX + (frameWidth - length) / 2;
    }

    public int getXforCenteredText(String text) { // Untuk layar penuh
=======
    public int getXforCenteredText(String text, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
>>>>>>> main
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont);
        return gp.screenWidth / 2 - length / 2;
    }
<<<<<<< HEAD

=======
    
    public int getXforCenteredTextInWindow(String text, int windowX, int windowWidth, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont);
        return windowX + (windowWidth - length) / 2;
    }

    private void drawTextWithShadow(String text, int x, int y, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (font != null) g2.setFont(originalFont);
    }

    // Overload untuk drawTextWithShadow jika font sudah di-set pada g2
    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
    }
>>>>>>> main
}