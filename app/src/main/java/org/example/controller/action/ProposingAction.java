package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Items.ItemDatabase;
import org.example.model.NPC.NPC;
import org.example.model.Player;
import org.example.model.enums.RelationshipStats;

public class ProposingAction implements Action {
    private static final int ENERGY_SUCCESS = 10;
    private static final int ENERGY_FAIL = 20;
    private static final int TIME_COST = 60;
    private static final int MAX_HEART = 150;

    private NPC npc;
    private Farm farm;

    public ProposingAction(NPC npc, Farm farm) {
        this.npc = npc;
        this.farm = farm;
    }

    @Override
    public String getActionName() {
        return "Proposing to " + npc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
            return player.getInventory().getInventory().containsKey(ItemDatabase.getItem("Proposal Ring"));
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (npc.getHeartPoints() >= MAX_HEART &&
            npc.getRelationshipsStatus() == RelationshipStats.SINGLE) {

            player.decreaseEnergy(ENERGY_SUCCESS);
            npc.setRelationshipsStatus(RelationshipStats.FIANCE);
            player.setPartner(npc);

            // Ubah status NPC menjadi FIANCE
            npc.setRelationshipsStatus(RelationshipStats.FIANCE);
            System.out.println(npc.getName() + " is now your fiance!");

            // Set NPC sebagai partner pemain
            player.setPartner(npc);
            System.out.println(player.getName() + " and " + npc.getName() + " are now engaged!");

            // "Proposal Ring tidak hilang setelah digunakan (reusable)" -> jadi tidak perlu removeItem
            // farm.getPlayerStats().recordEvent("Proposed", npc.getName()); // Contoh event logging
            System.out.println("LAMARAN BERHASIL 💍");
            player.getPlayerStats().incrementNpcVisitInteraction(npc.getName());
            npc.setFianceSinceDay(farm.getGameClock().getDay());

        } else {
            player.decreaseEnergy(ENERGY_FAIL);
        }

        farm.getGameClock().advanceTimeByMinutes(farm, TIME_COST);
    }
}
