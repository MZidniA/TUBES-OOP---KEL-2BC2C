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
>>>>>>> Stashed changes
import org.example.model.CookingInProgress;
import org.example.model.PlayerStats;

import java.util.List;
import java.util.Map;

public class CookingAction implements Action {

    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    private static final int COOKING_DURATION_HOURS = 1;
<<<<<<< Updated upstream
    // Definisikan nama item fuel jika akan digunakan secara spesifik
    private static final String COAL_ITEM_NAME = "Coal"; 
    private static final String FIREWOOD_ITEM_NAME = "Firewood";

=======
    private static final String COAL_ITEM_NAME = "Coal";
    // private static final String FIREWOOD_ITEM_NAME = "Firewood"; // Bisa disimpan jika ada logika lain
>>>>>>> Stashed changes

    private Recipe recipeToCook;
    private Items fuelToUse;

    public CookingAction(Recipe recipeToCook, Items fuelToUse) {
        this.recipeToCook = recipeToCook;
        this.fuelToUse = fuelToUse;
<<<<<<< Updated upstream
        // Inisialisasi database sebaiknya dilakukan sekali di awal permainan, bukan di setiap pembuatan aksi
        // if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
        // if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();
    }

    // Konstruktor alternatif jika diperlukan (misalnya dari ID)
    public CookingAction(String recipeId, String fuelItemId) {
        // Pastikan RecipeDatabase dan ItemDatabase sudah diinisialisasi sebelumnya
        this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
        this.fuelToUse = ItemDatabase.getItem(fuelItemId); // Asumsi getItem() ada atau getItemById()

        if (this.recipeToCook == null) {
            System.err.println("ERROR: Recipe with ID '" + recipeId + "' not found.");
        }
        if (this.fuelToUse == null) {
            System.err.println("ERROR: Fuel item with ID/name '" + fuelItemId + "' not found.");
=======
    }

    public CookingAction(String recipeId, String fuelItemId) {
        this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
        this.fuelToUse = ItemDatabase.getItem(fuelItemId);

        if (this.recipeToCook == null) {
            System.err.println("ERROR (CookingAction Constructor): Recipe with ID '" + recipeId + "' not found.");
        }
        if (this.fuelToUse == null) {
            System.err.println("ERROR (CookingAction Constructor): Fuel item with ID/name '" + fuelItemId + "' not found.");
>>>>>>> Stashed changes
        }
    }

    @Override
    public String getActionName() {
        return "Start Cooking (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
<<<<<<< Updated upstream
        if (recipeToCook == null) {
            System.out.println("LOG: Cannot cook. No recipe has been set for the action.");
            return false;
        }
        if (fuelToUse == null) {
            System.out.println("LOG: Cannot cook. No fuel has been set for the action.");
=======
        if (recipeToCook == null || fuelToUse == null) {
            System.out.println("LOG (canExecute): Cannot cook. Recipe or Fuel not properly set for the action.");
>>>>>>> Stashed changes
            return false;
        }

        Player player = farm.getPlayerModel();
        if (player == null) {
<<<<<<< Updated upstream
            System.err.println("ERROR: Player object is null in Farm. Cannot execute cooking action.");
            return false;
        }

        // --- SARAN 1: Pemeriksaan Lokasi ---
        // Asumsi: Anda memiliki cara untuk mendapatkan lokasi pemain dan ID kompor.
        // Ini adalah contoh yang disederhanakan. Anda perlu menyesuaikannya dengan implementasi map & objek Anda.
        // Misalnya, jika Anda memiliki sistem map dan objek interaktif:
        // GameMap currentMap = farm.getCurrentMap(); // Asumsi ada getter ini
        // String playerLocation = player.getCurrentLocationName(); // Asumsi ada getter ini
        // boolean atStove = false;
        // if ("Player's House".equalsIgnoreCase(playerLocation) && currentMap != null) {
        //     // Logika untuk memeriksa apakah pemain bersebelahan dengan objek STOVE_ID
        //     // Misalnya, menggunakan InteractionHelper seperti kode teman Anda, atau logika custom.
        //     // String adjacentObjectId = InteractionHelper.getAdjacentInteractableObject(player, currentMap);
        //     // if (PlayerHouseMap.STOVE_ID.equals(adjacentObjectId)) { // Asumsi STOVE_ID adalah konstanta
        //     //     atStove = true;
        //     // }
        //     // Untuk sementara, kita anggap jika di rumah sudah cukup, TAPI idealnya cek kompor.
        //     atStove = true; // HAPUS ATAU GANTI INI DENGAN LOGIKA CEK KOMPOR YANG BENAR
        // }
        // if (!atStove) {
        //     System.out.println("LOG: Cooking can only be done at the stove in your house.");
        //     return false;
        // }
        // Catatan: Untuk implementasi yang lebih baik, pemeriksaan interaksi dengan kompor
        // mungkin lebih cocok dilakukan oleh sistem UI/Controller sebelum membuat CookingAction.
        // Namun, jika ingin aksi ini lebih mandiri, logika di atas bisa diadaptasi.
        // Untuk sekarang, kita lanjutkan tanpa cek lokasi internal di sini, asumsi pemanggil sudah validasi.


        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats();
        if (playerStats == null) {
            System.err.println("WARNING: PlayerStats is null in Farm. Recipe unlock status and other stats-based checks cannot be performed for " + recipeToCook.getDisplayName());
            // Anda mungkin ingin return false di sini jika PlayerStats wajib ada untuk semua validasi
            // return false;
        }

        // --- SARAN 5: Pemeriksaan Kompor Sibuk ---
        if (farm.getActiveCookings() != null && !farm.getActiveCookings().isEmpty()) {
            // Cek apakah ada masakan yang sedang diproses (belum selesai atau belum diklaim)
            boolean isStoveActuallyBusy = farm.getActiveCookings().stream()
                                             .anyMatch(task -> task != null && !task.isClaimed()); // Asumsi isClaimed() ada di CookingInProgress
            if (isStoveActuallyBusy) {
               System.out.println("LOG: Stove is currently busy. Wait for the current dish to finish or be claimed.");
               return false;
            }
        }

        // 1. Cek Energi Player
=======
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
>>>>>>> Stashed changes
        if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
            System.out.println("LOG (canExecute): Not enough energy to cook " + recipeToCook.getDisplayName() +
                               ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
            return false;
        }

<<<<<<< Updated upstream
        // 2. Cek Resep Sudah Unlocked
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) { // Asumsi isUnlocked ada di Recipe
            System.out.println("LOG: Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
=======
        // Cek Resep Sudah Unlocked
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            System.out.println("LOG (canExecute): Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
        } else if (playerStats == null) {
            System.err.println("WARNING (canExecute): PlayerStats is null in Farm. Cannot check recipe unlock status for " + recipeToCook.getDisplayName() + ". Assuming unlocked for now, but this might be an issue.");
            // Jika PlayerStats wajib, maka return false;
>>>>>>> Stashed changes
        }
        // Tidak perlu 'else if (playerStats == null)' di sini karena sudah ditangani di atas.

<<<<<<< Updated upstream
        // 3. Cek Bahan Baku
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (requiredItem == null) {
                System.err.println("ERROR: Recipe " + recipeToCook.getDisplayName() + " has a null ingredient defined.");
                return false; // Resep tidak valid
            }

            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                // Menggunakan metode yang sudah ada untuk menghitung ikan yang tersedia
                long availableFishCount = inventory.getInventory().entrySet().stream()
                    .filter(invEntry -> invEntry.getKey() instanceof Food && ((Food)invEntry.getKey()).isFish()) // Asumsi Food punya isFish()
                    .mapToInt(Map.Entry::getValue)
                    .sum();
                if (availableFishCount < requiredQuantity) {
                    System.out.println("LOG: Not enough fish in inventory for " + recipeToCook.getDisplayName() +
                                       ". Need " + requiredQuantity + " any fish, have " + availableFishCount + ".");
=======
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
>>>>>>> Stashed changes
                    return false;
                }
            } else {
                if (!inventory.hasItem(requiredItem, requiredQuantity)) {
<<<<<<< Updated upstream
                    System.out.println("LOG: Missing ingredient: " + requiredQuantity + "x " + requiredItem.getName() +
                                       " for " + recipeToCook.getDisplayName());
=======
                    System.out.println("LOG (canExecute): Missing " + requiredQuantity + "x " + requiredItem.getName() + " for " + recipeToCook.getDisplayName());
>>>>>>> Stashed changes
                    return false;
                }
            }
        }

<<<<<<< Updated upstream
        // 4. Cek Bahan Bakar
=======
        // Cek Bahan Bakar
>>>>>>> Stashed changes
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG (canExecute): Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
<<<<<<< Updated upstream
             System.out.println("LOG: Pre-condition for cooking not met in execute(). Action aborted for " + (recipeToCook != null ? recipeToCook.getDisplayName() : "unknown recipe") + ".");
             return;
=======
            System.out.println("LOG (execute): Pre-condition for cooking (checked again) not met. Action aborted for " + (recipeToCook != null ? recipeToCook.getDisplayName() : "unknown recipe") + ".");
            return;
>>>>>>> Stashed changes
        }

        Player player = farm.getPlayerModel();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock();

        if (gameClock == null) {
<<<<<<< Updated upstream
            System.err.println("ERROR: GameClock is null in Farm. Cannot start cooking task with proper timing.");
            return;
        }

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + ". It will take " + COOKING_DURATION_HOURS + " game hour(s).");

