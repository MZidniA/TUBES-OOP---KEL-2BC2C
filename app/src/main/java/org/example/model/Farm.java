package org.example.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.Map.FarmMap; //
import org.example.model.NPC.NPC; //
import org.example.model.Items.Items; //
import org.example.model.enums.Season; //
import org.example.model.enums.Weather; //
import org.example.view.InteractableObject.InteractableObject; //

public class Farm {
    private final Player playerModel; //
    private int currentDay;
    private final InteractableObject[][] objects = new InteractableObject[6][300];  //
    private int currentMap = 0; //
    private List<CookingInProgress> activeCookings = new java.util.ArrayList<>(); //
    private final PlayerStats playerStats; //
    private final GameClock gameClock; //
    private final FarmMap farmMap = new FarmMap();  //
    private Weather currentWeather; //
    private Season currentSeason;   //
    private Map<String, NPC> npcMap = new HashMap<>(); //

    // === SHIPPING BIN FIELDS ===
    private Map<Items, Integer> itemsInShippingBin = new HashMap<>(); // Menyimpan item unik dan kuantitasnya di bin
    private static final int MAX_UNIQUE_ITEMS_IN_BIN = 16;
    private int goldFromLastShipment = 0;
    // ===========================

    public Farm(String farmName, Player playerModel) {
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName); //
        this.playerStats = new PlayerStats();  //
        this.gameClock = new GameClock();  //
        this.currentDay = 1;  //
        this.currentSeason = this.gameClock.getCurrentSeason(); // Inisialisasi dengan season dari gameClock
        this.currentWeather = this.gameClock.getTodayWeather(); // Inisialisasi dengan weather dari gameClock
    }

    public Player getPlayerModel() { return playerModel; } //
    public int getCurrentMap() { return currentMap; } //
    public InteractableObject[][] getAllObjects() { return objects; } //
    public InteractableObject[] getObjectsForCurrentMap() { return objects[currentMap]; } //
    public void setCurrentMap(int map) { this.currentMap = map; } //

    public String getMapPathFor(int mapIndex) { //
        return switch (mapIndex) {
            case 0 -> "/maps/map.txt";
            case 1 -> "/maps/beachmap.txt";
            case 2 -> "/maps/rivermap.txt";
            case 3 -> "/maps/townmap.txt";
            case 4 -> "/maps/housemap.txt";
            // case 5 -> (jika ada peta ke-5, contohnya Pond, tambahkan di sini)
            default -> "/maps/map.txt";
        };
    }

    public void clearObjects(int mapIndex) { //
        if (mapIndex >= 0 && mapIndex < objects.length) {
            for(int i = 0; i < objects[mapIndex].length; i++) {
                objects[mapIndex][i] = null;
            }
        }
    }

    public PlayerStats getPlayerStats() { return playerStats; } //
    public GameClock getGameClock() { return gameClock; } //

    public void addActiveCooking(CookingInProgress cookingTask) { //
        if (cookingTask != null) {
            activeCookings.add(cookingTask);
        }
    }
    public List<CookingInProgress> getActiveCookings() { return activeCookings; } //
    public FarmMap getFarmMap() { return this.farmMap; } //
    public int getCurrentDay() { return currentDay; } //
    public void setCurrentDay(int currentDay) { this.currentDay = currentDay; } //
    public LocalTime getCurrentTime() { return gameClock.getCurrentTime(); } //
    public void setCurrentTime(LocalTime newTime) { gameClock.setCurrentTime(newTime); } //
    public Season getCurrentSeason() { return currentSeason; } //
    public Weather getCurrentWeather() { return currentWeather; } //
    public void setCurrentSeason(Season nextSeason) { this.currentSeason = nextSeason; } //
    public void setCurrentWeather(Weather nextWeather) { this.currentWeather = nextWeather; } //

    public InteractableObject getObjectAtTile(int mapIndex, int col, int row, int tileSize) { //
        if (mapIndex < 0 || mapIndex >= objects.length) return null;
        for (InteractableObject obj : objects[mapIndex]) {
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == col && objRow == row) {
                    return obj;
                }
            }
        }
        return null;
    }

    public boolean removeObjectAtTile(int mapIndex, int col, int row, int tileSize) { //
        if (mapIndex < 0 || mapIndex >= objects.length || tileSize <= 0) {
            System.err.println("Farm.removeObjectAtTile: Invalid mapIndex or tileSize.");
            return false;
        }
        if (objects[mapIndex] == null) {
            System.err.println("Farm.removeObjectAtTile: Object array for map " + mapIndex + " is null.");
            return false;
        }

        for (int i = 0; i < objects[mapIndex].length; i++) {
            InteractableObject obj = objects[mapIndex][i];
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;

                if (objCol == col && objRow == row) {
                    System.out.println("Farm.removeObjectAtTile: MENEMUKAN objek '" + obj.name + "' di slot " + i +
                                       " pada (" + col + "," + row + ") untuk dihapus.");
                    objects[mapIndex][i] = null; // Hapus objek
                    System.out.println("Farm.removeObjectAtTile: Objek '" + (obj.name != null ? obj.name : "Unnamed") + "' BERHASIL DIHAPUS dari slot " + i + ".");
                    return true;
                }
            }
        }
        System.out.println("Farm.removeObjectAtTile: TIDAK ADA objek ditemukan di peta " + mapIndex +
                           " pada (" + col + "," + row + ") untuk dihapus.");
        return false;
    }

    public NPC getNPCByName(String name) { return npcMap.get(name); } //
    public void addNPC(NPC npc) { npcMap.put(npc.getName(), npc); } //

    public void updateCookingProgress() { //
        if (activeCookings == null || activeCookings.isEmpty()) return;
        Player player = getPlayerModel();
        if (player == null) return;

        LocalTime now = getCurrentTime(); //
        // Gunakan iterator untuk menghindari ConcurrentModificationException jika ada penghapusan
        activeCookings.removeIf(cooking -> {
            if (cooking != null && !cooking.isClaimed() && cooking.isCompleted(now)) { //
                player.getInventory().addInventory(cooking.getCookedDish(), cooking.getQuantityProduced()); //
                cooking.setClaimed(true); //
                System.out.println("Masakan " + cooking.getCookedDish().getName() + " selesai dimasak dan otomatis masuk ke inventory."); //
                return true; // Hapus dari list jika sudah diklaim
            }
            return false;
        });
    }

    // === SHIPPING BIN METHODS ===
    public boolean addItemToShippingBin(Items item, int quantityToAdd) {
        if (item == null || quantityToAdd <= 0 || !item.isShippable()) { //
            System.out.println("Farm: Attempted to add invalid or non-shippable item to bin: " + (item != null ? item.getName() : "null"));
            return false;
        }

        if (!itemsInShippingBin.containsKey(item) && itemsInShippingBin.size() >= MAX_UNIQUE_ITEMS_IN_BIN) {
            System.out.println("Farm: Shipping bin full of unique items ("+ itemsInShippingBin.size() + "/" + MAX_UNIQUE_ITEMS_IN_BIN +"). Cannot add new item type: " + item.getName()); //
            return false;
        }

        itemsInShippingBin.put(item, itemsInShippingBin.getOrDefault(item, 0) + quantityToAdd);
        System.out.println("Farm: Added " + quantityToAdd + "x " + item.getName() + " to shipping bin. Total in bin for this item: " + itemsInShippingBin.get(item)); //

        playerModel.getInventory().removeInventory(item, quantityToAdd); //
        return true;
    }

    public Map<Items, Integer> getItemsInShippingBin() {
        return itemsInShippingBin;
    }

    public int getUniqueItemCountInBin() {
        return itemsInShippingBin.size();
    }

    public static int getMaxUniqueItemsInBin() {
        return MAX_UNIQUE_ITEMS_IN_BIN;
    }

    public int processShippedItemsAndGetRevenue() {
        int totalRevenue = 0;
        if (itemsInShippingBin.isEmpty()) {
            System.out.println("Farm: No items in shipping bin to process for revenue.");
            return 0;
        }
        System.out.println("Farm: Processing shipped items for revenue...");
        for (Map.Entry<Items, Integer> entry : itemsInShippingBin.entrySet()) {
            Items item = entry.getKey();
            int quantity = entry.getValue();
            totalRevenue += item.getSellprice() * quantity; //
            System.out.println("  - Sold " + quantity + "x " + item.getName() + " for " + (item.getSellprice() * quantity) + "g"); //
        }
        itemsInShippingBin.clear();
        System.out.println("Farm: Total revenue from shipping: " + totalRevenue + "g. Bin is now empty.");
        return totalRevenue;
    }

    public void setGoldFromLastShipment(int gold) {
        this.goldFromLastShipment = gold;
        System.out.println("Farm: Gold from last shipment set to: " + gold + "g (will be given next morning).");
    }

    public int getGoldFromLastShipment() { // << FUNGSI BARU YANG DIMINTA
        System.out.println("Farm: getGoldFromLastShipment() called. Value: " + this.goldFromLastShipment + "g.");
        return this.goldFromLastShipment;
    }

    public int getAndClearGoldFromLastShipment() {
        int gold = this.goldFromLastShipment;
        this.goldFromLastShipment = 0;
        System.out.println("Farm: Gold from last shipment retrieved (" + gold + "g) and cleared.");
        return gold;
    }
    // ============================
}