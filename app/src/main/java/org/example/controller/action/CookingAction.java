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
    }

    @Override
    public String getActionName() {
        return "Start Cooking (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (recipeToCook == null || fuelToUse == null) {
            return false;
        }

        Player player = farm.getPlayerModel();
        if (player == null) {
            return false;
        }

        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats();

        if (farm.getActiveCookings() != null &&
            farm.getActiveCookings().stream().anyMatch(task -> task != null && !task.isClaimed())) {
            return false;
        }


        if (player.getEnergy() <= player.getMinEnergyOperational()) {
            return false;
        }


        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            return false;
        } else if (playerStats == null) {
           return false;
        }
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (requiredItem == null) {
                return false;
            }

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                long availableFishCount = inventory.getInventory().entrySet().stream()
                    .filter(invEntry -> invEntry.getKey() instanceof Fish) // Menggunakan instanceof Fish
                    .mapToInt(Map.Entry::getValue)
                    .sum();
                if (availableFishCount < requiredQuantity) {
                    return false;
                }
            } else {
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
                    return false;
                }
            }
        }

        if (!inventory.hasItem(fuelToUse, 1)) {
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            return;
        }

        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

        if (gameClock == null) {
            return;
        }



        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);


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
                player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
                return;
            }
        }


        if (!inventory.isRemoveInventory(fuelToUse, 1)) {
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            recipeToCook.getIngredients().forEach((itemKey, qty) -> {
                if (!RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(itemKey.getName())) {
                    inventory.addInventory(itemKey, qty); 
                }
            });
            return;
        }


        int dishesProduced = 1; 
        if (COAL_ITEM_NAME.equalsIgnoreCase(fuelToUse.getName())) {
            dishesProduced = 2; 
        } else {
            //System.out.println("LOG: Used Firewood/Other, producing " + dishesProduced + " dish.");
        }


        Food resultingDish = recipeToCook.getResultingDish();
        if (resultingDish != null) {
            CookingInProgress cookingTask = new CookingInProgress(resultingDish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask);
            //System.out.println("LOG: Cooking task added for " + resultingDish.getName() + " x" + dishesProduced);
        } else {
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            recipeToCook.getIngredients().forEach((itemKey, qty) -> {
                if (!RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(itemKey.getName())) {
                    inventory.addInventory(itemKey, qty);
                }
            });
        }
    }
}