        // 1. Kurangi Energi Pemain
        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT); // Asumsi decreaseEnergy ada di Player
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT + ". Remaining: " + player.getEnergy());

        // 2. Kurangi Bahan Baku dari Inventory
=======
            System.err.println("ERROR (execute): GameClock is null in Farm. Cannot start cooking task.");
            return;
        }

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + ".");

        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT + ". Remaining: " + player.getEnergy());

        // Kurangi Bahan Baku
>>>>>>> Stashed changes
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItemObject = entry.getKey(); // Ini adalah objek item dari definisi resep
            int requiredQuantity = entry.getValue();
<<<<<<< Updated upstream
            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                // Ambil ikan dari inventory untuk dikonsumsi.
                // Ini memerlukan logika untuk memilih ikan mana yang akan dikonsumsi jika ada beberapa jenis.
                // Untuk sederhana, kita ambil ikan pertama yang ditemukan sejumlah yang dibutuhkan.
                List<Items> fishConsumed = inventory.removeAnyFish(requiredQuantity); // Asumsi ada removeAnyFish di Inventory
                if (fishConsumed.size() == requiredQuantity) {
                    for(Items fish : fishConsumed){
                        System.out.println("- Consumed 1x " + fish.getName());
                    }
                } else {
                     System.err.println("ERROR: Failed to consume " + requiredQuantity + " fish, only " + fishConsumed.size() + " were removed. This should have been caught by canExecute.");
                     // Rollback energi jika perlu, atau batalkan aksi
                     player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT); // Contoh rollback
                     return;
                }
            } else {
                inventory.removeInventory(requiredItem, requiredQuantity); // Asumsi removeInventory void
                System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
            }
        }

        // 3. Kurangi Bahan Bakar
        inventory.removeInventory(fuelToUse, 1);
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");


        // 4. Tentukan jumlah hidangan yang dihasilkan
        int dishesProduced = 1; // Default
        // --- SARAN 2: Logika Efisiensi Coal ---
        // Jika "1 arang bisa masak 2 makanan" berarti 1 Coal menghasilkan 2 porsi dalam satu sesi masak:
        // if (fuelToUse != null && COAL_ITEM_NAME.equalsIgnoreCase(fuelToUse.getName())) {
        //     dishesProduced = 2;
        //     System.out.println("LOG: Using " + COAL_ITEM_NAME + ". Producing " + dishesProduced + " dishes.");
        // }
        // Jika interpretasinya beda (1 Coal untuk 2x aksi terpisah), logika ada di manajemen item Coal, bukan di sini.

        // 5. Buat Tugas Memasak Pasif
        Food dish = recipeToCook.getResultingDish(); // Asumsi getResultingDish() ada di Recipe
        if (dish != null) {
            CookingInProgress cookingTask = new CookingInProgress(dish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask); // Asumsi addActiveCooking ada di Farm
            System.out.println("LOG: Cooking task for " + dishesProduced + "x " + dish.getName() + " added. Will be ready in " + COOKING_DURATION_HOURS + " game hour(s).");
        } else {
            System.err.println("ERROR: Resulting dish is null for recipe " + recipeToCook.getDisplayName() + ". Cannot start cooking task.");
            // Rollback bahan bakar & energi
            inventory.addInventory(fuelToUse, 1); // Contoh rollback fuel
            player.increaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
            // Pertimbangkan rollback bahan baku juga jika implementasi memungkinkan
        }
        // Waktu akan berjalan normal, dan Farm akan menangani penyelesaian task.
=======
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
>>>>>>> Stashed changes
    }
}