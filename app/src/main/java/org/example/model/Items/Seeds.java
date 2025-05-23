package org.example.model.Items;

import org.example.model.enums.Season;

public class Seeds extends Items {
    private Season season;
    private int DaystoHarvest;

    public Seeds(String name, int sellprice, int buyprice, Season season, int DaystoHarvest) {
        super(name, sellprice, buyprice);
        this.season = season;
        this.DaystoHarvest = DaystoHarvest;
    }

    public Season getSeason() {
        return season;
    }
    public void setSeason(Season season) {

        this.season = season;
    }
    public int getDaystoHarvest() {
        return DaystoHarvest;
    }
    public void setDaystoHarvest(int DaystoHarvest) {
        this.DaystoHarvest = DaystoHarvest;
    }
    
}
