package org.example.controller;

import org.example.model.Items.Crops;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Map.Store;
import org.example.model.Player;
import org.example.model.Recipe;

public class StoreController {
    private final Store store;
    private final Player player;

    public StoreController(Store store, Player player) {
        this.store = store;
        this.player = player;
    }

    public boolean buyItem(String itemName) {
        Items item = ItemDatabase.getItem(itemName);
        if (item == null) return false;

        int price = item.getBuyprice();
        if (price <= 0 || player.getGold() < price) return false;

        player.setGold(player.getGold() - price);
        player.getInventory().addInventory(item, 1);
        player.getPlayerStats().recordGoldSpent(price);
        return true;
    }

    public boolean buyItem(Items item) {
        if (item == null) return false;

        int price = item.getBuyprice();
        if (price <= 0 || player.getGold() < price) return false;

        player.setGold(player.getGold() - price);
        player.getInventory().addInventory(item, 1);
        player.getPlayerStats().recordGoldSpent(price);
        return true;
    }

    public boolean buyCrop(String cropName) {
        int price = store.getCropBuyPrice(cropName);
        if (price <= 0 || player.getGold() < price) return false;

        Crops purchased = store.getCropByName(cropName);
        if (purchased == null) return false;

        player.setGold(player.getGold() - price);
        player.getInventory().addInventory(purchased, 1);
        player.getPlayerStats().recordGoldSpent(price);
        store.recordSale(cropName, 1);
        return true;
    }

    public boolean buyRecipe(Recipe recipe) {
        if (recipe == null || player.getPlayerStats().isRecipeUnlocked(recipe.getId())) return false;

        int price = 100;
        if (player.getGold() < price) return false;

        player.setGold(player.getGold() - price);
        player.getPlayerStats().unlockRecipe(recipe.getId());
        player.getPlayerStats().recordGoldSpent(price);
        return true;
    }
}