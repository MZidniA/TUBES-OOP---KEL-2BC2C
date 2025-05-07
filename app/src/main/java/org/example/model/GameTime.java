package org.example.model;

import java.time.LocalTime;

public class GameTime {
    private LocalTime currTime = LocalTime.of(6, 0);
    private int currDay = 1;
    private Season currSeason = Season.SPRING;
    private Weather currWeather = Weather.SUNNY;

    public LocalTime getCurrTime() { 
        return currTime; 
    }

    public void setCurrTime(LocalTime time) { 
        this.currTime = time; 
    }

    public int getCurrDay() { 
        return currDay; 
    }

    public void setCurrDay(int day) { 
        this.currDay = day; 
    }

    public Season getCurrSeason() { 
        return currSeason; 
    }
    public void setCurrSeason(Season season) { 
        this.currSeason = season; 
    }

    public Weather getCurrWeather() { 
        return currWeather; 
    }
    public void setCurrWeather(Weather weather) { 
        this.currWeather = weather; 
    }
}

