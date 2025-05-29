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
<<<<<<< Updated upstream
import org.example.model.Items.Food;
=======
import org.example.model.Items.Food; // Pastikan Fish tidak diimport jika tidak digunakan langsung di sini
import org.example.model.Items.Fish; // Import Fish jika menggunakan instanceof Fish
import org.example.model.CookingInProgress;
import org.example.model.PlayerStats;

import java.util.List;
import java.util.Map;

public class CookingAction implements Action {

    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    private static final int COOKING_DURATION_HOURS = 1;
    // Definisikan nama item fuel jika akan digunakan secara spesifik
    private static final String COAL_ITEM_NAME = "Coal"; 
    private static final String FIREWOOD_ITEM_NAME = "Firewood";


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
            System.err.println("ERROR: Player object is null in Farm. Cannot execute cooking action.");
            return false;
        }

        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats();

        // Cek kompor sibuk
        if (farm.getActiveCookings() != null &&
            farm.getActiveCookings().stream().anyMatch(task -> task != null && !task.isClaimed())) {
            System.out.println("LOG (canExecute): Stove is currently busy.");
            return false;
        }

        // Cek energi
        if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
            System.out.println("LOG (canExecute): Not enough energy to cook " + recipeToCook.getDisplayName() +
                               ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
            return false;
        }

        // Cek resep sudah unlocked
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            System.out.println("LOG (canExecute): Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
        } else if (playerStats == null) {
            System.err.println("WARNING (canExecute): PlayerStats is null in Farm. Cannot check recipe unlock status for " + recipeToCook.getDisplayName());
            return false;
        }

        // Cek bahan baku
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (requiredItem == null) {
                System.err.println("ERROR (canExecute): Recipe " + recipeToCook.getDisplayName() + " has a null ingredient defined.");
                return false;
            }

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {

                long availableFishCount = inventory.getInventory().entrySet().stream()
                    .filter(invEntry -> invEntry.getKey() instanceof Fish)
                    .mapToInt(Map.Entry::getValue)
                    .sum();

                if (availableFishCount < requiredQuantity) {
                    System.out.println("LOG (canExecute): Not enough fish for " + recipeToCook.getDisplayName() +
                                       ". Need " + requiredQuantity + ", have " + availableFishCount + ".");
                    return false;
                }
            } else {
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
                    System.out.println("LOG (canExecute): Missing " + requiredQuantity + "x " + requiredItem.getName() +
                                       " for " + recipeToCook.getDisplayName());
                    return false;
                }
            }
        }

        // Cek bahan bakar
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG (canExecute): Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            System.out.println("LOG: Pre-condition for cooking not met in execute(). Action aborted for " +
                (recipeToCook != null ? recipeToCook.getDisplayName() : "unknown recipe") + ".");
            return;
        }

        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

        if (gameClock == null) {
            System.err.println("ERROR: GameClock is null in Farm. Cannot start cooking task with proper timing.");
            return;
        }

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() +
            " using " + fuelToUse.getName() + ". It will take " + COOKING_DURATION_HOURS + " game hour(s).");

        // 1. Kurangi energi pemain
        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT + ". Remaining: " + player.getEnergy());

        // 2. Kurangi bahan baku dari inventory
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                List<Items> fishConsumed = inventory.removeAnyFish(requiredQuantity);
                if (fishConsumed.size() == requiredQuantity) {
                    for (Items fish : fishConsumed) {
                        System.out.println("- Consumed 1x " + fish.getName());
                    }
                } else {
                    System.err.println("ERROR: Failed to consume " + requiredQuantity + " fish, only " +
                        fishConsumed.size() + " were removed. This should have been caught by canExecute.");
                    player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT); // Rollback energi
                    return;
                }
            } else {
                inventory.removeInventory(requiredItem, requiredQuantity);
                System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
            }
        }

        // 3. Kurangi bahan bakar
        inventory.removeInventory(fuelToUse, 1);
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");

        // 4. Tentukan jumlah hidangan yang dihasilkan
        int dishesProduced = 1;
        // Opsi: Jika 1 Coal menghasilkan 2 dish, aktifkan logika berikut:
        if (fuelToUse != null && COAL_ITEM_NAME.equalsIgnoreCase(fuelToUse.getName())) {
            dishesProduced = 2;
            System.out.println("LOG: Using " + COAL_ITEM_NAME + ". Producing " + dishesProduced + " dishes.");
        }

        // 5. Buat CookingInProgress
        Food dish = recipeToCook.getResultingDish();
        if (dish != null) {
            CookingInProgress cookingTask = new CookingInProgress(dish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask);
            System.out.println("LOG: Cooking task for " + dishesProduced + "x " + dish.getName() +
                " added. Will be ready in " + COOKING_DURATION_HOURS + " game hour(s).");
        } else {
            System.err.println("ERROR: Resulting dish is null for recipe " + recipeToCook.getDisplayName() + ". Cannot start cooking task.");
            // Rollback bahan bakar & energi
            inventory.addInventory(fuelToUse, 1);
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            // Rollback bahan baku jika perlu
        }
    }
}