package org.example.view;


import org.example.controller.GameState; 
import org.example.model.Inventory;
import org.example.model.Player;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase;
import org.example.model.Items.Fish;
import org.example.model.Items.Food;
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
import java.util.List;
import java.util.Map;
import org.example.model.Farm;

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

    // Cooking Menu State

    public int selectedRecipeIndex = 0;
    public int selectedFuelIndex = 0;
    public int cookingMenuCommandNum = 0; // 0: Cook, 1: Cancel
    public List<Recipe> availableRecipesForUI; 
    public List<Items> availableFuelsForUI; 

    // UI Message State
    private String uiMessage = null;
    private boolean clearUiMessageNextFrame = false;

    private String temporaryMessage = null;
    private long messageDisplayTime = 0;
    private static final int MESSAGE_DURATION_MS = 3000;

    Color woodBrown = new Color(139, 69, 19);
    Color lightYellow = new Color(255, 253, 208);
    Color darkTextShadow = new Color(80, 40, 0, 150);
    Color borderColor = new Color(210, 180, 140);
    Color canCraftColor = new Color(144, 238, 144); // Light green
    Color cannotCraftColor = new Color(255, 99, 71); // Tomato red
    Color missingIngredientColor = new Color(255, 99, 71); // Tomato red for missing ingredients

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

  
public void draw(Graphics2D g2, GameState currentGameState, Farm farmInstance, Player playerInstance, Inventory playerInventory) {        this.g2 = g2; 
        drawTimeInfo();

        if (currentGameState.getGameState() == currentGameState.pause) {
            drawPauseScreen();
        } else if (currentGameState.getGameState() == currentGameState.inventory) {
            drawInventoryScreen(playerInventory); 
        } else if (currentGameState.getGameState() == currentGameState.cooking_menu) {
            if (farmInstance != null && playerInstance != null && playerInventory != null) {
                drawCookingMenuScreen(farmInstance, playerInstance, playerInventory);
            } else {
                System.err.println("GameStateUI.draw(): Data penting (Farm/Player/Inventory) null saat akan menggambar cooking menu.");
                // Opsional: gambar pesan error sederhana di layar
            }
        }

        if (temporaryMessage != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - messageDisplayTime < MESSAGE_DURATION_MS) {
                // Gambar pesan di lokasi yang sesuai di layar
                g2.setFont(stardewFont_30); // Atau font lain yang sesuai
                int x = getXforCenteredText(temporaryMessage, g2.getFont()); 
                int y = gp.screenHeight - (gp.tileSize*3); 

                java.awt.FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(temporaryMessage);
                int textHeight = fm.getHeight();
                g2.setColor(new Color(0,0,0,150)); 
                g2.fillRect(x - 10, y - fm.getAscent(), textWidth + 20, textHeight);

                drawTextWithShadow(temporaryMessage, x, y, g2.getFont(), Color.WHITE, Color.BLACK);
            } else {
                temporaryMessage = null; 
            }
        }
    }

    private void drawCookingMenuScreen(Farm farm, Player player, Inventory playerInventory) {
        if (g2 == null || gp == null) { // Pastikan g2 dan gp tidak null
            System.err.println("GameStateUI ERROR: Graphics2D (g2) or GamePanel (gp) is null in drawCookingMenuScreen.");
            return;
        }
        if (farm == null || player == null || playerInventory == null) {
            System.err.println("GameStateUI ERROR: Farm, Player, or Inventory is null in drawCookingMenuScreen.");
            drawTextWithShadow("Error: Cooking data unavailable.", gp.tileSize, gp.screenHeight / 2, stardewFont_30, Color.RED, darkTextShadow);
            return;
        }

        // Latar belakang menu
        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Frame utama window memasak
        int frameX = gp.tileSize / 2; // Sedikit lebih ke tepi
        int frameY = gp.tileSize / 2;
        int frameWidth = gp.screenWidth - gp.tileSize;
        int frameHeight = gp.screenHeight - gp.tileSize;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight, woodBrown);

        // Font
        Font titleFont = stardewFont_40; // Sudah didefinisikan dan di-load
        Font listFont = stardewFont_30;
        Font detailHeaderFont = stardewFont_30.deriveFont(Font.BOLD);
        Font detailFont = stardewFont_20.deriveFont(13f); // Sedikit perbesar detailFont
        Font commandFont = stardewFont_30.deriveFont(Font.BOLD, 14f); // Font untuk tombol Cook/Cancel, sesuaikan ukurannya

        // Judul Menu
        String title = "Stove - Let's Cook!";
        int titleX = getXforCenteredTextInWindow(title, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(title, titleX, titleY, titleFont);

        // Pembagian Area Layout (disesuaikan agar lebih proporsional)
        int listAreaX = frameX + gp.tileSize / 2;
        int listAreaY = titleY + gp.tileSize / 2;
        int listAreaWidth = (int) (frameWidth * 0.35); // Sedikit lebih kecil untuk daftar resep
        int listLineHeight = (int) (gp.tileSize * 0.8); // Ketinggian baris disesuaikan dengan font M

        int detailAreaX = listAreaX + listAreaWidth + gp.tileSize / 2;
        int detailAreaY = listAreaY; // Sejajarkan Y dengan daftar resep
        int detailAreaContentWidth = frameWidth - (detailAreaX - frameX) - gp.tileSize / 2;

        // Mengatur bottomAreaY agar ada ruang untuk tombol Cook/Cancel
        int commandAreaHeight = gp.tileSize * 2; // Perkiraan tinggi area untuk tombol
        int bottomAreaY = frameY + frameHeight - commandAreaHeight - (gp.tileSize / 2);


        // 1. Daftar Resep
        if (availableRecipesForUI == null) { // Populate jika null (pertama kali dibuka/di-reset)
            availableRecipesForUI = RecipeDatabase.getCookableRecipes(playerInventory, farm.getPlayerStats());
            if (availableRecipesForUI == null) availableRecipesForUI = new ArrayList<>();
        }

        if (availableRecipesForUI.isEmpty()) {
            String noRecipeText = "No recipes available or unlocked.";
            java.awt.FontMetrics fmNoRecipe = g2.getFontMetrics(listFont);
            int textWidthNoRecipe = fmNoRecipe.stringWidth(noRecipeText);

            // REVISI POSISI X UNTUK "No recipes available..."
            // Menengahkan dalam listArea, jika listAreaX sudah benar, ini akan bekerja.
            // Atau, beri margin kiri eksplisit jika listAreaX terlalu ke kiri.
            int noRecipeTextX = frameX + (frameWidth - textWidthNoRecipe) / 2;
            // Jika masih terpotong, coba ini:
            // int noRecipeTextX = listAreaX + gp.tileSize / 4; // Sedikit padding dari kiri listArea

            int noRecipeTextY = listAreaY + ( (bottomAreaY - listAreaY) / 2); // Tengahkan di area list secara vertikal
            drawTextWithShadow(noRecipeText, noRecipeTextX, noRecipeTextY, listFont);
        } else {
            // Perhitungan maxRecipesDisplay harus memperhitungkan ruang yang sekarang diambil oleh tombol Cook/Cancel
            // Jadi, kita gunakan bottomAreaY (yang sudah disesuaikan) untuk batas bawah list resep.
            int listMaxHeight = bottomAreaY - listAreaY - (gp.tileSize / 2); // Tinggi maksimal untuk daftar resep
            int maxRecipesDisplay = listMaxHeight / listLineHeight;
            maxRecipesDisplay = Math.max(1, maxRecipesDisplay);

            int displayStartIndex = 0;
            if (selectedRecipeIndex >= (maxRecipesDisplay - 1) / 2) {
                displayStartIndex = selectedRecipeIndex - (maxRecipesDisplay - 1) / 2;
            }
            displayStartIndex = Math.max(0, displayStartIndex);
            if (displayStartIndex + maxRecipesDisplay > availableRecipesForUI.size()) {
                displayStartIndex = Math.max(0, availableRecipesForUI.size() - maxRecipesDisplay);
            }

            for (int i = 0; i < maxRecipesDisplay; i++) {
                int actualIdx = displayStartIndex + i;
                if (actualIdx >= availableRecipesForUI.size()) break;

                Recipe recipe = availableRecipesForUI.get(actualIdx);
                if (recipe == null) continue;

                boolean canCraft = RecipeDatabase.canPlayerCookRecipe(playerInventory, recipe);

                String prefix = (actualIdx == selectedRecipeIndex) ? "> " : "  ";
                Color textColor = (actualIdx == selectedRecipeIndex) ? Color.YELLOW : (canCraft ? canCraftColor : cannotCraftColor);
                drawTextWithShadow(prefix + recipe.getDisplayName(), listAreaX, listAreaY + (i * listLineHeight), listFont, textColor, darkTextShadow);
            }
        }

        // 2. Detail Resep yang Dipilih
        if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty() && selectedRecipeIndex >= 0 && selectedRecipeIndex < availableRecipesForUI.size()) {
            Recipe currentRecipe = availableRecipesForUI.get(selectedRecipeIndex);
            if (currentRecipe == null) return;

            Food resultingDish = currentRecipe.getResultingDish();
            int currentDetailInternalY = detailAreaY; // Mulai Y untuk detail

            // Batas bawah untuk area detail agar tidak tumpang tindih dengan tombol Cook/Cancel
            int detailMaxY = bottomAreaY - (gp.tileSize / 2);


            if (resultingDish != null) {
                if (resultingDish.getImage() != null && (currentDetailInternalY + gp.tileSize * 2 + 10) < detailMaxY) {
                    int dishImageX = detailAreaX + (detailAreaContentWidth - gp.tileSize * 2) / 2;
                    g2.drawImage(resultingDish.getImage(), dishImageX, currentDetailInternalY, gp.tileSize * 2, gp.tileSize * 2, null);
                    currentDetailInternalY += gp.tileSize * 2 + 10;
                }
                if ((currentDetailInternalY + listLineHeight) < detailMaxY) {
                    String producesText = "Produces: " + resultingDish.getName();
                    int producesTextX = getXforCenteredTextInWindow(producesText, detailAreaX, detailAreaContentWidth, detailHeaderFont);
                    drawTextWithShadow(producesText, producesTextX, currentDetailInternalY, detailHeaderFont);
                    currentDetailInternalY += listLineHeight;
                }
            }

            if ((currentDetailInternalY + listLineHeight) < detailMaxY) {
                String ingredientsTitle = "Ingredients:";
                int ingredientsTitleX = getXforCenteredTextInWindow(ingredientsTitle, detailAreaX, detailAreaContentWidth, detailHeaderFont);
                drawTextWithShadow(ingredientsTitle, ingredientsTitleX, currentDetailInternalY, detailHeaderFont);
                currentDetailInternalY += listLineHeight;
            }

            Map<Items, Integer> ingredientsMap = currentRecipe.getIngredients();
            if (ingredientsMap != null) {
                for (Map.Entry<Items, Integer> entry : ingredientsMap.entrySet()) {
                    if ((currentDetailInternalY + listLineHeight - 8) >= detailMaxY) break; // Hentikan jika melebihi batas

                    Items requiredItem = entry.getKey();
                    if (requiredItem == null) continue;

                    int requiredQuantity = entry.getValue();
                    int ownedQuantity;

                    if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                        ownedQuantity = (int) playerInventory.getInventory().entrySet().stream()
                                        .filter(invE -> invE.getKey() instanceof Fish)
                                        .mapToLong(Map.Entry::getValue).sum();
                    } else {
                        ownedQuantity = playerInventory.getItemQuantity(requiredItem.getName());
                    }
                    boolean hasEnough = ownedQuantity >= requiredQuantity;

                    int ingredientRowX = detailAreaX + gp.tileSize / 4;
                    int textXOffset = 0;
                    if (requiredItem.getImage() != null) {
                        g2.drawImage(requiredItem.getImage(), ingredientRowX, currentDetailInternalY - (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.7), (int) (gp.tileSize * 0.7), null);
                        textXOffset = (int) (gp.tileSize * 0.8);
                    }
                    String ingredientText = requiredItem.getName() + ": " + ownedQuantity + "/" + requiredQuantity;
                    drawTextWithShadow(ingredientText, ingredientRowX + textXOffset, currentDetailInternalY, detailFont,
                                    (hasEnough ? canCraftColor : missingIngredientColor), darkTextShadow);
                    currentDetailInternalY += listLineHeight - 8;
                }
            }
        } else if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty()) {
            String selectRecipeText = "Select a recipe to see details.";
            int selectX = getXforCenteredTextInWindow(selectRecipeText, detailAreaX, detailAreaContentWidth, listFont);
            drawTextWithShadow(selectRecipeText, selectX, detailAreaY + gp.tileSize * 2, listFont);
        }


        // --- REVISI: Gambar Tombol Aksi (Cook / Cancel) ---
        // Posisi Y untuk tombol Cook/Cancel (di bawah daftar resep dan detail)
        int commandSectionY = bottomAreaY + (gp.tileSize /2) ; // Sedikit di bawah bottomAreaY

        String cookText = "Cook";
        String cancelText = "Cancel";

        java.awt.FontMetrics fmCommand = g2.getFontMetrics(commandFont);
        int cookTextWidth = fmCommand.stringWidth(cookText);
        int cancelTextWidth = fmCommand.stringWidth(cancelText);

        // Posisikan tombol-tombol ini di tengah area bawah (misalnya, di bawah area detail)
        // atau di tengah seluruh frame jika diinginkan.
        // Contoh: Menempatkan di tengah area bawah (detailAreaX hingga frameX + frameWidth)
        int totalCommandWidth = cookTextWidth + cancelTextWidth + gp.tileSize * 2; // Perkiraan lebar total dengan spasi
        int commandStartX = detailAreaX + (detailAreaContentWidth - totalCommandWidth) / 2;
        if (commandStartX < detailAreaX) commandStartX = detailAreaX + gp.tileSize / 4; // Pastikan tidak terlalu kiri

        int currentCommandX = commandStartX;

        // Tombol "Cook"
        if (cookingMenuCommandNum == 0) { // Jika "Cook" terpilih
            drawTextWithShadow("> " + cookText, currentCommandX, commandSectionY, commandFont, Color.YELLOW, darkTextShadow);
        } else {
            drawTextWithShadow("  " + cookText, currentCommandX, commandSectionY, commandFont, lightYellow, darkTextShadow);
        }
        currentCommandX += cookTextWidth + gp.tileSize * 2; // Spasi antar tombol

        // Tombol "Cancel"
        if (cookingMenuCommandNum == 1) { // Jika "Cancel" terpilih
            drawTextWithShadow("> " + cancelText, currentCommandX, commandSectionY, commandFont, Color.YELLOW, darkTextShadow);
        } else {
            drawTextWithShadow("  " + cancelText, currentCommandX, commandSectionY, commandFont, lightYellow, darkTextShadow);
        }
        // --- AKHIR REVISI TOMBOL AKSI ---
    }

    // Overload untuk warna default
    private void drawTextWithShadow(String text, int x, int y, Font font) {
        drawTextWithShadow(text, x, y, font, lightYellow, darkTextShadow);
    }
    private void drawTextWithShadow(String text, int x, int y) {
        drawTextWithShadow(text, x, y, g2.getFont(), lightYellow, darkTextShadow); // Gunakan font g2 saat ini
    }

    private void drawTextWithShadow(String text, int x, int y, Font font, Color textColor, Color shadowColor) {
        if (g2 == null || text == null) return;
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        else g2.setFont(defaultFont);

        g2.setColor(shadowColor);
        g2.drawString(text, x + 1, y + 1); // Offset bayangan kecil
        g2.setColor(textColor);
        g2.drawString(text, x, y);
        g2.setFont(originalFont); // Kembalikan font asli
    }

    public void showTemporaryMessage(String message) {
        this.temporaryMessage = message;
        this.messageDisplayTime = System.currentTimeMillis();
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

    // Removed duplicate drawTextWithShadow(String, int, int, Font) method to fix compilation error.


    public void setDialogue(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = false; 
        System.out.println("GameStateUI: Dialogue set to - \"" + message + "\"");
    }


    public void resetCookingMenuState() {
        this.selectedRecipeIndex = 0;
        this.selectedFuelIndex = 0;
        this.cookingMenuCommandNum = 0; // Default ke "COOK"
        this.availableRecipesForUI = null; // Akan di-refresh saat menu digambar
        this.uiMessage = null;
        this.clearUiMessageNextFrame = false;
        System.out.println("GameStateUI: Cooking menu state has been reset.");
    }
}