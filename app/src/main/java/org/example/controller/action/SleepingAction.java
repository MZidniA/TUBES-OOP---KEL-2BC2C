// Lokasi: src/main/java/org/example/controller/action/SleepingAction.java
package org.example.controller.action;

import java.time.LocalTime;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.Player;
import org.example.view.entitas.PlayerView;

public class SleepingAction implements Action {
    private final GameController controller;
    public SleepingAction(GameController controller) {
        this.controller = controller;
    }

    private void skipToMorning(Farm farm) {
        LocalTime now = farm.getGameClock().getCurrentTime();
        int nowMinutes = now.getHour() * 60 + now.getMinute();
        int morningMinutes = 6 * 60;

        int minutesToSkip;
        if (nowMinutes >= morningMinutes) {
            minutesToSkip = (24 * 60 - nowMinutes) + morningMinutes;
        } else {
            minutesToSkip = morningMinutes - nowMinutes;
        }

        farm.getGameClock().advanceTimeMinutes(minutesToSkip);
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
        Player player = farm.getPlayerModel();
        PlayerView playerView = controller.getPlayerViewInstance();
        int tileSize = controller.getTileSize();

        if (!canExecute(farm)) return;

        int maxEnergy = player.getMaxEnergy();
        int currentEnergy = player.getEnergy();
        

        


        if (currentEnergy == 0) {
            player.setEnergy(10);
            System.out.println("Energi habis total. Tidur hanya memulihkan 10 poin.");
        } else if (currentEnergy < (0.1 * maxEnergy)) {
            player.setEnergy(maxEnergy / 2);
            System.out.println("Energi terlalu rendah. Hanya terisi setengah.");
        }  else {
            player.setEnergy(maxEnergy);
            System.out.println("Tidur nyenyak. Energi pulih sepenuhnya.");
        }


        skipToMorning(farm);
        int spawnX = 6 * tileSize;
        int spawnY = 10 * tileSize;
        playerView.worldX = spawnX;
        playerView.worldY = spawnY;
        playerView.direction = "down";
        player.setCurrentHeldItem(null);



        farm.getGameClock().nextDay(null);
        System.out.println("Selamat pagi! Hari ke-" + farm.getGameClock().getDay() + ", pukul " + farm.getGameClock().getCurrentTime());
    }
}
