// Lokasi: src/main/java/org/example/controller/action/CookingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Inventory;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase; // Untuk mendapatkan instance Recipe jika hanya ID yang diberikan
import org.example.model.GameClock;
// PlayerStats mungkin tidak langsung digunakan di sini jika unlock dicek di level yang lebih tinggi
import org.example.model.Items.Items;
import org.example.model.Items.ItemDatabase; // Untuk mendapatkan instance Fuel
import org.example.model.Items.Food;

import java.util.List;
import java.util.Map;

public class CookingAction implements Action {

    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    private static final int TIME_COST_PER_COOKING_SESSION_HOURS = 1;

    private Recipe recipeToCook;
    private Items fuelToUse;

    /**
     * Konstruktor untuk CookingAction.
     * @param recipeToCook Objek Recipe yang akan dimasak.
     * @param fuelToUse Objek Items yang akan digunakan sebagai bahan bakar (Firewood atau Coal).
     */
    public CookingAction(Recipe recipeToCook, Items fuelToUse) {
        this.recipeToCook = recipeToCook;
        this.fuelToUse = fuelToUse;

        // Inisialisasi jika belum (sebaiknya dilakukan sekali di awal game)
        if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
        if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();
    }

    /**
     * Konstruktor alternatif jika hanya ID resep dan nama fuel yang diketahui.
     * @param recipeId ID dari resep yang akan dimasak.
     * @param fuelName Nama bahan bakar ("Firewood" atau "Coal").
     */
    public CookingAction(String recipeId, String fuelName) {
        if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
        if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();

        this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
        this.fuelToUse = ItemDatabase.getItem(fuelName);

        if (this.recipeToCook == null) {
            System.err.println("CookingAction Error: Recipe with ID '" + recipeId + "' not found.");
            // Bisa throw exception jika mau
        }
        if (this.fuelToUse == null || !(this.fuelToUse.getName().equals("Firewood") || this.fuelToUse.getName().equals("Coal"))) {
            System.err.println("CookingAction Error: Fuel '" + fuelName + "' is not valid (must be Firewood or Coal).");
            this.fuelToUse = null; // Set null jika tidak valid
        }
    }


    @Override
    public String getActionName() {
        return "Cook (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (recipeToCook == null) {
            System.out.println("LOG: Cannot cook. No recipe has been set for the action.");
            return false;
        }
        if (fuelToUse == null) {
            System.out.println("LOG: Cannot cook. No fuel has been set for the action.");
            return false;
        }

        Player player = farm.getPlayer();
        if (player == null) {
            System.out.println("LOG: Player not found.");
            return false;
        }
        Inventory inventory = player.getInventory();

        // 1. Cek Energi Player
        if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
            System.out.println("LOG: Not enough energy to cook " + recipeToCook.getDisplayName() +
                               ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
            return false;
        }

        // 2. Cek Resep Sudah Unlocked (asumsi PlayerStats diakses dari Farm)
        //    Dalam model ini, asumsi unlock sudah dicek sebelum CookingAction dibuat/dikonfigurasi.
        //    Namun, bisa ditambahkan pengecekan di sini sebagai safety net jika perlu.
        // if (farm.getPlayerStats() != null && !recipeToCook.isUnlocked(farm.getPlayerStats())) {
        //     System.out.println("LOG: Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
        //     return false;
        // }


        // 3. Cek Bahan Baku
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            // Penanganan placeholder "Any Fish"
            if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                List<Items> fishToUse = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity);
                if (fishToUse.size() < requiredQuantity) {
                    System.out.println("LOG: Not enough fish in inventory for " + recipeToCook.getDisplayName() +
                                       ". Need " + requiredQuantity + " any fish.");
                    return false;
                }
            } else if (requiredItem != null) { // Bahan spesifik
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
                    System.out.println("LOG: Missing ingredient: " + requiredQuantity + "x " + requiredItem.getName() +
                                       " for " + recipeToCook.getDisplayName());
                    return false;
                }
            } else {
                System.err.println("Warning: Null ingredient found in recipe " + recipeToCook.getDisplayName());
                return false; // Resep tidak valid
            }
        }

        // 4. Cek Bahan Bakar
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG: Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        // Asumsi canExecute() sudah dipanggil dan true
        Player player = farm.getPlayer();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

        System.out.println(player.getName() + " is cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + "...");

        // 1. Kurangi Energi Pemain
        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT);

        // 2. Kurangi Bahan Baku dari Inventory
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                List<Items> fishToConsume = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity);
                for (Items fish : fishToConsume) {
                    inventory.removeInventory(fish, 1);
                    System.out.println("- Consumed 1x " + fish.getName());
                }
            } else if (requiredItem != null) {
                inventory.removeInventory(requiredItem, requiredQuantity);
                System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
            }
        }

        // 3. Kurangi Bahan Bakar
        inventory.removeInventory(fuelToUse, 1);
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");

        // 4. Tentukan berapa banyak makanan yang dihasilkan berdasarkan fuel
        int dishesProduced = 1; // Default untuk Firewood
        if (fuelToUse.getName().equals("Coal")) {
            dishesProduced = 2;
        }

        // 5. Tambahkan Hasil Masakan ke Inventory
        Food dish = recipeToCook.getResultingDish();
        inventory.addInventory(dish, dishesProduced);
        System.out.println("Successfully cooked " + dishesProduced + "x " + dish.getName() + "!");

        // 6. Majukan Waktu Game
        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_PER_COOKING_SESSION_HOURS * 60);
            System.out.println("Game time advanced by " + TIME_COST_PER_COOKING_SESSION_HOURS + " hour(s).");
        }
    }
}