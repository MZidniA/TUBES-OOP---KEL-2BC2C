package org.example.view;

import org.example.controller.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke; // Untuk bingkai
import java.io.InputStream; // Untuk memuat font

public class GameStateUI {
    GamePanel gp;
    Graphics2D g2;
    Font stardewFont_40, stardewFont_30; // Ganti nama font jika Anda punya
    public int commandNum = 0;

    // Warna tema Stardew Valley (perkiraan)
    Color woodBrown = new Color(139, 69, 19); // Cokelat kayu
    Color lightYellow = new Color(255, 253, 208); // Kuning krem untuk teks
    Color darkTextShadow = new Color(80, 40, 0, 150); // Bayangan teks cokelat tua

    public GameStateUI(GamePanel gp) {
        this.gp = gp;

        // Coba muat font pixelated
        try {
            InputStream is = getClass().getResourceAsStream("/font/slkscr.ttf"); // Sesuaikan path ke font Anda
            if (is != null) {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
                stardewFont_40 = baseFont.deriveFont(40f);
                stardewFont_30 = baseFont.deriveFont(30f);
            } else {
                // Fallback ke Arial jika font tidak ditemukan
                System.err.println("Font kustom tidak ditemukan, menggunakan Arial.");
                stardewFont_40 = new Font("Arial", Font.BOLD, 40);
                stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback jika ada error saat memuat font
            stardewFont_40 = new Font("Arial", Font.BOLD, 40);
            stardewFont_30 = new Font("Arial", Font.PLAIN, 30);
        }
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow); 

        if (gp.gameState.getGameState() == gp.gameState.pause) {
            drawPauseScreen();
        }
    }

    private void drawPauseScreen() {
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize * 3;
        int frameWidth = gp.screenWidth - (gp.tileSize * 8);
        int frameHeight = gp.tileSize * 6; 

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2.setFont(stardewFont_40);
        g2.setColor(lightYellow);
        String text = "Paused";
        int x = getXforCenteredText(text);
        int y = frameY + gp.tileSize + 10; 
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);

        g2.setFont(stardewFont_30);


        text = "Continue";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;

        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);

        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawImage(null, x - gp.tileSize, y - gp.tileSize + 10, gp.tileSize, gp.tileSize, null); // Ganti null dengan gambar panah jika ada
            g2.drawString(">", x - gp.tileSize + 5, y); 
        }


        text = "Exit Game";
        x = getXforCenteredText(text);
        y += gp.tileSize + 10; 
        g2.setColor(darkTextShadow);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(lightYellow);
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawImage(null, x - gp.tileSize, y - gp.tileSize + 10, gp.tileSize, gp.tileSize, null); // Ganti null dengan gambar panah
            g2.drawString(">", x - gp.tileSize + 5, y);
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        Color windowBackgroundColor = new Color(101, 67, 33, 220); // Cokelat tua semi-transparan (seperti Stardew)
        g2.setColor(windowBackgroundColor);
        g2.fillRoundRect(x, y, width, height, 35, 35); // Kotak dengan sudut membulat
        Color borderColor = new Color(210, 180, 140, 255); // Cokelat muda (Tan)
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(5)); // Ketebalan bingkai
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }


    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }
}