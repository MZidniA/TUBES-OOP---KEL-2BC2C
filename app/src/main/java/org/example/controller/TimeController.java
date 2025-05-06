package org.example.controller;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import org.example.model.GameTime;
import org.example.model.Season;

public class TimeController {
    private final GameTime gameTime;
    private Timer timer;

    public TimeController(GameTime gameTime) {
        this.gameTime = gameTime;
    }

    public void updateTime() {
        gameTime.setCurrTime(gameTime.getCurrTime().plusMinutes(5));
        if (gameTime.getCurrTime().equals(LocalTime.MIDNIGHT)) {
            skipToNextDay();
        }
    }

    public void skipToNextDay() {
        gameTime.setCurrDay(gameTime.getCurrDay() + 1);
        gameTime.setCurrTime(LocalTime.of(6, 0));
        if (gameTime.getCurrDay() > 10) {
            switchSeason();
        }
    }

    private void switchSeason() {
        Season[] seasons = Season.values();
        int next = (gameTime.getCurrSeason().ordinal() + 1) % seasons.length;
        gameTime.setCurrSeason(seasons[next]);
        gameTime.setCurrDay(1);
    }

    public void startGameClock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { updateTime(); }
        }, 0, 1000);
    }
}
