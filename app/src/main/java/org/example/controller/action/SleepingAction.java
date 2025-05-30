// Lokasi: src/main/java/org/example/controller/action/SleepingAction.java
package org.example.controller.action;

import java.time.LocalTime;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Player;
import org.example.model.enums.SleepReason;
import org.example.view.entitas.PlayerView;

public class SleepingAction implements Action {
    private final GameController controller;
    public SleepingAction(GameController controller) {
        this.controller = controller;
    }

    
    @Override
    public String getActionName() {
        return "Tidur (Sleep)";
    }

    @Override
    public boolean canExecute(Farm farm) {
        return true;
    }

     @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) return;

        System.out.println("SleepingAction: Player initiated sleep. Preparing end-of-day report.");
        Player player = farm.getPlayerModel();
        player.setSleepReason(SleepReason.NORMAL); // Tandai tidur normal

        // Atur energi untuk HARI BERIKUTNYA (akan diterapkan setelah report)
        // Ini bisa disimpan sementara atau langsung di-set jika PlayerStats menghandle energi antar hari.
        // Untuk saat ini, biarkan GameController.proceedToNextDayFromReport mengatur energi akhir.
        // SleepingAction hanya menentukan ALASAN tidur.
        // Contoh:
        GameClock gameClock = farm.getGameClock(); //
        LocalTime currentTime = gameClock.getCurrentTime(); //
        int maxEnergy = player.getMaxEnergy(); //
        int energyForNextDay;
        String endOfDayMessage = "You slept soundly.";

        if (currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(LocalTime.of(2,0))) {
            energyForNextDay = (int)(maxEnergy * 0.75);
            endOfDayMessage = "You went to bed late, feeling a bit tired.";
            System.out.println("SleepingAction: Late sleep detected. Next day energy target: 75%");
        } else if (currentTime.isBefore(LocalTime.MIDNIGHT) && currentTime.getHour() >= 22) { // Tidur jam 10-12 malam
             energyForNextDay = maxEnergy; // Pulih penuh
             System.out.println("SleepingAction: Normal sleep time. Next day energy target: 100%");
        }
        else { // Tidur terlalu awal atau kondisi lain
            energyForNextDay = maxEnergy; // Asumsi pulih penuh
            System.out.println("SleepingAction: Regular sleep. Next day energy target: 100%");
        }
        // Sebenarnya, energi akan di-set di GameController.proceedToNextDayFromReport()
        // Di sini kita hanya menentukan pesan.
        // Player.energy akan di-set di GameController setelah player bangun


        // Proses event akhir hari (tanaman, kalkulasi shipping)
        controller.processEndOfDayEvents();

        // Pindah ke state layar laporan
        if (controller.getGameStateUI() != null) {
            controller.getGameStateUI().setEndOfDayInfo(endOfDayMessage, controller.getFarmModel().getGoldFromLastShipment());
        }
        controller.getGameState().setGameState(controller.getGameState().day_report);
        if (controller.getTimeManager() != null) controller.getTimeManager().stopTimeSystem();
    }
}
