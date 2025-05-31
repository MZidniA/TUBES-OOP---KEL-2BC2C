package org.example.controller;


import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Inventory; 
import org.example.model.Player;
import org.example.model.NPC.NPC;
import org.example.model.Recipe; 
import org.example.model.RecipeDatabase; 
import org.example.model.Items.Fish; 
import org.example.model.Items.ItemDatabase; 
import org.example.model.Items.Items; 
import org.example.model.enums.RelationshipStats;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.GamePanel;
import org.example.view.GameStateUI;
import java.time.LocalTime;
import java.util.Map;
import java.util.List;

public class CheatManager {

    private GameController controller; 
    private Farm farm;
    private GameStateUI gameStateUI;
    private GamePanel gamePanel;
    private TimeManager timeManager;
    private GameClock gameClock;

    public CheatManager(GameController controller) {
        this.controller = controller;
        this.farm = controller.getFarmModel();
        this.gameStateUI = controller.getGameStateUI();
        this.gamePanel = controller.getGamePanel();
        this.timeManager = controller.getTimeManager();
        this.gameClock = this.farm.getGameClock(); 
    }

    public void cheat_setGoldToWinningAmount() {
        if (farm != null && farm.getPlayerModel() != null) {
            farm.getPlayerModel().setGold(17209); 
        } else {
            return;
        }
    }

    public void cheat_addGold5K() {
        if (farm != null && farm.getPlayerModel() != null) {
            Player playerModel = farm.getPlayerModel(); 
            playerModel.setGold(playerModel.getGold() + 5000); 
            if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: Added 5000g to gold!"); 
            if (gamePanel != null) gamePanel.repaint();
        } else {
            System.out.println("CHEAT FAILED: Player model not available for cheat_addGold5K.");
        }
    }

    public void cheat_marryNpc(String npcName) {
        if (farm != null && farm.getPlayerModel() != null) {
            Player playerModel = farm.getPlayerModel(); 
            NPC targetNpc = farm.getNPCByName(npcName); 

            if (targetNpc != null) {
                playerModel.setPartner(targetNpc); 
                targetNpc.setRelationshipsStatus(RelationshipStats.SPOUSE); 

                if (playerModel.getPlayerStats() != null) { 
                    playerModel.getPlayerStats().setnpcfriendshipPoints(targetNpc.getName(), 150); 
                }
                if (gamePanel != null) gamePanel.repaint();
            } else {

                if (gameStateUI != null) gameStateUI.showTemporaryMessage("CHEAT: NPC " + npcName + " not found!"); 
            }
        } else {
            return;
        }
    }

    public void cheat_setWeather(Weather weather) {
        if (farm != null && farm.getGameClock() != null && timeManager != null) {
            farm.getGameClock().setTodayWeather(weather); 
            timeManager.notifyObservers(); 
        } else {
            System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting weather.");
        }
    }

    public void cheat_setWeatherToRainy() {
        cheat_setWeather(Weather.RAINY); 
    }

    public void cheat_cycleWeather() {
        if (farm != null && farm.getGameClock() != null) {
            Weather currentWeather = farm.getGameClock().getTodayWeather(); 
            Weather nextWeather = (currentWeather == Weather.SUNNY) ? Weather.RAINY : Weather.SUNNY; 
            cheat_setWeather(nextWeather);
        } else {
            System.out.println("CHEAT FAILED: GameClock not available for cycling weather.");
        }
    }

    public void cheat_setSeason(Season season) {
        if (farm != null && farm.getGameClock() != null && timeManager != null) {
            farm.getGameClock().setCurrentSeason(season); 
            timeManager.notifyObservers(); 
        } else {
            System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting season.");
        }
    }

    public void cheat_setNextSeason() {
        if (farm != null && farm.getGameClock() != null) {
            Season currentSeason = farm.getGameClock().getCurrentSeason(); 
            Season nextSeason; 
            switch (currentSeason) { 
                case SPRING: nextSeason = Season.SUMMER; break;
                case SUMMER: nextSeason = Season.FALL; break;
                case FALL:   nextSeason = Season.WINTER; break;
                case WINTER: nextSeason = Season.SPRING; break;
                default:     nextSeason = Season.SPRING;
            }
            cheat_setSeason(nextSeason);
        } else {
            System.out.println("CHEAT FAILED: GameClock not available for cycling season.");
        }
    }

    public void cheat_setTime(LocalTime time) {
        if (farm != null && farm.getGameClock() != null && timeManager != null) {
            farm.getGameClock().setCurrentTime(time); 
            timeManager.notifyObservers(); 
        } else {
            System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting time.");
        }
    }

    public void activateSetTimeTo2AMCheat() {
        if (farm != null || farm.getGameClock() != null || timeManager != null) {
            gameClock.setCurrentTime(java.time.LocalTime.of(1, 50));
            timeManager.notifyObservers(); 
        }
        else {
            System.out.println("CHEAT FAILED: GameClock or TimeManager not available for setting time to 2AM.");
        }

    } 

    private void addIngredientsToInventory(String recipeId, boolean addFullAmount) {
        if (farm == null || farm.getPlayerModel() == null || farm.getPlayerModel().getInventory() == null) {
            return;
        }
    
        Recipe recipe = RecipeDatabase.getRecipeById(recipeId); 
        if (recipe == null) {
            return;
        }
    
        Inventory playerInventory = farm.getPlayerModel().getInventory(); 
        String type = addFullAmount ? "Full" : "Partial";

    
        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) { 
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();
            int quantityToAdd = 0;

            if (addFullAmount) {
                quantityToAdd = requiredQuantity;
            } else { 
                if (requiredQuantity == 1) {
                    quantityToAdd = 0;
                } else if (requiredQuantity > 1) { 
                    quantityToAdd = Math.max(1, requiredQuantity / 2);
                }
            }
            if (quantityToAdd > 0) {
                Items itemToAdd = requiredItem; 
                if (requiredItem.getName().equals(RecipeDatabase.ANY_FISH_INGREDIENT_NAME)) { 
                    itemToAdd = ItemDatabase.getItem("Carp"); 
                    if (itemToAdd == null) { 
                        List<Items> allFish = ItemDatabase.getItemsByCategory("Fish"); 
                        if (!allFish.isEmpty()) {
                            itemToAdd = allFish.get(0);
                        } else {
                            continue; 
                        }
                    }
                } else {
                    return;
                }
                playerInventory.addInventory(itemToAdd, quantityToAdd); 
            } else {
                
                if (requiredQuantity > 0) { 
                    return;
                }
            }
        }    
        if (gamePanel != null) gamePanel.repaint();
    }

    public void cheat_addRecipeBundleStarterPack() {
        Inventory playerInventory = farm.getPlayerModel().getInventory(); 
        addIngredientsToInventory("recipe_1", true);  
        addIngredientsToInventory("recipe_2", true);  
        addIngredientsToInventory("recipe_6", false); 
        Items coal = ItemDatabase.getItem("Coal");
        Items firewood = ItemDatabase.getItem("Firewood");
        playerInventory.addInventory(coal, 5);
        playerInventory.addInventory(firewood, 5);


        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }

}