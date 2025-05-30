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

            // ✅ Ini baris yang kamu tambahkan
            npc.setFianceSinceDay(farm.getGameClock().getDay());

            System.out.println("LAMARAN BERHASIL 💍");

        } else {
            player.decreaseEnergy(ENERGY_FAIL);
            System.out.println("LAMARAN DITOLAK");
        }

        farm.getGameClock().advanceTimeByMinutes(farm, TIME_COST);
    }
}
