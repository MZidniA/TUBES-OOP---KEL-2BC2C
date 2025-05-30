package org.example.model.Items;


import java.util.HashMap;
import java.util.Map;
import java.util.EnumSet;
import org.example.model.enums.Season;

public class SeedFactory {
    public static Map<String, Items> createSeeds() {
        Map<String, Items> seeds = new HashMap<>();

        seeds.put("Parsnip Seeds", new Seeds("Parsnip Seeds", 10, 20, EnumSet.of(Season.SPRING), 1));
        seeds.put("Cauliflower Seeds", new Seeds("Cauliflower Seeds", 40, 80, EnumSet.of(Season.SPRING), 5));
        seeds.put("Potato Seeds", new Seeds("Potato Seeds", 25, 50, EnumSet.of(Season.SPRING), 3));
        seeds.put("Wheat Seeds", new Seeds("Wheat Seeds", 30, 60, EnumSet.of(Season.SPRING, Season.FALL), 1));

        seeds.put("Blueberry Seeds", new Seeds("Blueberry Seeds", 40, 80, EnumSet.of(Season.SUMMER), 7));
        seeds.put("Tomato Seeds", new Seeds("Tomato Seeds", 25, 50, EnumSet.of(Season.SUMMER), 3));
        seeds.put("Hot Pepper Seeds", new Seeds("Hot Pepper Seeds", 20, 40, EnumSet.of(Season.SUMMER), 1));
        seeds.put("Melon Seeds", new Seeds("Melon Seeds", 40, 80, EnumSet.of(Season.SUMMER), 4));

        seeds.put("Cranberry Seeds", new Seeds("Cranberry Seeds", 50, 100, EnumSet.of(Season.FALL), 2));
        seeds.put("Pumpkin Seeds", new Seeds("Pumpkin Seeds", 75, 150, EnumSet.of(Season.FALL), 7));

        seeds.put("Grape Seeds", new Seeds("Grape Seeds", 30, 60, EnumSet.of(Season.FALL), 3));

        return seeds;
    }
}