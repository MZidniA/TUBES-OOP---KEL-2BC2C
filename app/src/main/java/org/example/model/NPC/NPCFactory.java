package org.example.model.NPC;

import java.util.Map ;   
import java.util.HashMap ;



public class NPCFactory {
    public static Map<String, NPC> createNPCs() {
        Map<String, NPC> npc = new HashMap<>();

        npc.put("Abigail", new AbigailNPC());
        npc.put("Mayor Tadi", new MayorTadiNPC());
        npc.put("Caroline", new CarolineNPC());
        npc.put("Dasco", new DascoNPC());
        npc.put("EMily", new EmilyNPC());
        npc.put("Perry", new PerryNPC());

        return npc;
    }
}
