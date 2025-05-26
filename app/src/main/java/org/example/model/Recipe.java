// Lokasi: src/main/java/org/example/model/Recipe.java
package org.example.model; // Sesuaikan package jika perlu

import org.example.model.Items.Items;
import org.example.model.Items.Food;
import org.example.model.PlayerStats;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class Recipe {
    private String id; // e.g., "recipe_1"
    private String displayName; // e.g., "Fish n' Chips"
    private Food resultingDish; // Objek Food yang dihasilkan
    private Map<Items, Integer> ingredients; // Bahan utama dan jumlahnya
    // Untuk "Any Fish", kita bisa menggunakan placeholder atau kategori.
    // Untuk saat ini, kita akan biarkan value Items bisa berupa kategori jika diperlukan.
    // Atau, jika Items adalah bahan spesifik, maka kita perlu Items placeholder "AnyFish"
    // Untuk bahan bakar, kita akan handle secara terpisah di CookingAction
    // karena spesifikasinya 1 firewood = 1 makanan, 1 coal = 2 makanan.

    private String unlockMechanism; // "default", "store", "achievement" (sesuai spek)
    private String unlockDetail; // Deskripsi tambahan untuk unlock, misal nama item yang harus didapat
    private transient Predicate<PlayerStats> unlockConditionChecker; // transient agar tidak ikut serialisasi jika ada

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

    // Getters
    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Food getResultingDish() {
        return resultingDish;
    }

    public Map<Items, Integer> getIngredients() {
        return ingredients;
    }

    public String getUnlockMechanism() {
        return unlockMechanism;
    }

    public String getUnlockDetail() {
        return unlockDetail;
    }

    /**
     * Mengecek apakah resep ini sudah terbuka untuk pemain berdasarkan statistik mereka.
     * Untuk resep "store" atau "default", diasumsikan selalu unlocked jika sudah ada di RecipeBook.
     * Aksi pembelian di store akan menangani penambahan resep ke daftar yang diketahui pemain jika perlu.
     * @param stats Statistik pemain.
     * @return true jika resep terbuka, false jika tidak.
     */
    public boolean isUnlocked(PlayerStats stats) {
        if ("default".equalsIgnoreCase(unlockMechanism) || "Beli di store".equalsIgnoreCase(unlockMechanism)) {
            return true; // Resep bawaan atau yang dari store dianggap unlocked by default di sini
                         // Logika "apakah pemain sudah membeli" mungkin ada di tempat lain jika perlu
        }
        if ("achievement".equalsIgnoreCase(unlockMechanism) && unlockConditionChecker != null && stats != null) {
            return unlockConditionChecker.test(stats);
        }
        return false; // Defaultnya tidak unlocked jika tidak ada kondisi lain atau stats null
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
               "id='" + id + '\'' +
               ", displayName='" + displayName + '\'' +
               '}';
    }
}