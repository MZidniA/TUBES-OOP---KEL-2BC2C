package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.model.Items.Fish;
import org.example.model.Items.Food;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.enums.FishType;

public class RecipeDatabase {
    private static final Map<String, Recipe> recipes = new HashMap<>();
    private static boolean isInitialized = false;

    public static final String ANY_FISH_INGREDIENT_NAME = "Any Fish";
    // Definisikan konstanta nama placeholder lain jika ada
    // public static final String ANY_COMMON_FISH_INGREDIENT_NAME = "Any Common Fish";

    public static synchronized void initialize() {
        if (isInitialized) return;

        if (!ItemDatabase.isInitialized()) {
            ItemDatabase.initialize(); // Pastikan ItemDatabase siap (terutama placeholder)
        }
        recipes.clear();

        // --- Dapatkan semua objek Items dari ItemDatabase SEKALI di awal ---
        Items wheat = ItemDatabase.getItem("Wheat");
        Items potato = ItemDatabase.getItem("Potato");
        Items salmon = ItemDatabase.getItem("Salmon");
        Items pufferfish = ItemDatabase.getItem("Pufferfish");
        Items grape = ItemDatabase.getItem("Grape");
        Items egg = ItemDatabase.getItem("Egg");
        Items cauliflower = ItemDatabase.getItem("Cauliflower");
        Items parsnip = ItemDatabase.getItem("Parsnip");
        Items tomato = ItemDatabase.getItem("Tomato");
        Items hotPepper = ItemDatabase.getItem("Hot Pepper");
        Items melon = ItemDatabase.getItem("Melon");
        Items cranberry = ItemDatabase.getItem("Cranberry");
        Items blueberry = ItemDatabase.getItem("Blueberry");
        Items legendFishItem = ItemDatabase.getItem("Legend");
        Items pumpkin = ItemDatabase.getItem("Pumpkin");
        Items eggplant = ItemDatabase.getItem("Eggplant");

        Items anyFishPlaceholder = ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME);
        if (anyFishPlaceholder == null) { // Pengecekan kritis untuk placeholder
            System.err.println("RecipeDatabase Init CRITICAL ERROR: Placeholder '" + ANY_FISH_INGREDIENT_NAME + 
                               "' not found in ItemDatabase. Recipes using it will be invalid.");
            // Resep yang menggunakan anyFishPlaceholder tidak akan bisa dibuat jika ini null.
        }

        // Dapatkan objek hasil masakan (Food)
        Food fishNChipsDish = (Food) ItemDatabase.getItem("Fish n’ Chips");
        Food baguetteDish = (Food) ItemDatabase.getItem("Baguette");
        Food sashimiDish = (Food) ItemDatabase.getItem("Sashimi");
        Food fuguDish = (Food) ItemDatabase.getItem("Fugu");
        Food wineDish = (Food) ItemDatabase.getItem("Wine");
        Food pumpkinPieDish = (Food) ItemDatabase.getItem("Pumpkin Pie");
        Food veggieSoupDish = (Food) ItemDatabase.getItem("Veggie Soup");
        Food fishStewDish = (Food) ItemDatabase.getItem("Fish Stew");
        Food spakborSaladDish = (Food) ItemDatabase.getItem("Spakbor Salad");
        Food fishSandwichDish = (Food) ItemDatabase.getItem("Fish Sandwich");
        Food legendsOfSpakborDish = (Food) ItemDatabase.getItem("The Legends of Spakbor");

        // --- Mulai Definisi Resep dengan Validasi ---
        Map<Items, Integer> tempIngredients;

