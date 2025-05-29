// Lokasi: src/main/java/org/example/model/Recipe.java
package org.example.model;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.example.model.Items.Fish;
import org.example.model.Items.Food;
import org.example.model.Items.Items;

public class Recipe {
    private String id;
    private String displayName;
    private Food resultingDish;
    private Map<Items, Integer> ingredients;
    private String unlockMechanism; // e.g., "default", "store", "achievement_fishing", "achievement_item"
    private String unlockDetail;    // e.g., "", "Recipe Shop", "Catch 10 fish", "Find Hot Pepper"
    private transient Predicate<PlayerStats> unlockConditionChecker; // Untuk logika unlock yang kompleks

    public Recipe(String id, String displayName, Food resultingDish, Map<Items, Integer> ingredients,
                  String unlockMechanism, String unlockDetail, Predicate<PlayerStats> unlockConditionChecker) {
        this.id = id;
        this.displayName = displayName;
        this.resultingDish = resultingDish;
        this.ingredients = ingredients;
        this.unlockMechanism = unlockMechanism;
        this.unlockDetail = unlockDetail;
        this.unlockConditionChecker = unlockConditionChecker;
    }

    // Getters (tetap sama)
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Food getResultingDish() { return resultingDish; }
    public Map<Items, Integer> getIngredients() { return ingredients; }
    public String getUnlockMechanism() { return unlockMechanism; }
    public String getUnlockDetail() { return unlockDetail; }


    /**
     * Mengecek apakah resep ini sudah terbuka untuk pemain.
     * CookingAction akan memanggil metode ini.
     * @param stats Statistik pemain.
     * @return true jika resep terbuka, false jika tidak.
     */
    public boolean isUnlocked(PlayerStats stats) {
        if (stats == null) {
            // Jika tidak ada PlayerStats, hanya resep yang secara eksplisit "default"
            // tanpa kondisi lebih lanjut yang bisa dianggap unlocked.
            // Atau, Anda bisa memutuskan ini selalu false jika stats adalah null.
            return "default".equalsIgnoreCase(unlockMechanism) && (unlockConditionChecker == null);
        }

        // Prioritas utama: apakah ID resep ini sudah tercatat sebagai unlocked di PlayerStats?
        // Ini mencakup resep default (yang ditambahkan saat PlayerStats dibuat),
        // resep yang dibeli (yang ditambahkan saat dibeli),
        // dan resep achievement yang kondisinya sudah terpenuhi dan ID-nya sudah dicatat.
        if (stats.isRecipeUnlocked(this.id)) {
            return true;
        }

        // Jika belum ada di PlayerStats.unlockedRecipeIds,
        // dan ini adalah resep tipe "achievement" dengan checker,
        // kita bisa mengecek kondisinya.
        // PENTING: Jika kondisi terpenuhi, sistem lain (bukan Recipe.isUnlocked)
        // yang seharusnya memanggil stats.unlockRecipe(this.id).
        // Metode isUnlocked() ini hanya untuk *mengecek status saat ini*.
        // Namun, jika Anda ingin pengecekan dinamis dari Predicate menjadi penentu utama
        // untuk tipe achievement, maka baris berikut ini valid.
        if ("achievement".equalsIgnoreCase(unlockMechanism) && unlockConditionChecker != null) {
            // Jika predicate true, berarti pemain memenuhi syarat.
            // Idealnya, setelah ini true, game akan memanggil stats.unlockRecipe(this.id)
            // sehingga pengecekan berikutnya akan langsung true dari stats.isRecipeUnlocked(this.id).
            return unlockConditionChecker.test(stats);
        }
        
        // Untuk resep "default" murni yang mungkin tidak ditambahkan ke PlayerStats (meskipun sebaiknya iya),
        // bisa ditambahkan pengecekan di sini. Tapi lebih baik semua resep yang bisa dimasak ada di PlayerStats.
        // if ("default".equalsIgnoreCase(unlockMechanism)) {
        //     return true;
        // }

        // Jika tidak ada kondisi di atas yang terpenuhi, resep dianggap terkunci.
        return false;
    }

    // equals, hashCode, toString (tetap sama)
    @Override
    public boolean equals(Object o) { /* ... kode Anda ... */ return Objects.equals(id, ((Recipe)o).id); }
    @Override
    public int hashCode() { return Objects.hash(id); }
    // Removed duplicate toString() method to resolve the error.

    public boolean canCraft(Inventory playerInventory) {
        if (playerInventory == null) {
            System.err.println("Recipe.canCraft: playerInventory is null.");
            return false;
        }
        if (this.getIngredients() == null) { // Asumsi getIngredients() mengembalikan Map<Items, Integer>
            System.err.println("Recipe.canCraft: Ingredients for recipe '" + this.getDisplayName() + "' is null.");
            return false;
        }

        for (Map.Entry<Items, Integer> entry : this.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQty = entry.getValue();

            if (requiredItem == null) {
                System.err.println("Recipe.canCraft: Recipe '" + this.getDisplayName() + "' contains a null requiredItem.");
                return false; // Resep tidak valid
            }

            // Jika bahan adalah "Any Fish"
            // Pastikan RecipeDatabase.ANY_FISH_INGREDIENT_NAME adalah konstanta String yang benar, contoh: "Any Fish"
            if (RecipeDatabase.ANY_FISH_INGREDIENT_NAME != null &&
                requiredItem.getName().equals(RecipeDatabase.ANY_FISH_INGREDIENT_NAME)) {

                // Cek total ikan di inventory menggunakan instanceof Fish
                int fishCount = 0;
                for (Map.Entry<Items, Integer> invEntry : playerInventory.getInventory().entrySet()) {
                    Items itemInInventory = invEntry.getKey();
                    if (itemInInventory instanceof Fish) { // Menggunakan instanceof Fish
                        fishCount += invEntry.getValue();
                    }
                }
                if (fishCount < requiredQty) {
                    // System.out.println("LOG (Recipe.canCraft): Not enough 'Any Fish' for " + this.getDisplayName() + ". Need: " + requiredQty + ", Have: " + fishCount);
                    return false;
                }
            } else {
                // Bahan biasa
                if (!playerInventory.hasItem(requiredItem, requiredQty)) {
                    // System.out.println("LOG (Recipe.canCraft): Missing ingredient " + requiredItem.getName() + " for " + this.getDisplayName() + ". Need: " + requiredQty + ", Have: " + playerInventory.getItemQuantity(requiredItem));
                    return false;
                }
            }
        }
        return true; // Semua bahan tersedia
    }

}