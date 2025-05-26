package org.example.model.Items;

import java.util.HashMap;
import java.util.Map;

public class FoodFactory {
    public static Map<String, Food> createFood() {
        Map<String, Food> food = new HashMap<>();

        food.put("Fish n’ Chips", new Food("Fish n’ Chips", 135, 150, 50));
        food.put("Baguette", new Food("Baguette", 80, 100, 25));
        food.put("Sashimi", new Food("Sashimi", 275, 300, 70));
        food.put("Fugu", new Food("Fugu", 135, 0, 50));
        food.put("Wine", new Food("Wine", 90, 100, 20));
        food.put("Pumpkin Pie", new Food("Pumpkin Pie", 100, 120, 35));
        food.put("Veggie Soup", new Food("Veggie Soup", 120, 140, 40));
        food.put("Fish Stew", new Food("Fish Stew", 260, 280, 70));
        food.put("Spakbor Salad", new Food("Spakbor Salad", 250, 0, 70));
        food.put("Fish Sandwich", new Food("Fish Sandwich", 180, 200, 50));
        food.put("The Legends of Spakbor", new Food("The Legends of Spakbor", 2000, 0, 100)); 
        food.put("Cooked Pig’s Head", new Food("Cooked Pig’s Head", 0, 1000, 100)); 
        food.put("Egg", new Food("Egg", 5, 10, 50));

        return food;
    }
}