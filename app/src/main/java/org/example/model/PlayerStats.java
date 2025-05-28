// Lokasi: org.example.model.PlayerStats.java
package org.example.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; // IMPORT BARU
import java.util.HashSet; // IMPORT BARU

import org.example.model.NPC.NPC;
import org.example.model.enums.FishType;

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent;
    private int totalCropsHarvested;
    private Map<FishType, Integer> TotalFishCaught; // Variabel Anda diawali huruf besar, biasanya lowercase
    private int totalDaysPlayed;

    private Map<String, Integer> npcFriendshipPoints;
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;

    // BARU: Menyimpan ID resep yang sudah unlocked
    private Set<String> unlockedRecipeIds;

    public PlayerStats() {
        this.totalIncome = 0;
        this.totalGoldSpent = 0;
        this.totalCropsHarvested = 0;
        this.TotalFishCaught = new HashMap<>();
        for (FishType type : FishType.values()) {
            TotalFishCaught.put(type, 0);
        }
        this.totalDaysPlayed = 0;

        this.npcFriendshipPoints = new HashMap<>();
        this.npcTotalChat = new HashMap<>();
        this.npcTotalGift = new HashMap<>();

        // BARU: Inisialisasi set untuk resep yang unlocked
        this.unlockedRecipeIds = new HashSet<>();
        // Di sini Anda bisa menambahkan resep "default" atau "bawaan"
        // Misalnya, jika ada resep dengan ID "recipe_default_omelette"
        // unlockRecipe("recipe_default_omelette");
        // Atau ini bisa ditangani saat RecipeDatabase diinisialisasi.
    }

    // --- Metode BARU untuk manajemen resep ---
    /**
     * Menandai bahwa pemain telah mempelajari atau membuka resep baru.
     * @param recipeId ID dari resep yang di-unlock.
     */
    public void unlockRecipe(String recipeId) {
        if (recipeId != null && !recipeId.isEmpty()) {
            if (this.unlockedRecipeIds.add(recipeId)) { // .add() mengembalikan true jika set diubah
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' unlocked!");
            } else {
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' already unlocked.");
            }
        }
    }

    /**
     * Mengecek apakah pemain sudah mengetahui/membuka resep tertentu.
     * @param recipeId ID dari resep yang akan dicek.
     * @return true jika resep sudah unlocked, false jika belum.
     */
    public boolean isRecipeUnlocked(String recipeId) {
        if (recipeId == null) return false;
        return this.unlockedRecipeIds.contains(recipeId);
    }

    /**
     * Mendapatkan semua ID resep yang sudah diketahui pemain.
     * @return Set dari ID resep yang unlocked.
     */
    public Set<String> getUnlockedRecipeIds() {
        return new HashSet<>(this.unlockedRecipeIds); // Kembalikan salinan agar tidak bisa diubah dari luar
    }
    // --- Akhir Metode BARU ---


    public void recordIncome(int amount) {
        if (amount > 0) this.totalIncome += amount;
    }

    public void recordGoldSpent(int amount) {
        if (amount > 0) this.totalGoldSpent += amount;
    }

    public void recordCropsHarvested(int count) {
        if (count > 0) this.totalCropsHarvested += count;
    }

    public void recordFishCaught(FishType type) {
        // Menggunakan nama variabel yang konsisten (lowercase)
        TotalFishCaught.put(type, TotalFishCaught.getOrDefault(type, 0) + 1);
    }

    public void incrementDaysPlayed() {
        this.totalDaysPlayed++;
    }

    public void recordNpcInteraction(String npcName, String interactionType, Integer currentHeartPoints) {
        if (npcName == null) return;

        npcFriendshipPoints.putIfAbsent(npcName, 0);
        npcTotalChat.putIfAbsent(npcName, 0);
        npcTotalGift.putIfAbsent(npcName, 0);

        switch (interactionType.toLowerCase()) {
            case "friendship":
                if (currentHeartPoints != null) npcFriendshipPoints.put(npcName, currentHeartPoints);
                break;
            case "chatting":
                npcTotalChat.put(npcName, npcTotalChat.get(npcName) + 1);
                break;
            case "gifting":
                npcTotalGift.put(npcName, npcTotalGift.get(npcName) + 1);
                break;
        }
    }

    public void updateAllNpcFriendshipPoints(List<NPC> npcListParam) { // Ganti nama parameter agar tidak sama dengan field
        if (npcListParam == null) return;
        for (NPC npc : npcListParam) {
            if (npc != null) { // Tambahkan null check untuk npc
                recordNpcInteraction(npc.getName(), "friendship", npc.getHeartPoints());
            }
        }
    }

    // Getters (saya ubah nama variabel TotalFishCaught menjadi totalFishCaught untuk konsistensi)
    public int getTotalIncome() { return totalIncome; }
    public int gettotalGoldSpent() { return totalGoldSpent; } // typo? -> getTotalGoldSpent
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public Map<FishType, Integer> getTotalFishCaught() { return TotalFishCaught; } // Menggunakan nama variabel yang sudah diubah
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public Map<String, Integer> getNpcFriendshipPoints() { return npcFriendshipPoints; }
    public Map<String, Integer> getNpcTotalChat() { return npcTotalChat; } // typo? -> getNpcTotalChat
    public Map<String, Integer> getNpcTotalGift() { return npcTotalGift; } // typo? -> getNpcTotalGift
}