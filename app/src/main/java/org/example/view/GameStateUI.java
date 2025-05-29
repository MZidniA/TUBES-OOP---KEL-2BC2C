package org.example.view;

import org.example.view.GamePanel; // Tetap dibutuhkan untuk konstanta layout
import org.example.controller.GameState; // Import GameState dari controller
import org.example.controller.action.UpdateAndShowLocationAction;
import org.example.model.Inventory;
import org.example.model.Items.Items;
import org.example.model.enums.Season;
import java.time.LocalTime;
import org.example.model.GameClock; 
import org.example.model.enums.Weather; 

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class GameStateUI implements TimeObserver { 
    GamePanel gp; 
    Graphics2D g2;
    GameClock gameClock; 
    Font stardewFont_40, stardewFont_30, stardewFont_20, defaultFont;
    public int commandNum = 0; 
    public int slotCol = 0;
    public int slotRow = 0;

    private int currentDay = 1;
    private Season currentSeason = Season.SPRING; 
    private LocalTime currentTime = LocalTime.of(6,0);
    private Weather currentWeather = Weather.SUNNY;

    private java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");


    // Warna tema
    Color woodBrown = new Color(139, 69, 19);
    Color lightYellow = new Color(255, 253, 208);
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140);

    public GameStateUI(GamePanel gp) {
        this.gp = gp;
        defaultFont = new Font("Arial", Font.PLAIN, 12);

        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); 
            if (is == null) {
                is = getClass().getResourceAsStream("/font/PressStart2P.ttf"); 
                 if (is == null) {
                    throw new Exception("File font tidak ditemukan: /font/slkscr.ttf atau /font/PressStart2P.ttf");
                 }
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            stardewFont_40 = baseFont.deriveFont(15f);
            stardewFont_30 = baseFont.deriveFont(10f);
            stardewFont_20 = baseFont.deriveFont(5f);
            System.out.println("Font kustom berhasil dimuat.");
        } catch (Exception e) {
            System.err.println("Gagal memuat font kustom, menggunakan Arial. Error: " + e.getMessage());
            stardewFont_40 = new Font("Arial", Font.BOLD, 15);
            stardewFont_30 = new Font("Arial", Font.PLAIN, 10);
            stardewFont_20 = new Font("Arial", Font.PLAIN, 5);
        }
    }

  
    public void draw(Graphics2D g2, GameState currentGameState, Inventory playerInventory) {
        this.g2 = g2; 
        drawTimeInfo();
        if (currentGameState.getGameState() == currentGameState.pause) {
            drawPauseScreen();
        } else if (currentGameState.getGameState() == currentGameState.inventory) {
            drawInventoryScreen(playerInventory); 
        }

    }

    @Override
    public void onTimeUpdate(int day, Season season, Weather weather, LocalTime time) {
        this.currentDay = day;
        this.currentSeason = season;
        this.currentWeather = weather; 
        this.currentTime = time;
    }

    private void drawTimeInfo() {
    if (g2 == null || gp == null) return;

    String seasonText = (currentSeason != null) ? currentSeason.toString() : "Musim?";
    String weatherText = (currentWeather != null) ? currentWeather.toString() : "Cuaca?";
    String dayText = "Hari " + currentDay;
    String timeText = (currentTime != null) ? currentTime.format(timeFormatter) : "--:--";
    
    String locationInfoString = "Lokasi: N/A";
    if (gp.getController() != null && gp.getController().getFarmModel() != null) {
        locationInfoString = gp.getPlayerCurrentLocationDetail();
    }

    Font mainFont = (stardewFont_20 != null) ? stardewFont_20.deriveFont(16f) : new Font("Arial", Font.PLAIN, 16);
    Font LocationFont = (stardewFont_30 != null) ? stardewFont_30.deriveFont(14f) : new Font("Arial", Font.PLAIN, 14);
    
    int yPosisi = 30;
    int blockSpace = 20; 
    int marginKanan = 10;

    g2.setFont(mainFont);
    java.awt.FontMetrics fmWaktu = g2.getFontMetrics();

    drawTextWithShadow(seasonText, gp.screenWidth - fmWaktu.stringWidth(seasonText) - marginKanan, yPosisi);
    yPosisi += blockSpace;
    
    drawTextWithShadow(weatherText, gp.screenWidth - fmWaktu.stringWidth(weatherText) - marginKanan, yPosisi);
    yPosisi += blockSpace;
    
    drawTextWithShadow(dayText, gp.screenWidth - fmWaktu.stringWidth(dayText) - marginKanan, yPosisi);
    yPosisi += blockSpace;
    
    drawTextWithShadow(timeText, gp.screenWidth - fmWaktu.stringWidth(timeText) - marginKanan, yPosisi);
    yPosisi += blockSpace; 

    g2.setFont(LocationFont);
    java.awt.FontMetrics fmlocation = g2.getFontMetrics();
    
    String locationName = locationInfoString;
    String coordinates = "";

    if (locationInfoString.contains("(")) {
        locationName = locationInfoString.substring(0, locationInfoString.lastIndexOf("(")).trim();
        coordinates = locationInfoString.substring(locationInfoString.lastIndexOf("("));
    }
    
    String[] locationWord = locationName.split(" ");

    for (String name : locationWord) {
        if (name.isEmpty()) continue;
        int lebarKata = fmlocation.stringWidth(name);
        int xKata = gp.screenWidth - lebarKata - marginKanan;
        drawTextWithShadow(name, xKata, yPosisi);
        yPosisi += blockSpace;
    }

        if (!coordinates.isEmpty()) {
            int lebarcoordinates = fmlocation.stringWidth(coordinates);
            int xcoordinates = gp.screenWidth - lebarcoordinates - marginKanan;
            drawTextWithShadow(coordinates, xcoordinates, yPosisi);
        }
    }

    private void drawPauseScreen() {
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.screenWidth - (gp.tileSize * 8);
        int frameHeight = gp.tileSize * 6;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(0, 0, 0, 210));

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(Font.BOLD, 15F));
        Font optionFont = (stardewFont_30 != null ? stardewFont_30 : defaultFont.deriveFont(Font.PLAIN, 10F));

        String text = "Paused";
        int x = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int y = frameY + gp.tileSize + gp.tileSize / 2;
        drawTextWithShadow(text, x, y, titleFont);

        g2.setFont(optionFont); 
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
        final int frameHeight = gp.tileSize * 8; 

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(101, 67, 33, 230));

        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(Font.BOLD, 15F));
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
        final int slotYStart = titleY + gp.tileSize + (gp.tileSize / 2); 

     
        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());

        Font itemPlaceholderFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(10F) : defaultFont.deriveFont(10F));
        Font quantityFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(Font.BOLD, 12F) : defaultFont.deriveFont(Font.BOLD, 12F));
        Font hotkeyFont = (stardewFont_20 != null ? stardewFont_20.deriveFont(10F) : defaultFont.deriveFont(10F));

        for (int i = 0; i < maxSlotsToDisplay; i++) {
            int col = i % slotsPerRow;
            int row = i / slotsPerRow;

            int currentSlotX = slotXStart + col * (slotSize + slotGap);
            int currentSlotY = slotYStart + row * (slotSize + slotGap);
            
        
            if (row == 0 && i < 12) { 
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


            g2.setColor(new Color(80, 40, 0, 200)); 
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
                int maxNameLengthInSlot = 7; 
                if (itemName.length() > maxNameLengthInSlot) {
                    itemName = itemName.substring(0, Math.min(itemName.length(), maxNameLengthInSlot - 2)) + "..";
                }
                int textWidth = g2.getFontMetrics().stringWidth(itemName);
                int textX = currentSlotX + (slotSize - textWidth) / 2;
                int textY = currentSlotY + (slotSize / 2) + (g2.getFontMetrics().getAscent() / 3); 
                g2.drawString(itemName, textX, textY);

                if (quantity > 1) {
                    g2.setFont(quantityFont);
                    String qtyText = String.valueOf(quantity);
                    int qtyTextWidth = g2.getFontMetrics().stringWidth(qtyText);
                    int qtyX = currentSlotX + slotSize - qtyTextWidth - 4;
                    int qtyY = currentSlotY + slotSize - 4;
                    drawTextWithShadow(qtyText, qtyX, qtyY, quantityFont); 
                }
            }
        }
        

        int cursorX = slotXStart + (slotSize + slotGap) * slotCol;
        int cursorY = slotYStart + (slotSize + slotGap) * slotRow;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX - 2, cursorY - 2, slotSize + 4, slotSize + 4, 10, 10);
    }

    public void drawSubWindow(int x, int y, int width, int height, Color backgroundColor) {
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont);
        return gp.screenWidth / 2 - length / 2;
    }
    
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


    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
    }
}