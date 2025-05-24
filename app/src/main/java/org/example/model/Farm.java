package org.example.model;
import java.util.List;

import org.example.model.Map.FarmMap;
import org.example.model.NPC.NPC;

public class Farm {
    private String farmName;
    private Player player;
    private FarmMap farmMap;
    private GameClock gameClock;
    private List<NPC> npcList; 
    private PlayerStats playerStats;



    public Farm(String farmName, Player player, List<NPC> initialNpcList) {
        this.farmName = farmName;
        this.player = player;

        if (this.player != null) {
            this.player.setFarmname(this.farmName); 
        }
        
        this.farmMap = new FarmMap(); 
        this.gameClock = new GameClock(); 
        this.playerStats = new PlayerStats(); 
        this.npcList = initialNpcList; 
    }

    public String getFarmName() { 
        return farmName; 
    }

    public Player getPlayer() { 
        return player; 
    }

    public FarmMap getFarmMap() { 
        return farmMap; 
    }

    public GameClock getGameClock() { 
        return gameClock; 
    }

    public List<NPC> getNpcList() { 
        return npcList; 
    }

    public PlayerStats getPlayerStats() { 
        return playerStats; 
    }
}   