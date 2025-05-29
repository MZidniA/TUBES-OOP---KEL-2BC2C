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
import org.example.model.Items.Food;
import org.example.model.CookingInProgress; // IMPORT BARU
import org.example.model.PlayerStats;      // IMPORT BARU (untuk unlock)


import java.util.List;
import java.util.Map;

public class CookingAction implements Action {

    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    // TIME_COST_PER_COOKING_SESSION_HOURS sekarang adalah DURASI, bukan time skip
    private static final int COOKING_DURATION_HOURS = 1;

    private Recipe recipeToCook;
    private Items fuelToUse;

    public CookingAction(Recipe recipeToCook, Items fuelToUse) {
        // ... (konstruktor sama) ...
        this.recipeToCook = recipeToCook;
        this.fuelToUse = fuelToUse;
        if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
        if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();
    }

    public CookingAction(String recipeId, String fuelName) {
        // ... (konstruktor sama) ...
        if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
        if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();
        this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
        this.fuelToUse = ItemDatabase.getItem(fuelName);
        // ... (validasi null sama) ...
    }

    @Override
    public String getActionName() {
        return "Start Cooking (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe") + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (recipeToCook == null) {
            //System.out.println("LOG: Cannot cook. No recipe has been set.");
            return false;
        }
        if (fuelToUse == null) {
            //System.out.println("LOG: Cannot cook. No fuel has been set.");
            return false;
        }

        Player player = farm.getPlayerModel();
        if (player == null) {
            System.out.println("LOG: Player not found.");
            return false;
        }
        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats(); 

        if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
            System.out.println("LOG: Not enough energy to cook " + recipeToCook.getDisplayName() +
                               ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
            return false;
        }

     
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            System.out.println("LOG: Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
        } else if (playerStats == null) {
            System.err.println("WARNING: PlayerStats is null in Farm, cannot check recipe unlock status for " + recipeToCook.getDisplayName());
            return false;
        }


        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                List<Items> fishToUse = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity);
                if (fishToUse.size() < requiredQuantity) {
                    System.out.println("LOG: Not enough fish in inventory for " + recipeToCook.getDisplayName() +
                                       ". Need " + requiredQuantity + " any fish.");
                    return false;
                }
            } else if (requiredItem != null) {
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
                    System.out.println("LOG: Missing ingredient: " + requiredQuantity + "x " + requiredItem.getName() + " for " + recipeToCook.getDisplayName());
                    return false;
                }
            } else { 
                 return false;}
        }


        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG: Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        
        if (!canExecute(farm)) { 
             System.out.println("LOG: Pre-condition for cooking not met in execute(). Action aborted.");
             return;
        }

        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock(); 

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + ". It will take " + COOKING_DURATION_HOURS + " hour(s).");

        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT);

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
                inventory.removeInventory(requiredItem, requiredQuantity); // Menggunakan removeInventory
                System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
            }
        }

 
        inventory.removeInventory(fuelToUse, 1); // Menggunakan removeInventory
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");


        int dishesProduced = 1;
     
        Food dish = recipeToCook.getResultingDish();
        if (dish != null && gameClock != null) {
            CookingInProgress cookingTask = new CookingInProgress(dish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask); // Tambahkan ke list di Farm
        } else {
            System.err.println("ERROR: Resulting dish or gameClock is null. Cannot start cooking task.");
        }

    }
}