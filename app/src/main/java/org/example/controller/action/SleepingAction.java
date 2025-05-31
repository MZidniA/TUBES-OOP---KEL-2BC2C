// Lokasi: src/main/java/org/example/controller/action/SleepingAction.java
package org.example.controller.action;

import java.time.LocalTime;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Player;
import org.example.model.enums.SleepReason;


public class SleepingAction implements Action {
    private final GameController controller;
    public SleepingAction(GameController controller) {
        this.controller = controller;
    }

    
    @Override
    public String getActionName() {
        return "Tidur";
    }

    @Override
    public boolean canExecute(Farm farm) {
        return true;
    }

     @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) return;


        Player player = farm.getPlayerModel();
        player.setSleepReason(SleepReason.NORMAL); // Tandai tidur normal

    
        GameClock gameClock = farm.getGameClock(); //
        LocalTime currentTime = gameClock.getCurrentTime(); //
        int maxEnergy = player.getMaxEnergy(); //
        int energyForNextDay;
        String endOfDayMessage = "Kamu Tertidur Dengan Nyenyak.";

        if (currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(LocalTime.of(2,0))) {
            energyForNextDay = (int)(maxEnergy * 0.75);
            endOfDayMessage = "Kamu Kelelahan Karena Telat Tidur.";
        } else if (currentTime.isBefore(LocalTime.MIDNIGHT) && currentTime.getHour() >= 22) { 
             energyForNextDay = maxEnergy; 
        }
        else { 
            energyForNextDay = maxEnergy; 
        }
        player.setEnergy(energyForNextDay); 
        controller.processEndOfDayEvents();

        if (controller.getGameStateUI() != null) {
            controller.getGameStateUI().setEndOfDayInfo(endOfDayMessage, controller.getFarmModel().getGoldFromLastShipment());
        }
        controller.getGameState().setGameState(controller.getGameState().day_report);
        if (controller.getTimeManager() != null) controller.getTimeManager().stopTimeSystem();
    }
}
