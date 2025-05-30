package org.example.view;


import org.example.controller.GameState; 
import org.example.model.Inventory;
<<<<<<< Updated upstream
import org.example.model.Recipe;
=======
import org.example.model.Player;
import org.example.model.Items.Fish;
import org.example.model.Items.Food;
import org.example.model.Items.ItemDatabase;
>>>>>>> Stashed changes
import org.example.model.Items.Items;
import org.example.model.enums.Season;
import java.time.LocalTime;

import org.example.model.Farm;
import org.example.model.GameClock; 
import org.example.model.enums.Weather;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase;
import org.example.view.GamePanel;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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

    private String uiMessage = null;
    private boolean clearUiMessageNextFrame = false; // Untuk pesan sementara

    // Variabel State untuk Cooking Menu
    private int selectedRecipeIndex = 0;
    private int selectedFuelIndex = 0;
    private int cookingMenuCommandNum = 0; // 0: Biasanya untuk aksi utama (misal, "COOK"), 1: Untuk "CANCEL"
    private java.util.List<org.example.model.Recipe> availableRecipesForUI; // Menggunakan List dari model Recipe
    private java.util.List<Items> availableFuelsForUI; 

    private java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm");

    // Cooking Menu State
    private int selectedRecipeIndex = 0;
    private int selectedFuelIndex = 0;
    private int cookingMenuCommandNum = 0; // 0: Cook, 1: Cancel
    private List<Recipe> availableRecipesForUI;
    private List<Items> availableFuelsForUI;
    private String uiMessage = null;
    private boolean clearUiMessageNextFrame = false;


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
        this.availableFuelsForUI = new ArrayList<>();
        Items firewood = ItemDatabase.getItem("Firewood"); // Pastikan nama item ini ada di ItemDatabase
        Items coal = ItemDatabase.getItem("Coal");       // Pastikan nama item ini ada di ItemDatabase
        if (firewood != null) this.availableFuelsForUI.add(firewood);
        if (coal != null) this.availableFuelsForUI.add(coal);
        
    }

  
    public void draw(Graphics2D g2, GameState currentGameStateManager, Farm farm) { // Terima Farm
        this.g2 = g2; 
        Player player = (farm != null) ? farm.getPlayerModel() : null;
        Inventory playerInventory = (player != null) ? player.getInventory() : null;
        
        drawTimeInfo(); // Selalu gambar info waktu

        int currentState = currentGameStateManager.getGameState(); // Dapatkan nilai int dari state

        if (currentState == currentGameStateManager.play) {
            if (uiMessage != null) {
                Font messageFont = (stardewFont_30 != null ? stardewFont_30 : defaultFont.deriveFont(Font.PLAIN, 12f));
                int messageX = getXforCenteredText(uiMessage, messageFont);
                int messageY = gp.screenHeight - gp.tileSize; 
                drawTextWithShadow(uiMessage, messageX, messageY, messageFont);

                if (clearUiMessageNextFrame) {
                    uiMessage = null;
                    clearUiMessageNextFrame = false;
                }
            }
        } else if (currentState == currentGameStateManager.pause) {
            drawPauseScreen();
        } else if (currentState == currentGameStateManager.inventory) {
            if (playerInventory != null) {
                drawInventoryScreen(playerInventory); 
            } else {
                drawTextWithShadow("Inventory not available.", 
                                   getXforCenteredText("Inventory not available.", stardewFont_30), 
                                   gp.screenHeight / 2, 
                                   stardewFont_30);
            }
        } else if (currentState == currentGameStateManager.cooking_menu) { // <-- TAMBAHKAN INI
            drawCookingMenuScreen(farm, player, playerInventory); // Kirim parameter yang relevan
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
    }


    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    public void setDialogue(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDialogue'");
=======

    public void resetCookingMenuState() {
        selectedRecipeIndex = 0;
        selectedFuelIndex = 0;
        cookingMenuCommandNum = 0;
        availableRecipesForUI = null; // Will be repopulated on next menu open
        uiMessage = null;
        clearUiMessageNextFrame = false;
    }


    public void setDialogue(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = false;
    }


    public void showTemporaryMessage(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = true;
>>>>>>> Stashed changes
    }
=======
    private void drawTextWithShadow(String text, int x, int y, Font font, Color textColor, Color shadowColor) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);

        g2.setColor(shadowColor);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(textColor);
        g2.drawString(text, x, y);
    }

    public int getCookingMenuCommandNum() {
        return cookingMenuCommandNum;
    }

    public void setCookingMenuCommandNum(int commandNum) {
        this.cookingMenuCommandNum = commandNum;
        System.out.println("GameStateUI: Command number set to " + commandNum);
    }   

    public void resetCookingMenuState() {
        this.selectedRecipeIndex = 0;    // Pilihan resep kembali ke item pertama
        this.selectedFuelIndex = 0;      // Pilihan bahan bakar kembali ke item pertama
        this.cookingMenuCommandNum = 0;  // Pilihan command kembali ke "COOK" (atau command default Anda)
        
        if (this.availableRecipesForUI != null) {
            this.availableRecipesForUI.clear(); 
        }
        this.availableRecipesForUI = null; // Set ke null agar di-repopulate saat drawCookingMenuScreen
        
        
        this.uiMessage = null; // Hapus pesan UI yang mungkin masih ada dari sesi menu sebelumnya
        this.clearUiMessageNextFrame = false;
        System.out.println("GameStateUI: Cooking menu state has been reset.");
    }

    public String getUiMessage(){
        return uiMessage;
    }

    public void setDialogue(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = false; // Pastikan pesan ini tidak dihapus otomatis oleh logika pesan sementara
        System.out.println("GameStateUI: Dialogue set to - \"" + message + "\"");
    }

    public void showTemporaryMessage(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = true;
        System.out.println("GameStateUI: Temporary message - \"" + message + "\"");
    }

    public java.util.List<Recipe> getAvailableRecipesForUI() {
        return availableRecipesForUI;
    }

    public java.util.List<Items> getAvailableFuelsForUI() {
        // availableFuelsForUI diinisialisasi di konstruktor dan biasanya tidak berubah.
        return availableFuelsForUI;
    }

    public int getSelectedRecipeIndex() {
        return selectedRecipeIndex;
    }

    public int getSelectedFuelIndex() {
        return selectedFuelIndex;
    }
    

    // Setter untuk navigasi menu (dipanggil oleh KeyHandler melalui GameController)
    public void setSelectedRecipeIndex(int index) {
        if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty()) {
            if (index < 0) this.selectedRecipeIndex = availableRecipesForUI.size() - 1; // Wrap around
            else if (index >= availableRecipesForUI.size()) this.selectedRecipeIndex = 0; // Wrap around
            else this.selectedRecipeIndex = index;
        } else {
            this.selectedRecipeIndex = 0; 
        }
         this.uiMessage = null; // Hapus pesan saat navigasi
    }

    public void setSelectedFuelIndex(int index) {
        if (availableFuelsForUI != null && !availableFuelsForUI.isEmpty()) {
            if (index < 0) this.selectedFuelIndex = availableFuelsForUI.size() - 1;
            else if (index >= availableFuelsForUI.size()) this.selectedFuelIndex = 0;
            else this.selectedFuelIndex = index;
        } else {
            this.selectedFuelIndex = 0;
        }
        this.uiMessage = null; // Hapus pesan saat navigasi
    }

    private void drawCookingMenuScreen(Farm farm, Player player, Inventory playerInventory) {
        if (farm == null || player == null || playerInventory == null) {
            System.err.println("GameStateUI ERROR: Farm, Player, or Inventory is null in drawCookingMenuScreen.");
            drawTextWithShadow("Error: Cooking data unavailable.", gp.tileSize, gp.screenHeight / 2, stardewFont_30);
            return;
        }

        // Latar belakang menu
        g2.setColor(new Color(0, 0, 0, 220)); // Latar belakang gelap transparan
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Frame utama window memasak
        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight, woodBrown); // Gunakan warna woodBrown

        // Font
        Font titleFont = (stardewFont_40 != null ? stardewFont_40 : defaultFont.deriveFont(Font.BOLD, 22f)); // Perbesar sedikit
        Font listFont = (stardewFont_30 != null ? stardewFont_30 : defaultFont.deriveFont(Font.PLAIN, 16f)); // Perbesar sedikit
        Font detailHeaderFont = listFont.deriveFont(Font.BOLD);
        Font detailFont = (stardewFont_20 != null ? stardewFont_20 : defaultFont.deriveFont(Font.PLAIN, 14f)); // Perbesar sedikit
        Font commandFont = listFont.deriveFont(Font.BOLD);

        // Judul Menu
        String title = "Stove - Let's Cook!";
        int titleX = getXforCenteredTextInWindow(title, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(title, titleX, titleY, titleFont);

        // Pembagian Area Layout
        int listAreaX = frameX + gp.tileSize;
        int listAreaY = titleY + gp.tileSize + 10;
        int listAreaWidth = (int) (frameWidth * 0.4) - gp.tileSize; // Area untuk daftar resep
        int listLineHeight = 25; // Ketinggian per baris resep

        int detailAreaX = listAreaX + listAreaWidth + gp.tileSize;
        int detailAreaY = listAreaY;
        int detailAreaWidth = frameWidth - (listAreaWidth + gp.tileSize * 2) - (frameX + gp.tileSize - frameX) ; // Lebar sisa untuk detail

        int bottomSectionY = frameY + frameHeight - gp.tileSize * 3 - 20; // Area untuk fuel dan tombol

        // 1. Daftar Resep (Recipe List)
        if (availableRecipesForUI == null) {
            // Gunakan getCookableRecipes dari RecipeDatabase yang sudah Anda miliki
            availableRecipesForUI = RecipeDatabase.getCookableRecipes(playerInventory, farm.getPlayerStats());
            if (availableRecipesForUI == null) { // Jika getCookableRecipes bisa return null
                 availableRecipesForUI = new ArrayList<>();
            }
        }

        if (availableRecipesForUI.isEmpty()) {
            String noRecipeText = "No recipes available.";
            int noRecipeTextX = listAreaX + (listAreaWidth - g2.getFontMetrics(listFont).stringWidth(noRecipeText)) / 2;
            int noRecipeTextY = listAreaY + listLineHeight * 2;
            drawTextWithShadow(noRecipeText, noRecipeTextX, noRecipeTextY, listFont);
        } else {
            int maxRecipesToDisplay = 10; // Jumlah resep yang ditampilkan sekaligus (bisa digulir)
            int displayOffset = 0;
            if (selectedRecipeIndex >= maxRecipesToDisplay / 2) {
                displayOffset = selectedRecipeIndex - (maxRecipesToDisplay / 2);
                if (displayOffset + maxRecipesToDisplay > availableRecipesForUI.size()) {
                    displayOffset = Math.max(0, availableRecipesForUI.size() - maxRecipesToDisplay);
                }
            }

            for (int i = 0; i < maxRecipesToDisplay; i++) {
                int actualIndexInList = displayOffset + i;
                if (actualIndexInList >= availableRecipesForUI.size()) break;

                Recipe recipe = availableRecipesForUI.get(actualIndexInList);
                boolean canCraft = RecipeDatabase.getCookableRecipes(playerInventory, farm.getPlayerStats()).contains(recipe); // Cek lagi apakah bisa dimasak dengan bahan saat ini

                String prefix = (actualIndexInList == selectedRecipeIndex) ? "> " : "  ";
                Color textColor = (actualIndexInList == selectedRecipeIndex) ? Color.YELLOW : (canCraft ? lightYellow : Color.GRAY);
                drawTextWithShadow(prefix + recipe.getDisplayName(), listAreaX, listAreaY + (i * listLineHeight), listFont, textColor, darkTextShadow);
            }
        }

        // 2. Detail Resep yang Dipilih (Selected Recipe Details)
        if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty() && selectedRecipeIndex >= 0 && selectedRecipeIndex < availableRecipesForUI.size()) {
            Recipe currentRecipe = availableRecipesForUI.get(selectedRecipeIndex);
            Food resultingDish = currentRecipe.getResultingDish();
            int currentDetailY = detailAreaY;

            // Gambar Hasil Masakan (jika ada)
            if (resultingDish != null && resultingDish.getImage() != null) {
                g2.drawImage(resultingDish.getImage(), detailAreaX, currentDetailY, gp.tileSize * 2, gp.tileSize * 2, null);
                currentDetailY += gp.tileSize * 2 + 5; // Spasi setelah gambar
            }
            
            // Nama Hasil Masakan
            drawTextWithShadow("Produces: " + (resultingDish != null ? resultingDish.getName() : "N/A"), detailAreaX, currentDetailY, detailHeaderFont);
            currentDetailY += listLineHeight;

            // Bahan-Bahan
            drawTextWithShadow("Ingredients:", detailAreaX, currentDetailY, detailHeaderFont);
            currentDetailY += listLineHeight;

            for (Map.Entry<Items, Integer> entry : currentRecipe.getIngredients().entrySet()) {
                Items requiredItem = entry.getKey();
                int requiredQuantity = entry.getValue();
                int ownedQuantity;

                if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                    ownedQuantity = (int) playerInventory.getInventory().entrySet().stream()
                                      .filter(invE -> invE.getKey() instanceof Fish) // Asumsi Fish adalah subclass Items
                                      .mapToLong(Map.Entry::getValue).sum();
                } else {
                    ownedQuantity = playerInventory.getItemQuantity(requiredItem.getName()); // Ambil berdasarkan nama jika objeknya placeholder
                }
                boolean hasEnough = ownedQuantity >= requiredQuantity;
                
                String ingredientName = requiredItem.getName();
                if (requiredItem.getImage() != null) {
                     g2.drawImage(requiredItem.getImage(), detailAreaX, currentDetailY - listLineHeight/2 , gp.tileSize-4, gp.tileSize-4, null);
                     ingredientName = ""; // Jangan gambar nama jika sudah ada gambar
                }

                drawTextWithShadow(ingredientName + " " + ownedQuantity + "/" + requiredQuantity, 
                                   detailAreaX + (requiredItem.getImage() != null ? gp.tileSize : 0), 
                                   currentDetailY, detailFont, 
                                   (hasEnough ? lightYellow : Color.RED), darkTextShadow);
                currentDetailY += listLineHeight;
            }
        } else if (availableRecipesForUI != null && availableRecipesForUI.isEmpty()){
             drawTextWithShadow("Select a recipe.", detailAreaX, detailAreaY, listFont);
        }


        // Garis Pemisah
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(frameX + 20, bottomSectionY - 10, frameX + frameWidth - 20, bottomSectionY - 10);

        // 3. Pilihan Bahan Bakar (Fuel Selection)
        int fuelSectionX = listAreaX;
        int fuelSectionY = bottomSectionY;
        drawTextWithShadow("Fuel:", fuelSectionX, fuelSectionY, detailHeaderFont);
        fuelSectionY += listLineHeight;

        if (availableFuelsForUI == null || availableFuelsForUI.isEmpty()) {
            drawTextWithShadow("No fuel items defined!", fuelSectionX, fuelSectionY, listFont, Color.ORANGE, darkTextShadow);
        } else {
            for (int i = 0; i < availableFuelsForUI.size(); i++) {
                Items fuel = availableFuelsForUI.get(i);
                int fuelOwned = playerInventory.getItemQuantity(fuel.getName());
                String fuelText = fuel.getName() + " (" + fuelOwned + ")";
                
                String prefix = (i == selectedFuelIndex) ? "> " : "  ";
                Color fuelColor = (i == selectedFuelIndex) ? Color.YELLOW : (fuelOwned > 0 ? lightYellow : Color.GRAY);
                // Posisikan pilihan fuel berdampingan
                int fuelItemX = fuelSectionX + (i * (listAreaWidth / 2)); // Sesuaikan pembagian lebar
                drawTextWithShadow(prefix + fuelText, fuelItemX, fuelSectionY, listFont, fuelColor, darkTextShadow);
            }
        }

        // 4. Tombol Aksi (COOK / CANCEL)
        int commandSectionX = detailAreaX; // Sejajarkan dengan area detail
        int commandY = bottomSectionY + listLineHeight; // Di bawah fuel, atau sejajar

        String cookText = "COOK";
        int cookTextX = commandSectionX + (detailAreaWidth / 4) - (g2.getFontMetrics(commandFont).stringWidth(cookText) / 2);
        if (cookingMenuCommandNum == 0) {
            drawTextWithShadow("> " + cookText, cookTextX - 10, commandY, commandFont, Color.YELLOW, darkTextShadow);
        } else {
            drawTextWithShadow(cookText, cookTextX, commandY, commandFont);
        }

        String cancelText = "CANCEL";
        int cancelTextX = commandSectionX + (detailAreaWidth * 3 / 4) - (g2.getFontMetrics(commandFont).stringWidth(cancelText) / 2);
        if (cookingMenuCommandNum == 1) {
            drawTextWithShadow("> " + cancelText, cancelTextX - 10, commandY, commandFont, Color.YELLOW, darkTextShadow);
        } else {
            drawTextWithShadow(cancelText, cancelTextX, commandY, commandFont);
        }

        // 5. Tampilkan Pesan UI (jika ada)
        if (uiMessage != null) {
            int messageY = frameY + frameHeight - gp.tileSize / 2 - 5; // Paling bawah
            int messageX = getXforCenteredTextInWindow(uiMessage, frameX, frameWidth, listFont);
            drawTextWithShadow(uiMessage, messageX, messageY, listFont, Color.ORANGE, darkTextShadow);
            if (clearUiMessageNextFrame) {
                uiMessage = null;
                clearUiMessageNextFrame = false;
            }
        }
    }

    
>>>>>>> Stashed changes
}