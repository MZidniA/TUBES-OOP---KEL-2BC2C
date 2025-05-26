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
    public int mapTileNum[][][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[500];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map.txt", 0);
        loadMap("/maps/beachmap.txt", 1);
    }

    public void getTileImage() {
        // Farm Map
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

        // Beach and Ocean Map
        setup(91, "LightSandPatch", false);
        setup(92, "DarkSandPatch", false);
        setup(93, "Sea", true);
        setup(94, "BeachTree1", true);
        setup(95, "BeachTree2", true);
        setup(96, "SandMix2", false);
        setup(97, "Beach4", false);
        setup(98, "Beach2", false);
        setup(99, "99", true);
        setup(100, "Beach5", false);
        setup(101, "SandMix4", false);

        // River Map
        setup(102, "102", false);
        setup(103, "103", false);
        setup(104, "104", false);
        setup(105, "105", false);
        setup(106, "106", false);
        setup(107, "107", false);
        setup(108, "108", false);
        setup(109, "109", false);
        setup(110, "110", false);
        setup(111, "111", false);
        setup(112, "112", false);
        setup(113, "113", false);
        setup(114, "114", true);
        setup(115, "115", true);
        setup(116, "116", true);
        setup(117, "117", true);
        setup(118, "118", true);
        setup(119, "119", true);
        setup(120, "120", true);
        setup(121, "121", true);
        setup(122, "122", true);
        setup(123, "123", true);
        setup(124, "124", true);
        setup(125, "125", true);
        setup(126, "126", false);
        setup(127, "127", true);
        setup(128, "128", false);
        setup(129, "129", false);
        setup(130, "130", false);
        setup(131, "131", false);
        setup(132, "132", false);
        setup(133, "133", false);

        // Town Map
        setup(134, "134", false);
        setup(135, "135", true);
        setup(134, "134", false);
        setup(135, "135", true);
        setup(136, "136", true);
        setup(137, "137", false);
        setup(138, "138", false);
        setup(139, "139", false);
        setup(140, "140", false);
        setup(141, "141", false);
        setup(142, "142", false);
        setup(143, "143", false);
        setup(144, "144", false);
        setup(145, "145", false);
        setup(146, "146", false);
        setup(147, "147", false);
        setup(148, "148", false);
        setup(149, "149", false);
        setup(150, "150", false);
        setup(151, "151", false);
        setup(152, "152", false);
        setup(153, "153", false);
        setup(154, "154", true);
        setup(155, "155", true);
        setup(156, "156", true);
        setup(157, "157", true);
        setup(158, "158", true);
        setup(159, "159", true);
        setup(160, "160", true);
        setup(170, "170", true);
        setup(171, "171", true);
        setup(172, "172", true);
        setup(173, "173", true);
        setup(174, "174", true);
        setup(175, "175", true);
        setup(176, "176", true);
        setup(177, "177", true);
        setup(178, "178", true);
        setup(179, "179", true);
        setup(180, "180", true);
        setup(181, "181", true);
        setup(182, "182", true);
        setup(183, "183", true);
        setup(184, "184", true);
        setup(185, "185", true);
        setup(186, "186", true);
        setup(187, "187", true);
        setup(188, "188", true);
        setup(189, "189", true);
        setup(190, "190", true);
        setup(191, "191", true);
        setup(192, "192", true);
        setup(193, "193", true);
        setup(194, "194", true);
        setup(195, "195", true);
        setup(196, "196", true);
        setup(197, "197", true);
        setup(198, "198", true);
        setup(199, "199", true);
        setup(200, "200", true);
        setup(201, "201", true);
        setup(202, "202", true);
        setup(203, "203", true);
        setup(204, "204", true);
        setup(205, "205", true);
        setup(206, "206", true);
        setup(207, "207", true);
        setup(208, "208", true);
        setup(209, "209", true);
        setup(210, "210", true);
        setup(211, "211", true);
        setup(212, "212", true);
        setup(213, "213", true);
        setup(214, "214", true);
        setup(215, "215", true);
        setup(216, "216", true);
        setup(217, "217", true);
        setup(218, "218", true);
        setup(219, "219", true);
        setup(220, "220", true);
        setup(221, "221", true);
        setup(222, "222", true);
        setup(223, "223", true);
        setup(224, "224", true);
        setup(225, "225", true);
        setup(226, "226", true);
        setup(227, "227", true);
        setup(228, "228", true);
        setup(229, "229", true);
        setup(230, "230", true);
        setup(231, "231", true);
        setup(232, "232", true);
        setup(233, "233", true);
        setup(234, "234", true);
        setup(235, "235", true);
        setup(236, "236", true);
        setup(237, "237", true);
        setup(238, "238", true);
        setup(239, "239", true);
        setup(240, "240", true);
        setup(241, "241", true);
        setup(242, "242", true);
        setup(243, "243", true);
        setup(244, "244", true);
        setup(245, "245", true);
        setup(246, "246", true);
        setup(247, "247", true);
        setup(248, "248", true);
        setup(249, "249", true);
        setup(250, "250", true);
        setup(251, "251", true);
        setup(252, "252", true);
        setup(253, "253", true);
        setup(254, "254", true);
        setup(255, "255", true);
        setup(256, "256", true);
        setup(257, "257", true);
        setup(258, "258", true);
        setup(259, "259", true);
        setup(260, "260", true);
        setup(261, "261", true);
        setup(262, "262", true);
        setup(263, "263", true);
        setup(264, "264", true);
        setup(265, "265", true);
        setup(266, "266", true);
        setup(267, "267", true);
        setup(268, "268", true);
        setup(269, "269", true);
        setup(270, "270", true);
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

    public void loadMap(String filePath, int map) {
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
                    mapTileNum[map][col][row] = num;
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
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
    
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
