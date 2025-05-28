// // Lokasi: src/main/java/org/example/controller/action/CookingAction.java
// package org.example.controller.action;

<<<<<<< HEAD
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

=======
// import org.example.model.Farm;
// import org.example.model.Player;
// import org.example.model.Inventory;
// import org.example.model.Recipe;
// import org.example.model.RecipeDatabase; // Untuk mendapatkan instance Recipe jika hanya ID yang diberikan
// import org.example.model.GameClock;
// // PlayerStats mungkin tidak langsung digunakan di sini jika unlock dicek di level yang lebih tinggi
// import org.example.model.Items.Items;
// import org.example.model.Items.ItemDatabase; // Untuk mendapatkan instance Fuel
// import org.example.model.Items.Food;
>>>>>>> main

// import java.util.List;
// import java.util.Map;

// public class CookingAction implements Action {

<<<<<<< HEAD
    private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
    // TIME_COST_PER_COOKING_SESSION_HOURS sekarang adalah DURASI, bukan time skip
    private static final int COOKING_DURATION_HOURS = 1;
=======
//     private static final int ENERGY_COST_PER_COOKING_ATTEMPT = 10;
//     private static final int TIME_COST_PER_COOKING_SESSION_HOURS = 1;
>>>>>>> main

//     private Recipe recipeToCook;
//     private Items fuelToUse;

<<<<<<< HEAD
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
            System.out.println("LOG: Cannot cook. No recipe has been set.");
            return false;
        }
        if (fuelToUse == null) {
            System.out.println("LOG: Cannot cook. No fuel has been set.");
            return false;
        }

        Player player = farm.getPlayer();
        if (player == null) {
            System.out.println("LOG: Player not found.");
            return false;
        }
        Inventory inventory = player.getInventory();
        PlayerStats playerStats = farm.getPlayerStats(); // Ambil PlayerStats
=======
//     /**
//      * Konstruktor untuk CookingAction.
//      * @param recipeToCook Objek Recipe yang akan dimasak.
//      * @param fuelToUse Objek Items yang akan digunakan sebagai bahan bakar (Firewood atau Coal).
//      */
//     public CookingAction(Recipe recipeToCook, Items fuelToUse) {
//         this.recipeToCook = recipeToCook;
//         this.fuelToUse = fuelToUse;

//         // Inisialisasi jika belum (sebaiknya dilakukan sekali di awal game)
//         if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
//         if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();
//     }

//     /**
//      * Konstruktor alternatif jika hanya ID resep dan nama fuel yang diketahui.
//      * @param recipeId ID dari resep yang akan dimasak.
//      * @param fuelName Nama bahan bakar ("Firewood" atau "Coal").
//      */
//     public CookingAction(String recipeId, String fuelName) {
//         if (!RecipeDatabase.isInitialized()) RecipeDatabase.initialize();
//         if (!ItemDatabase.isInitialized()) ItemDatabase.initialize();

//         this.recipeToCook = RecipeDatabase.getRecipeById(recipeId);
//         this.fuelToUse = ItemDatabase.getItem(fuelName);

//         if (this.recipeToCook == null) {
//             System.err.println("CookingAction Error: Recipe with ID '" + recipeId + "' not found.");
//             // Bisa throw exception jika mau
//         }
//         if (this.fuelToUse == null || !(this.fuelToUse.getName().equals("Firewood") || this.fuelToUse.getName().equals("Coal"))) {
//             System.err.println("CookingAction Error: Fuel '" + fuelName + "' is not valid (must be Firewood or Coal).");
//             this.fuelToUse = null; // Set null jika tidak valid
//         }
//     }


//     @Override
//     public String getActionName() {
//         return "Cook (" + (recipeToCook != null ? recipeToCook.getDisplayName() : "No Recipe Selected") + ")";
//     }

//     @Override
//     public boolean canExecute(Farm farm) {
//         if (recipeToCook == null) {
//             System.out.println("LOG: Cannot cook. No recipe has been set for the action.");
//             return false;
//         }
//         if (fuelToUse == null) {
//             System.out.println("LOG: Cannot cook. No fuel has been set for the action.");
//             return false;
//         }

//         Player player = farm.getPlayer();
//         if (player == null) {
//             System.out.println("LOG: Player not found.");
//             return false;
//         }
//         Inventory inventory = player.getInventory();
>>>>>>> main

//         // 1. Cek Energi Player
//         if (player.getEnergy() < ENERGY_COST_PER_COOKING_ATTEMPT) {
//             System.out.println("LOG: Not enough energy to cook " + recipeToCook.getDisplayName() +
//                                ". Need " + ENERGY_COST_PER_COOKING_ATTEMPT + ", has " + player.getEnergy() + ".");
//             return false;
//         }

