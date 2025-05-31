package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.Player;

public class MovingAction implements Action{
    
    private int newX;
    private int newY;

    public MovingAction(int x, int y) {
        this.newX = x;
        this.newY = y;
    }

    @Override
    public String getActionName() {
        return "Moving to (" + newX + ", " + newY + ")";
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();


        if (player.getTileX() == newX && player.getTileY() == newY) {
            //System.out.println("Kamu sudah berada di lokasi tersebut.");
            return false;
        }


        return true;
    }

    @Override
    public void execute(Farm farm) {
        Player player = farm.getPlayerModel();

        if (!canExecute(farm)) return;

        player.setTileX(newX);
        player.setTileY(newY);
    }
}
