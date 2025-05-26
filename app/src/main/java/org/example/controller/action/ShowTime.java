package org.example.controller.action;

import org.example.model.Farm;
import org.example.model.enums.Season;

public class ShowTime implements Action {
    @Override
    public String getActionName() {
        return "Show Time";
    }

    @Override
    public boolean canExecute(Farm farm) {
        // Selalu bisa dilakukan
        return true;
    }

    @Override
    public void execute(Farm farm) {
        Season season = farm.getGameClock().getCurrentSeason();
        int day = farm.getGameClock().getDay();
        String time = farm.getGameClock().getCurrentTime().toString();

        System.out.println("Waktu saat ini di Spakbor Hills");
        System.out.println("Musim: " + season);
        System.out.println("Hari ke: " + day);
        System.out.println("Jam: " + time);
    }

}
