package org.example.view;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

import org.example.controller.GamePanel;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10]; // Jumlah jenis tile saat ini: 0–6
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
        }

        public void getTileImage() {
        try {
            tile[0] = new Tile(); // Grass
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Tillable_Land.png"));

            tile[1] = new Tile(); // House
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/House.png"));

            tile[2] = new Tile(); // Shipping Bin
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/ShippingBin.png"));

           

            tile[4] = new Tile(); // Tilled land
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Tilled_Land.png"));

            tile[5] = new Tile(); // Planted land
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Planted_Land.png"));
            tile[6] = new Tile(); // Pond
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Pond.png"));

        } catch (IOException e) {
            System.err.println("Failed to load tile images:");
            e.printStackTrace();
        }
    }

    public void loadMap() {
        try {
            InputStream is = getClass().getResourceAsStream("/map/FarmMap.txt");
            if (is == null) {
                System.err.println("Map file not found: /map/FarmMap.txt");
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;

            while (row < gp.maxScreenRow) {
                String line = br.readLine();
                if (line == null) break; // prevent NPE if file is short

                String[] numbers = line.trim().split(" ");
                for (int col = 0; col < gp.maxScreenCol && col < numbers.length; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }

                row++;
            }

            br.close();

        } catch (Exception e) {
            System.err.println("Error loading map:");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int tileNum = mapTileNum[col][row];
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;

                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
                    g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}
