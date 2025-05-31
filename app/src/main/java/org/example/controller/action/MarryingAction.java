package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.NPC.NPC;
import org.example.model.Player;
import org.example.model.enums.LocationType;
import org.example.model.enums.RelationshipStats;

public class MarryingAction implements Action {
    private final NPC npc;

    public MarryingAction(NPC npc) {
        this.npc = npc;
    }

    @Override
    public String getActionName() {
        return "Marrying " + npc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        GameClock clock = farm.getGameClock();

        return npc.getRelationshipsStatus() == RelationshipStats.FIANCE &&
               npc.getFianceSinceDay() >= 0 &&
               clock.getDay() > npc.getFianceSinceDay(); // minimal 1 hari setelah tunangan
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        npc.setRelationshipsStatus(RelationshipStats.SPOUSE);
        player.setPartner(npc);
        farm.getGameClock().setCurrentTime(java.time.LocalTime.of(22, 0));
        player.setCurrentLocationType(LocationType.HOUSE);
        player.setTilePosition(7, 10);
        farm.setCurrentMap(4); 
    }
}
