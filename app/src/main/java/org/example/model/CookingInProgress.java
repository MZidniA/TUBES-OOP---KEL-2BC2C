package org.example.model;

import java.time.LocalTime;

import org.example.model.Items.Food;

public class CookingInProgress {
    private Food resultingDish;
    private int quantity;
    private LocalTime completionGameTime;
    private boolean isClaimed; 

    public CookingInProgress(Food resultingDish, int quantity, LocalTime startGameTime, int cookingDurationHours) {
        this.resultingDish = resultingDish;
        this.quantity = quantity;
        this.completionGameTime = startGameTime.plusHours(cookingDurationHours);
        this.isClaimed = false;
    }

    public Food getResultingDish() {
        return resultingDish;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalTime getCompletionGameTime() {
        return completionGameTime;
    }

    public boolean isCompleted(LocalTime currentGameTime) {
        return !currentGameTime.isBefore(completionGameTime);
    }
    
    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed(boolean claimed) {
        isClaimed = claimed;
    }
}