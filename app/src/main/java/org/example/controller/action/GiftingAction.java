package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Items.Items;
import org.example.model.NPC.NPC;
import org.example.model.Player;

public class GiftingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 10;

    private final NPC targetNpc;
    private final Items giftItem;

    public GiftingAction(NPC npc, Items item) {
        this.targetNpc = npc;
        this.giftItem = item;
    }

    @Override
    public String getActionName() {
        return "Gifting " + giftItem.getName() + " to " + targetNpc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        return player.getEnergy() >= ENERGY_COST && !targetNpc.hasGiftedToday();
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        int effect = 0;
        if (targetNpc.getLovedItems().contains(giftItem)) {
            effect = 25;
        } else if (targetNpc.getLikedItems().contains(giftItem)) {
            effect = 20;
        } else if (targetNpc.getHatedItems().contains(giftItem)) {
            effect = -25;
        }

        // Update heartPoints dan pastikan max 100
        int updatedHeart = Math.min(100, targetNpc.getHeartPoints() + effect);
        targetNpc.setHeartPoints(updatedHeart);

        // Set tanggal hadiah hari ini
        targetNpc.setLastGiftDateToday();

        // Kurangi energi
        player.decreaseEnergy(ENERGY_COST);

        // Waktu maju
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        // Hapus item dari inventory
        player.getInventory().removeInventory(giftItem, 1);

        // Log
        System.out.println("Kamu memberikan " + giftItem.getName() + " ke " + targetNpc.getName());
        System.out.println("Efek: " + effect + " heartPoints, sekarang: " + updatedHeart);
    }
}
