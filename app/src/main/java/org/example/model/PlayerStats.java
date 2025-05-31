package org.example.model;

import java.util.HashMap;
import java.util.HashSet;
// import java.util.List; // Tidak terpakai secara langsung di sini, bisa dihapus jika tidak ada kegunaan lain
import java.util.Map;
import java.util.Set;

// import org.example.model.NPC.NPC; // Tidak terpakai secara langsung di sini
import org.example.model.Items.Items; // Import Items
import org.example.model.enums.FishType;

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent; // Sesuai spesifikasi: "total expenditure"
    private int totalCropsHarvested;
    private Map<FishType, Integer> totalFishCaught;
    private int totalDaysPlayed;

    // NPC Interaction Tracking
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;
    private Map<String, Integer> npcTotalVisit; // BARU: Untuk Visiting Frequency
    private Map<String, Integer> npcFriendshipPoints; // Added for friendship points

    // Lainnya (sudah ada dari file Anda)
    private Set<String> unlockedRecipeIds;
    private Set<String> obtainedItemsLog;
    private int totalMinutesPlayed = 0; // Mungkin tidak langsung untuk end game stats, tapi biarkan jika ada guna lain

    // Flag untuk memastikan statistik end game hanya ditampilkan sekali per pemicu milestone
    private boolean hasShownEndGameStats = false;

    public PlayerStats() {
        this.totalIncome = 0;
        this.totalGoldSpent = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = new HashMap<>();
        for (FishType type : FishType.values()) {
            this.totalFishCaught.put(type, 0);
        }
        this.totalDaysPlayed = 0; // Dimulai dari 0, akan diincrement oleh GameClock

        this.npcTotalGift = new HashMap<>();
        this.npcTotalVisit = new HashMap<>(); // Inisialisasi map baru
        this.npcFriendshipPoints = new HashMap<>(); // Initialize friendship points map

        this.unlockedRecipeIds = new HashSet<>();
        this.obtainedItemsLog = new HashSet<>();
        this.obtainedItemsLog = new HashSet<>();
        
        unlockDefaultRecipes(); // Sudah ada
    }

    private void unlockDefaultRecipes() { // Sudah ada
        unlockRecipe("recipe_2"); 
        unlockRecipe("recipe_5"); 
        unlockRecipe("recipe_6"); 
        unlockRecipe("recipe_9"); 
    }

    public void unlockRecipe(String recipeId) { // Sudah ada
        if (recipeId != null && !recipeId.isEmpty()) {
            if (this.unlockedRecipeIds.add(recipeId)) { 
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' unlocked!");
            } else {
                return;
            }
        }
    }

    public boolean isRecipeUnlocked(String recipeId) { // Sudah ada
        if (recipeId == null) return false;
        return this.unlockedRecipeIds.contains(recipeId);
    }
    
    public Set<String> getUnlockedRecipeIds() { // Sudah ada
        return new HashSet<>(this.unlockedRecipeIds); 
    }

    public void recordObtainedItem(Items item) { // Sudah ada
        if (item != null && item.getName() != null && !item.getName().isEmpty()) {
            if (this.obtainedItemsLog.add(item.getName())) {
                System.out.println("PlayerStats LOG: Item '" + item.getName() + "' recorded as obtained.");
                checkAndUnlockRecipesOnItemObtained(item.getName());
            }
        }
    }
    
    public void recordObtainedItemByName(String itemName) { // Sudah ada
        if (itemName != null && !itemName.isEmpty()) {
            if (this.obtainedItemsLog.add(itemName)) {
                System.out.println("PlayerStats LOG: Item '" + itemName + "' recorded as obtained by name.");
                checkAndUnlockRecipesOnItemObtained(itemName);
            }
        }
    }

    public boolean hasObtainedItem(String itemName) { // Sudah ada
        if (itemName == null || itemName.isEmpty()) {
            return false;
        }
        return this.obtainedItemsLog.contains(itemName);
    }
    
    public Set<String> getObtainedItemsLog() { // Sudah ada
        return new HashSet<>(this.obtainedItemsLog);
    }

    public void checkAndUnlockRecipesOnItemObtained(String itemName) { // Sudah ada
        if ("Pufferfish".equals(itemName)) unlockRecipe("recipe_4");
        if ("Hot Pepper".equals(itemName)) unlockRecipe("recipe_8");
        if ("Legend".equals(itemName)) unlockRecipe("recipe_11");
    }

    public void checkAndUnlockRecipesOnStatChange() { // Sudah ada
        Map<FishType, Integer> fishMap = getTotalFishCaught();
        if (fishMap != null) {
            int totalFish = 0;
            for (Integer count : fishMap.values()) {
                if (count != null) totalFish += count;
            }
            if (totalFish >= 10) {
                unlockRecipe("recipe_3"); 
            }
        }
        if (getTotalCropsHarvested() > 0) {
            unlockRecipe("recipe_7"); 
        }
    }

    // === Metode untuk Statistik End Game ===

    public void recordIncome(int amount) {
        if (amount > 0) {
            this.totalIncome += amount;
            System.out.println("PlayerStats LOG: Income recorded: +" + amount + "g. Total Income: " + this.totalIncome + "g.");
        }
    }

    public void recordGoldSpent(int amount) {
        if (amount > 0) {
            this.totalGoldSpent += amount;
            System.out.println("PlayerStats LOG: Expenditure recorded: -" + amount + "g. Total Spent: " + this.totalGoldSpent + "g.");
        }
    }

    public void recordCropsHarvested(int count) { // Parameter sudah benar `int count`
        if (count > 0) {
            this.totalCropsHarvested += count;
            System.out.println("PlayerStats LOG: Crops harvested: +" + count + ". Total Harvested: " + this.totalCropsHarvested + ".");
            checkAndUnlockRecipesOnStatChange();
        }
    }

    public void recordFishCaught(FishType type, String fishName) { // Sudah ada dan OK
        if (type != null && fishName != null) {
            totalFishCaught.put(type, totalFishCaught.getOrDefault(type, 0) + 1);
            System.out.println("PlayerStats LOG: Fish caught: " + fishName + " (" + type + "). Total for type: " + totalFishCaught.get(type) + ".");
            recordObtainedItemByName(fishName); 
            checkAndUnlockRecipesOnStatChange(); 
        }
    }

    public void incrementDaysPlayed() {
        this.totalDaysPlayed++;
        System.out.println("PlayerStats LOG: Day incremented. Total Days Played: " + this.totalDaysPlayed + ".");
    }

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
    public int getTotalIncome() { return totalIncome; }
    public int getTotalGoldSpent() { return totalGoldSpent; } 
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public Map<FishType, Integer> getTotalFishCaught() { return new HashMap<>(totalFishCaught); } // Kembalikan copy untuk enkapsulasi
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public Map<String, Integer> getNpcTotalChat() { return new HashMap<>(npcTotalChat); }
    public Map<String, Integer> getNpcTotalGift() { return new HashMap<>(npcTotalGift); }
    public Map<String, Integer> getNpcTotalVisit() { return new HashMap<>(npcTotalVisit); } // BARU

    public boolean hasShownEndGameStats() { return hasShownEndGameStats; }
    public void setHasShownEndGameStats(boolean status) { this.hasShownEndGameStats = status; }
    public int getTotalMinutesPlayed(){ return totalMinutesPlayed;} // Getter jika dibutuhkan
    
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

}