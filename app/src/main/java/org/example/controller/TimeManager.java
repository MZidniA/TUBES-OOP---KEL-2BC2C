package org.example.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.example.model.Farm;
import org.example.model.GameClock;
import org.example.model.Player;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.TimeObserver;

public class TimeManager {
    private Thread timeThread;
    private boolean running;
    private final GameClock gameClockModel; 
    private final Farm farmModel; 
    private final int REAL_SECOND_TO_GAME_MINUTE = 5; 

    private List<TimeObserver> observers;

    public TimeManager(Farm farm, GameClock gameClockModel) { // Terima Farm dan GameClock
        this.farmModel = farm;
        this.gameClockModel = gameClockModel;
        this.observers = new ArrayList<>();
    }

    public void addObserver(TimeObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {

        LocalTime currentTime = gameClockModel.getCurrentTime();
        int currentDay = gameClockModel.getDay();
        Season currentSeason = gameClockModel.getCurrentSeason();
        Weather currentWeather = gameClockModel.getTodayWeather();

        for (TimeObserver observer : observers) {
            observer.onTimeUpdate(currentDay, currentSeason, currentWeather, currentTime);
        }
    }

    public void startTimeSystem() {
        if (timeThread == null || !timeThread.isAlive()) {
            running = true;
            timeThread = new Thread(() -> {
                long lastTimeMillis = System.currentTimeMillis();
                LocalTime twoAM = LocalTime.of(2, 0); 
                LocalTime sixAM = LocalTime.of(6, 0); 


                while (running) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long elapsed = currentTimeMillis - lastTimeMillis;

                    if (elapsed >= 1000) { 
                        gameClockModel.advanceTimeMinutes(REAL_SECOND_TO_GAME_MINUTE);
                        lastTimeMillis = currentTimeMillis;
                        notifyObservers(); 


                        Player playerModel = farmModel.getPlayerModel();
                        LocalTime currentGameTime = gameClockModel.getCurrentTime();

                        
                        if (playerModel != null && 
                            (!currentGameTime.isBefore(twoAM) && currentGameTime.isBefore(sixAM)) &&
                            !playerModel.isForceSleepByTime() && !playerModel.isPassedOut() ) {
                            
                            System.out.println("TimeManager: Waktu sudah jam 02:00 atau lebih, memaksa pemain tidur.");
                            playerModel.setForceSleepByTime(true);
                        }
                    }

                    try {
                        Thread.sleep(100); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        running = false;
                    }
                }
            });
            timeThread.setName("GameTimeThread");
            timeThread.start();
        }
    }

    public void stopTimeSystem() {
        running = false;
        if (timeThread != null) {
            timeThread.interrupt();
        }
        System.out.println("TimeManager: Time system stopped.");
    }
}