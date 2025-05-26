// Lokasi: src/main/java/org/example/view/TimeObserver.java (atau package lain yang sesuai)
package org.example.view; // Atau sesuaikan dengan package view Anda

import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import java.time.LocalTime;

public interface TimeObserver {
    void onTimeUpdate(int day, Season season, Weather weather, LocalTime time);
}