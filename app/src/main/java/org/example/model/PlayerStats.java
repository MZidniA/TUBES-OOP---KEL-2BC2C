package org.example.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set; 

import org.example.model.Items.Items;
import org.example.model.enums.FishType; 

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent;
    private int totalCropsHarvested;
    private Map<FishType, Integer> totalFishCaught;
    private int totalDaysPlayed;

    // NPC Interaction Tracking
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;
    private Map<String, Integer> npcTotalVisit; 

    private Map<String, Integer> npcFriendshipPoints;

    private Set<String> unlockedRecipeIds;
    private Set<String> obtainedItemsLog; 
    private int totalMinutesPlayed = 0;

    private boolean hasShownEndGameStats = false;

    public PlayerStats() {
        this.totalIncome = 0;
        this.totalGoldSpent = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = new HashMap<>(); 
        for (FishType type : FishType.values()) {
            this.totalFishCaught.put(type, 0); 
        }
        this.totalDaysPlayed = 0;

        this.npcFriendshipPoints = new HashMap<>();
        this.npcTotalChat = new HashMap<>();
        this.npcTotalGift = new HashMap<>();
        this.npcTotalVisit = new HashMap<>();

        this.unlockedRecipeIds = new HashSet<>();
        this.obtainedItemsLog = new HashSet<>(); 
        
        unlockDefaultRecipes();
    }

    private void unlockDefaultRecipes() {
        unlockRecipe("recipe_2"); 
        unlockRecipe("recipe_5"); 
        unlockRecipe("recipe_6"); 
        unlockRecipe("recipe_9"); 
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

    public void checkAndUnlockRecipesOnItemObtained(String itemName) {
        if ("Pufferfish".equals(itemName)) unlockRecipe("recipe_4"); // Fugu
        if ("Hot Pepper".equals(itemName)) unlockRecipe("recipe_8"); // Fish Stew
        if ("Legend".equals(itemName)) unlockRecipe("recipe_11"); // The Legends of Spakbor
    }

    public void checkAndUnlockRecipesOnStatChange() {
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
        if (getTotalCropsHarvested() > 0) {
            unlockRecipe("recipe_7"); // Veggie Soup
        }
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
            checkAndUnlockRecipesOnStatChange(); 
        }
    }
    public void recordFishCaught(FishType type, String fishName) { 
        if (type != null && fishName != null) {
            totalFishCaught.put(type, totalFishCaught.getOrDefault(type, 0) + 1);
            recordObtainedItemByName(fishName); 
            checkAndUnlockRecipesOnStatChange(); 
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