// Lokasi: src/main/java/org/example/model/Recipe.java
package org.example.model; // Sesuaikan package jika perlu

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.example.model.Items.Food;
import org.example.model.Items.Items;

public class Recipe {
    private String id;
    private String displayName; 
    private Food resultingDish;
    private Map<Items, Integer> ingredients; 
    private String unlockMechanism; 
    private String unlockDetail;
    private transient Predicate<PlayerStats> unlockConditionChecker; 

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
            return true; 
        }
        if ("achievement".equalsIgnoreCase(unlockMechanism) && unlockConditionChecker != null && stats != null) {
            return unlockConditionChecker.test(stats);
        }
        return false; 
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