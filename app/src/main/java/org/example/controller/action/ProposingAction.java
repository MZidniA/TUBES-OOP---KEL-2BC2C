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

    private boolean success;
    private boolean rejected;
    private boolean noRing;
    private boolean alreadyFiance;
    private boolean alreadySpouse;

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

        if (npc.getRelationshipsStatus() == RelationshipStats.FIANCE) {
            alreadyFiance = true;
            return;
        }

        if (npc.getRelationshipsStatus() == RelationshipStats.SPOUSE) {
            alreadySpouse = true;
            return;
        }

        if (!canExecute(farm)) {
            noRing = true;
            return;
        }

        if (npc.getHeartPoints() >= MAX_HEART) {
            player.decreaseEnergy(ENERGY_SUCCESS);
            npc.setRelationshipsStatus(RelationshipStats.FIANCE);
            player.setPartner(npc);
            npc.setFianceSinceDay(farm.getGameClock().getDay());
            farm.getGameClock().advanceTimeByMinutes(farm, TIME_COST);
            success = true;
        } else {
            player.decreaseEnergy(ENERGY_FAIL);
            farm.getGameClock().advanceTimeByMinutes(farm, TIME_COST);
            rejected = true;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isRejected() {
        return rejected;
    }

    public boolean isNoRing() {
        return noRing;
    }

    public boolean isAlreadyFiance() {
        return alreadyFiance;
    }

    public boolean isAlreadySpouse() {
        return alreadySpouse;
    }
}
