    package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;


public class WatchingAction implements Action{

    public static final int ENERGY_COST = 5;
    public static final int TIME_COST_MINUTES = 15;

    @Override
    public String getActionName() {
        return "Watching";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (player.getEnergy() < ENERGY_COST) {
            return false;
        }

        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        player.decreaseEnergy(ENERGY_COST);
        farm.getGameClock().advanceTimeMinutes(TIME_COST_MINUTES);
    }
}
    

