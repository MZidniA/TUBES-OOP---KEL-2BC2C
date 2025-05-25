package org.example.controller;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[100];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map.txt");
    }

    public void getTileImage() {
        setup(0, "RumputSummer", false);
        setup(1, "Tree1", true);
        setup(2, "Tree2", true);
        setup(3, "2", true); 
        setup(4, "3", true);
        setup(5, "4", true);
        setup(6, "5", true);
        setup(7, "7", true);
        setup(8, "8", true);
        setup(9, "9", true);
        setup(10, "10", true);
        setup(11, "11", true);
        setup(12, "12", true);
        setup(13, "18", true);
        setup(14, "17", true);
        setup(15, "16", true);
        setup(16, "15", true);
        setup(17, "14", true);
        setup(18, "13", true);
        setup(19, "19", true);
        setup(20, "20", true);
        setup(21, "21", true);
        setup(22, "22", true);
        setup(23, "23", true);
        setup(24, "24", true);
        setup(25, "30", true);
        setup(26, "29", true);
        setup(27, "28", true);
        setup(28, "27", true);
        setup(29, "26", true);
        setup(30, "25", true);
        setup(31, "31", true);
        setup(32, "32", true);
        setup(33, "33", true);
        setup(34, "Path", true); 
        setup(35, "35", true);
        setup(36, "36", true);
        setup(37, "Path", false); 
        setup(38, "RumputCornerKiri", false);
        setup(39, "RumputKiriBawah", false);
        setup(40, "RumputKananBawah", false);
        setup(41, "TebingKiriPojok", false); 
        setup(42, "UnderTebingKiriPojok", true);
        setup(43, "TebingTengah", true);
        setup(44, "TebingTengah2", true);
        setup(45, "TebingKiriAtas", false);
        setup(46, "TebingTengahKanan", false);
        setup(47, "TebingTengahKanan1", true);
        setup(48, "TebingTengahKiri1", true);
        setup(49, "TebingKiriPojok", false); 
        setup(50, "TebingTengahKanan2", false);
        setup(51, "TebingTengahKananPojok", true);
        setup(52, "TebingBawah52", true);
        setup(53, "53", true);
        setup(54, "54", true);
        setup(55, "55", false); 
        setup(56, "56", false);
        setup(57, "57", true);
        setup(58, "58", true);
        setup(59, "59", true);
        setup(60, "RumputKiriBawah", false); 
        setup(61, "Path", true); 
        setup(62, "62", true);
        setup(63, "63", true);
        setup(64, "64", true);
        setup(65, "RumputCorner3", false);
        setup(66, "RumputCorner5", false);
        setup(67, "RumputCorner4", false);
        setup(68, "RumputCorner2", false);
        setup(69, "RumputKananBawah", false); 
        setup(70, "Rumput70", false);
        setup(71, "RumputRata", false);
        setup(72, "TanahLubang72", false); 
        setup(73, "RumputCornerKanan", false);
        setup(74, "Rumput74", false);
        setup(75, "75", false); 
        setup(76, "Path", false); 
        setup(77, "77", false);
        setup(78, "78", true);
        setup(79, "79", true);
        setup(80, "80", false); 
        setup(81, "81", true);
        setup(82, "82", false);
        setup(83, "83", true);
        setup(84, "84", true);
        setup(85, "85", true);
        setup(86, "RumputCorner86", false);
        setup(87, "RumputCorner87", false);
        setup(89, "Rumput89", false);
        setup(88, "Path", true); 
        setup(90, "Path", true);
    }
    

    public void setup(int index, String imageName, boolean collision) {
        
        UtilityTool uTool = new UtilityTool();


        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
    
            int row = 0;
    
            while (row < gp.maxWorldRow) {
                String line = br.readLine();
                if (line == null) break;
    
                String[] numbers = line.split("\\s+");
    
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                }
                row++;
            }
    
            br.close();
            System.out.println("Map loaded successfully.");
        } catch (Exception e) {
            System.out.println("Error loading map: " + e.getMessage());
        }
    }
    

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
    
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
    
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
    
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                    && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                    && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
    
                if (tileNum < tile.length && tile[tileNum] != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY,gp.tileSize, gp.tileSize,null);
                } else {
                    System.out.println("Undefined tile index: " + tileNum);
                }
            }
    
            worldCol++;
    
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }    
}
