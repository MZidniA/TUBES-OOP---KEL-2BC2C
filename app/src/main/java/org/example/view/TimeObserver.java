package org.example.view; 

import java.time.LocalTime;

import org.example.model.enums.Season;
import org.example.model.enums.Weather;

public interface TimeObserver {
    void onTimeUpdate(int day, Season season, Weather weather, LocalTime time);
}