<<<<<<< HEAD
        // 2. Cek Resep Sudah Unlocked (BARU DIAKTIFKAN)
        if (playerStats != null && !recipeToCook.isUnlocked(playerStats)) {
            System.out.println("LOG: Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
            return false;
        } else if (playerStats == null) {
            System.err.println("WARNING: PlayerStats is null in Farm, cannot check recipe unlock status for " + recipeToCook.getDisplayName());
            // Anda bisa memutuskan apakah ini kondisi gagal atau lanjut dengan asumsi unlocked
            // return false; // Jika PlayerStats wajib ada
        }


        // 3. Cek Bahan Baku (Sama seperti sebelumnya)
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
                    System.out.println("LOG: Missing ingredient: " + requiredQuantity + "x " + requiredItem.getName() +
                                       " for " + recipeToCook.getDisplayName());
                    return false;
                }
            } else { /* ... (penanganan error null ingredient) ... */ return false;}
        }

        // 4. Cek Bahan Bakar (Sama seperti sebelumnya)
        if (!inventory.hasItem(fuelToUse, 1)) {
            System.out.println("LOG: Not enough " + fuelToUse.getName() + " to use as fuel.");
            return false;
        }
=======
//         // 2. Cek Resep Sudah Unlocked (asumsi PlayerStats diakses dari Farm)
//         //    Dalam model ini, asumsi unlock sudah dicek sebelum CookingAction dibuat/dikonfigurasi.
//         //    Namun, bisa ditambahkan pengecekan di sini sebagai safety net jika perlu.
//         // if (farm.getPlayerStats() != null && !recipeToCook.isUnlocked(farm.getPlayerStats())) {
//         //     System.out.println("LOG: Recipe " + recipeToCook.getDisplayName() + " is not unlocked yet.");
//         //     return false;
//         // }


//         // 3. Cek Bahan Baku
//         for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
//             Items requiredItem = entry.getKey();
//             int requiredQuantity = entry.getValue();

//             // Penanganan placeholder "Any Fish"
//             if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
//                 List<Items> fishToUse = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity);
//                 if (fishToUse.size() < requiredQuantity) {
//                     System.out.println("LOG: Not enough fish in inventory for " + recipeToCook.getDisplayName() +
//                                        ". Need " + requiredQuantity + " any fish.");
//                     return false;
//                 }
//             } else if (requiredItem != null) { // Bahan spesifik
//                 if (!inventory.hasItem(requiredItem, requiredQuantity)) {
//                     System.out.println("LOG: Missing ingredient: " + requiredQuantity + "x " + requiredItem.getName() +
//                                        " for " + recipeToCook.getDisplayName());
//                     return false;
//                 }
//             } else {
//                 System.err.println("Warning: Null ingredient found in recipe " + recipeToCook.getDisplayName());
//                 return false; // Resep tidak valid
//             }
//         }

//         // 4. Cek Bahan Bakar
//         if (!inventory.hasItem(fuelToUse, 1)) {
//             System.out.println("LOG: Not enough " + fuelToUse.getName() + " to use as fuel.");
//             return false;
//         }
>>>>>>> main

//         return true;
//     }

<<<<<<< HEAD
    @Override
    public void execute(Farm farm) {
        // canExecute() idealnya sudah dipanggil sebelum ini.
        // Namun, sebagai pengaman, bisa panggil lagi atau pastikan state masih valid.
        if (!canExecute(farm)) { // Panggil canExecute lagi sebagai double check jika perlu
             System.out.println("LOG: Pre-condition for cooking not met in execute(). Action aborted.");
             return;
        }

        Player player = farm.getPlayer();
        Inventory inventory = player.getInventory();
        GameClock gameClock = farm.getGameClock(); // Untuk mendapatkan waktu mulai memasak

        System.out.println(player.getName() + " started cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + ". It will take " + COOKING_DURATION_HOURS + " hour(s).");

        // 1. Kurangi Energi Pemain (Langsung)
        player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
        System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT);

        // 2. Kurangi Bahan Baku dari Inventory (Langsung)
        for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
                List<Items> fishToConsume = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity); // Ambil lagi untuk konsumsi
                for (Items fish : fishToConsume) {
<<<<<<< Updated upstream
                    inventory.removeItem(fish, 1);
                    System.out.println("- Consumed 1x " + fish.getName());
                }
            } else if (requiredItem != null) {
                inventory.removeItem(requiredItem, requiredQuantity);
=======
                    inventory.removeItem(fish, 1); // Menggunakan removeItem dari Inventory Anda
                    System.out.println("- Consumed 1x " + fish.getName());
                }
            } else if (requiredItem != null) {
                inventory.removeItem(requiredItem, requiredQuantity); // Menggunakan removeItem
>>>>>>> Stashed changes
                System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
            }
        }

