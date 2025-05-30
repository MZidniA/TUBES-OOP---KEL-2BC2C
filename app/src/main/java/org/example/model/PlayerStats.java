// Lokasi: org.example.model.PlayerStats.java
package org.example.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.example.model.NPC.NPC;
import org.example.model.enums.FishType;
import org.example.model.Items.Items; // Import Items untuk recordObtainedItem

public class PlayerStats {
    private int totalIncome;
    private int totalGoldSpent;
    private int totalCropsHarvested;
    private Map<FishType, Integer> totalFishCaught; // Konsistenkan penamaan ke lowercase: totalFishCaught
    private int totalDaysPlayed;

    private Map<String, Integer> npcFriendshipPoints;
    private Map<String, Integer> npcTotalChat;
    private Map<String, Integer> npcTotalGift;

    private Set<String> unlockedRecipeIds;
    private Set<String> obtainedItemsLog; // <-- BARU: Untuk melacak item yang pernah diperoleh

    public PlayerStats() {
        this.totalIncome = 0;
        this.totalGoldSpent = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = new HashMap<>(); // Menggunakan nama variabel yang dikoreksi
        for (FishType type : FishType.values()) {
            this.totalFishCaught.put(type, 0); // Menggunakan nama variabel yang dikoreksi
        }
        this.totalDaysPlayed = 0;

        this.npcFriendshipPoints = new HashMap<>();
        this.npcTotalChat = new HashMap<>();
        this.npcTotalGift = new HashMap<>();

        this.unlockedRecipeIds = new HashSet<>();
        this.obtainedItemsLog = new HashSet<>(); // <-- Inisialisasi Set baru
        
        // Tambahkan resep default yang langsung terbuka saat permainan dimulai
        // Misalnya, jika Baguette dan Wine adalah default:
        // unlockRecipe("recipe_2"); // Baguette
        // unlockRecipe("recipe_5"); // Wine
        // unlockRecipe("recipe_6"); // Pumpkin Pie
        // unlockRecipe("recipe_9"); // Spakbor Salad
        // Sebaiknya pemanggilan unlockRecipe("id_resep_default") dilakukan di sini
        // agar konsisten dengan Recipe.isUnlocked()
    }

    // --- Metode untuk Resep (sudah ada) ---
    public void unlockRecipe(String recipeId) {
        if (recipeId != null && !recipeId.isEmpty()) {
            if (this.unlockedRecipeIds.add(recipeId)) {
                System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' unlocked!");
            } else {
                // System.out.println("PlayerStats LOG: Recipe '" + recipeId + "' already unlocked."); // Bisa dikomentari jika terlalu verbose
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

    // --- Metode untuk Mencatat Item yang Diperoleh ---
    /**
     * Mencatat bahwa pemain telah memperoleh sebuah item.
     * Ini bisa dipanggil saat item ditambahkan ke inventory untuk pertama kalinya,
     * atau saat item penting tertentu diperoleh.
     * @param item Item yang diperoleh.
     */
    public void recordObtainedItem(Items item) {
        if (item != null && item.getName() != null && !item.getName().isEmpty()) {
            if (this.obtainedItemsLog.add(item.getName())) { // Gunakan nama item sebagai identifier
                System.out.println("PlayerStats LOG: Item '" + item.getName() + "' recorded as obtained.");
            }
        }
    }
    
    /**
     * Mencatat bahwa pemain telah memperoleh sebuah item berdasarkan namanya.
     * @param itemName Nama item yang diperoleh.
     */
    public void recordObtainedItemByName(String itemName) {
        if (itemName != null && !itemName.isEmpty()) {
            if (this.obtainedItemsLog.add(itemName)) {
                System.out.println("PlayerStats LOG: Item '" + itemName + "' recorded as obtained by name.");
            }
        }
    }

    /**
     * Mengecek apakah pemain pernah memperoleh item tertentu.
     * Digunakan untuk kondisi unlock resep.
     * @param itemName Nama item yang akan dicek.
     * @return true jika pemain pernah memperoleh item tersebut, false jika tidak.
     */
    public boolean hasObtainedItem(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return false;
        }
        return this.obtainedItemsLog.contains(itemName);
    }
    
    // Getter untuk melihat item yang pernah diperoleh (opsional, untuk debugging)
    public Set<String> getObtainedItemsLog() {
        return new HashSet<>(this.obtainedItemsLog);
    }

    // --- Metode Statistik Lainnya (sudah ada) ---
    public void recordIncome(int amount) {
        if (amount > 0) this.totalIncome += amount;
    }

    public void recordGoldSpent(int amount) {
        if (amount > 0) this.totalGoldSpent += amount;
    }

    public void recordCropsHarvested(int count) {
        if (count > 0) this.totalCropsHarvested += count;
        // Contoh penggunaan: Jika memanen Parsnip untuk pertama kali dan ingin unlock sesuatu
        // if (this.totalCropsHarvested > 0 && !hasObtainedItem("First Harvest Achievement")) {
        //     recordObtainedItemByName("First Harvest Achievement"); // Tandai achievement panen pertama
        // }
    }

    public void recordFishCaught(FishType type, String fishName) { // Tambahkan fishName
        totalFishCaught.put(type, totalFishCaught.getOrDefault(type, 0) + 1);
        recordObtainedItemByName(fishName); // Catat ikan spesifik yang ditangkap
    }

    public void incrementDaysPlayed() {
        this.totalDaysPlayed++;
    }

    public void recordNpcInteraction(String npcName, String interactionType, Integer currentHeartPoints) {
        // ... (implementasi Anda sudah oke) ...
    }

    public void updateAllNpcFriendshipPoints(List<NPC> npcListParam) {
        // ... (implementasi Anda sudah oke) ...
    }

    // Getters
    public int getTotalIncome() { return totalIncome; }
    public int getTotalGoldSpent() { return totalGoldSpent; } // Koreksi nama getter
    public int getTotalCropsHarvested() { return totalCropsHarvested; }
    public Map<FishType, Integer> getTotalFishCaught() { return totalFishCaught; } // Koreksi nama getter
    public int getTotalDaysPlayed() { return totalDaysPlayed; }
    public Map<String, Integer> getNpcFriendshipPoints() { return npcFriendshipPoints; }
    public Map<String, Integer> getNpcTotalChat() { return npcTotalChat; } 
    public Map<String, Integer> getNpcTotalGift() { return npcTotalGift; }
}