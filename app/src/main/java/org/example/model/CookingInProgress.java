package org.example.model; // Atau package yang sesuai

import org.example.model.Items.Food;
import java.time.LocalTime;

public class CookingInProgress {
    private Food resultingDish;
    private int quantity;
    private LocalTime completionGameTime; // Waktu game saat masakan selesai
    private boolean isClaimed; // Untuk menandai apakah sudah diambil pemain

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
        // Selesai jika waktu saat ini sama atau setelah waktu penyelesaian
        // Perlu penanganan jika melewati tengah malam dan completionGameTime ada di hari berikutnya
        // Untuk kesederhanaan awal, kita asumsikan durasi tidak melewati tengah malam,
        // atau GameClock/Farm menangani progresi hari dengan benar.
        return !currentGameTime.isBefore(completionGameTime);
    }
    
    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed(boolean claimed) {
        isClaimed = claimed;
    }
}