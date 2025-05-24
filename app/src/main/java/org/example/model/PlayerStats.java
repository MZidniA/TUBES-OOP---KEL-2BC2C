package org.example.model;

import org.example.model.NPC.NPC; 
import org.example.model.enums.FishType; 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerStats {
    private int totalIncome;                    
    private int totalGoldSpent;               
    private int totalCropsHarvested;            
    private Map<FishType, Integer> TotalFishCaught; 
    private int totalDaysPlayed;                


    private Map<String, Integer> npcFriendshipPoints;  
    private Map<String, Integer> npcTotalChat; 
    private Map<String, Integer> npcTotalGift;  


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
        if (npcName == null) 
            return;


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

    public void updateAllNpcFriendshipPoints(List<NPC> npcList) {
        if (npcList == null) return;
        for (NPC npc : npcList) {
            recordNpcInteraction(npc.getName(), "friendship", npc.getHeartPoints());
        }
    }


    public int getTotalIncome() {
        return totalIncome; 
    }

    public int gettotalGoldSpent() { 
        return totalGoldSpent; 
    }

    public int getTotalCropsHarvested() {
        return totalCropsHarvested; 
    }

    public Map<FishType, Integer> getTotalFishCaught() { 
        return TotalFishCaught; 
    }

    public int getTotalDaysPlayed() { 
        return totalDaysPlayed; 
    }

    public Map<String, Integer> getNpcFriendshipPoints() { 
        return npcFriendshipPoints; 
    }

    public Map<String, Integer> getnpcTotalChat() { 
        return npcTotalChat; 
    }

    public Map<String, Integer> getnpcTotalGift() { 
        return npcTotalGift; 
    }

}
