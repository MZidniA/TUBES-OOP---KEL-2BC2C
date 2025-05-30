package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.NPC.NPC;
import org.example.model.enums.LocationType;
import org.example.model.Player;

public class ChattingAction implements Action {

    private static final int ENERGY_COST = 10;
    private static final int TIME_COST_MINUTES = 10;
    private static final int HEART_POINT_GAIN = 10;

    private NPC targetNpc;
    private LocationType npcHouse;

    public ChattingAction(NPC targetNpc, LocationType npcHouse) {
        this.targetNpc = targetNpc;
        this.npcHouse = npcHouse;
    }

    @Override
    public String getActionName() {
        return "Chatting with " + targetNpc.getName();
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (player.getCurrentLocationType() != npcHouse) {
            System.out.println("Kamu harus berada di rumah " + targetNpc.getName() + " untuk ngobrol.");
            return false;
        }

        if (player.getEnergy() < ENERGY_COST) {
            System.out.println("Energi kamu tidak cukup untuk ngobrol.");
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (!canExecute(farm)) return;

        // Cari NPC asli dari farm (bukan instance baru)
        NPC npc = farm.getNPCByName(targetNpc.getName());

        if (npc == null) {
            System.out.println("NPC " + targetNpc.getName() + " tidak ditemukan dalam farm.");
            return;
        }

        int current = npc.getHeartPoints();
        int updated = Math.min(current + HEART_POINT_GAIN, 100);
        npc.setHeartPoints(updated);

        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);

        System.out.println("Kamu ngobrol hangat dengan " + npc.getName() + ".");
        System.out.println("HeartPoint +" + (updated - current) + " (sekarang: " + updated + ")");
        System.out.println("Relationship Status: " + npc.getRelationshipStatus());
        System.out.println("Energi -10. Waktu maju 10 menit.");
    }
}