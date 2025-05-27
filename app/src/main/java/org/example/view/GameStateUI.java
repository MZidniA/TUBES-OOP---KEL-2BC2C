// Lokasi: src/main/java/org/example/view/GameStateUI.java
package org.example.view;

import org.example.controller.GamePanel;
import org.example.model.Inventory;
import org.example.model.Items.Items; // Pastikan import ini benar
import org.example.model.enums.Season;
import org.example.model.enums.Weather;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;

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

    public GameStateUI(GamePanel gp) {
        this.gp = gp;

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
            }
        } catch (Exception e) {
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
        }
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

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);
        String text = "Paused";
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
            }
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color windowBackgroundColor = new Color(101, 67, 33, 220);
        g2.setColor(windowBackgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        Color currentBorderColor = new Color(210, 180, 140, 255); // Diganti namanya agar tidak konflik
        g2.setColor(currentBorderColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text, int frameX, int frameWidth) { // Untuk subwindow
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return frameX + (frameWidth - length) / 2;
    }

    public int getXforCenteredText(String text) { // Untuk layar penuh
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

}