package org.example.model;

import java.time.LocalTime;
import java.util.Random;

import org.example.model.enums.Season;
import org.example.model.enums.Weather;

public class GameClock {
    private LocalTime currentTime;
    private int day;
    private Season currentSeason;
    private Weather todayWeather;
    private final Random random = new Random();

    private int daysIntoSeason;
    private int rainyDaysThisSeason;

    public GameClock() {
        this.currentTime = LocalTime.of(6, 0); 
        this.day = 1;
        this.currentSeason = Season.SPRING;
        this.daysIntoSeason = 1;
        this.rainyDaysThisSeason = 0;
        randomizeWeatherForDay(); 
    }

    public void advanceTimeMinutes(int minutes) {
        this.currentTime = this.currentTime.plusMinutes(minutes);
    }

    public void nextDay(PlayerStats playerstats) {
        this.day++;
        this.daysIntoSeason++;

        if (playerstats != null) {
            playerstats.incrementDaysPlayed(); 
        }
  
        if (this.daysIntoSeason > 10) {
            nextSeason();
        } else {
            randomizeWeatherForDay();
        }

        this.currentTime = LocalTime.of(6, 0); 
    }

    public void setDay(int newDay) {
        if (newDay > 0) {
            this.day = newDay;
            this.daysIntoSeason = (newDay - 1) % 10 + 1; 
            if (this.daysIntoSeason == 1) {
                nextSeason(); 
            } else {
                randomizeWeatherForDay();
            }
        }
    }

    private void nextSeason() {
        int nextSeasonOrdinal = (this.currentSeason.ordinal() + 1) % Season.values().length;
        this.currentSeason = Season.values()[nextSeasonOrdinal];
        this.daysIntoSeason = 1; 
        this.rainyDaysThisSeason = 0; 
        randomizeWeatherForDay(); 
    }

    private void randomizeWeatherForDay() {
        int daysRemainingInSeason = 10 - (this.daysIntoSeason -1);
        int rainyDaysNeeded = 2 - this.rainyDaysThisSeason;

        if (rainyDaysNeeded > 0 && rainyDaysNeeded >= daysRemainingInSeason) {
            this.todayWeather = Weather.RAINY;
        } else {
            double chanceOfRain = (this.rainyDaysThisSeason < 2) ? 0.25 : 0.10; 
            if (random.nextDouble() < chanceOfRain) {
                this.todayWeather = Weather.RAINY;
            } else {
                this.todayWeather = Weather.SUNNY;
            }
        }
        
        if (this.todayWeather == Weather.RAINY) {
            this.rainyDaysThisSeason++;
        }
    }

    public boolean isDayTime() {
        return !currentTime.isBefore(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(18, 0));
    }

    public boolean isNightTime() {
        return !currentTime.isBefore(LocalTime.of(18, 0)) || currentTime.isBefore(LocalTime.of(6, 0));
    }


    public LocalTime getCurrentTime() { 
        return currentTime; 
    }

    public int getDay() {
        return day; 
    }

    public Season getCurrentSeason() {
        return currentSeason; 
    }

    public void setCurrentSeason(Season newSeason) {
        if (newSeason != null) {
            this.currentSeason = newSeason;
            this.daysIntoSeason = 1; 
            this.rainyDaysThisSeason = 0; 
            randomizeWeatherForDay(); 
        }
    }

    public Weather getTodayWeather() {
        return todayWeather; 
    }

    public void setTodayWeather(Weather newWeather) {
        if (newWeather != null) {
            this.todayWeather = newWeather;
            if (newWeather == Weather.RAINY) {
                this.rainyDaysThisSeason++;
            }
        }
    }
    
    public int getDaysIntoSeason() { 
        return daysIntoSeason; 
    }

    public void setCurrentTime(LocalTime newTime) {
        if (newTime != null) {
            this.currentTime = newTime;
        }
    }

    // Add this method to provide weather forecast
    public Weather getWeatherForecast() {
        return Weather.SUNNY; 
    }
    public void advanceTimeByMinutes(Farm farm, int minutes) {
        int totalMinutes = this.currentTime.getHour() * 60 + this.currentTime.getMinute();
        totalMinutes += minutes;

        int newHour = totalMinutes / 60;
        int newMinute = totalMinutes % 60;

        if (newHour >= 24) {
            nextDay(farm.getPlayerStats());
            this.currentTime = LocalTime.of(newHour % 24, newMinute); 
        } else {
            this.currentTime = LocalTime.of(newHour, newMinute);
        }
    }

}
