// --- MarryingAction.java ---
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Items.ItemDatabase;
import org.example.model.NPC.NPC;
import org.example.model.Player;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;

public class MarryingAction implements Action {
    private static final int ENERGY_COST = 80;
    private final NPC npc;

    private boolean success;
    private boolean failedNotFiance;
    private boolean failedSameDay;
    private boolean noRing;

    public MarryingAction(NPC npc) {
        this.npc = npc;
    }

    @Override
    public String getActionName() {
        return "Marrying " + npc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        GameClock clock = farm.getGameClock();

        if (!player.getInventory().getInventory().containsKey(ItemDatabase.getItem("Proposal Ring"))) {
            noRing = true;
            return false;
        }

        if (npc.getRelationshipsStatus() != RelationshipStats.FIANCE) {
            failedNotFiance = true;
            return false;
        }

        if (clock.getDay() == npc.getFianceSinceDay()) {
            failedSameDay = true;
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        npc.setRelationshipsStatus(RelationshipStats.SPOUSE);
        player.setPartner(npc);
        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().setCurrentTime(java.time.LocalTime.of(22, 0));
        player.setCurrentLocationType(LocationType.HOUSE);
        player.setTilePosition(7, 10);
        farm.setCurrentMap(4);
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailedNotFiance() {
        return failedNotFiance;
    }

    public boolean isFailedSameDay() {
        return failedSameDay;
    }

    public boolean isNoRing() {
        return noRing;
    }
}
