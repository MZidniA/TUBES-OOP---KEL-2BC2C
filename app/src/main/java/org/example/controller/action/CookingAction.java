// Lokasi: src/main/java/org/example/controller/action/CookingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Inventory;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase;
import org.example.model.GameClock;
import org.example.model.Items.Items;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Food; // Pastikan Fish tidak diimport jika tidak digunakan langsung di sini
import org.example.model.Items.Fish; // Import Fish jika menggunakan instanceof Fish
import org.example.model.CookingInProgress;
import org.example.model.PlayerStats;

import java.util.List;
import java.util.Map;

public class CookingAction implements Action {

    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    private static final int COOKING_DURATION_HOURS = 1;
    private static final String COAL_ITEM_NAME = "Coal";
    // private static final String FIREWOOD_ITEM_NAME = "Firewood"; // Bisa disimpan jika ada logika lain

    private Recipe recipeToCook;
    private Items fuelToUse;

    public CookingAction(Recipe recipeToCook, Items fuelToUse) {
        this.recipeToCook = recipeToCook;
        this.fuelToUse = fuelToUse;
    }

    public CookingAction(String recipeId, String fuelItemId) {
        this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
        this.fuelToUse = ItemDatabase.getItem(fuelItemId);

        if (this.recipeToCook == null) {
            System.err.println("ERROR (CookingAction Constructor): Recipe with ID '" + recipeId + "' not found.");
        }
        if (this.fuelToUse == null) {
            System.err.println("ERROR (CookingAction Constructor): Fuel item with ID/name '" + fuelItemId + "' not found.");
        }
    }

    @Override
    public String getActionName() {
        return "Start Cooking (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (recipeToCook == null || fuelToUse == null) {
            System.out.println("LOG (canExecute): Cannot cook. Recipe or Fuel not properly set for the action.");
            return false;
        }

        Player player = farm.getPlayerModel();
        if (player == null) {
            System.err.println("ERROR (canExecute): Player object is null in Farm.");
            return false;
        }

        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats();

        // Pemeriksaan Kompor Sibuk
        if (farm.getActiveCookings() != null &&
            farm.getActiveCookings().stream().anyMatch(task -> task != null && !task.isClaimed())) {
            System.out.println("LOG (canExecute): Stove is currently busy.");
            return false;
        }

        // Cek Energi Player
        if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
            System.out.println("LOG (canExecute): Not enough energy to cook " + recipeToCook.getDisplayName() +
                               ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
            return false;
        }

        // Cek Resep Sudah Unlocked
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            System.out.println("LOG (canExecute): Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
        } else if (playerStats == null) {
            System.err.println("WARNING (canExecute): PlayerStats is null in Farm. Cannot check recipe unlock status for " + recipeToCook.getDisplayName() + ". Assuming unlocked for now, but this might be an issue.");
            // Jika PlayerStats wajib, maka return false;
        }

        // Cek Bahan Baku
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (requiredItem == null) {
                System.err.println("ERROR (canExecute): Recipe " + recipeToCook.getDisplayName() + " has a null ingredient defined.");
                return false;
            }

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                long availableFishCount = inventory.getInventory().entrySet().stream()
                    .filter(invEntry -> invEntry.getKey() instanceof Fish) // Menggunakan instanceof Fish
                    .mapToInt(Map.Entry::getValue)
                    .sum();
                if (availableFishCount < requiredQuantity) {
                    System.out.println("LOG (canExecute): Not enough fish for " + recipeToCook.getDisplayName() + ". Need " + requiredQuantity + ", have " + availableFishCount + ".");
                    return false;
                }
            } else {
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
                    System.out.println("LOG (canExecute): Missing " + requiredQuantity + "x " + requiredItem.getName() + " for " + recipeToCook.getDisplayName());
                    return false;
                }
            }
        }

        // Cek Bahan Bakar
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG (canExecute): Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            System.out.println("LOG (execute): Pre-condition for cooking (checked again) not met. Action aborted for " + (recipeToCook != null ? recipeToCook.getDisplayName() : "unknown recipe") + ".");
            return;
        }

        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

        if (gameClock == null) {
            System.err.println("ERROR (execute): GameClock is null in Farm. Cannot start cooking task.");
            return;
        }

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + ".");

        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT + ". Remaining: " + player.getEnergy());

        // Kurangi Bahan Baku
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItemObject = entry.getKey(); // Ini adalah objek item dari definisi resep
            int requiredQuantity = entry.getValue();
            boolean consumedSuccessfully;

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItemObject.getName())) {
                List<Items> fishConsumed = inventory.removeAnyFish(requiredQuantity);
                consumedSuccessfully = (fishConsumed.size() == requiredQuantity);
                if (consumedSuccessfully) {
                    fishConsumed.forEach(fish -> System.out.println("- Consumed 1x " + fish.getName() + " (as AnyFish)"));
                }
            } else {
                consumedSuccessfully = inventory.isRemoveInventory(requiredItemObject, requiredQuantity);
                if (consumedSuccessfully) {
                    System.out.println("- Consumed " + requiredQuantity + "x " + requiredItemObject.getName());
                }
            }

            if (!consumedSuccessfully) {
                System.err.println("ERROR (execute): Failed to consume ingredient " + requiredItemObject.getName() + ". Rolling back energy.");
                player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
                return;
            }
        }

        // Kurangi Bahan Bakar
        if (!inventory.isRemoveInventory(fuelToUse, 1)) {
            System.err.println("ERROR (execute): Failed to consume fuel " + fuelToUse.getName() + ". Rolling back energy & (attempting) ingredients.");
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            // Rollback ingredients (ini bisa kompleks dan mungkin tidak sempurna, terutama untuk "AnyFish")
            recipeToCook.getIngredients().forEach((itemKey, qty) -> {
                if (!RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(itemKey.getName())) {
                    inventory.addInventory(itemKey, qty); // Hanya item non-AnyFish yang mudah dikembalikan
                }
            });
            return;
        }
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");

        // Tentukan jumlah hidangan yang dihasilkan (Efisiensi Coal)
        int dishesProduced = 1;
        if (fuelToUse != null && COAL_ITEM_NAME.equalsIgnoreCase(fuelToUse.getName())) {
            dishesProduced = 2;
            System.out.println("LOG (execute): Using " + COAL_ITEM_NAME + ". Producing " + dishesProduced + " dishes.");
        }

        Food resultingDish = recipeToCook.getResultingDish();
        if (resultingDish != null) {
            CookingInProgress cookingTask = new CookingInProgress(resultingDish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask);
            System.out.println("LOG (execute): Cooking task for " + dishesProduced + "x " + resultingDish.getName() + " added. Will be ready in " + COOKING_DURATION_HOURS + " game hour(s).");
        } else {
            System.err.println("ERROR (execute): Resulting dish is null for recipe " + recipeToCook.getDisplayName() + ". Rolling back fuel & energy.");
            inventory.addInventory(fuelToUse, 1); // Rollback fuel
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            // Rollback ingredients
            recipeToCook.getIngredients().forEach((itemKey, qty) -> {
                 if (!RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(itemKey.getName())) {
                    inventory.addInventory(itemKey, qty);
                }
            });
        }
    }
}