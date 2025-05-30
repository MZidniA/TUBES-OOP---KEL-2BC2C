package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.Inventory;
import org.example.model.Recipe;
import org.example.model.GameClock;
<<<<<<< Updated upstream
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
=======
import org.example.model.Items.Items; // Item generik untuk bahan bakar
import org.example.model.Items.Food;  // Hasil masakan adalah Food
import org.example.model.Items.Fish;  // Untuk mengecek instance Fish
import org.example.model.CookingInProgress;
import org.example.model.RecipeDatabase; // Untuk placeholder ANY_FISH

>>>>>>> Stashed changes
import java.util.Map;
import java.util.List; // Untuk getFishIngredientsFromInventory jika masih dipakai

public class CookingAction implements Action {

<<<<<<< Updated upstream
    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    private static final int COOKING_DURATION_HOURS = 1;
    // Definisikan nama item fuel jika akan digunakan secara spesifik
    private static final String COAL_ITEM_NAME = "Coal"; 
    private static final String FIREWOOD_ITEM_NAME = "Firewood";

=======
    private static final int ENERGY_COST_INITIATION = 10;
    private static final int COOKING_DURATION_GAME_HOURS = 1;
>>>>>>> Stashed changes

    private final Recipe recipeToCook;
    private final Items fuelItem; // Bahan bakar yang dipilih pemain

<<<<<<< Updated upstream
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
=======
    /**
     * Konstruktor untuk CookingAction.
     * @param recipeToCook Resep yang akan dimasak.
     * @param fuelItem Item yang akan digunakan sebagai bahan bakar.
     */
    public CookingAction(Recipe recipeToCook, Items fuelItem) {
        this.recipeToCook = recipeToCook;
        this.fuelItem = fuelItem;
>>>>>>> Stashed changes
    }

    @Override
    public String getActionName() {
<<<<<<< Updated upstream
        return "Start Cooking (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
=======
        return "Cooking: " + (recipeToCook != null ? recipeToCook.getDisplayName() : "Unknown Recipe");
>>>>>>> Stashed changes
    }

    @Override
    public boolean canExecute(Farm farm) {
<<<<<<< Updated upstream
        if (recipeToCook == null || fuelToUse == null) {
            System.out.println("LOG (canExecute): Cannot cook. Recipe or Fuel not properly set for the action.");
=======
        if (recipeToCook == null || fuelItem == null || farm == null) {
            System.err.println("CookingAction Error: Recipe, Fuel, or Farm is null.");
>>>>>>> Stashed changes
            return false;
        }

        Player player = farm.getPlayerModel();
<<<<<<< Updated upstream
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
=======
        Inventory inventory = player.getInventory();

        if (player == null || inventory == null) {
            System.err.println("CookingAction Error: Player or Inventory is null.");
            return false;
        }

        // 1. Cek Energi Pemain
        if (player.getEnergy() < ENERGY_COST_INITIATION) {
            System.out.println("CookingAction: Not enough energy. Need " + ENERGY_COST_INITIATION + ", Has " + player.getEnergy());
            // Pesan UI akan ditangani oleh GameController/GameStateUI saat gagal
            return false;
        }

        // 2. Cek Resep Sudah Terbuka (isUnlocked sudah ada di Recipe.java)
        if (!recipeToCook.isUnlocked(farm.getPlayerStats())) {
            System.out.println("CookingAction: Recipe '" + recipeToCook.getDisplayName() + "' is not unlocked.");
            return false;
        }

        // 3. Cek Ketersediaan Bahan Baku
>>>>>>> Stashed changes
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredIngredient = entry.getKey();
            int requiredQuantity = entry.getValue();

<<<<<<< Updated upstream
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
=======
            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredIngredient.getName())) {
                // Cek jumlah total ikan di inventory
                long totalFishInInventory = inventory.getInventory().entrySet().stream()
                    .filter(invEntry -> invEntry.getKey() instanceof Fish)
                    .mapToLong(Map.Entry::getValue)
                    .sum();
                if (totalFishInInventory < requiredQuantity) {
                    System.out.println("CookingAction: Not enough 'Any Fish'. Need " + requiredQuantity + ", Have " + totalFishInInventory);
                    return false;
                }
            } else {
                if (!inventory.hasItem(requiredIngredient, requiredQuantity)) {
                    System.out.println("CookingAction: Missing ingredient " + requiredIngredient.getName() + ". Need " + requiredQuantity);
>>>>>>> Stashed changes
                    return false;
                }
            }
        }

