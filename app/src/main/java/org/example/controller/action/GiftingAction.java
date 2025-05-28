package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Items.Items;
import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;
import org.example.model.Player;

public class GiftingAction implements Action{
    
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 10;

    private NPC targetNpc;
    private LocationType npcHouse;
    private Items itemToGive;

    public GiftingAction(NPC targetNpc, LocationType npcHouse, Items itemToGive) {
        this.targetNpc = targetNpc;
        this.npcHouse = npcHouse;
        this.itemToGive = itemToGive;
    }

    @Override
    public String getActionName() {
        return "Gifting to " + targetNpc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (player.getCurrentLocationType() != npcHouse) {
            System.out.println("Kamu harus berada di rumah " + targetNpc.getName() + " untuk memberi hadiah.");
            return false;
        }

        if (!player.getInventory().hasItem(itemToGive, 1)) {
            System.out.println("Kamu tidak memiliki item " + itemToGive.getName() + " di inventory.");
            return false;
        }

        if (player.getEnergy() < ENERGY_COST) {
            System.out.println("Energi kamu tidak cukup untuk memberi hadiah.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (!canExecute(farm)) return;

        // Kurangi energi
        player.decreaseEnergy(ENERGY_COST);

        // Tambah waktu
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        // Hapus item dari inventory
        player.getInventory().removeInventory(itemToGive, 1);

        // Tentukan efek hadiah
        int heartPointsAdded = 0;
        if (targetNpc.getLovedItems().contains(itemToGive.getName())) {
            heartPointsAdded = 25;
        } else if (targetNpc.getLikedItems().contains(itemToGive.getName())) {
            heartPointsAdded = 20;
        } else if (targetNpc.getHatedItems().contains(itemToGive.getName())) {
            heartPointsAdded = -25;
        }

        // Update heart point NPC
        targetNpc.setHeartPoints(targetNpc.getHeartPoints() + heartPointsAdded);

        System.out.println("Kamu memberi " + itemToGive.getName() + " kepada " + targetNpc.getName() + ".");
        if (heartPointsAdded > 0) {
            System.out.println(targetNpc.getName() + " menyukai hadiahmu! HeartPoint +" + heartPointsAdded);
        } else if (heartPointsAdded < 0) {
            System.out.println(targetNpc.getName() + " sangat tidak menyukai hadiah ini... HeartPoint " + heartPointsAdded);
        } else {
            System.out.println(targetNpc.getName() + " tidak terlalu peduli dengan hadiah tersebut.");
        }

        System.out.println("Energi -5. Waktu maju 10 menit.");
    }
}
