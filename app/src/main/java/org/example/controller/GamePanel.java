// Lokasi: src/main/java/org/example/controller/GamePanel.java
package org.example.controller;

import java.awt.*;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.example.view.GameStateUI;

public class GamePanel extends JPanel implements Runnable {
    public final int originalTileSize = 16;
    public final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxMap = 3;
    
    private final GameController controller;
    private final JFrame frame;
    public Font customFont;
    private Thread gameThread;
    private int FPS = 60;

    public GamePanel(JFrame frame, GameController controller) {
        this.frame = frame;
        this.controller = controller;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);

        addKeyListener(controller.getKeyHandler());
        setFocusable(true);

        loadCustomFont();
    }

    public void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            if (is == null) throw new Exception("Font file tidak ditemukan.");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 12f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (Exception e) {
            customFont = new Font("Arial", Font.PLAIN, 18);
        }
    }

    public void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
            try { gameThread.join(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGameThread() {
        if (gameThread != null) {
            gameThread.interrupt();
            try { gameThread.join(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            gameThread = null;
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null && !Thread.currentThread().isInterrupted()) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                controller.update();
                repaint();
                delta--;
            }

            try { Thread.sleep(1); } catch (InterruptedException e) { break; }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        controller.draw(g2);
        g2.dispose();
    }

    public int getTileSize() { return tileSize; }
    public int getScreenWidth() { return screenWidth; }
    public int getScreenHeight() { return screenHeight; }
}