// Lokasi: src/main/java/org/example/model/RecipeBook.java
package org.example.model; // Sesuaikan package jika perlu

import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Items.Food;
import org.example.model.Items.Fish; // Untuk mengecek instance Fish
import org.example.model.enums.FishType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeDatabase {
    private static final Map<String, Recipe> recipes = new HashMap<>();
    private static boolean isInitialized = false;

    // Item placeholder untuk "Any Fish"
    // Ini bisa menjadi kelas khusus atau hanya instance Items dengan nama tertentu
    // Untuk kesederhanaan, kita bisa menggunakan nama string khusus dan menanganinya di CookingAction
    public static final String ANY_FISH_INGREDIENT_NAME = "Any Fish";
    public static final String ANY_COMMON_FISH_INGREDIENT_NAME = "Any Common Fish";
    public static final String ANY_REGULAR_FISH_INGREDIENT_NAME = "Any Regular Fish";
    public static final String ANY_LEGENDARY_FISH_INGREDIENT_NAME = "Any Legendary Fish";


    public static synchronized void initialize() {
        if (isInitialized) return;
        if (!ItemDatabase.isInitialized()) { // Pastikan ItemDatabase sudah diinisialisasi
            ItemDatabase.initialize();
        }
        recipes.clear();

        // --- Helper untuk mendapatkan item dari ItemDatabase ---
        Items wheat = ItemDatabase.getItem("Wheat");
        Items potato = ItemDatabase.getItem("Potato"); // Asumsi ada "Potato" di CropsFactory
        Items salmon = ItemDatabase.getItem("Salmon");
        Items pufferfish = ItemDatabase.getItem("Pufferfish");
        Items grape = ItemDatabase.getItem("Grape");
        Items egg = ItemDatabase.getItem("Egg"); // Asumsi ada "Egg"
        Items cauliflower = ItemDatabase.getItem("Cauliflower");
        Items parsnip = ItemDatabase.getItem("Parsnip");
        Items tomato = ItemDatabase.getItem("Tomato");
        Items hotPepper = ItemDatabase.getItem("Hot Pepper");
        Items melon = ItemDatabase.getItem("Melon");
        Items cranberry = ItemDatabase.getItem("Cranberry");
        Items blueberry = ItemDatabase.getItem("Blueberry"); // Asumsi ada "Blueberry" di CropsFactory
        Items legendFishItem = ItemDatabase.getItem("Legend"); // Ikan "Legend"

        // --- Hasil Masakan dari FoodFactory ---
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


        // === Inisialisasi Resep ===

        // recipe_1: Fish n' Chips
        Map<Items, Integer> r1Ingredients = new HashMap<>();
        // Untuk "Any Fish", kita akan gunakan placeholder dan logika khusus di CookingAction
        // Untuk sementara, kita bisa letakkan item non-spesifik atau null jika ItemDatabase tidak punya "Any Fish"
        // Ini akan jadi penanda di CookingAction untuk mencari ikan apapun.
        r1Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 2); // Placeholder
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
        r3Ingredients.put(salmon, 3); // Menggunakan Salmon x3 sesuai spek (bukan 10 jenis ikan berbeda)
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
                    // Cek apakah pemain pernah menangkap Pufferfish
                    // Ini bisa dilakukan dengan mengecek inventory atau log penangkapan ikan
                    // Untuk sekarang, asumsikan ada cara untuk mengecek.
                    // Atau, jika hanya perlu Pufferfish di inventory saat ini:
                    // (Logika ini mungkin lebih cocok di canCook())
                    // Untuk unlock, kita asumsikan PlayerStats punya info apakah Pufferfish pernah ditangkap
                    // Jika PlayerStats tidak punya, kita bisa asumsikan true jika Pufferfish-nya ada
                    // atau membuat kondisi yang selalu true dan validasi bahan di canCook.
                    // Mari kita asumsikan stats akan memiliki cara melacak "item pernah didapatkan"
                    // Untuk contoh: kita cek apakah ada di inventory (walaupun ini lebih ke canCook)
                    // Cara yang lebih baik: PlayerStats.hasObtainedItem("Pufferfish")
                    // Kita akan buat unlock condition yang lebih sederhana untuk contoh:
                    return true; // Sementara, akan dicek bahan saat memasak
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
        r6Ingredients.put(ItemDatabase.getItem("Pumpkin"), 1); // Asumsi "Pumpkin" ada
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
        r8Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 2); // Placeholder
        recipes.put("recipe_8", new Recipe("recipe_8", "Fish Stew", fishStewDish, r8Ingredients,
                "achievement", "Dapatkan \"Hot Pepper\"",
                stats -> {
                    // Asumsi ada cara untuk cek apakah "Hot Pepper" pernah didapatkan
                    // atau ada di inventory. Kita sederhanakan untuk sekarang.
                    return true; // Validasi bahan akan dilakukan saat canCook
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
        r10Ingredients.put(ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME), 1); // Placeholder
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
        r11Ingredients.put(ItemDatabase.getItem("Eggplant"), 1); // Asumsi "Eggplant" ada
        recipes.put("recipe_11", new Recipe("recipe_11", "The Legends of Spakbor", legendsOfSpakborDish, r11Ingredients,
                "achievement", "Memancing \"Legend\"",
                stats -> {
                    if (stats == null || legendFishItem == null) return false;
                    // Cek apakah ikan Legend pernah ditangkap.
                    // Misal, dengan PlayerStats.hasCaughtFish(FishType.LEGENDARY, "Legend")
                    // Atau lebih sederhana, jika item "Legend" (ikan) ada di inventory:
                    // (Ini lebih ke canCook, untuk unlock bisa jadi perlu mekanisme lain)
                    // Untuk contoh:
                    return stats.getTotalFishCaught().getOrDefault(FishType.LEGENDARY, 0) > 0 &&
                           stats.getTotalFishCaught().containsKey(FishType.LEGENDARY); // Lebih baik jika bisa cek spesifik Legend
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
        Map<Items, Integer> currentInventory = new HashMap<>(playerInventory.getInventory()); // Salinan untuk dimodifikasi sementara

        // Ambil semua ikan dari inventory
        List<Map.Entry<Items, Integer>> allFishInInventory = currentInventory.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Fish)
                .collect(Collectors.toList());

        // Prioritaskan Common, Regular, Legendary (bisa disesuaikan)
        allFishInInventory.sort((e1, e2) -> {
            Fish f1 = (Fish) e1.getKey();
            Fish f2 = (Fish) e2.getKey();
            // Asumsi FishType punya urutan alami atau kita definisikan di sini
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
            return new ArrayList<>(); // Tidak cukup ikan
        }
        return fishToConsume;
    }

    public static boolean isInitialized() {
    return isInitialized;
    }
}