<<<<<<< Updated upstream
        // 3. Kurangi Bahan Bakar
        inventory.removeItem(fuelToUse, 1);
=======
        // 3. Kurangi Bahan Bakar (Langsung)
        inventory.removeItem(fuelToUse, 1); // Menggunakan removeItem
>>>>>>> Stashed changes
        System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");

        // 4. Tentukan berapa banyak makanan yang dihasilkan berdasarkan fuel
        int dishesProduced = 1;
        if (fuelToUse.getName().equals("Coal")) {
            dishesProduced = 2;
        }

        // 5. BUAT TUGAS MEMASAK PASIF, JANGAN TAMBAHKAN MAKANAN KE INV LANGSUNG
        Food dish = recipeToCook.getResultingDish();
        if (dish != null && gameClock != null) {
            CookingInProgress cookingTask = new CookingInProgress(dish, dishesProduced, gameClock.getCurrentTime(), COOKING_DURATION_HOURS);
            farm.addActiveCooking(cookingTask); // Tambahkan ke list di Farm
        } else {
            System.err.println("ERROR: Resulting dish or gameClock is null. Cannot start cooking task.");
        }

        // 6. JANGAN MAJUKAN WAKTU GAME SECARA LANGSUNG DI SINI
        // Waktu akan berjalan normal, dan farm.checkCompletedCookings() akan menangani hasilnya.
        // System.out.println("Game time advanced by " + COOKING_DURATION_HOURS + " hour(s)."); // HAPUS INI
    }
}
=======
//     @Override
//     public void execute(Farm farm) {
//         // Asumsi canExecute() sudah dipanggil dan true
//         Player player = farm.getPlayer();
//         Inventory inventory = player.getInventory();
//         GameClock gameClock = farm.getGameClock();

//         System.out.println(player.getName() + " is cooking " + recipeToCook.getDisplayName() + " using " + fuelToUse.getName() + "...");

//         // 1. Kurangi Energi Pemain
//         player.decreaseEnergy(ENERGY_COST_PER_COOKING_ATTEMPT);
//         System.out.println("- Energy consumed: " + ENERGY_COST_PER_COOKING_ATTEMPT);

//         // 2. Kurangi Bahan Baku dari Inventory
//         for (Map.Entry<Items, Integer> entry : recipeToCook.getIngredients().entrySet()) {
//             Items requiredItem = entry.getKey();
//             int requiredQuantity = entry.getValue();

//             if (requiredItem != null && RecipeDatabase.ANY_FISH_INGREDIENT_NAME.equals(requiredItem.getName())) {
//                 List<Items> fishToConsume = RecipeDatabase.getFishIngredientsFromInventory(inventory, requiredQuantity);
//                 for (Items fish : fishToConsume) {
//                     inventory.removeInventory(fish, 1);
//                     System.out.println("- Consumed 1x " + fish.getName());
//                 }
//             } else if (requiredItem != null) {
//                 inventory.removeInventory(requiredItem, requiredQuantity);
//                 System.out.println("- Consumed " + requiredQuantity + "x " + requiredItem.getName());
//             }
//         }

//         // 3. Kurangi Bahan Bakar
//         inventory.removeInventory(fuelToUse, 1);
//         System.out.println("- Consumed 1x " + fuelToUse.getName() + " as fuel.");

//         // 4. Tentukan berapa banyak makanan yang dihasilkan berdasarkan fuel
//         int dishesProduced = 1; // Default untuk Firewood
//         if (fuelToUse.getName().equals("Coal")) {
//             dishesProduced = 2;
//         }

//         // 5. Tambahkan Hasil Masakan ke Inventory
//         Food dish = recipeToCook.getResultingDish();
//         inventory.addInventory(dish, dishesProduced);
//         System.out.println("Successfully cooked " + dishesProduced + "x " + dish.getName() + "!");

//         // 6. Majukan Waktu Game
//         if (gameClock != null) {
//             gameClock.advanceTimeMinutes(TIME_COST_PER_COOKING_SESSION_HOURS * 60);
//             System.out.println("Game time advanced by " + TIME_COST_PER_COOKING_SESSION_HOURS + " hour(s).");
//         }
//     }
// }
>>>>>>> main