<<<<<<< Updated upstream
        // Cek bahan bakar
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG (canExecute): Not enough " + fuelToUse.getName() + " to use as fuel.");
=======
        // 4. Cek Ketersediaan Bahan Bakar
        // Fuel bisa berupa "Firewood" (1x masak) atau "Coal" (2x masak)
        if (!inventory.hasItem(fuelItem, 1)) {
            System.out.println("CookingAction: Not enough fuel: " + fuelItem.getName());
>>>>>>> Stashed changes
            return false;
        }
        
        // (Spesifikasi tidak secara eksplisit menyebut kompor sibuk sebagai penghalang memulai,
        // tapi CookingInProgress akan ditambahkan ke list. Farm bisa saja memiliki batasan jumlah kompor aktif)

        return true;
    }

    @Override
    public void execute(Farm farm) {
<<<<<<< Updated upstream
        if (!canExecute(farm)) {
            System.out.println("LOG: Pre-condition for cooking not met in execute(). Action aborted for " +
                (recipeToCook != null ? recipeToCook.getDisplayName() : "unknown recipe") + ".");
            return;
        }

=======
        // Diasumsikan canExecute() sudah true
>>>>>>> Stashed changes
        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

<<<<<<< Updated upstream
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
=======
        // 1. Kurangi Energi Pemain
        player.decreaseEnergy(ENERGY_COST_INITIATION);

        // 2. Kurangi Bahan Baku dari Inventory
>>>>>>> Stashed changes
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredIngredient = entry.getKey();
            int requiredQuantity = entry.getValue();

<<<<<<< Updated upstream
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
=======
            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredIngredient.getName())) {
                // Logika untuk mengurangi ikan apa saja
                // Anda perlu metode di Inventory yang bisa mengurangi ikan generik
                // Contoh: inventory.removeAnyFishItems(requiredQuantity);
                // File Inventory.java Anda sudah punya removeAnyFish()
                List<Items> consumedFish = inventory.removeAnyFish(requiredQuantity);
                if (consumedFish.size() < requiredQuantity) {
                    // Seharusnya tidak terjadi jika canExecute() benar
                    System.err.println("CookingAction EXECUTE ERROR: Failed to consume enough 'Any Fish'. This should not happen.");
                    // Rollback energy (opsional, atau biarkan energi terpakai sebagai penalti)
                     player.increaseEnergy(ENERGY_COST_INITIATION); // Contoh rollback
                    return;
                }
            } else {
                if (!inventory.hasItem(requiredIngredient, requiredQuantity)) {
                     System.err.println("CookingAction EXECUTE ERROR: Failed to consume " + requiredIngredient.getName() + ". This should not happen.");
                     player.increaseEnergy(ENERGY_COST_INITIATION);
                     return;
                }
                else {
                    inventory.removeInventory(requiredIngredient, requiredQuantity); // Kurangi bahan baku
                }
            }
        }

        // 3. Kurangi Bahan Bakar dari Inventory
        if (!inventory.hasItem(fuelItem, 1)) {
             System.err.println("CookingAction EXECUTE ERROR: Failed to consume fuel " + fuelItem.getName() + ". This should not happen.");
             player.increaseEnergy(ENERGY_COST_INITIATION);
             // Rollback bahan baku (lebih kompleks, bisa diabaikan jika error ini jarang terjadi)
             return ;
        } else {
            inventory.removeInventory(fuelItem, 1); // Kurangi 1 bahan bakar
        }

        // 4. Tentukan Jumlah Hasil Masakan (berdasarkan fuel)
        int dishesProduced = 1;
        if ("Coal".equalsIgnoreCase(fuelItem.getName())) {
            dishesProduced = 2; // Coal memasak 2 makanan per fuel
        }

        // 5. Buat Task CookingInProgress
        Food dishToCreate = recipeToCook.getResultingDish();
        if (dishToCreate != null) {
            CookingInProgress newTask = new CookingInProgress(
                dishToCreate,
                dishesProduced,
                gameClock.getCurrentTime(), // Waktu mulai memasak
                COOKING_DURATION_GAME_HOURS
            );
            farm.addActiveCooking(newTask); // Tambahkan ke daftar masakan aktif di Farm
            System.out.println("CookingAction: Started cooking " + dishesProduced + "x " + dishToCreate.getName() + ". Will be ready in " + COOKING_DURATION_GAME_HOURS + " game hour(s).");
        } else {
            System.err.println("CookingAction ERROR: Resulting dish for recipe " + recipeToCook.getDisplayName() + " is null.");
            // Rollback energi dan bahan bakar
            player.increaseEnergy(ENERGY_COST_INITIATION);
            inventory.addInventory(fuelItem, 1);
            // Rollback bahan baku (lebih kompleks)
        }
        
        // Waktu game akan terus berjalan. Proses memasak bersifat pasif.
        // Pemain bisa melakukan hal lain.
        // Farm.updateCookingProgress() akan menangani saat masakan selesai.
>>>>>>> Stashed changes
    }
}