// --- GiftingAction.java ---
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
        return player.getEnergy() >= ENERGY_COST &&
               player.getInventory().getInventory().getOrDefault(giftItem, 0) > 0 &&
               !giftItem.getName().equals("Proposal Ring"); // ⛔ tidak boleh gift cincin
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        // Cegah jika tetap dipanggil manual
        if (giftItem.getName().equals("Proposal Ring")) {
            System.out.println("Proposal Ring tidak bisa diberikan sebagai hadiah.");
            return;
        }

        int effect = 0;
        if (targetNpc.getLovedItems().contains(giftItem)) {
            effect = 25;
        } else if (targetNpc.getLikedItems().contains(giftItem)) {
            effect = 20;
        } else if (targetNpc.getHatedItems().contains(giftItem)) {
            effect = -25;
        }

        int updatedHeart = Math.min(150, targetNpc.getHeartPoints() + effect);
        targetNpc.setHeartPoints(updatedHeart);

        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeByMinutes(farm, TIME_COST_MINUTES);
        player.getInventory().removeInventory(giftItem, 1);
        player.getPlayerStats().incrementNpcGiftInteraction(targetNpc.getName()); // targetNpc adalah NPC yang menerima hadiah
        player.getPlayerStats().incrementNpcVisitInteraction(targetNpc.getName()); // Sesuai permintaan Anda

        System.out.println("Kamu memberikan " + giftItem.getName() + " ke " + targetNpc.getName());
        System.out.println("Efek: " + effect + " heartPoints, sekarang: " + updatedHeart);
    }
}
