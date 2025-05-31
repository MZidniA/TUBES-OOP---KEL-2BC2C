// Lokasi: src/main/java/org/example/controller/action/ProposingAction.java
package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Inventory;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.NPC.NPC;
import org.example.model.Player;
import org.example.model.enums.RelationshipStats;

public class ProposingAction implements Action {

    private static final int ENERGY_COST_PROPOSAL_ACCEPTED = -10; // Energi untuk melamar (jika diterima)
    private static final int ENERGY_COST_PROPOSAL_REJECTED = -20; // Energi jika ditolak
    private static final int TIME_PENALTY_REJECTED_HOURS = 1;     // Penalti waktu jika ditolak
    private static final int REQUIRED_HEART_POINTS_BACHELOR = 150;
    private static final String PROPOSAL_RING_ITEM_NAME = "Proposal Ring"; // Asumsi nama item

    private NPC targetNpc;

    /**
     * Konstruktor untuk ProposingAction.
     * @param targetNpc NPC yang akan dilamar.
     */
    public ProposingAction(NPC targetNpc) {
        this.targetNpc = targetNpc;
        // Pastikan ItemDatabase diinisialisasi
        if (!ItemDatabase.isInitialized()) {
            ItemDatabase.initialize();
        }
    }

    @Override
    public String getActionName() {
        return "Propose to " + (targetNpc != null ? targetNpc.getName() : "NPC");
    }

    @Override
    public boolean canExecute(Farm farm) {
        if (targetNpc == null) {
            System.out.println("LOG: No NPC targeted for proposal.");
            return false;
        }

        Player player = farm.getPlayerModel();
        if (player == null) {
            System.out.println("LOG: Player not found.");
            return false;
        }
        Inventory inventory = player.getInventory();

        // 1. Cek apakah pemain sudah punya partner
        if (player.getPartner() != null || targetNpc.getRelationshipsStatus() == RelationshipStats.FIANCE || targetNpc.getRelationshipsStatus() == RelationshipStats.MARRIED ) {
            System.out.println("LOG: " + player.getName() + " or " + targetNpc.getName() + " is already in a committed relationship (fiance/married/has partner).");
            return false;
        }
         // Cek apakah target NPC sudah jadi fiance pemain lain (jika ada multiplayer, atau untuk konsistensi)
        // Untuk single player, cek status targetNpc dan player.partner sudah cukup.


        // 2. Cek Energi Pemain (minimal untuk kasus ditolak, karena itu cost terbesar)
        if (player.getEnergy() < Math.abs(ENERGY_COST_PROPOSAL_REJECTED)) { // Abs karena costnya negatif
            System.out.println("LOG: Not enough energy to propose to " + targetNpc.getName() +
                               ". Need at least " + Math.abs(ENERGY_COST_PROPOSAL_REJECTED) + " energy.");
            return false;
        }

        // 3. Cek apakah pemain memiliki "Proposal Ring"
        Items proposalRing = ItemDatabase.getItem(PROPOSAL_RING_ITEM_NAME);
        if (proposalRing == null) {
            System.err.println("ERROR: Item '" + PROPOSAL_RING_ITEM_NAME + "' not found in ItemDatabase. Proposal cannot proceed.");
            return false;
        }
        if (!inventory.hasItem(proposalRing, 1)) {
            System.out.println("LOG: You need a " + PROPOSAL_RING_ITEM_NAME + " to propose.");
            return false;
        }

        // 4. Cek apakah NPC adalah tipe yang bisa dilamar (bachelor/bachelorette)
        //    Spesifikasi menyebutkan heartPoints bachelor max 150. Asumsikan hanya bachelor NPC yang bisa mencapai 150 dan bisa dilamar.
        //    Jika ada cara eksplisit menandai NPC sebagai 'bachelor', itu lebih baik.
        //    Untuk sekarang, kita asumsikan semua NPC yang di-pass ke action ini adalah kandidat.

        // 5. Cek apakah NPC sudah menjadi fiance (seharusnya sudah tercover di poin 1)
        if (targetNpc.getRelationshipsStatus() == RelationshipStats.FIANCE || targetNpc.getRelationshipsStatus() == RelationshipStats.MARRIED) {
            System.out.println("LOG: " + targetNpc.getName() + " is already engaged or married.");
            return false;
        }
        
        // 6. Cek apakah pemain adalah target lamaran yang valid untuk NPC ini
        //    (misalnya, beberapa game membatasi berdasarkan gender, dll. Spesifikasi tidak menyebutkan, jadi kita abaikan)


        // Semua cek awal lolos. Kondisi penerimaan (heart points) akan dicek di execute.
        return true;
    }

    @Override
    public void execute(Farm farm) {
        // Asumsi canExecute() sudah dipanggil dan true
        Player player = farm.getPlayerModel();
        GameClock gameClock = farm.getGameClock();

        System.out.println(player.getName() + " is proposing to " + targetNpc.getName() + "...");

        // Cek kondisi penerimaan lamaran: Heart Points NPC
        if (targetNpc.getHeartPoints() >= REQUIRED_HEART_POINTS_BACHELOR) {
            // Lamaran DITERIMA
            System.out.println(targetNpc.getName() + ": Yes, I will! I'm so happy!");
            player.decreaseEnergy(Math.abs(ENERGY_COST_PROPOSAL_ACCEPTED)); // Energi berkurang
            System.out.println("- " + player.getName() + "'s energy decreased by " + Math.abs(ENERGY_COST_PROPOSAL_ACCEPTED) + ".");

            // Ubah status NPC menjadi FIANCE
            targetNpc.setRelationshipsStatus(RelationshipStats.FIANCE);
            System.out.println(targetNpc.getName() + " is now your fiance!");

            // Set NPC sebagai partner pemain
            player.setPartner(targetNpc);
            System.out.println(player.getName() + " and " + targetNpc.getName() + " are now engaged!");

            // "Proposal Ring tidak hilang setelah digunakan (reusable)" -> jadi tidak perlu removeItem
            // farm.getPlayerStats().recordEvent("Proposed", targetNpc.getName()); // Contoh event logging
            System.out.println("LAMARAN BERHASIL 💍");
            player.getPlayerStats().incrementNpcVisitInteraction(targetNpc.getName());

        } else {
            // Lamaran DITOLAK
            System.out.println(targetNpc.getName() + ": I'm sorry, " + player.getName() + "... I like you, but I'm not ready for that yet.");
            player.decreaseEnergy(Math.abs(ENERGY_COST_PROPOSAL_REJECTED)); // Energi berkurang lebih banyak
            System.out.println("- " + player.getName() + "'s energy decreased by " + Math.abs(ENERGY_COST_PROPOSAL_REJECTED) + ".");

            // Majukan waktu game
            if (gameClock != null) {
                gameClock.advanceTimeMinutes(TIME_PENALTY_REJECTED_HOURS * 60);
                System.out.println("- Game time advanced by " + TIME_PENALTY_REJECTED_HOURS + " hour(s) due to rejection.");
            }
        }
    }
}