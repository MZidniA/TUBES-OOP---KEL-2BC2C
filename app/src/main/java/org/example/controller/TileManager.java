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
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputSummer.png"));
            
            // Tree
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Tree1.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Tree2.png"));
            tile[2].collision = true;
            
            // House
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/2.png"));
            tile[3].collision = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/3.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/4.png"));
            tile[5].collision = true;

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/5.png"));
            tile[6].collision = true;

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/7.png"));
            tile[7].collision = true;

            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/tiles/8.png"));
            tile[8].collision = true;

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/tiles/9.png"));
            tile[9].collision = true;

            tile[10] = new Tile();
            tile[10].image = ImageIO.read(getClass().getResourceAsStream("/tiles/10.png"));
            tile[10].collision = true;

            tile[11] = new Tile();
            tile[11].image = ImageIO.read(getClass().getResourceAsStream("/tiles/11.png"));
            tile[11].collision = true;

            tile[12] = new Tile();
            tile[12].image = ImageIO.read(getClass().getResourceAsStream("/tiles/12.png"));
            tile[12].collision = true;

            tile[13] = new Tile();
            tile[13].image = ImageIO.read(getClass().getResourceAsStream("/tiles/18.png"));
            tile[13].collision = true;

            tile[14] = new Tile();
            tile[14].image = ImageIO.read(getClass().getResourceAsStream("/tiles/17.png"));
            tile[14].collision = true;

            tile[15] = new Tile();
            tile[15].image = ImageIO.read(getClass().getResourceAsStream("/tiles/16.png"));
            tile[15].collision = true;

            tile[16] = new Tile();
            tile[16].image = ImageIO.read(getClass().getResourceAsStream("/tiles/15.png"));
            tile[16].collision = true;

            tile[17] = new Tile();
            tile[17].image = ImageIO.read(getClass().getResourceAsStream("/tiles/14.png"));
            tile[17].collision = true;

            tile[18] = new Tile();
            tile[18].image = ImageIO.read(getClass().getResourceAsStream("/tiles/13.png"));
            tile[18].collision = true;

            tile[19] = new Tile();
            tile[19].image = ImageIO.read(getClass().getResourceAsStream("/tiles/19.png"));
            tile[19].collision = true;

            tile[20] = new Tile();
            tile[20].image = ImageIO.read(getClass().getResourceAsStream("/tiles/20.png"));
            tile[20].collision = true;

            tile[21] = new Tile();
            tile[21].image = ImageIO.read(getClass().getResourceAsStream("/tiles/21.png"));
            tile[21].collision = true;

            tile[22] = new Tile();
            tile[22].image = ImageIO.read(getClass().getResourceAsStream("/tiles/22.png"));
            tile[22].collision = true;

            tile[23] = new Tile();
            tile[23].image = ImageIO.read(getClass().getResourceAsStream("/tiles/23.png"));
            tile[23].collision = true;

            tile[24] = new Tile();
            tile[24].image = ImageIO.read(getClass().getResourceAsStream("/tiles/24.png"));
            tile[24].collision = true;

            tile[25] = new Tile();
            tile[25].image = ImageIO.read(getClass().getResourceAsStream("/tiles/30.png"));
            tile[25].collision = true;

            tile[26] = new Tile();
            tile[26].image = ImageIO.read(getClass().getResourceAsStream("/tiles/29.png"));
            tile[26].collision = true;

            tile[27] = new Tile();
            tile[27].image = ImageIO.read(getClass().getResourceAsStream("/tiles/28.png"));
            tile[27].collision = true;

            tile[28] = new Tile();
            tile[28].image = ImageIO.read(getClass().getResourceAsStream("/tiles/27.png"));
            tile[28].collision = true;

            tile[29] = new Tile();
            tile[29].image = ImageIO.read(getClass().getResourceAsStream("/tiles/26.png"));
            tile[29].collision = true;

            tile[30] = new Tile();
            tile[30].image = ImageIO.read(getClass().getResourceAsStream("/tiles/25.png"));
            tile[30].collision = true;

            tile[31] = new Tile();
            tile[31].image = ImageIO.read(getClass().getResourceAsStream("/tiles/31.png"));
            tile[31].collision = true;

            tile[32] = new Tile();
            tile[32].image = ImageIO.read(getClass().getResourceAsStream("/tiles/32.png"));
            tile[32].collision = true;

            tile[33] = new Tile();
            tile[33].image = ImageIO.read(getClass().getResourceAsStream("/tiles/33.png"));
            tile[33].collision = true;

            tile[34] = new Tile();
            tile[34].image = ImageIO.read(getClass().getResourceAsStream("/tiles/34.png"));
            tile[34].collision = true;

            tile[35] = new Tile();
            tile[35].image = ImageIO.read(getClass().getResourceAsStream("/tiles/35.png"));
            tile[35].collision = true;

            tile[36] = new Tile();
            tile[36].image = ImageIO.read(getClass().getResourceAsStream("/tiles/36.png"));
            tile[36].collision = true;
            
            // Path 
            tile[37] = new Tile();
            tile[37].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Path.png"));

            // Rumput
            tile[38] = new Tile();
            tile[38].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCornerKiri.png"));

            tile[39] = new Tile();
            tile[39].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputKiriBawah.png"));

            tile[40] = new Tile();
            tile[40].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputKananBawah.png"));

            tile[41] = new Tile();
            tile[41].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingKiriPojok.png"));

            tile[42] = new Tile();
            tile[42].image = ImageIO.read(getClass().getResourceAsStream("/tiles/UnderTebingKiriPojok.png"));
            tile[42].collision = true;
            
            tile[43] = new Tile();
            tile[43].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengah.png"));
            tile[43].collision = true;

            tile[44] = new Tile();
            tile[44].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengah2.png"));
            tile[44].collision = true;
            
            tile[45] = new Tile();
            tile[45].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingKiriAtas.png"));

            tile[46] = new Tile();
            tile[46].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengahKanan.png"));

            tile[47] = new Tile();
            tile[47].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengahKanan1.png"));
            tile[47].collision = true;

            tile[48] = new Tile();
            tile[48].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengahKiri1.png"));
            tile[48].collision = true;

            tile[49] = new Tile();
            tile[49].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingKiriPojok.png"));

            tile[50] = new Tile();
            tile[50].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengahKanan2.png"));

            tile[51] = new Tile();
            tile[51].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingTengahKananPojok.png"));
            tile[51].collision = true;

            tile[52] = new Tile();
            tile[52].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TebingBawah52.png"));
            tile[52].collision = true;

            tile[53] = new Tile();
            tile[53].image = ImageIO.read(getClass().getResourceAsStream("/tiles/53.png"));
            tile[53].collision = true;

            tile[54] = new Tile();
            tile[54].image = ImageIO.read(getClass().getResourceAsStream("/tiles/54.png"));
            tile[54].collision = true;

            tile[55] = new Tile();
            tile[55].image = ImageIO.read(getClass().getResourceAsStream("/tiles/55.png"));

            tile[56] = new Tile();
            tile[56].image = ImageIO.read(getClass().getResourceAsStream("/tiles/56.png"));
            
            tile[57] = new Tile();
            tile[57].image = ImageIO.read(getClass().getResourceAsStream("/tiles/57.png"));
            tile[57].collision = true;

            tile[58] = new Tile();
            tile[58].image = ImageIO.read(getClass().getResourceAsStream("/tiles/58.png"));
            tile[58].collision = true;

            tile[59] = new Tile();
            tile[59].image = ImageIO.read(getClass().getResourceAsStream("/tiles/59.png"));
            tile[59].collision = true;

            tile[60] = new Tile();
            tile[60].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputKiriBawah.png"));

            tile[61] = new Tile();
            tile[61].image = ImageIO.read(getClass().getResourceAsStream("/tiles/ShippingBin.png"));
            tile[61].collision = true;

            tile[62] = new Tile();
            tile[62].image = ImageIO.read(getClass().getResourceAsStream("/tiles/62.png"));
            tile[62].collision = true;

            tile[63] = new Tile();
            tile[63].image = ImageIO.read(getClass().getResourceAsStream("/tiles/63.png"));
            tile[63].collision = true;

            tile[64] = new Tile();
            tile[64].image = ImageIO.read(getClass().getResourceAsStream("/tiles/64.png"));
            tile[64].collision = true;

            tile[65] = new Tile();
            tile[65].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner3.png"));

            tile[66] = new Tile();
            tile[66].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner5.png"));

            tile[67] = new Tile();
            tile[67].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner4.png"));

            tile[68] = new Tile();
            tile[68].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner2.png"));

            tile[69] = new Tile();
            tile[69].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputKananBawah.png"));

            tile[70] = new Tile();
            tile[70].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Rumput70.png"));

            tile[71] = new Tile();
            tile[71].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputRata.png"));

            tile[72] = new Tile();
            tile[72].image = ImageIO.read(getClass().getResourceAsStream("/tiles/TanahLubang72.png"));

            tile[73] = new Tile();
            tile[73].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCornerKanan.png"));

            tile[74] = new Tile();
            tile[74].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Rumput74.png"));

            // Kolam
            tile[75] = new Tile();
            tile[75].image = ImageIO.read(getClass().getResourceAsStream("/tiles/75.png"));

            tile[76] = new Tile();
            tile[76].image = ImageIO.read(getClass().getResourceAsStream("/tiles/76.png"));
            
            tile[77] = new Tile();
            tile[77].image = ImageIO.read(getClass().getResourceAsStream("/tiles/77.png"));

            tile[78] = new Tile();
            tile[78].image = ImageIO.read(getClass().getResourceAsStream("/tiles/78.png"));
            tile[78].collision = true;

            tile[79] = new Tile();
            tile[79].image = ImageIO.read(getClass().getResourceAsStream("/tiles/79.png"));
            tile[79].collision = true;

            tile[80] = new Tile();
            tile[80].image = ImageIO.read(getClass().getResourceAsStream("/tiles/80.png"));

            tile[81] = new Tile();
            tile[81].image = ImageIO.read(getClass().getResourceAsStream("/tiles/81.png"));
            tile[81].collision = true;

            tile[82] = new Tile();
            tile[82].image = ImageIO.read(getClass().getResourceAsStream("/tiles/82.png"));

            tile[83] = new Tile();
            tile[83].image = ImageIO.read(getClass().getResourceAsStream("/tiles/83.png"));
            tile[83].collision = true;

            tile[84] = new Tile();
            tile[84].image = ImageIO.read(getClass().getResourceAsStream("/tiles/84.png"));
            tile[84].collision = true;

            tile[85] = new Tile();
            tile[85].image = ImageIO.read(getClass().getResourceAsStream("/tiles/85.png"));
            tile[85].collision = true;

            // Rumput Part 2
            tile[86] = new Tile();
            tile[86].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner86.png"));

            tile[87] = new Tile();
            tile[87].image = ImageIO.read(getClass().getResourceAsStream("/tiles/RumputCorner87.png"));

            tile[89] = new Tile();
            tile[89].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Rumput89.png"));

            // Tile
            tile[88] = new Tile();
            tile[88].image = ImageIO.read(getClass().getResourceAsStream("/tiles/PlantedTile.png"));
            tile[88].collision = true;

            tile[90] = new Tile();
            tile[90].image = ImageIO.read(getClass().getResourceAsStream("/tiles/UnplantedTile.png"));
            tile[90].collision = true;


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
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
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
