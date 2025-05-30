package org.example.model;

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

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public Food getResultingDish() { return resultingDish; }
    public Map<Items, Integer> getIngredients() { return ingredients; }
    public String getUnlockMechanism() { return unlockMechanism; }
    public String getUnlockDetail() { return unlockDetail; }

    public boolean isUnlocked(PlayerStats stats) {
        if (stats == null) {
            return "default".equalsIgnoreCase(unlockMechanism) && (unlockConditionChecker == null);
        }

        if (stats.isRecipeUnlocked(this.id)) {
            return true;
        }

        if ("achievement".equalsIgnoreCase(unlockMechanism) && unlockConditionChecker != null) {
            return unlockConditionChecker.test(stats);
        }
        
        return false;
    }

    @Override
    public boolean equals(Object o) { 
        return Objects.equals(id, ((Recipe)o).id); 
    }

    @Override
    public int hashCode() { 
        return Objects.hash(id); 
    }
}