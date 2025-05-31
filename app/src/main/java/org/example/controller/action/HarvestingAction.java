package org.example.controller.action;

import org.example.controller.GameController;
import org.example.controller.UtilityTool;
import org.example.model.Farm;
import org.example.model.Map.FarmMap;
import org.example.model.Map.Plantedland;
import org.example.model.Map.Tile;
import org.example.model.Map.Tilledland;
import org.example.model.Player;
import org.example.model.Items.Crops;
import org.example.model.Items.Seeds;
import org.example.model.GameClock;
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.InteractableObject.PlantedTileObject;
import org.example.view.InteractableObject.UnplantedTileObject;

public class HarvestingAction implements Action {
    private static final int ENERGY_COST = 5;
    private static final int TIME_COST_MINUTES = 5;

    private GameController controller;
    private int targetCol;
    private int targetRow;

    public HarvestingAction(GameController controller, int targetCol, int targetRow) {
        this.controller = controller;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    @Override
    public String getActionName() {
        return "Panen Tanaman (Harvest)";
    }

    private Plantedland getTargetPlantedLandModel(Farm farm) {
        FarmMap farmMap = farm.getFarmMap();
        if (farmMap == null) return null;
        Tile targetDataTile = farmMap.getTile(targetCol, targetRow);
        if (targetDataTile instanceof Plantedland) {
            return (Plantedland) targetDataTile;
        }
        return null;
    }

    @Override
    public boolean canExecute(Farm farm) {
        Player player = farm.getPlayerModel();
        if (player == null || controller == null) return false;
        if (player.getEnergy() < ENERGY_COST) return false;

        Plantedland targetPlantModel = getTargetPlantedLandModel(farm);
        if (targetPlantModel == null) return false;
        if (!targetPlantModel.isHarvestable()) return false;
        
        InteractableObject targetVisualObject = farm.getObjectAtTile(farm.getCurrentMap(), targetCol, targetRow, controller.getTileSize());
        if (!(targetVisualObject instanceof PlantedTileObject)) return false;

        return true;
    }

    @Override
    public void execute(Farm farm) {
        if (!canExecute(farm)) {
            return;
        }

        Player player = farm.getPlayerModel();
        FarmMap farmMap = farm.getFarmMap();
        GameClock gameClock = farm.getGameClock();
        Plantedland targetPlantModel = getTargetPlantedLandModel(farm);

        if (targetPlantModel == null) return;

        player.decreaseEnergy(ENERGY_COST);

        Seeds harvestedSeedType = targetPlantModel.getPlantedSeed();
        Crops yield = null;
        int quantityYielded = 1; 


        String seedName = harvestedSeedType.getName();

        if (seedName.equalsIgnoreCase("Parsnip Seeds")) {
            yield = new Crops("Parsnip",  35, 50, 1, 3);
        } else if (seedName.equalsIgnoreCase("Cauliflower Seeds")) {
            yield = new Crops("Cauliflower", 150, 200, 1, 3);
        } else if (seedName.equalsIgnoreCase("Potato Seeds")) {
            yield = new Crops("Potato",  80, 0, 1, 3);
        } else if (seedName.equalsIgnoreCase("Wheat Seeds")) {
            yield = new Crops("Wheat",  30, 50, 3, 3);
        } else if (seedName.equalsIgnoreCase("Blueberry Seeds")) {
            yield = new Crops("Blueberry", 40, 150, 3, 3);
        } else if (seedName.equalsIgnoreCase("Tomato Seeds")) {
            yield = new Crops("Tomato",  60, 90, 1, 3);
        } else if (seedName.equalsIgnoreCase("Hot Pepper Seeds")) {
            yield = new Crops("Hot Pepper", 40, 0, 1, 3);
        } else if (seedName.equalsIgnoreCase("Melon Seeds")) {
            yield = new Crops("Melon",  250, 0, 1, 3);
        } else if (seedName.equalsIgnoreCase("Cranberry Seeds")) {
            yield = new Crops("Cranberry", 25, 0, 10, 3);
        } else if (seedName.equalsIgnoreCase("Pumpkin Seeds")) {
            yield = new Crops("Pumpkin", 250, 300, 1, 3);
        } else if (seedName.equalsIgnoreCase("Grape Seeds")) {
            yield = new Crops("Grape", 10, 100, 20, 3);
        } else {
            System.err.println("Gada Benih Bernanma" + seedName);
        }

        if (yield != null) {
            quantityYielded = yield.getJumlahcropperpanen(); 
            player.getInventory().addInventory(yield, quantityYielded);
            player.getPlayerStats().recordCropsHarvested(quantityYielded);
            System.out.println("Berhasil memanen " + quantityYielded + " " + yield.getName());
        } else {
            System.err.println("Gagal mendapatkan hasil panen untuk " + seedName);
        }

        farm.removeObjectAtTile(farm.getCurrentMap(), targetCol, targetRow, controller.getTileSize());
       

        farmMap.setTile(targetCol, targetRow, new Tilledland(targetCol, targetRow));

        InteractableObject[][] allObjects = farm.getAllObjects();
        boolean unplanted = false;
        for (int i = 0; i < allObjects[farm.getCurrentMap()].length; i++) {
            if (allObjects[farm.getCurrentMap()][i] == null) {
                
                UnplantedTileObject unplantedTile = new UnplantedTileObject(); // Panggil konstruktor kosong


                unplantedTile.worldX = targetCol * controller.getTileSize();
                unplantedTile.worldY = targetRow * controller.getTileSize(); 
                if (unplantedTile.image != null) {
                    UtilityTool uTool = new UtilityTool(); 
                    unplantedTile.image = uTool.scaleImage(unplantedTile.image, controller.getTileSize(), controller.getTileSize());
                    if (unplantedTile.image == null) {
                        System.err.println("HarvestingAction: Gambar UnplantedTileObject menjadi NULL setelah scaling.");
                    }
                } else {
                    System.err.println("HarvestingAction: Gambar UnplantedTileObject adalah NULL sebelum scaling (gagal load di UnplantedTileObject.loadImage()).");
                }
                
                allObjects[farm.getCurrentMap()][i] = unplantedTile;
                unplanted = true;
                break;
            }
        }
        if (!unplanted) {
            System.err.println("HarvestingAction WARNING: Tidak ada slot kosong untuk UnplantedTileObject baru setelah panen.");
        }

        if (gameClock != null) {
            gameClock.advanceTimeMinutes(TIME_COST_MINUTES);
        }
    }
}   