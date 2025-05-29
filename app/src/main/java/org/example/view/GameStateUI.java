package org.example.view;

import org.example.controller.GameState; // Assumed to have static state constants
import org.example.model.Inventory;
import org.example.model.Items.Items;
import org.example.model.Items.Food;
import org.example.model.Items.Fish;
import org.example.model.Items.ItemDatabase;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase;
import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.CookingInProgress;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStateUI implements TimeObserver {
    private final GamePanel gp; // Marked as final
    private Graphics2D g2; // Should be set in draw method, not stored long-term if Graphics context changes
    private final Font stardewFont_L, stardewFont_M, stardewFont_S, defaultFont;

    public int commandNum = 0; // For pause menu
    public int slotCol = 0;    // For inventory navigation
    public int slotRow = 0;    // For inventory navigation

    // TimeInfo State
    private int currentDay = 1;
    private Season currentSeason = Season.SPRING;
    private LocalTime currentTime = LocalTime.of(6, 0);
    private Weather currentWeather = Weather.SUNNY;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // Cooking Menu State
    private int selectedRecipeIndex = 0;
    private int selectedFuelIndex = 0;
    private int cookingMenuCommandNum = 0; // 0: Cook, 1: Cancel
    private List<Recipe> availableRecipesForUI;
    private List<Items> availableFuelsForUI;
    private String uiMessage = null;
    private boolean clearUiMessageNextFrame = false;

    // Theme Colors (Consider making these static final if they are truly constant, or part of a Theme class)
    private final Color woodBrown = new Color(139, 69, 19, 230);
    private final Color lightYellow = new Color(255, 253, 208);
    private final Color darkTextShadow = new Color(60, 30, 0, 150);
    private final Color borderColor = new Color(210, 180, 140);
    private final Color missingIngredientColor = new Color(255, 100, 100, 220);
    private final Color canCraftColor = lightYellow;
    private final Color cannotCraftColor = new Color(160, 160, 160);

    public GameStateUI(GamePanel gp) {
        this.gp = gp;
        this.defaultFont = new Font("Arial", Font.PLAIN, 12);
        Font baseFont = null;

        Font tempFont_L;
        Font tempFont_M;
        Font tempFont_S;
        try (InputStream isPrimary = getClass().getResourceAsStream("/font/slkscr.ttf");
             InputStream isFallback = getClass().getResourceAsStream("/font/PressStart2P.ttf")) {

            if (isPrimary != null) {
                baseFont = Font.createFont(Font.TRUETYPE_FONT, isPrimary);
            } else if (isFallback != null) {
                System.out.println("[GameStateUI INFO] Primary font not found, using fallback PressStart2P.ttf.");
                baseFont = Font.createFont(Font.TRUETYPE_FONT, isFallback);
            } else {
                throw new IOException("Custom font files not found (/font/slkscr.ttf or /font/PressStart2P.ttf).");
            }
            tempFont_L = baseFont.deriveFont(22f);
            tempFont_M = baseFont.deriveFont(16f);
            tempFont_S = baseFont.deriveFont(12f);
            System.out.println("[GameStateUI INFO] Custom font successfully loaded.");
        } catch (FontFormatException | IOException e) {
            System.err.println("[GameStateUI WARNING] Failed to load custom font, using Arial. Error: " + e.getMessage());
            tempFont_L = new Font("Arial", Font.BOLD, 22);
            tempFont_M = new Font("Arial", Font.PLAIN, 16);
            tempFont_S = new Font("Arial", Font.PLAIN, 12);
        }
        stardewFont_L = tempFont_L;
        stardewFont_M = tempFont_M;
        stardewFont_S = tempFont_S;


        availableFuelsForUI = new ArrayList<>();
        Items firewood = ItemDatabase.getItem("Firewood");
        Items coal = ItemDatabase.getItem("Coal");
        if (firewood != null) availableFuelsForUI.add(firewood);
        if (coal != null) availableFuelsForUI.add(coal);
    }

    public void setDialogue(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = false;
    }

    public void showTemporaryMessage(String message) {
        this.uiMessage = message;
        this.clearUiMessageNextFrame = true;
    }

    public void resetCookingMenuState() {
        selectedRecipeIndex = 0;
        selectedFuelIndex = 0;
        cookingMenuCommandNum = 0;
        availableRecipesForUI = null; // Will be repopulated on next menu open
        uiMessage = null;
        clearUiMessageNextFrame = false;
    }

    @Override
    public void onTimeUpdate(int day, Season season, Weather weather, LocalTime time) {
        this.currentDay = day;
        this.currentSeason = season;
        this.currentWeather = weather;
        this.currentTime = time;
    }

    public void draw(Graphics2D g2, GameState currentGameState, Farm farm) {
        this.g2 = g2; // Set graphics context for this draw call
        Player player = (farm != null) ? farm.getPlayerModel() : null;
        Inventory playerInventory = (player != null) ? player.getInventory() : null;

        // Always draw time info if available
        drawTimeInfo();

        // Draw UI based on game state
        if (currentGameState.getGameState() == GameState.PLAY) { // Use static constant
            if (uiMessage != null) {
                drawTextWithShadow(uiMessage, gp.tileSize, gp.screenHeight - gp.tileSize, stardewFont_M);
                if (clearUiMessageNextFrame) {
                    uiMessage = null;
                    clearUiMessageNextFrame = false;
                }
            }
        } else if (currentGameState.getGameState() == GameState.PAUSE) { // Use static constant
            drawPauseScreen();
        } else if (currentGameState.getGameState() == GameState.INVENTORY) { // Use static constant
            if (playerInventory != null) {
                drawInventoryScreen(playerInventory);
            } else {
                 drawTextWithShadow("Inventory data not available.", gp.tileSize, gp.screenHeight / 2, stardewFont_M, Color.RED, Color.BLACK);
            }
        } else if (currentGameState.getGameState() == GameState.COOKING_MENU) { // Use static constant
            drawCookingMenuScreen(farm, player, currentGameState); // player can be null, checked inside
        }
    }

    // --- HELPER DRAWING METHODS ---
    private void drawSubWindow(int x, int y, int width, int height, Color backgroundColor) {
        g2.setColor(backgroundColor);
        g2.fillRoundRect(x, y, width, height, 25, 25);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x + 3, y + 3, width - 6, height - 6, 20, 20);
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }

    private int getXforCenteredTextInWindow(String text, int windowX, int windowWidth, Font font) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        if (font != null) g2.setFont(originalFont);
        return windowX + (windowWidth - length) / 2;
    }

    private void drawTextWithShadow(String text, int x, int y, Font font, Color textColor, Color shadowColor) {
        Font originalFont = g2.getFont();
        if (font != null) g2.setFont(font);
        g2.setColor(shadowColor);
        g2.drawString(text, x + 1, y + 1); // Small shadow offset
        g2.setColor(textColor);
        g2.drawString(text, x, y);
        if (font != null) g2.setFont(originalFont);
    }

    private void drawTextWithShadow(String text, int x, int y, Font font) {
        drawTextWithShadow(text, x, y, font, lightYellow, darkTextShadow); // Default colors
    }


    // --- SPECIFIC UI DRAWING METHODS ---
    private void drawTimeInfo() {
        if (g2 == null || gp == null) return;

        String seasonText = (currentSeason != null) ? currentSeason.toString() : "Season?";
        String weatherText = (currentWeather != null) ? currentWeather.toString() : "Weather?";
        String dayText = "Day " + currentDay;
        String timeText = (currentTime != null) ? currentTime.format(timeFormatter) : "--:--";

        Font timeFont = stardewFont_M.deriveFont(14f);
        g2.setFont(timeFont);

        int textX = gp.screenWidth - (int)(gp.tileSize * 4.5);
        int startY = gp.tileSize / 2 + 10;
        int lineHeight = 18;

        drawTextWithShadow(timeText, textX, startY, timeFont);
        drawTextWithShadow(dayText, textX, startY + lineHeight, timeFont);
        drawTextWithShadow(seasonText, textX, startY + lineHeight * 2, timeFont);
        drawTextWithShadow(weatherText, textX, startY + lineHeight * 3, timeFont);
    }

    private void drawPauseScreen() {
        final int frameX = gp.tileSize * 5;
        final int frameY = gp.tileSize * 4;
        final int frameWidth = gp.screenWidth - (gp.tileSize * 10);
        final int frameHeight = gp.tileSize * 5;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, new Color(0, 0, 0, 220)); // Dark semi-transparent background

        Font titleFont = stardewFont_L;
        Font optionFont = stardewFont_M;

        String text = "Paused";
        int x = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int y = frameY + gp.tileSize;
        drawTextWithShadow(text, x, y, titleFont);

        // Option 1: Continue
        text = "Continue";
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize * 1.5;
        if (commandNum == 0) drawTextWithShadow("> " + text, x - gp.tileSize/2, y, optionFont, Color.YELLOW, darkTextShadow);
        else drawTextWithShadow(text, x, y, optionFont);

        // Option 2: Exit Game
        text = "Exit Game"; // Make sure GameController.confirmPauseUISelection handles this
        x = getXforCenteredTextInWindow(text, frameX, frameWidth, optionFont);
        y += gp.tileSize;
        if (commandNum == 1) drawTextWithShadow("> " + text, x - gp.tileSize/2, y, optionFont, Color.YELLOW, darkTextShadow);
        else drawTextWithShadow(text, x, y, optionFont);
    }

    private void drawInventoryScreen(Inventory inventory) {
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        final int frameHeight = gp.tileSize * 8; // Adjust as needed

        drawSubWindow(frameX, frameY, frameWidth, frameHeight, woodBrown);

        Font titleFont = stardewFont_L;
        String text = "Inventory";
        int titleX = getXforCenteredTextInWindow(text, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(text, titleX, titleY, titleFont);

        // Slot configuration (could be constants or configurable)
        final int slotsPerRow = 12;
        final int totalDisplayRows = 3;
        final int slotSize = gp.tileSize;
        final int slotPadding = 8;
        final int slotDisplaySize = slotSize + slotPadding; // Slot clickable area size
        final int slotGap = 4; // Gap between slots

        final int gridWidth = (slotsPerRow * slotDisplaySize) - slotGap;
        final int slotXStart = frameX + (frameWidth - gridWidth) / 2;
        final int slotYStart = titleY + gp.tileSize + (gp.tileSize / 2);

        ArrayList<Map.Entry<Items, Integer>> inventoryList = new ArrayList<>(inventory.getInventory().entrySet());
        Font itemFont = stardewFont_S.deriveFont(10f); // For item name/placeholder
        Font quantityFont = stardewFont_S.deriveFont(Font.BOLD, 12f);

        for (int i = 0; i < slotsPerRow * totalDisplayRows; i++) {
            int col = i % slotsPerRow;
            int row = i / slotsPerRow;

            int currentSlotX = slotXStart + col * slotDisplaySize;
            int currentSlotY = slotYStart + row * slotDisplaySize;

            // Draw slot background
            g2.setColor(new Color(80, 40, 0, 180)); // Darker wood for slot background
            g2.fillRoundRect(currentSlotX, currentSlotY, slotDisplaySize - slotGap, slotDisplaySize - slotGap, 8, 8);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(currentSlotX, currentSlotY, slotDisplaySize - slotGap, slotDisplaySize - slotGap, 8, 8);

            if (i < inventoryList.size()) {
                Items item = inventoryList.get(i).getKey();
                Integer quantity = inventoryList.get(i).getValue();

                // Draw item image
                if (item.getImage() != null) {
                    // Center image within the slot (slotSize for image, slotDisplaySize for padded area)
                    g2.drawImage(item.getImage(), currentSlotX + slotPadding/2, currentSlotY + slotPadding/2, slotSize, slotSize, null);
                } else {
                    drawTextWithShadow("?", currentSlotX + slotDisplaySize/2 - 5, currentSlotY + slotDisplaySize/2 + 5, itemFont);
                }

                // Draw quantity
                if (quantity > 1) {
                    String qtyText = String.valueOf(quantity);
                    int qtyTextWidth = g2.getFontMetrics(quantityFont).stringWidth(qtyText);
                    // Position quantity at bottom-right of the slot
                    drawTextWithShadow(qtyText, currentSlotX + slotDisplaySize - slotGap - qtyTextWidth - 2, currentSlotY + slotDisplaySize - slotGap - 2, quantityFont, Color.WHITE, new Color(0,0,0,100));
                }
            }
        }

        // Draw inventory selection cursor
        int cursorX = slotXStart + slotDisplaySize * slotCol;
        int cursorY = slotYStart + slotDisplaySize * slotRow;
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX - 2, cursorY - 2, slotDisplaySize - slotGap + 4, slotDisplaySize - slotGap + 4, 10, 10);
        g2.setStroke(new BasicStroke(1)); // Reset stroke
    }

    private void drawCookingMenuScreen(Farm farm, Player player, GameState currentGameState) {
        if (farm == null || player == null || player.getInventory() == null) {
            System.err.println("ERROR (drawCookingMenuScreen): Farm, Player, or Inventory is null.");
            drawTextWithShadow("Error: Cooking data unavailable.", gp.tileSize, gp.screenHeight / 2, stardewFont_M, Color.RED, darkTextShadow);
            return;
        }
        Inventory playerInventory = player.getInventory();

        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int frameX = gp.tileSize;
        int frameY = gp.tileSize;
        int frameWidth = gp.screenWidth - (gp.tileSize * 2);
        int frameHeight = gp.screenHeight - (gp.tileSize * 2);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight, woodBrown);

        Font titleFont = stardewFont_L;
        Font listFont = stardewFont_M;
        Font detailHeaderFont = stardewFont_M.deriveFont(Font.BOLD); // Font untuk header "Produces", "Ingredients"
        Font detailFont = stardewFont_S.deriveFont(13f);
        Font fuelFont = stardewFont_M;
        Font commandFont = stardewFont_M.deriveFont(Font.BOLD);

        // Title
        String title = "Stove - Let's Cook!";
        int titleX = getXforCenteredTextInWindow(title, frameX, frameWidth, titleFont);
        int titleY = frameY + gp.tileSize;
        drawTextWithShadow(title, titleX, titleY, titleFont);

        // Layout Areas
        int listAreaX = frameX + gp.tileSize;
        int listAreaY = titleY + gp.tileSize; // Y awal untuk daftar resep
        int listAreaWidth = frameWidth / 3;
        int listLineHeight = (int)(gp.tileSize * 0.9);

        // REVISI 2: Geser area detail resep ke bawah
        int detailAreaX = listAreaX + listAreaWidth + gp.tileSize / 2;
        int detailAreaY = listAreaY + listLineHeight; // Mulai detail resep satu baris di bawah daftar resep
                                                      // Atau bisa juga sama dengan listAreaY jika ingin sejajar tapi kontennya yg diatur
                                                      // Untuk sekarang kita geser seluruh bloknya
        int detailAreaContentWidth = frameWidth - (detailAreaX - frameX) - gp.tileSize; // Lebar area untuk konten detail

        // REVISI 3: Geser Separator Line dan Area Bawah (Fuel & Tombol)
        int bottomAreaY = frameY + frameHeight - gp.tileSize * 4 - 10; // Geser ke atas sedikit lagi untuk memberi ruang

        // 1. Recipe List
        if (availableRecipesForUI == null) {
             if (farm.getPlayerModel() != null) { // player sudah merupakan farm.getPlayerModel()
                availableRecipesForUI = RecipeDatabase.getAvailableRecipes(player); // Menggunakan player langsung
             }
             if (availableRecipesForUI == null) availableRecipesForUI = new ArrayList<>();
        }

        if (availableRecipesForUI.isEmpty()) {
            // REVISI 1: Tengahkan "No recipes unlocked yet."
            String noRecipeText = "No recipes unlocked yet.";
            int noRecipeTextX = getXforCenteredTextInWindow(noRecipeText, listAreaX, listAreaWidth, listFont);
            // Posisikan di tengah vertikal area daftar resep (perkiraan)
            int noRecipeTextY = listAreaY + (frameHeight - listAreaY - (bottomAreaY - listAreaY)) / 2; // Perkiraan tengah
            drawTextWithShadow(noRecipeText, noRecipeTextX, noRecipeTextY, listFont);
        } else {
            int maxRecipesDisplay = 7;
            int displayStartIndex = Math.max(0, selectedRecipeIndex - (maxRecipesDisplay / 2));
            if (displayStartIndex + maxRecipesDisplay > availableRecipesForUI.size()) {
                displayStartIndex = Math.max(0, availableRecipesForUI.size() - maxRecipesDisplay);
            }

            for (int i = 0; i < maxRecipesDisplay; i++) {
                int actualIdx = displayStartIndex + i;
                if (actualIdx >= availableRecipesForUI.size()) break;

                Recipe recipe = availableRecipesForUI.get(actualIdx);
                boolean canCraft = recipe.canCraft(playerInventory);

                String prefix = (actualIdx == selectedRecipeIndex) ? "> " : "  ";
                Color textColor = (actualIdx == selectedRecipeIndex) ? Color.YELLOW : (canCraft ? canCraftColor : cannotCraftColor);
                drawTextWithShadow(prefix + recipe.getDisplayName(), listAreaX, listAreaY + (i * listLineHeight), listFont, textColor, darkTextShadow);
            }
        }

        // 2. Selected Recipe Details
        if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty() && selectedRecipeIndex >= 0 && selectedRecipeIndex < availableRecipesForUI.size()) {
            Recipe currentRecipe = availableRecipesForUI.get(selectedRecipeIndex);
            Food resultingDish = currentRecipe.getResultingDish();
            int currentDetailInternalY = detailAreaY; // Y internal untuk penempatan di dalam area detail

            // Resulting Dish Image and Name
            if (resultingDish != null) {
                if (resultingDish.getImage() != null) {
                    // REVISI 2: Tengahkan gambar hasil (relatif ke detailAreaX dan detailAreaContentWidth)
                    int dishImageX = detailAreaX + (detailAreaContentWidth - gp.tileSize * 2) / 2;
                    g2.drawImage(resultingDish.getImage(), dishImageX, currentDetailInternalY, gp.tileSize * 2, gp.tileSize * 2, null);
                }
                String producesText = "Produces: " + resultingDish.getName();
                int producesTextX = getXforCenteredTextInWindow(producesText, detailAreaX, detailAreaContentWidth, detailHeaderFont);
                drawTextWithShadow(producesText, producesTextX, currentDetailInternalY + gp.tileSize * 2 + 10, detailHeaderFont); // Di bawah gambar
                currentDetailInternalY += gp.tileSize * 2 + 15 + listLineHeight;
            }

            // Ingredients
            String ingredientsTitle = "Ingredients:";
            int ingredientsTitleX = getXforCenteredTextInWindow(ingredientsTitle, detailAreaX, detailAreaContentWidth, detailHeaderFont);
            drawTextWithShadow(ingredientsTitle, ingredientsTitleX, currentDetailInternalY, detailHeaderFont);
            currentDetailInternalY += listLineHeight;

            for (Map.Entry<Items, Integer> entry : currentRecipe.getIngredients().entrySet()) {
                Items requiredItem = entry.getKey();
                int requiredQuantity = entry.getValue();
                int ownedQuantity;
                boolean hasEnough;

                if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                    ownedQuantity = (int) playerInventory.getInventory().entrySet().stream()
                                      .filter(invE -> invE.getKey() instanceof Fish)
                                      .mapToInt(Map.Entry::getValue).sum();
                } else {
                    ownedQuantity = playerInventory.getItemQuantity(requiredItem);
                }
                hasEnough = ownedQuantity >= requiredQuantity;

                int ingredientRowX = detailAreaX + gp.tileSize/2; // X awal untuk baris ingredient
                if (requiredItem.getImage() != null) {
                    g2.drawImage(requiredItem.getImage(), ingredientRowX, currentDetailInternalY - (int)(gp.tileSize*0.6), (int)(gp.tileSize*0.8), (int)(gp.tileSize*0.8), null);
                }
                String ingredientText = requiredItem.getName() + ": " + ownedQuantity + "/" + requiredQuantity;
                // REVISI 2: Tengahkan teks bahan (opsional, atau biarkan rata kiri setelah gambar)
                // int ingredientTextX = getXforCenteredTextInWindow(ingredientText, detailAreaX, detailAreaContentWidth, detailFont);
                // drawTextWithShadow(ingredientText, ingredientTextX, currentDetailInternalY, detailFont, ... );
                // Atau rata kiri setelah gambar:
                drawTextWithShadow(ingredientText,
                                   ingredientRowX + gp.tileSize, currentDetailInternalY, detailFont,
                                   (hasEnough ? canCraftColor : missingIngredientColor), darkTextShadow);
                currentDetailInternalY += listLineHeight - 5;
            }
        } else if (availableRecipesForUI != null && availableRecipesForUI.isEmpty()){
             String noDetailsText = "No recipes to show details for.";
             int noDetailsX = getXforCenteredTextInWindow(noDetailsText, detailAreaX, detailAreaContentWidth, detailFont);
             drawTextWithShadow(noDetailsText, noDetailsX, detailAreaY, detailFont);
        }


        // REVISI 3: Geser Separator Line dan Area Bawah (Fuel & Tombol)
        // int bottomAreaY = frameY + frameHeight - gp.tileSize * 4 - 10; // Duplicate removed
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(frameX + 20, bottomAreaY - 10, frameX + frameWidth - 20, bottomAreaY - 10);

        // 3. Fuel Selection
        int fuelDisplayX = frameX + gp.tileSize;
        int fuelDisplayY = bottomAreaY; // Mulai dari Y area bawah yang baru
        drawTextWithShadow("Fuel:", fuelDisplayX, fuelDisplayY, fuelFont.deriveFont(Font.BOLD));
        fuelDisplayY += listLineHeight;

        if (availableFuelsForUI.isEmpty()) {
            drawTextWithShadow("No fuel items configured!", fuelDisplayX, fuelDisplayY, fuelFont, Color.ORANGE, darkTextShadow);
        } else {
            for (int i = 0; i < availableFuelsForUI.size(); i++) {
                Items fuel = availableFuelsForUI.get(i);
                int fuelOwned = playerInventory.getItemQuantity(fuel);
                String fuelText = fuel.getName() + " (" + fuelOwned + ")";
                int currentFuelX = fuelDisplayX + (i * (frameWidth / (availableFuelsForUI.size() + 1) )); // Distribusi lebih merata

                if (fuel.getImage() != null) {
                    g2.drawImage(fuel.getImage(), currentFuelX, fuelDisplayY - (int)(gp.tileSize * 0.6), (int)(gp.tileSize*0.8), (int)(gp.tileSize*0.8), null);
                }
                Color fuelColor = (i == selectedFuelIndex) ? Color.YELLOW : (fuelOwned > 0 ? canCraftColor : cannotCraftColor);
                String prefix = (i == selectedFuelIndex) ? "> " : "  ";
                drawTextWithShadow(prefix + fuelText, currentFuelX + gp.tileSize, fuelDisplayY, fuelFont, fuelColor, darkTextShadow);
            }
        }

        // 4. Action Buttons
        int commandDisplayY = bottomAreaY + listLineHeight * 2 - 5; // Sesuaikan Y relatif terhadap bottomAreaY
        String cookText = "COOK";
        int cookTextX = getXforCenteredTextInWindow(cookText, frameX, frameWidth / 2, commandFont);
        Color cookColor = (cookingMenuCommandNum == 0) ? Color.YELLOW : lightYellow;
        drawTextWithShadow((cookingMenuCommandNum == 0 ? "> " : "") + cookText, cookTextX, commandDisplayY, commandFont, cookColor, darkTextShadow);

        String cancelText = "CANCEL";
        int cancelTextX = getXforCenteredTextInWindow(cancelText, frameX + frameWidth / 2, frameWidth / 2, commandFont);
        Color cancelColor = (cookingMenuCommandNum == 1) ? Color.YELLOW : lightYellow;
        drawTextWithShadow((cookingMenuCommandNum == 1 ? "> " : "") + cancelText, cancelTextX, commandDisplayY, commandFont, cancelColor, darkTextShadow);

        // 5. Active Cooking Status
        List<CookingInProgress> activeCookings = farm.getActiveCookings();
        if (activeCookings != null && !activeCookings.isEmpty()) {
            CookingInProgress currentTask = activeCookings.stream().filter(task -> task != null && !task.isClaimed()).findFirst().orElse(null);
            if (currentTask != null) {
                // ... (logika teks dan warna status masakan aktif tetap sama) ...
                String ongoingText;
                Font ongoingFont = stardewFont_S.deriveFont(11f); // Perkecil sedikit
                Color ongoingColor;
                if (currentTask.isReadyToClaim(farm.getCurrentTime())) {
                    ongoingText = currentTask.getCookedDish().getName() + " is READY! (Claim at Stove)";
                    ongoingColor = new Color(100, 255, 100);
                } else {
                    long remainingMinutes = currentTask.getRemainingGameMinutes(farm.getCurrentTime());
                    ongoingText = "Cooking: " + currentTask.getCookedDish().getName() + " (" + (remainingMinutes > 0 ? remainingMinutes + "m" : "Almost...") +")";
                    ongoingColor = lightYellow;
                }
                int ongoingTextX = getXforCenteredTextInWindow(ongoingText, frameX, frameWidth, ongoingFont);
                drawTextWithShadow(ongoingText, ongoingTextX, frameY + frameHeight - gp.tileSize/2 , ongoingFont, ongoingColor, darkTextShadow); // Geser sedikit ke atas
            }
        }
        // Tampilkan pesan UI jika ada
        if (uiMessage != null && currentGameState.getGameState() == GameState.COOKING_MENU) {
            int messageY = frameY + frameHeight - gp.tileSize - 20; // Posisi pesan UI
             if (activeCookings != null && !activeCookings.isEmpty() && activeCookings.stream().anyMatch(t -> t != null && !t.isClaimed())) {
                messageY -= gp.tileSize/2; // Naikkan jika ada info masakan aktif
            }
            drawTextWithShadow(uiMessage, getXforCenteredTextInWindow(uiMessage, frameX, frameWidth, stardewFont_M), messageY, stardewFont_M);
            if (clearUiMessageNextFrame) {
                uiMessage = null;
                clearUiMessageNextFrame = false;
            }
        }
    }

    // --- Getters and Setters for UI State (Mainly for Cooking Menu) ---
    public int getSelectedRecipeIndex() { return selectedRecipeIndex; }
    public void setSelectedRecipeIndex(int index) {
        if (availableRecipesForUI != null && !availableRecipesForUI.isEmpty()) {
            if (index < 0) this.selectedRecipeIndex = availableRecipesForUI.size() - 1; // Wrap around
            else if (index >= availableRecipesForUI.size()) this.selectedRecipeIndex = 0; // Wrap around
            else this.selectedRecipeIndex = index;
        } else {
            this.selectedRecipeIndex = 0; // Default if no recipes
        }
         this.uiMessage = null; // Clear previous messages on navigation
    }
    public List<Recipe> getAvailableRecipesForUI() { return availableRecipesForUI; } // Used by KeyHandler

    public int getSelectedFuelIndex() { return selectedFuelIndex; }
    public void setSelectedFuelIndex(int index) {
        if (availableFuelsForUI != null && !availableFuelsForUI.isEmpty()) {
            if (index < 0) this.selectedFuelIndex = availableFuelsForUI.size() - 1;
            else if (index >= availableFuelsForUI.size()) this.selectedFuelIndex = 0;
            else this.selectedFuelIndex = index;
        } else {
            this.selectedFuelIndex = 0;
        }
        this.uiMessage = null;
    }
    public List<Items> getAvailableFuelsForUI() { return availableFuelsForUI; } // Used by KeyHandler

    public int getCookingMenuCommandNum() { return cookingMenuCommandNum; }
    public void setCookingMenuCommandNum(int num) { // Typically 0 for Cook, 1 for Cancel
        if (num < 0) this.cookingMenuCommandNum = 1; // Wrap to last command
        else if (num > 1) this.cookingMenuCommandNum = 0; // Wrap to first command
        else this.cookingMenuCommandNum = num;
        this.uiMessage = null;
    }
}