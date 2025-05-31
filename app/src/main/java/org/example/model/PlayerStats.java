package org.example.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.example.model.Items.Items;
import org.example.model.NPC.NPC;
import org.example.model.enums.FishType;

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent;
    private int totalCropsHarvested;
    private Map<FishType, Integer> TotalFishCaught; 
    private int totalDaysPlayed;

    private Map<String, Integer> npcFriendshipPoints;
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;

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


        this.unlockedRecipeIds = new HashSet<>();
        
    }

    public void unlockRecipe(String recipeId) {
        if (recipeId != null && !recipeId.isEmpty()) {
            if (this.unlockedRecipeIds.add(recipeId)) { 
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' unlocked!");
            } else {
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' already unlocked.");
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

    public void updateAllNpcFriendshipPoints(List<NPC> npcListParam) { 
        if (npcListParam == null) return;
        for (NPC npc : npcListParam) {
            if (npc != null) { 
                recordNpcInteraction(npc.getName(), "friendship", npc.getHeartPoints());
            }
        }
    }

    public int getTotalIncome() { return totalIncome; }
    public int gettotalGoldSpent() { return totalGoldSpent; } 
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public Map<FishType, Integer> getTotalFishCaught() { return TotalFishCaught; } 
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public Map<String, Integer> getNpcFriendshipPoints() { return npcFriendshipPoints; }
    public Map<String, Integer> getNpcTotalChat() { return npcTotalChat; } 
    public Map<String, Integer> getNpcTotalGift() { return npcTotalGift; }

    public void recordObtainedItem(Items item) {
        if (item == null || item.getName() == null || item.getName().isEmpty()) return;

        System.out.println("PlayerStats: Item diperoleh - " + item.getName());
    }
}