        // recipe_1: Fish n' Chips
        if (fishNChipsDish != null && anyFishPlaceholder != null && wheat != null && potato != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(anyFishPlaceholder, 2);
            tempIngredients.put(wheat, 1);
            tempIngredients.put(potato, 1);
            recipes.put("recipe_1", new Recipe("recipe_1", "Fish n' Chips", fishNChipsDish, tempIngredients, "Beli di store", "Beli di store", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Fish n' Chips'. Missing components."); }

        // recipe_2: Baguette
        if (baguetteDish != null && wheat != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(wheat, 3);
            recipes.put("recipe_2", new Recipe("recipe_2", "Baguette", baguetteDish, tempIngredients, "Default/Bawaan", "Default/Bawaan", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Baguette'. Missing components."); }

        // recipe_3: Sashimi
        if (sashimiDish != null && salmon != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(salmon, 3);
            recipes.put("recipe_3", new Recipe("recipe_3", "Sashimi", sashimiDish, tempIngredients, "achievement", "Setelah memancing 10 ikan (total)",
                stats -> {
                    if (stats == null) return false;
                    Map<FishType, Integer> fishMap = stats.getTotalFishCaught();
                    if (fishMap == null) return false;
                    return fishMap.values().stream().mapToInt(Integer::intValue).sum() >= 10;
                }));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Sashimi'. Missing components."); }
        
        // recipe_4: Fugu
        if (fuguDish != null && pufferfish != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(pufferfish, 1);
            recipes.put("recipe_4", new Recipe("recipe_4", "Fugu", fuguDish, tempIngredients, "achievement", "Memancing pufferfish",
                stats -> stats != null && stats.hasObtainedItem("Pufferfish"))); // Membutuhkan PlayerStats.hasObtainedItem()
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Fugu'. Missing components."); }

        // recipe_5: Wine
        if (wineDish != null && grape != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(grape, 2);
            recipes.put("recipe_5", new Recipe("recipe_5", "Wine", wineDish, tempIngredients, "Default/Bawaan", "Default/Bawaan", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Wine'. Missing components."); }

        // recipe_6: Pumpkin Pie
        if (pumpkinPieDish != null && egg != null && wheat != null && pumpkin != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(egg, 1);
            tempIngredients.put(wheat, 1);
            tempIngredients.put(pumpkin, 1);
            recipes.put("recipe_6", new Recipe("recipe_6", "Pumpkin Pie", pumpkinPieDish, tempIngredients, "Default/Bawaan", "Default/Bawaan", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Pumpkin Pie'. Missing components."); }

        // recipe_7: Veggie Soup
        if (veggieSoupDish != null && cauliflower != null && parsnip != null && potato != null && tomato != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(cauliflower, 1);
            tempIngredients.put(parsnip, 1);
            tempIngredients.put(potato, 1);
            tempIngredients.put(tomato, 1);
            recipes.put("recipe_7", new Recipe("recipe_7", "Veggie Soup", veggieSoupDish, tempIngredients, "achievement", "Memanen untuk pertama kalinya",
                stats -> stats != null && stats.getTotalCropsHarvested() > 0));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Veggie Soup'. Missing components."); }

        // recipe_8: Fish Stew (Bahan: Any Fish x2, Hot Pepper x1, Cauliflower x2)
        if (fishStewDish != null && anyFishPlaceholder != null && hotPepper != null && cauliflower != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(anyFishPlaceholder, 2);
            tempIngredients.put(hotPepper, 1);
            tempIngredients.put(cauliflower, 2);
            recipes.put("recipe_8", new Recipe("recipe_8", "Fish Stew", fishStewDish, tempIngredients, "achievement", "Dapatkan \"Hot Pepper\"",
                stats -> stats != null && stats.hasObtainedItem("Hot Pepper")));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Fish Stew'. Missing components."); }

        // recipe_9: Spakbor Salad
        if (spakborSaladDish != null && melon != null && cranberry != null && blueberry != null && tomato != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(melon, 1);
            tempIngredients.put(cranberry, 1);
            tempIngredients.put(blueberry, 1);
            tempIngredients.put(tomato, 1);
            recipes.put("recipe_9", new Recipe("recipe_9", "Spakbor Salad", spakborSaladDish, tempIngredients, "Default/Bawaan", "Default/Bawaan", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Spakbor Salad'. Missing components."); }

        // recipe_10: Fish Sandwich (Bahan: Any fish x1, Wheat 2x, Tomato 1x, Hot Pepper 1x)
        if (fishSandwichDish != null && anyFishPlaceholder != null && wheat != null && tomato != null && hotPepper != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(anyFishPlaceholder, 1);
            tempIngredients.put(wheat, 2);
            tempIngredients.put(tomato, 1);
            tempIngredients.put(hotPepper, 1);
            recipes.put("recipe_10", new Recipe("recipe_10", "Fish Sandwich", fishSandwichDish, tempIngredients, "Beli di store", "Beli di store", null));
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'Fish Sandwich'. Missing components."); }
        
        // recipe_11: The Legends of Spakbor (Bahan: Legend fish 1x, Potato 2x, Parsnip 1x, Tomato 1x, Eggplant 1x)
        if (legendsOfSpakborDish != null && legendFishItem != null && potato != null && parsnip != null && tomato != null && eggplant != null) {
            tempIngredients = new HashMap<>();
            tempIngredients.put(legendFishItem, 1);
            tempIngredients.put(potato, 2);
            tempIngredients.put(parsnip, 1);
            tempIngredients.put(tomato, 1);
            tempIngredients.put(eggplant, 1);
            recipes.put("recipe_11", new Recipe("recipe_11", "The Legends of Spakbor", legendsOfSpakborDish, tempIngredients, "achievement", "Memancing \"Legend\"",
                stats -> stats != null && stats.hasObtainedItem("Legend"))); // Atau cek statistik ikan Legend jika ada
        } else { System.err.println("RecipeDatabase Init ERROR: Could not create 'The Legends of Spakbor'. Missing components."); }

        isInitialized = true;
        System.out.println("LOG: RecipeDatabase initialized. Number of successfully created recipes: " + recipes.size());
    }

    public static Recipe getRecipeById(String recipeId) {
        if (!isInitialized) initialize();
        return recipes.get(recipeId);
    }

    // ... (getRecipeByName, getAllRecipes tetap sama) ...

    public static List<Recipe> getCookableRecipes(Inventory playerInventory, PlayerStats playerStats) {
        if (!isInitialized) initialize(); // Pastikan sudah diinisialisasi
        if (playerInventory == null || playerStats == null) {
            System.err.println("RecipeDatabase.getCookableRecipes: Inventory or PlayerStats is null.");
            return new ArrayList<>(); // Kembalikan list kosong jika input penting null
        }
        List<Recipe> cookable = new ArrayList<>();
        for (Recipe recipe : recipes.values()) {
            if (recipe != null && recipe.getIngredients() != null && 
                recipe.isUnlocked(playerStats) && canPlayerCookRecipe(playerInventory, recipe)) {
                cookable.add(recipe);
            }
        }
        return cookable;
    }

    public static boolean canPlayerCookRecipe(Inventory playerInventory, Recipe recipe) {
        if (playerInventory == null || recipe == null || recipe.getIngredients() == null) {
            return false;
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey(); // Ini adalah OBJEK Items dari definisi resep
            int requiredQuantity = entry.getValue();

            if (requiredItem == null) { // Ini seharusnya tidak terjadi jika inisialisasi resep sudah benar
                System.err.println("RecipeDatabase.canPlayerCookRecipe CRITICAL ERROR: Found a null ingredient key in recipe: '" + 
                                   (recipe.getDisplayName() != null ? recipe.getDisplayName() : "UNKNOWN RECIPE") + 
                                   "'. This recipe definition is invalid.");
                return false;
            }

            // Bandingkan berdasarkan OBJEK Items jika memungkinkan, atau NAMA jika itu placeholder
            if (ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) { // Perbandingan nama untuk placeholder
                int fishCount = 0;
                for (Map.Entry<Items, Integer> invEntry : playerInventory.getInventory().entrySet()) {
                    if (invEntry.getKey() != null && invEntry.getKey() instanceof Fish) {
                        fishCount += invEntry.getValue();
                    }
                }
                if (fishCount < requiredQuantity) return false;
            }
            // Tambahkan else if untuk placeholder lain jika ada
            else { // Bahan spesifik (bukan placeholder)
                // Gunakan objek Items langsung untuk hasItem
                if (!playerInventory.hasItem(requiredItem, requiredQuantity)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static List<Recipe> getAvailableRecipes(Player player) { // Tetap menggunakan Player
        if (!isInitialized) initialize();
        List<Recipe> available = new ArrayList<>();
        if (player == null || player.getPlayerStats() == null) {
            System.err.println("RecipeDatabase.getAvailableRecipes: Player or PlayerStats is null.");
            return available;
        }
        PlayerStats stats = (PlayerStats) player.getPlayerStats();
        for (Recipe recipe : recipes.values()) {
            if (recipe != null && recipe.isUnlocked(stats)) {
                available.add(recipe);
            }
        }
        return available;
    }
    
    public static boolean isInitialized() { return isInitialized; }
    // getFishIngredientsFromInventory tidak perlu diubah jika sudah berfungsi
    public static List<Items> getFishIngredientsFromInventory(Inventory playerInventory, int quantityBerapaBanyak) { /* ... */ return new ArrayList<>();}

}