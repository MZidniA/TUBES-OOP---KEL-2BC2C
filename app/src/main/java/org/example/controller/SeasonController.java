package org.example.controller;

import java.util.List;

import org.example.model.Season;
import org.example.model.SeasonManager;

public class SeasonController {
    private final SeasonManager seasonManager;

    public SeasonController(SeasonManager seasonManager) {
        this.seasonManager = seasonManager;
    }

    public void checkCropSurvival(List<Crop> allCrops, Season currentSeason) {
        for (int i = 0; i < allCrops.size(); i++) {
            Crop crop = allCrops.get(i);
            if (!crop.isCompatibleWith(currentSeason)) {
                allCrops.remove(i);
                i--;
            }
        }
    }

    public boolean validateFishAvailability(Fish fish, Season currentSeason) {
        return seasonManager.getFishForSeason(currentSeason).contains(fish);
    }
}
