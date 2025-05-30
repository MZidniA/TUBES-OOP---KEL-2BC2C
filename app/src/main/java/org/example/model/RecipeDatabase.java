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
import org.example.model.PlayerStats;

public class RecipeDatabase {
    private static final Map<String, Recipe> recipes = new HashMap<>();
    private static boolean isInitialized = false;
    // Placeholder Items untuk "Any Fish" dan jenis ikan lainnya
    // Penting: Objek-objek ini harus diinisialisasi dengan benar dan tidak null.
    // Kita akan membuat instance khusus untuk placeholder ini.
    // public static final Items ANY_COMMON_FISH_PLACEHOLDER = ItemDatabase.getItem(ANY_COMMON_FISH_INGREDIENT_NAME);
    // public static final Items ANY_REGULAR_FISH_PLACEHOLDER = ItemDatabase.getItem(ANY_REGULAR_FISH_INGREDIENT_NAME);
    // public static final Items ANY_LEGENDARY_FISH_PLACEHOLDER = ItemDatabase.getItem(ANY_LEGENDARY_FISH_INGREDIENT_NAME);

    // Konstanta nama placeholder (tetap String untuk perbandingan nama jika diperlukan)
    public static final String ANY_FISH_INGREDIENT_NAME = "Any Fish";
    public static final String ANY_COMMON_FISH_INGREDIENT_NAME = "Any Common Fish";
    public static final String ANY_REGULAR_FISH_INGREDIENT_NAME = "Any Regular Fish";
    public static final String ANY_LEGENDARY_FISH_INGREDIENT_NAME = "Any Legendary Fish";
    public static final Items ANY_FISH_PLACEHOLDER = ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME); // Akan mengambil dari ItemDatabase
    

    public static synchronized void initialize() {
        if (isInitialized) return;

        // Pastikan ItemDatabase sudah siap dan placeholder item juga ada di ItemDatabase
        if (!ItemDatabase.isInitialized()) {
            ItemDatabase.initialize();
        }
        
        // Tambahkan item placeholder ke ItemDatabase jika belum ada (ini penting!)
        // Jika ItemDatabase.getItem() mengembalikan null untuk placeholder, maka akan error.
        // Sebaiknya placeholder ini adalah instance Items yang valid.
        // Misalnya, di ItemDatabase.initialize(), Anda bisa menambahkan:
        // items.put(RecipeDatabase.ANY_FISH_INGREDIENT_NAME, new Misc(RecipeDatabase.ANY_FISH_INGREDIENT_NAME, 0, 0)); // atau tipe Items lain
        // Hal ini untuk memastikan ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME) tidak null.

        recipes.clear();

        // Helper untuk mendapatkan item dan menangani jika null
        Items wheat = ItemDatabase.getItem("Wheat");
        Items potato = ItemDatabase.getItem("Potato");
        Items salmon = ItemDatabase.getItem("Salmon");
        Items pufferfish = ItemDatabase.getItem("Pufferfish");
        Items grape = ItemDatabase.getItem("Grape");
        Items egg = ItemDatabase.getItem("Egg"); // Pastikan "Egg" ada di ItemDatabase Anda
        Items cauliflower = ItemDatabase.getItem("Cauliflower");
        Items parsnip = ItemDatabase.getItem("Parsnip");
        Items tomato = ItemDatabase.getItem("Tomato");
        Items hotPepper = ItemDatabase.getItem("Hot Pepper");
        Items melon = ItemDatabase.getItem("Melon");
        Items cranberry = ItemDatabase.getItem("Cranberry");
        Items blueberry = ItemDatabase.getItem("Blueberry");
        Items legendFishItem = ItemDatabase.getItem("Legend");
        Items pumpkin = ItemDatabase.getItem("Pumpkin"); // Pastikan "Pumpkin" ada
        Items eggplant = ItemDatabase.getItem("Eggplant"); // Pastikan "Eggplant" ada

        // Validasi item bahan dasar (jika null, resep yang menggunakannya tidak akan valid)
        if (wheat == null) System.err.println("RecipeDatabase Init ERROR: Item 'Wheat' not found in ItemDatabase.");
        if (potato == null) System.err.println("RecipeDatabase Init ERROR: Item 'Potato' not found in ItemDatabase.");
        if (salmon == null) System.err.println("RecipeDatabase Init ERROR: Item 'Salmon' not found in ItemDatabase.");
        if (pufferfish == null) System.err.println("RecipeDatabase Init ERROR: Item 'Pufferfish' not found in ItemDatabase.");
        if (grape == null) System.err.println("RecipeDatabase Init ERROR: Item 'Grape' not found in ItemDatabase.");
        if (egg == null) System.err.println("RecipeDatabase Init ERROR: Item 'Egg' not found in ItemDatabase.");
        if (cauliflower == null) System.err.println("RecipeDatabase Init ERROR: Item 'Cauliflower' not found in ItemDatabase.");
        if (parsnip == null) System.err.println("RecipeDatabase Init ERROR: Item 'Parsnip' not found in ItemDatabase.");
        if (tomato == null) System.err.println("RecipeDatabase Init ERROR: Item 'Tomato' not found in ItemDatabase.");
        if (hotPepper == null) System.err.println("RecipeDatabase Init ERROR: Item 'Hot Pepper' not found in ItemDatabase.");
        if (melon == null) System.err.println("RecipeDatabase Init ERROR: Item 'Melon' not found in ItemDatabase.");
        if (cranberry == null) System.err.println("RecipeDatabase Init ERROR: Item 'Cranberry' not found in ItemDatabase.");
        if (blueberry == null) System.err.println("RecipeDatabase Init ERROR: Item 'Blueberry' not found in ItemDatabase.");
        if (legendFishItem == null) System.err.println("RecipeDatabase Init ERROR: Item 'Legend' not found in ItemDatabase.");
        if (pumpkin == null) System.err.println("RecipeDatabase Init ERROR: Item 'Pumpkin' not found in ItemDatabase.");
        if (eggplant == null) System.err.println("RecipeDatabase Init ERROR: Item 'Eggplant' not found in ItemDatabase.");

        // Ambil item placeholder "Any Fish" dari ItemDatabase
        // PASTIKAN "Any Fish" SUDAH ADA DI ItemDatabase.java sebagai item valid (meskipun tidak bisa dimiliki pemain)
        Items anyFishPlaceholder = ItemDatabase.getItem(ANY_FISH_INGREDIENT_NAME);
        if (anyFishPlaceholder == null) {
            System.err.println("RecipeDatabase Init CRITICAL ERROR: Placeholder item '" + ANY_FISH_INGREDIENT_NAME + "' not found in ItemDatabase. Recipes using it will fail.");
            // Anda bisa melempar exception di sini atau membuat placeholder darurat
            // anyFishPlaceholder = new Misc(ANY_FISH_INGREDIENT_NAME, 0, 0); // Darurat, sebaiknya ada di ItemDatabase
        }


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
        if (anyFishPlaceholder != null) r1Ingredients.put(anyFishPlaceholder, 2); // Gunakan placeholder yang sudah divalidasi
        if (wheat != null) r1Ingredients.put(wheat, 1);
        if (potato != null) r1Ingredients.put(potato, 1);
        if (fishNChipsDish != null && !r1Ingredients.containsKey(null)) { // Pastikan tidak ada key null
            recipes.put("recipe_1", new Recipe("recipe_1", "Fish n' Chips", fishNChipsDish, r1Ingredients,
                    "Beli di store", "Beli di store", null));
        } else {
            System.err.println("Failed to create recipe_1: Fish n' Chips due to missing items/dish.");
        }


        // recipe_2: Baguette
        Map<Items, Integer> r2Ingredients = new HashMap<>();
        if (wheat != null) r2Ingredients.put(wheat, 3);
        if (baguetteDish != null && !r2Ingredients.containsKey(null)) {
            recipes.put("recipe_2", new Recipe("recipe_2", "Baguette", baguetteDish, r2Ingredients,
                    "Default/Bawaan", "Default/Bawaan", null));
        } else {
            System.err.println("Failed to create recipe_2: Baguette due to missing items/dish.");
        }

        // recipe_3: Sashimi
        Map<Items, Integer> r3Ingredients = new HashMap<>();
        if (salmon != null) r3Ingredients.put(salmon, 3);
        if (sashimiDish != null && !r3Ingredients.containsKey(null)) {
            recipes.put("recipe_3", new Recipe("recipe_3", "Sashimi", sashimiDish, r3Ingredients,
                    "achievement", "Setelah memancing 10 ikan (total)",
                    stats -> { /* ... kondisi ... */ return true; })); // Sesuaikan kondisi
        } else {
             System.err.println("Failed to create recipe_3: Sashimi due to missing items/dish.");
        }


        // recipe_4: Fugu
        Map<Items, Integer> r4Ingredients = new HashMap<>();
        if (pufferfish != null) r4Ingredients.put(pufferfish, 1);
        if (fuguDish != null && !r4Ingredients.containsKey(null)) {
            recipes.put("recipe_4", new Recipe("recipe_4", "Fugu", fuguDish, r4Ingredients,
                    "achievement", "Memancing pufferfish",
                    stats -> { /* ... kondisi ... */ return true; })); // Sesuaikan kondisi
        } else {
            System.err.println("Failed to create recipe_4: Fugu due to missing items/dish.");
        }

        // recipe_5: Wine
        Map<Items, Integer> r5Ingredients = new HashMap<>();
        if (grape != null) r5Ingredients.put(grape, 2);
        if (wineDish != null && !r5Ingredients.containsKey(null)) {
            recipes.put("recipe_5", new Recipe("recipe_5", "Wine", wineDish, r5Ingredients,
                    "Default/Bawaan", "Default/Bawaan", null));
        } else {
            System.err.println("Failed to create recipe_5: Wine due to missing items/dish.");
        }

        // recipe_6: Pumpkin Pie
        Map<Items, Integer> r6Ingredients = new HashMap<>();
        if (egg != null) r6Ingredients.put(egg, 1);
        if (wheat != null) r6Ingredients.put(wheat, 1);
        if (pumpkin != null) r6Ingredients.put(pumpkin, 1);
        if (pumpkinPieDish != null && !r6Ingredients.containsKey(null)) {
            recipes.put("recipe_6", new Recipe("recipe_6", "Pumpkin Pie", pumpkinPieDish, r6Ingredients,
                    "Default/Bawaan", "Default/Bawaan", null));
        } else {
            System.err.println("Failed to create recipe_6: Pumpkin Pie due to missing items/dish.");
        }

        // recipe_7: Veggie Soup
        Map<Items, Integer> r7Ingredients = new HashMap<>();
        if (cauliflower != null) r7Ingredients.put(cauliflower, 1);
        if (parsnip != null) r7Ingredients.put(parsnip, 1);
        if (potato != null) r7Ingredients.put(potato, 1);
        if (tomato != null) r7Ingredients.put(tomato, 1);
        if (veggieSoupDish != null && !r7Ingredients.containsKey(null)) {
            recipes.put("recipe_7", new Recipe("recipe_7", "Veggie Soup", veggieSoupDish, r7Ingredients,
                    "achievement", "Memanen untuk pertama kalinya",
                    stats -> stats != null && stats.hasObtainedItem("Parsnip"))); // Anda perlu metode hasObtainedItem di PlayerStats

        } else {
            System.err.println("Failed to create recipe_7: Veggie Soup due to missing items/dish.");
        }

        // recipe_8: Fish Stew
        Map<Items, Integer> r8Ingredients = new HashMap<>();
        if (anyFishPlaceholder != null) r8Ingredients.put(anyFishPlaceholder, 2); // Gunakan placeholder
        // Spesifikasi di PDF juga menyebutkan Hot Pepper x1, Cauliflower x2. Anda mungkin perlu menambahkannya.
        if (hotPepper != null) r8Ingredients.put(hotPepper, 1);
        if (cauliflower != null) r8Ingredients.put(cauliflower, 2);
        if (fishStewDish != null && !r8Ingredients.containsKey(null)) {
            recipes.put("recipe_8", new Recipe("recipe_8", "Fish Stew", fishStewDish, r8Ingredients,
                    "achievement", "Dapatkan \"Hot Pepper\"",
                    stats -> stats != null && stats.hasObtainedItem("Hot Pepper"))); // Anda perlu metode hasObtainedItem di PlayerStats
        } else {
            System.err.println("Failed to create recipe_8: Fish Stew due to missing items/dish.");
        }


        // recipe_9: Spakbor Salad
        Map<Items, Integer> r9Ingredients = new HashMap<>();
        if (melon != null) r9Ingredients.put(melon, 1);
        if (cranberry != null) r9Ingredients.put(cranberry, 1);
        if (blueberry != null) r9Ingredients.put(blueberry, 1);
        if (tomato != null) r9Ingredients.put(tomato, 1);
        if (spakborSaladDish != null && !r9Ingredients.containsKey(null)) {
            recipes.put("recipe_9", new Recipe("recipe_9", "Spakbor Salad", spakborSaladDish, r9Ingredients,
                    "Default/Bawaan", "Default/Bawaan", null));
        } else {
            System.err.println("Failed to create recipe_9: Spakbor Salad due to missing items/dish.");
        }

        // recipe_10: Fish Sandwich
        Map<Items, Integer> r10Ingredients = new HashMap<>();
        if (anyFishPlaceholder != null) r10Ingredients.put(anyFishPlaceholder, 1); // Gunakan placeholder
        if (wheat != null) r10Ingredients.put(wheat, 2);
        if (tomato != null) r10Ingredients.put(tomato, 1);
        if (hotPepper != null) r10Ingredients.put(hotPepper, 1);
        if (fishSandwichDish != null && !r10Ingredients.containsKey(null)) {
            recipes.put("recipe_10", new Recipe("recipe_10", "Fish Sandwich", fishSandwichDish, r10Ingredients,
                    "Beli di store", "Beli di store", null));
        } else {
            System.err.println("Failed to create recipe_10: Fish Sandwich due to missing items/dish.");
        }

        // recipe_11: The Legends of Spakbor
        Map<Items, Integer> r11Ingredients = new HashMap<>();
        if (legendFishItem != null) r11Ingredients.put(legendFishItem, 1);
        if (potato != null) r11Ingredients.put(potato, 2);
        if (parsnip != null) r11Ingredients.put(parsnip, 1);
        if (tomato != null) r11Ingredients.put(tomato, 1);
        if (eggplant != null) r11Ingredients.put(eggplant, 1);
        if (legendsOfSpakborDish != null && !r11Ingredients.containsKey(null)) {
            recipes.put("recipe_11", new Recipe("recipe_11", "The Legends of Spakbor", legendsOfSpakborDish, r11Ingredients,
                    "achievement", "Memancing \"Legend\"",
                    stats -> { /* ... kondisi ... */ return true; })); // Sesuaikan kondisi
        } else {
            System.err.println("Failed to create recipe_11: The Legends of Spakbor due to missing items/dish.");
        }

        isInitialized = true;
        System.out.println("LOG: RecipeDatabase initialized with " + recipes.size() + " recipes.");
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

    public static List<Recipe> getCookableRecipes(Inventory playerInventory, PlayerStats playerStats) {
        if (!isInitialized) initialize();
        List<Recipe> cookable = new ArrayList<>();
        for (Recipe recipe : recipes.values()) {
            // Pastikan resep tidak null dan memiliki bahan sebelum dicek lebih lanjut
            if (recipe != null && recipe.getIngredients() != null && recipe.isUnlocked(playerStats) && canPlayerCookRecipe(playerInventory, recipe)) {
                cookable.add(recipe);
            }
        }
        return cookable;
    }

    private static boolean canPlayerCookRecipe(Inventory playerInventory, Recipe recipe) {
        if (playerInventory == null || recipe == null || recipe.getIngredients() == null) {
            return false;
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            // KRUSIAL: Tambahkan pengecekan null untuk requiredItem SEBELUM memanggil .getName()
            if (requiredItem == null) {
                System.err.println("RecipeDatabase.canPlayerCookRecipe CRITICAL ERROR: Found a null ingredient key in recipe: '" + recipe.getDisplayName() + "'. This recipe definition is invalid.");
                return false; // Resep dengan bahan null tidak bisa dimasak
            }

            // Jika nama item adalah placeholder "Any Fish"
            if (ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) { // Sekarang aman memanggil getName()
                int fishCount = 0;
                for (Map.Entry<Items, Integer> invEntry : playerInventory.getInventory().entrySet()) {
                    // Pastikan invEntry.getKey() tidak null sebelum instanceof
                    if (invEntry.getKey() != null && invEntry.getKey() instanceof Fish) {
                        fishCount += invEntry.getValue();
                    }
                }
                if (fishCount < requiredQuantity) return false;
            }
            // Tambahkan logika untuk placeholder lain (ANY_COMMON_FISH, dll.) di sini jika perlu
            // else if (ANY_COMMON_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) { ... }
            else { // Bahan spesifik (bukan placeholder)
                if (!playerInventory.hasItem(requiredItem, requiredQuantity)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Items> getFishIngredientsFromInventory(Inventory playerInventory, int quantityBerapaBanyak) {
        // ... (implementasi Anda sudah oke, pastikan getKey() tidak null saat diakses) ...
        List<Items> fishToConsume = new ArrayList<>();
        if (playerInventory == null || quantityBerapaBanyak <=0) return fishToConsume;

        Map<Items, Integer> currentInventory = new HashMap<>(playerInventory.getInventory());

        List<Map.Entry<Items, Integer>> allFishInInventory = currentInventory.entrySet().stream()
                .filter(entry -> entry.getKey() instanceof Fish) // Pastikan entry.getKey() tidak null
                .collect(Collectors.toList());

        // Sortir ikan (misalnya berdasarkan tipe atau harga jual, opsional)
        allFishInInventory.sort((e1, e2) -> {
            Fish f1 = (Fish) e1.getKey(); // Aman jika sudah difilter instanceof
            Fish f2 = (Fish) e2.getKey();
            // Contoh sortir sederhana, bisa disesuaikan
            return Integer.compare(f1.getSellprice(), f2.getSellprice());
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
            return new ArrayList<>(); // Kembalikan list kosong jika tidak cukup
        }
        return fishToConsume;
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static List<Recipe> getAvailableRecipes(Player playerModel) {
        if (!isInitialized) initialize();
        List<Recipe> available = new ArrayList<>();
        if (playerModel == null) return available;

        // Ambil statistik pemain jika diperlukan untuk unlock
        PlayerStats stats = playerModel.getPlayerStats();

        for (Recipe recipe : recipes.values()) {
            if (recipe.isUnlocked(stats)) {
                available.add(recipe);
            }
        }
        return available;
    }
}