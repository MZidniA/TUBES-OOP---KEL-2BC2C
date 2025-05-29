package org.example.model.Map;

import java.util.Random;

import org.example.model.Items.Seeds;
import org.example.model.enums.LocationType;

public class FarmMap {
    private static final int SIZE = 32; 
    private Tile[][] map; 

    public FarmMap() {
        map = new Tile[SIZE][SIZE];
        initializeMap();
    }

    public Tile[][] getMap() {
        return map;
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
            // Pastikan tile yang akan ditanami adalah Tilledland (atau tipe yang sesuai)
            // Atau, Anda bisa mengizinkan penanaman di atas Tillableland jika TillingAction hanya mengubah tile dasar
            // dan tidak membuat objek UnplantedTileObject.
            // Untuk saat ini, kita asumsikan TillingAction sudah membuat Tilledland (sebagai Tile data).
            if (map[col][row] instanceof Tilledland) { // Atau tipe tile dasar tanah yang sudah dicangkul
                map[col][row] = new Plantedland(col, row, seed); // Ganti tile lama dengan Plantedland baru
                System.out.println("FarmMap: Benih '" + seed.getName() + "' ditanam di tile data (" + col + "," + row + "). Tipe tile sekarang: Plantedland.");
                return true;
            } else {
                System.err.println("FarmMap: Tidak bisa menanam benih di (" + col + "," + row + "). Tile bukan Tilledland. Tipe tile saat ini: " + (map[col][row] != null ? map[col][row].getClass().getSimpleName() : "null"));
                return false;
            }
        }
        System.err.println("FarmMap: Gagal menanam benih di (" + col + "," + row + "). Koordinat tidak valid atau benih null.");
        return false;
    }

    /**
     * Mengembalikan tile ke kondisi Tillableland setelah panen atau tanaman mati.
     * @param col Kolom tile.
     * @param row Baris tile.
     */
    public void setTileToTillable(int col, int row) {
        if (col >= 0 && col < getSize() && row >= 0 && row < getSize()) {
            map[col][row] = new Tillableland(col, row); // Ganti dengan Tillableland baru
            System.out.println("FarmMap: Tile (" + col + "," + row + ") diubah kembali menjadi Tillableland.");
        }
    }



    private void initializeMap() {
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = new Tillableland(i, j);
            }
        }

        int houseX = random.nextInt(SIZE - 7) + 1;
        int houseY = random.nextInt(SIZE - 7) + 1; 
        placeObject(houseX, houseY, 6, 6, House.class);

        placeShippingBin(houseX, houseY);

        int pondX = random.nextInt(SIZE - 5) + 1;
        int pondY = random.nextInt(SIZE - 4) + 1;
        placeObject(pondX, pondY, 4, 3, Pond.class);
    }

    private void placeObject(int startX, int startY, int width, int height, Class<? extends Tile> tileClass) {
        for (int i = startX; i < startX + width; i++) {
            for (int j = startY; j < startY + height; j++) {
                try {
                    map[i][j] = tileClass.getConstructor(int.class, int.class).newInstance(i, j);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void placeShippingBin(int houseX, int houseY) {
        int startX = houseX + 4;
        int startY = houseY + 6;     

        if (isAreaFree(startX, startY, 2, 3)) {
            placeObject(startX, startY, 2, 3, ShippingBin.class);
        } else {
            System.err.println("Failed to place ShippingBin to the right of House.");
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