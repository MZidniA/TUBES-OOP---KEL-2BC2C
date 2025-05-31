package org.example.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.Map.FarmMap; 
import org.example.model.NPC.NPC; 
import org.example.model.Items.Items; 
import org.example.model.enums.Season; 
import org.example.model.enums.Weather; 
import org.example.view.InteractableObject.InteractableObject; 

public class Farm {
    private final Player playerModel; 
    private int currentDay;
    private final InteractableObject[][] objects = new InteractableObject[6][300];  
    private int currentMap = 0; 
    private List<CookingInProgress> activeCookings = new java.util.ArrayList<>(); 
    private final PlayerStats playerStats; 
    private final GameClock gameClock; 
    private final FarmMap farmMap = new FarmMap();  
    private Weather currentWeather; 
    private Season currentSeason;   
    private Map<String, NPC> npcMap = new HashMap<>(); 
    private Map<Items, Integer> itemsInShippingBin = new HashMap<>();
    private static final int MAX_UNIQUE_ITEMS_IN_BIN = 16;
    private int goldFromLastShipment = 0;


    public Farm(String farmName, Player playerModel) {
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName); //
        this.playerStats = new PlayerStats();  //
        this.gameClock = new GameClock();  //
        this.currentDay = 1;  //
        this.currentSeason = this.gameClock.getCurrentSeason(); 
        this.currentWeather = this.gameClock.getTodayWeather(); 
    }

    public Player getPlayerModel() { return playerModel; } 
    public int getCurrentMap() { return currentMap; } 
    public InteractableObject[][] getAllObjects() { return objects; } 
    public InteractableObject[] getObjectsForCurrentMap() { return objects[currentMap]; } 
    public void setCurrentMap(int map) { this.currentMap = map; } 

    public String getMapPathFor(int mapIndex) { 
        return switch (mapIndex) {
            case 0 -> "/maps/map.txt";
            case 1 -> "/maps/beachmap.txt";
            case 2 -> "/maps/rivermap.txt";
            case 3 -> "/maps/townmap.txt";
            case 4 -> "/maps/housemap.txt";
            default -> "/maps/map.txt";
        };
    }

    public void clearObjects(int mapIndex) { 
        if (mapIndex >= 0 && mapIndex < objects.length) {
            for(int i = 0; i < objects[mapIndex].length; i++) {
                objects[mapIndex][i] = null;
            }
        }
    }

    public PlayerStats getPlayerStats() { return playerStats; } 
    public GameClock getGameClock() { return gameClock; } 

    public void addActiveCooking(CookingInProgress cookingTask) { 
        if (cookingTask != null) {
            activeCookings.add(cookingTask);
        }
    }
    public List<CookingInProgress> getActiveCookings() { return activeCookings; } 
    public FarmMap getFarmMap() { return this.farmMap; } 
    public int getCurrentDay() { return currentDay; } 
    public void setCurrentDay(int currentDay) { this.currentDay = currentDay; } 
    public LocalTime getCurrentTime() { return gameClock.getCurrentTime(); } 
    public void setCurrentTime(LocalTime newTime) { gameClock.setCurrentTime(newTime); } 
    public Season getCurrentSeason() { return currentSeason; } 
    public Weather getCurrentWeather() { return currentWeather; } 
    public void setCurrentSeason(Season nextSeason) { this.currentSeason = nextSeason; } 
    public void setCurrentWeather(Weather nextWeather) { this.currentWeather = nextWeather; } 

    public InteractableObject getObjectAtTile(int mapIndex, int col, int row, int tileSize) { 
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

    public boolean removeObjectAtTile(int mapIndex, int col, int row, int tileSize) { 
        if (mapIndex < 0 || mapIndex >= objects.length || tileSize <= 0) {
            return false;
        }
        if (objects[mapIndex] == null) {
            return false;
        }

        for (int i = 0; i < objects[mapIndex].length; i++) {
            InteractableObject obj = objects[mapIndex][i];
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;

                if (objCol == col && objRow == row) {
                    objects[mapIndex][i] = null;  
                    return true;
                }
            }
        }
        return false;
    }

    public NPC getNPCByName(String name) { 
        return npcMap.get(name);
     } 
    public void addNPC(NPC npc) { npcMap.put(npc.getName(), npc); } 

    public void updateCookingProgress() { 
        if (activeCookings == null || activeCookings.isEmpty()) return;
        Player player = getPlayerModel();
        if (player == null) return;

        LocalTime now = getCurrentTime(); 
        activeCookings.removeIf(cooking -> {
            if (cooking != null && !cooking.isClaimed() && cooking.isCompleted(now)) { //
                player.getInventory().addInventory(cooking.getCookedDish(), cooking.getQuantityProduced()); //
                cooking.setClaimed(true); //
                return true; 
            }
            return false;
        });
    }

    public boolean addItemToShippingBin(Items item, int quantityToAdd) {
        if (item == null || quantityToAdd <= 0 || !item.isShippable()) { 
            return false;
        }

        if (!itemsInShippingBin.containsKey(item) && itemsInShippingBin.size() >= MAX_UNIQUE_ITEMS_IN_BIN) {
            System.out.println("Farm: Shipping bin full of unique items ("+ itemsInShippingBin.size() + "/" + MAX_UNIQUE_ITEMS_IN_BIN +"). Cannot add new item type: " + item.getName()); 
            return false;
        }

        itemsInShippingBin.put(item, itemsInShippingBin.getOrDefault(item, 0) + quantityToAdd);
        System.out.println("Farm: Added " + quantityToAdd + "x " + item.getName() + " to shipping bin. Total in bin for this item: " + itemsInShippingBin.get(item)); 

        playerModel.getInventory().removeInventory(item, quantityToAdd); 
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
            return 0;
        }
        for (Map.Entry<Items, Integer> entry : itemsInShippingBin.entrySet()) {
            Items item = entry.getKey();
            int quantity = entry.getValue();
            totalRevenue += item.getSellprice() * quantity; 
        }
        itemsInShippingBin.clear();
        return totalRevenue;
    }

    public void setGoldFromLastShipment(int gold) {
        this.goldFromLastShipment = gold;
    }

    public int getGoldFromLastShipment() { 
        return this.goldFromLastShipment;
    }

    public int getAndClearGoldFromLastShipment() {
        int gold = this.goldFromLastShipment;
        this.goldFromLastShipment = 0;
        return gold;
    }
    
    public Map<String, NPC> getNPCMap() {
        return npcMap;
    }
}