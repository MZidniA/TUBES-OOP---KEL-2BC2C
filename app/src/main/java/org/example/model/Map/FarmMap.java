package org.example.model.Map;

import java.util.Random;

import org.example.model.Items.Seeds;
import org.example.model.enums.LocationType;

public class FarmMap {
    private static final int SIZE = 32;
    private Tile[][] map;

    public FarmMap() {
        map = new Tile[SIZE][SIZE];
    }

    public Tile[][] getMap() {
        return map;
    }

    public LocationType getFarmLocation() {
        return LocationType.FARM;
    }

    public LocationType getFarmLocation() {
        return LocationType.FARM;
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            return map[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
            map[x][y] = tile;
        }
    }

    public int getSize() {
        return SIZE;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public boolean plantSeedOnTile(int col, int row, Seeds seed) {
        if (col >= 0 && col < getSize() && row >= 0 && row < getSize() && seed != null) {
            if (map[col][row] instanceof Tilledland) {
                map[col][row] = new Plantedland(col, row, seed);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void setTileToTillable(int col, int row) {
        if (col >= 0 && col < getSize() && row >= 0 && row < getSize()) {
            map[col][row] = new Tillableland(col, row);

        }
    }

    private boolean isAreaFree(int startX, int startY, int width, int height) {
        if (startX < 1 || startY < 1 || startX + width > SIZE - 1 || startY + height > SIZE - 1) {
            return false;
        }

        for (int i = startX; i < startX + width; i++) {
            for (int j = startY; j < startY + height; j++) {
                if (!(map[i][j] instanceof Tillableland)) {
                    return false;
                }
            }
        }

        return true;
    }
}