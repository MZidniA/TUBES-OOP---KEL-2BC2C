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
        Player player = farm.getPlayerModel();
        GameClock clock = farm.getGameClock();

        return npc.getRelationshipsStatus() == RelationshipStats.FIANCE &&
               npc.getFianceSinceDay() >= 0 &&
               clock.getDay() > npc.getFianceSinceDay(); // minimal 1 hari setelah tunangan
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        // Set status jadi SPOUSE
        npc.setRelationshipsStatus(RelationshipStats.MARRIED);
        player.setPartner(npc);
        player.getPlayerStats().incrementNpcVisitInteraction(npc.getName());

        // ⏰ Skip waktu ke jam 22:00
        farm.getGameClock().setCurrentTime(java.time.LocalTime.of(22, 0));

        // 🏠 Teleport player ke rumah (misalnya Map 4 dan posisi tempat tidur)
        player.setCurrentLocationType(org.example.model.enums.LocationType.HOUSE);
        player.setTilePosition(7, 10); // posisi di rumah (sesuaikan)
        farm.setCurrentMap(4); // Map index 4 misalnya untuk rumah
    }
}
