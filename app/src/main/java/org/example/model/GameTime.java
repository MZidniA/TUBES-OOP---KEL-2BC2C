package org.example.model;

import java.time.LocalTime;


public class GameTime {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAnyTime;

    public GameTime(String start, String end) {
        if (start.equalsIgnoreCase("Any") || end.equalsIgnoreCase("Any")) {
            this.isAnyTime = true;
        } else {
            this.startTime = LocalTime.parse(start);
            this.endTime = LocalTime.parse(end);
            this.isAnyTime = false;
        }
    }

    public boolean isWithin(LocalTime currentTime) {
        if (isAnyTime) return true;
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }

    public boolean isAnyTime() {
        return isAnyTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

