package org.example.model;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.model.Map.FarmMap;
import org.example.model.NPC.NPC;
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

    private int goldFromLastShipment = 0;

    public Farm(String farmName, Player playerModel) { 
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName);
        this.playerStats = new PlayerStats(); 
        this.gameClock = new GameClock(); 
        this.currentDay = 1; 
        this.currentWeather = Weather.SUNNY; 
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

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public GameClock getGameClock() {
        return gameClock;
    }

    public void addActiveCooking(CookingInProgress cookingTask) {
        if (cookingTask != null) {
            activeCookings.add(cookingTask);
        }
    }

    public List<CookingInProgress> getActiveCookings() {
        return activeCookings;
    }

    public FarmMap getFarmMap() {
        return this.farmMap;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public LocalTime getCurrentTime() {
        return gameClock.getCurrentTime();
    }

    public void setCurrentTime(LocalTime newTime) {
        gameClock.setCurrentTime(newTime);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentSeason(Season nextSeason) {
        this.currentSeason = nextSeason;
    }

    public void setCurrentWeather(Weather nextWeather) {
        this.currentWeather = nextWeather;
    }


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
                    System.out.println("Farm.removeObjectAtTile: Objek '" + obj.name + "' BERHASIL DIHAPUS dari slot " + i + ".");
                    return true;
                }
            }
        }
        System.out.println("Farm.removeObjectAtTile: TIDAK ADA objek ditemukan di peta " + mapIndex + 
                           " pada (" + col + "," + row + ") untuk dihapus.");
        return false;
    }

    public NPC getNPCByName(String name) {
        return npcMap.get(name);
    }

    public void addNPC(NPC npc) {
        npcMap.put(npc.getName(), npc);
    }

    public void updateCookingProgress() {
        if (activeCookings == null || activeCookings.isEmpty()) return;
        Player player = getPlayerModel();
        if (player == null) return;

        LocalTime now = getCurrentTime();
        for (CookingInProgress cooking : activeCookings) {
            if (cooking != null && !cooking.isClaimed() && cooking.isCompleted(now)) {
                // Tambahkan hasil masakan ke inventory pemain
                player.getInventory().addInventory(cooking.getCookedDish(), cooking.getQuantityProduced());
                cooking.setClaimed(true);
                System.out.println("Masakan " + cooking.getCookedDish().getName() + " selesai dimasak dan otomatis masuk ke inventory.");
            }
        }
    }

    /**
     * Returns the gold from the last shipment and resets it to zero.
     */
    public int getAndClearGoldFromLastShipment() {
        int gold = this.goldFromLastShipment;
        this.goldFromLastShipment = 0;
        return gold;
    }

    /**
     * Call this method to add gold to the last shipment (e.g., when shipping items).
     */
    public void addGoldToLastShipment(int amount) {
        this.goldFromLastShipment += amount;
    }

    public Map<String, NPC> getNPCMap() {
        return this.npcMap;
    }
}
