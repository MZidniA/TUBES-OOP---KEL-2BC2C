package org.example.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set; 

import org.example.model.NPC.NPC; // Pastikan import ini ada
import org.example.model.Items.Items; // Import Items
import org.example.model.enums.FishType;

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent;
    private int totalCropsHarvested;
    private Map<FishType, Integer> totalFishCaught; // Konsistenkan ke lowercase: totalFishCaught
    private int totalDaysPlayed;

    // NPC Interaction Tracking
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;
    private Map<String, Integer> npcTotalVisit; 

    private Map<String, Integer> npcFriendshipPoints;

    private Set<String> unlockedRecipeIds;
    private Set<String> obtainedItemsLog; // Untuk melacak item yang pernah diperoleh
    private int totalMinutesPlayed = 0;

    private boolean hasShownEndGameStats = false;

    public PlayerStats() {
        this.totalIncome = 0;
        this.totalGoldSpent = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = new HashMap<>(); // Nama variabel dikoreksi
        for (FishType type : FishType.values()) {
            this.totalFishCaught.put(type, 0); // Nama variabel dikoreksi
        }
        this.totalDaysPlayed = 0;

        this.npcFriendshipPoints = new HashMap<>();
        this.npcTotalChat = new HashMap<>();
        this.npcTotalGift = new HashMap<>();
        this.npcTotalVisit = new HashMap<>();

        this.unlockedRecipeIds = new HashSet<>();
        this.obtainedItemsLog = new HashSet<>(); 
        
        // Unlock resep default saat PlayerStats dibuat
        unlockDefaultRecipes();
    }

    private void unlockDefaultRecipes() {
        // ID Resep diambil dari spesifikasi atau RecipeDatabase.java
        // Pastikan ID ini sama dengan yang digunakan saat mendefinisikan resep di RecipeDatabase
        unlockRecipe("recipe_2"); // Baguette ("Default/Bawaan")
        unlockRecipe("recipe_5"); // Wine ("Default/Bawaan")
        unlockRecipe("recipe_6"); // Pumpkin Pie ("Default/Bawaan")
        unlockRecipe("recipe_9"); // Spakbor Salad ("Default/Bawaan")
    }

    public void unlockRecipe(String recipeId) {
        if (recipeId != null && !recipeId.isEmpty()) {
            if (this.unlockedRecipeIds.add(recipeId)) { 
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' unlocked!");
            } else {
                return;
            }
        }
    }

    public boolean isRecipeUnlocked(String recipeId) {
        if (recipeId == null) return false;
        return this.unlockedRecipeIds.contains(recipeId);
    }
    
    public Set<String> getUnlockedRecipeIds() {
        return new HashSet<>(this.unlockedRecipeIds); 
    }

    public void recordObtainedItem(Items item) {
        if (item != null && item.getName() != null && !item.getName().isEmpty()) {
            if (this.obtainedItemsLog.add(item.getName())) {
                System.out.println("PlayerStats LOG: Item '" + item.getName() + "' recorded as obtained.");
                // Setelah item penting diperoleh, cek apakah ada resep yang terbuka karenanya
                checkAndUnlockRecipesOnItemObtained(item.getName());
            }
        }
    }
    
    public void recordObtainedItemByName(String itemName) {
        if (itemName != null && !itemName.isEmpty()) {
            if (this.obtainedItemsLog.add(itemName)) {
                System.out.println("PlayerStats LOG: Item '" + itemName + "' recorded as obtained by name.");
                checkAndUnlockRecipesOnItemObtained(itemName);
            }
        }
    }

    public boolean hasObtainedItem(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return false;
        }
        return this.obtainedItemsLog.contains(itemName);
    }
    
    public Set<String> getObtainedItemsLog() {
        return new HashSet<>(this.obtainedItemsLog);
    }

    // Metode untuk memeriksa dan unlock resep setelah item diperoleh atau statistik berubah
    public void checkAndUnlockRecipesOnItemObtained(String itemName) {
        if ("Pufferfish".equals(itemName)) unlockRecipe("recipe_4"); // Fugu
        if ("Hot Pepper".equals(itemName)) unlockRecipe("recipe_8"); // Fish Stew
        if ("Legend".equals(itemName)) unlockRecipe("recipe_11"); // The Legends of Spakbor
        // Tambahkan pengecekan untuk item lain yang membuka resep
    }

    public void checkAndUnlockRecipesOnStatChange() {
        // Untuk resep Sashimi (10 ikan)
        Map<FishType, Integer> fishMap = getTotalFishCaught();
        if (fishMap != null) {
            int totalFish = 0;
            for (Integer count : fishMap.values()) {
                if (count != null) totalFish += count;
            }
            if (totalFish >= 10) {
                unlockRecipe("recipe_3"); // Sashimi
            }
        }
        // Untuk resep Veggie Soup (panen pertama)
        if (getTotalCropsHarvested() > 0) {
            unlockRecipe("recipe_7"); // Veggie Soup
        }
        // Tambahkan pengecekan untuk statistik lain yang membuka resep
    }

    // Getters & Setters untuk Statistik (sudah ada, pastikan konsisten)
    public void recordIncome(int amount) {
        if (amount > 0) {
            this.totalIncome += amount;
            System.out.println("PlayerStats LOG: Income increased by " + amount + " (Total: " + totalIncome + ")");
        }
    }

    public void recordGoldSpent(int amount) {
        if (amount > 0) {
            this.totalGoldSpent += amount;
            System.out.println("PlayerStats LOG: Gold spent increased by " + amount + " (Total: " + totalGoldSpent + ")");
        }
    }

    public void recordCropsHarvested(int count) {
        if (count > 0) {
            this.totalCropsHarvested += count;
            checkAndUnlockRecipesOnStatChange(); // Cek resep setelah panen
        }
    }
    public void recordFishCaught(FishType type, String fishName) { // Modifikasi untuk menerima nama ikan
        if (type != null && fishName != null) {
            totalFishCaught.put(type, totalFishCaught.getOrDefault(type, 0) + 1);
            recordObtainedItemByName(fishName); // Catat ikan spesifik yang ditangkap
            checkAndUnlockRecipesOnStatChange(); // Cek resep setelah menangkap ikan
        }
    }
    public void incrementDaysPlayed() {
        this.totalDaysPlayed += 1;
        System.out.println("PlayerStats LOG: Days played incremented. Total days: " + totalDaysPlayed);
    }
    // ... (metode recordNpcInteraction, updateAllNpcFriendshipPoints, dan getter lainnya) ...
    public int getTotalIncome() { return totalIncome; }
    public int getTotalGoldSpent() { return totalGoldSpent; } 
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public Map<FishType, Integer> getTotalFishCaught() { return totalFishCaught; } 
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public void addMinutes(int minutes) {
        if (minutes > 0) {
            this.totalMinutesPlayed += minutes;
            System.out.println("PlayerStats LOG: Waktu bertambah " + minutes + " menit (Total: " + totalMinutesPlayed + ")");
        }
    }

    public void setnpcfriendshipPoints(String npcName, int points) {
        if (npcName != null && !npcName.isEmpty()) {
            this.npcFriendshipPoints.put(npcName, points);
        }
    }

    // === Metode untuk Statistik End Game ===

    public void incrementNpcChatInteraction(String npcName) {
        if (npcName != null && !npcName.isEmpty()) {
            this.npcTotalChat.put(npcName, this.npcTotalChat.getOrDefault(npcName, 0) + 1);
            System.out.println("PlayerStats LOG: Chat with " + npcName + " recorded. Total chats: " + this.npcTotalChat.get(npcName) + ".");
        }
    }

    public void incrementNpcGiftInteraction(String npcName) {
        if (npcName != null && !npcName.isEmpty()) {
            this.npcTotalGift.put(npcName, this.npcTotalGift.getOrDefault(npcName, 0) + 1);
            System.out.println("PlayerStats LOG: Gift to " + npcName + " recorded. Total gifts: " + this.npcTotalGift.get(npcName) + ".");
        }
    }

    public void incrementNpcVisitInteraction(String npcName) { // BARU
        if (npcName != null && !npcName.isEmpty()) {
            this.npcTotalVisit.put(npcName, this.npcTotalVisit.getOrDefault(npcName, 0) + 1);
            System.out.println("PlayerStats LOG: Visit to " + npcName + " recorded. Total visits: " + this.npcTotalVisit.get(npcName) + ".");
        }
    }

    // Getters untuk semua statistik yang dibutuhkan oleh End Game GUI
    public Map<String, Integer> getNpcTotalChat() { return new HashMap<>(npcTotalChat); }
    public Map<String, Integer> getNpcTotalGift() { return new HashMap<>(npcTotalGift); }
    public Map<String, Integer> getNpcTotalVisit() { return new HashMap<>(npcTotalVisit); } // BARU

    public boolean hasShownEndGameStats() { return hasShownEndGameStats; }
    public void setHasShownEndGameStats(boolean status) { this.hasShownEndGameStats = status; }

    public int getTotalMinutesPlayed(){ return totalMinutesPlayed;} // Getter jika dibutuhkan
}