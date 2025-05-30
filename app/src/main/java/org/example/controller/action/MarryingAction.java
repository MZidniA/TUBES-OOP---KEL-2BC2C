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

        // Ubah status jadi SPOUSE
        npc.setRelationshipsStatus(RelationshipStats.SPOUSE);

        // Skip waktu ke 22.00
        farm.getGameClock().setCurrentTime(java.time.LocalTime.of(22,0));

        // Kembalikan player ke rumah
        player.setCurrentLocationType(LocationType.HOUSE);

        // Optional: reset partner daily actions, dsb.
        System.out.println("Player menikah dengan " + npc.getName());
    }
}
