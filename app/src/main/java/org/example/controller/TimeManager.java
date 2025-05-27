// package org.example.controller;

// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.List;

// import org.example.model.Farm;
// import org.example.model.enums.Season;
// import org.example.model.enums.Weather;
// import org.example.view.TimeObserver;

// public class TimeManager {
//     private Thread timeThread;
//     private boolean running;
//     private Farm farm; 
//     private final int REAL_SECOND_TO_GAME_MINUTE = 5; 

//     private List<TimeObserver> observers;

//     public TimeManager(Farm farm) { 
//         this.farm = farm;
//         this.observers = new ArrayList<>();
//     }

//     public void addObserver(TimeObserver observer) {
//         observers.add(observer);
//     }

//     private void notifyObservers() {
//         for (TimeObserver observer : observers) {
//             observer.onTimeUpdate(farm.getCurrentDay(), farm.getCurrentSeason(), farm.getCurrentWeather(), farm.getCurrentTime());
//         }
//     }

//     public void startTimeSystem() {
//         if (timeThread == null || !timeThread.isAlive()) {
//             running = true;
//             timeThread = new Thread(() -> {
//                 long lastTime = System.currentTimeMillis();

//                 while (running) {
//                     long currentTimeMillis = System.currentTimeMillis();
//                     long elapsed = currentTimeMillis - lastTime;

//                     if (elapsed >= 1000) { 
//                         updateGameTime(REAL_SECOND_TO_GAME_MINUTE);
//                         lastTime = currentTimeMillis; 
//                         notifyObservers();
//                     }

//                     try {
//                         Thread.sleep(100);
//                     } catch (InterruptedException e) {
//                         Thread.currentThread().interrupt(); 
//                         running = false;
//                     }
//                 }
//             });
//             timeThread.setName("GameTimeThread"); 
//             timeThread.start();
//         }
//     }

//     public void stopTimeSystem() {
//         running = false;
//         if (timeThread != null) {
//             timeThread.interrupt();
//         }
//     }

//     private void updateGameTime(int minutesToAdd) {
//         LocalTime newTime = farm.getCurrentTime().plusMinutes(minutesToAdd);
//         farm.setCurrentTime(newTime);

//         if (newTime.isBefore(farm.getCurrentTime())) { 
//              farm.setCurrentDay(farm.getCurrentDay() + 1);

//             if (farm.getCurrentDay() % 10 == 1) { 
//                 Season nextSeason = null;
//                 switch (farm.getCurrentSeason()) {
//                     case SPRING:
//                         nextSeason = Season.SUMMER;
//                         break;
//                     case SUMMER:
//                         nextSeason = Season.FALL;
//                         break;
//                     case FALL:
//                         nextSeason = Season.WINTER;
//                         break;
//                     case WINTER:
//                         nextSeason = Season.SPRING; 
//                         break;
//                 }
//                 farm.setCurrentSeason(nextSeason); 
//             }

//             if (Math.random() < 0.3) { 
//                 farm.setCurrentWeather(Weather.RAINY);
//             } else {
//                 farm.setCurrentWeather(Weather.SUNNY);
//             }
//         }
//     }
// }