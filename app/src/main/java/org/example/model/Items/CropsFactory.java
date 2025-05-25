package org.example.model.Items;

import java.util.HashMap;
import java.util.Map;

public class CropsFactory {

    public static Map<String, Crops> createCropsMap() {
        Map<String, Crops> crops = new HashMap<>();

        crops.put("Parsnip", new Crops("Parsnip", 35, 50, 1, 3));
        crops.put("Cauliflower", new Crops("Cauliflower", 150, 200, 1,3));
        crops.put("Potato", new Crops("Potato", 80, 0, 1,3));
        crops.put("Wheat", new Crops("Wheat", 30, 50, 3,3));
        crops.put("Blueberry", new Crops("Blueberry", 40, 150, 3,3));
        crops.put("Tomato", new Crops("Tomato", 60, 90, 1, 3));
        crops.put("Hot Pepper", new Crops("Hot Pepper", 40, 0, 1, 3));
        crops.put("Melon", new Crops("Melon", 250, 0, 1,3));
        crops.put("Cranberry", new Crops("Cranberry", 25, 0, 10,3));
        crops.put("Pumpkin", new Crops("Pumpkin", 250, 300, 1,3));
        crops.put("Grape", new Crops("Grape", 10, 100, 20,3));

        return crops;
    }
}
