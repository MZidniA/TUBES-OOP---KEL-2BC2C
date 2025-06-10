package org.example.model;

public class WeatherSystem {
    private int rainyDaysCount = 0;

    public int getRainyDaysCount() { 
        return rainyDaysCount; 
    }

    public void incrementRainyDays() { 
        rainyDaysCount++; 
    }
    
    public void resetRainyDays() { 
        rainyDaysCount = 0; 
    }
}
