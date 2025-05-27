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
    public static final String ANY_COMMON_FISH_INGREDIENT_NAME = "Any Common Fish";
    public static final String ANY_REGULAR_FISH_INGREDIENT_NAME = "Any Regular Fish";
    public static final String ANY_LEGENDARY_FISH_INGREDIENT_NAME = "Any Legendary Fish";


    public static synchronized void initialize() {
        if (isInitialized) return;
        if (!ItemDatabase.isInitialized()) {
            ItemDatabase.initialize();
        }
        recipes.clear();

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

        // recipe_1: Fish n' Chips
        Map<Items, Integer> r1Ingredients = new HashMap<>();
        r1Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 2);
        r1Ingredients.put(wheat, 1);
        r1Ingredients.put(potato, 1);
        recipes.put("recipe_1", new Recipe("recipe_1", "Fish n' Chips", fishNChipsDish, r1Ingredients,
                "Beli di store", "Beli di store", null));

        // recipe_2: Baguette
        Map<Items, Integer> r2Ingredients = new HashMap<>();
        r2Ingredients.put(wheat, 3);
        recipes.put("recipe_2", new Recipe("recipe_2", "Baguette", baguetteDish, r2Ingredients,
                "Default/Bawaan", "Default/Bawaan", null));

        // recipe_3: Sashimi
        Map<Items, Integer> r3Ingredients = new HashMap<>();
        r3Ingredients.put(salmon, 3); 
        recipes.put("recipe_3", new Recipe("recipe_3", "Sashimi", sashimiDish, r3Ingredients,
                "achievement", "Setelah memancing 10 ikan (total)",
                stats -> {
                    if (stats == null) return false;
                    int totalFish = 0;
                    for (Integer count : stats.getTotalFishCaught().values()) {
                        totalFish += count;
                    }
                    return totalFish >= 10;
                }));

        // recipe_4: Fugu
        Map<Items, Integer> r4Ingredients = new HashMap<>();
        r4Ingredients.put(pufferfish, 1);
        recipes.put("recipe_4", new Recipe("recipe_4", "Fugu", fuguDish, r4Ingredients,
                "achievement", "Memancing pufferfish",
                stats -> {
                    if (stats == null || pufferfish == null) return false;
                    return true; 
                }));


        // recipe_5: Wine
        Map<Items, Integer> r5Ingredients = new HashMap<>();
        r5Ingredients.put(grape, 2);
        recipes.put("recipe_5", new Recipe("recipe_5", "Wine", wineDish, r5Ingredients,
                "Default/Bawaan", "Default/Bawaan", null));

        // recipe_6: Pumpkin Pie
        Map<Items, Integer> r6Ingredients = new HashMap<>();
        r6Ingredients.put(egg, 1);
        r6Ingredients.put(wheat, 1);
        r6Ingredients.put(ItemDatabase.getItem("Pumpkin"), 1); 
        recipes.put("recipe_6", new Recipe("recipe_6", "Pumpkin Pie", pumpkinPieDish, r6Ingredients,
                "Default/Bawaan", "Default/Bawaan", null));

        // recipe_7: Veggie Soup
        Map<Items, Integer> r7Ingredients = new HashMap<>();
        r7Ingredients.put(cauliflower, 1);
        r7Ingredients.put(parsnip, 1);
        r7Ingredients.put(potato, 1);
        r7Ingredients.put(tomato, 1);
        recipes.put("recipe_7", new Recipe("recipe_7", "Veggie Soup", veggieSoupDish, r7Ingredients,
                "achievement", "Memanen untuk pertama kalinya",
                stats -> stats != null && stats.getTotalCropsHarvested() > 0));

        // recipe_8: Fish Stew
        Map<Items, Integer> r8Ingredients = new HashMap<>();
        r8Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 2); 
        recipes.put("recipe_8", new Recipe("recipe_8", "Fish Stew", fishStewDish, r8Ingredients,
                "achievement", "Dapatkan \"Hot Pepper\"",
                stats -> {
                    return true;
                }));

        // recipe_9: Spakbor Salad
        Map<Items, Integer> r9Ingredients = new HashMap<>();
        r9Ingredients.put(melon, 1);
        r9Ingredients.put(cranberry, 1);
        r9Ingredients.put(blueberry, 1);
        r9Ingredients.put(tomato, 1);
        recipes.put("recipe_9", new Recipe("recipe_9", "Spakbor Salad", spakborSaladDish, r9Ingredients,
                "Default/Bawaan", "Default/Bawaan", null));

        // recipe_10: Fish Sandwich
        Map<Items, Integer> r10Ingredients = new HashMap<>();
        r10Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 1); 
        r10Ingredients.put(wheat, 2);
        r10Ingredients.put(tomato, 1);
        r10Ingredients.put(hotPepper, 1);
        recipes.put("recipe_10", new Recipe("recipe_10", "Fish Sandwich", fishSandwichDish, r10Ingredients,
                "Beli di store", "Beli di store", null));

        // recipe_11: The Legends of Spakbor
        Map<Items, Integer> r11Ingredients = new HashMap<>();
        r11Ingredients.put(legendFishItem, 1);
        r11Ingredients.put(potato, 2);
        r11Ingredients.put(parsnip, 1);
        r11Ingredients.put(tomato, 1);
        r11Ingredients.put(ItemDatabase.getItem("Eggplant"), 1); 
        recipes.put("recipe_11", new Recipe("recipe_11", "The Legends of Spakbor", legendsOfSpakborDish, r11Ingredients,
                "achievement", "Memancing \"Legend\"",
                stats -> {
                    if (stats == null || legendFishItem == null) return false;
                    return stats.getTotalFishCaught().getOrDefault(FishType.LEGENDARY, 0) > 0 &&
                           stats.getTotalFishCaught().containsKey(FishType.LEGENDARY); 
                }));


        isInitialized = true;
        System.out.println("LOG: RecipeBook initialized with " + recipes.size() + " recipes.");
    }

    public static Recipe getRecipeById(String recipeId) {
        if (!isInitialized) initialize();
        return recipes.get(recipeId);
    }

    public static Recipe getRecipeByName(String displayName) {
        if (!isInitialized) initialize();
        for (Recipe recipe : recipes.values()) {
            if (recipe.getDisplayName().equalsIgnoreCase(displayName)) {
                return recipe;
            }
        }
        System.err.println("Recipe not found by name: " + displayName);
        return null;
    }

    public static List<Recipe> getAllRecipes() {
        if (!isInitialized) initialize();
        return new ArrayList<>(recipes.values());
    }

    /**
     * Mendapatkan daftar resep yang bisa dimasak oleh pemain berdasarkan inventory dan status unlock.
     * @param playerInventory Inventory pemain.
     * @param playerStats Statistik pemain (untuk cek unlock).
     * @return List resep yang bisa dimasak.
     */
    public static List<Recipe> getCookableRecipes(Inventory playerInventory, PlayerStats playerStats) {
        if (!isInitialized) initialize();
        List<Recipe> cookable = new ArrayList<>();
        for (Recipe recipe : recipes.values()) {
            if (recipe.isUnlocked(playerStats) && canPlayerCookRecipe(playerInventory, recipe)) {
                cookable.add(recipe);
            }
        }
        return cookable;
    }

    /**
     * Helper method untuk mengecek apakah pemain memiliki cukup bahan untuk satu resep.
     * Tidak termasuk bahan bakar.
     */
    private static boolean canPlayerCookRecipe(Inventory playerInventory, Recipe recipe) {
        if (playerInventory == null || recipe == null || recipe.getIngredients() == null) {
            return false;
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (requiredItem == null) { // Kemungkinan placeholder seperti "Any Fish"
                // Jika nama item adalah placeholder "Any Fish"
                if (ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                    int fishCount = 0;
                    for (Map.Entry<Items, Integer> invEntry : playerInventory.getInventory().entrySet()) {
                        if (invEntry.getKey() instanceof Fish) {
                            fishCount += invEntry.getValue();
                        }
                    }
                    if (fishCount < requiredQuantity) return false;
                }
                // Tambahkan logika untuk placeholder lain jika ada
                else {
                     System.err.println("Warning: Unhandled null or placeholder ingredient in recipe: " + recipe.getDisplayName());
                    return false; // Tidak bisa memproses bahan null/placeholder yang tidak dikenal
                }
            } else { // Bahan spesifik
                if (!playerInventory.hasItem(requiredItem, requiredQuantity)) {
                    return false;
                }
            }
        }
        return true;
    }

     /**
     * Digunakan untuk mendapatkan item ikan yang akan dikonsumsi dari inventory.
     * Memprioritaskan ikan common, lalu regular, lalu legendary.
     * @param playerInventory Inventory pemain
     * @param quantityBerapaBanyak Jumlah ikan yang dibutuhkan
     * @return List ikan yang akan dikonsumsi, atau list kosong jika tidak cukup.
     */
    public static List<Items> getFishIngredientsFromInventory(Inventory playerInventory, int quantityBerapaBanyak) {
        List<Items> fishToConsume = new ArrayList<>();
        Map<Items, Integer> currentInventory = new HashMap<>(playerInventory.getInventory());

        List<Map.Entry<Items, Integer>> allFishInInventory = currentInventory.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Fish)
                .collect(Collectors.toList());

        allFishInInventory.sort((e1, e2) -> {
            Fish f1 = (Fish) e1.getKey();
            Fish f2 = (Fish) e2.getKey();
            return f1.getFishType().iterator().next().compareTo(f2.getFishType().iterator().next());
        });

        for (Map.Entry<Items, Integer> fishEntry : allFishInInventory) {
            if (fishToConsume.size() >= quantityBerapaBanyak) break;

            Items fishItem = fishEntry.getKey();
            int availableQuantity = fishEntry.getValue();
            int needed = quantityBerapaBanyak - fishToConsume.size();
            int take = Math.min(needed, availableQuantity);

            for (int i = 0; i < take; i++) {
                fishToConsume.add(fishItem);
            }
        }

        if (fishToConsume.size() < quantityBerapaBanyak) {
            return new ArrayList<>();
        }
        return fishToConsume;
    }

    public static boolean isInitialized() {
    return isInitialized;
    }
}