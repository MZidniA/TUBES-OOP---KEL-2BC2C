package org.example.controller.action;

import org.example.controller.GameController;
import org.example.model.Farm;
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
        int energySaatTidurDimulai = player.getEnergy();

        player.setSleepReason(SleepReason.NORMAL);

        int maxEnergy = player.getMaxEnergy();
        int energyForNextDay;

        if (energySaatTidurDimulai < (0.1 * maxEnergy) && energySaatTidurDimulai > 0) {
            energyForNextDay = (int)(maxEnergy * 0.5);
        } else if (energySaatTidurDimulai <= 0) {
            energyForNextDay = 10;
        } else {
            energyForNextDay = maxEnergy;
        }
        
        player.setEnergy(energyForNextDay);

        if (controller != null) {
            controller.initiateSleepSequence();
        } else {
            System.err.println("SleepingAction ERROR: GameController adalah null!");
        }
    }
}