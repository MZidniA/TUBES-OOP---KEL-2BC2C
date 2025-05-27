// Lokasi: src/main/java/org/example/controller/GamePanel.java
package org.example.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.ArrayList; // Untuk List NPC
import java.util.List;    // Untuk List NPC

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities; // Walaupun tidak digunakan di kode ini, baik untuk ada jika perlu

// Import Model dan View yang Relevan
import org.example.model.Farm;
import org.example.model.Player; // Model Player
import org.example.model.NPC.NPC; // Sesuaikan path jika perlu
import org.example.model.Sound;
import org.example.view.GameStateUI;
import org.example.view.TimeObserver; // Interface observer
import org.example.view.InteractableObject.InteractableObject;
import org.example.view.entitas.PlayerView;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS (tetap sama)
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 18;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS (tetap sama)
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxMap = 6;
    public int currentMap = 0;

    int FPS = 60;
    Thread gameThread;

    // Game Components
    public TileManager tileM;
    public KeyHandler keyH;
    public GameState gameState;
    public GameStateUI gameStateUI; // Akan diinisialisasi dengan benar
    public CollisionChecker cChecker;
    public AssetSetter aSetter;

    // Entities & Game Logic
    public Farm farm; // Model Farm
    public Player pModel; // Model Player (data)
    public PlayerView player; // View Player (representasi visual & input)
    public List<NPC> npcList;
    public TimeManager timeManager;

    public InteractableObject obj[][] = new InteractableObject[maxMap][20];
    private JFrame frame; // Referensi ke JFrame utama
    Sound music = new Sound();
    public Font customFont;

    public GamePanel(JFrame frame) {
        this.frame = frame; // Simpan referensi JFrame
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);

        // LANGKAH 1: Inisialisasi Model Inti
        this.pModel = new Player("John", "Male", "Nama Farm Awal"); // Nama farm akan di-set oleh Farm
        this.npcList = new ArrayList<>();
        // this.npcList.add(new AbigailNPC(this)); // Sesuaikan konstruktor NPC

        // LANGKAH 2: Inisialisasi Farm (bergantung pada pModel dan npcList)
        this.farm = new Farm("Spakbor Valley", this.pModel, this.npcList);
        System.out.println("GamePanel Constructor: Farm initialized - " + this.farm.getFarmName() + ", Initial Time: " + this.farm.getCurrentTime());

        // LANGKAH 3: Inisialisasi Controller dan View yang bergantung pada GamePanel atau Model lain
        this.keyH = new KeyHandler(this); // KeyHandler membutuhkan GamePanel
        this.tileM = new TileManager(this);
        this.cChecker = new CollisionChecker(this);
        this.aSetter = new AssetSetter(this);

        // PlayerView membutuhkan GamePanel, KeyHandler, dan Player Model dari Farm
        this.player = new PlayerView(this, this.keyH, this.farm.getPlayer());

        // LANGKAH 4: Inisialisasi TimeManager (bergantung pada Farm)
        this.timeManager = new TimeManager(this.farm);
        System.out.println("GamePanel Constructor: TimeManager initialized.");

        // LANGKAH 5: Inisialisasi GameStateUI (bergantung pada GamePanel)
        // Pastikan GameStateUI.java Anda adalah versi yang sudah mengimplementasikan TimeObserver
        this.gameStateUI = new GameStateUI(this);
        System.out.println("GamePanel Constructor: GameStateUI initialized.");

        // LANGKAH 6: Inisialisasi GameState
        this.gameState = new GameState(); // State awal biasanya 'play' atau 'title'

        this.addKeyListener(keyH);
        this.setFocusable(true);

        loadCustomFont();
        setupGame(); // Panggil setupGame untuk langkah finalisasi
    }

    public void loadCustomFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (is == null) {
                System.err.println("Font kustom '/fonts/PressStart2P-Regular.ttf' tidak ditemukan. Menggunakan Arial.");
                customFont = new Font("Arial", Font.PLAIN, 18);
                return;
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 18f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
            System.out.println("Font kustom berhasil dimuat: PressStart2P-Regular");
        } catch (Exception e) {
            System.err.println("Error memuat font kustom: " + e.getMessage());
            customFont = new Font("Arial", Font.PLAIN, 18); // Fallback
        }
    }

    public void setupGame() {
        // Semua objek utama (Farm, TimeManager, GameStateUI) sudah diinisialisasi di konstruktor.
        // Metode ini untuk menghubungkan observer dan memulai sistem.

        // Daftarkan GameStateUI sebagai observer ke TimeManager
        if (this.gameStateUI instanceof TimeObserver) {
            this.timeManager.addObserver((TimeObserver) this.gameStateUI);
            System.out.println("GamePanel setupGame: GameStateUI berhasil didaftarkan sebagai TimeObserver.");
        } else {
            // Ini akan terjadi jika GameStateUI.java Anda BELUM diupdate untuk implements TimeObserver
            System.err.println("GamePanel setupGame: KRITIKAL! GameStateUI tidak mengimplementasikan TimeObserver. Tampilan waktu tidak akan berfungsi.");
        }

        // Mulai sistem waktu dari TimeManager
        // Pemanggilan ini dipindah ke startGameThread() agar waktu hanya berjalan saat game benar-benar dimulai
        // this.timeManager.startTimeSystem(); // Pindah ke startGameThread

        aSetter.setInteractableObject(); // Setup objek-objek di map
        gameState.setGameState(gameState.play); // Set state awal game
        System.out.println("GamePanel setupGame: Selesai. State awal: " + gameState.getGameState());
    }

    public void startGameThread() {
        if (gameThread != null && gameThread.isAlive()) {
            // Hentikan thread lama jika ada (jarang terjadi kecuali ada restart logic)
            gameThread.interrupt();
            try {
                gameThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Gagal menghentikan game thread sebelumnya saat memulai yang baru.");
            }
        }
        gameThread = new Thread(this);
        gameThread.start();
        System.out.println("GamePanel startGameThread: Game thread dimulai.");

        // Mulai TimeManager HANYA saat game thread benar-benar dimulai
        if (this.timeManager != null) {
            this.timeManager.startTimeSystem();
            System.out.println("GamePanel startGameThread: Time system dimulai via TimeManager.");
        } else {
            System.err.println("GamePanel startGameThread: TimeManager null, tidak bisa memulai sistem waktu.");
        }

        // music.setFile(...); // Tentukan file musik Anda
        // music.play();
        // music.loop();
    }

    public void stopGameThread() {
        if (timeManager != null) {
            timeManager.stopTimeSystem();
            System.out.println("GamePanel stopGameThread: Time system dihentikan.");
        }
        if (gameThread != null) {
            gameThread.interrupt(); // Minta thread untuk berhenti
            try {
                gameThread.join(1000); // Tunggu thread game selesai (opsional, dengan timeout)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Set kembali status interrupted
                System.err.println("Interupsi saat menunggu game thread berhenti.");
            }
            gameThread = null; // Set ke null setelah berhenti
            System.out.println("GamePanel stopGameThread: Game thread dihentikan.");
        }
        // music.stop();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        // Hapus timer dan drawCount yang mencetak FPS di sini agar tidak terlalu berisik,
        // kecuali Anda memang sedang membutuhkannya untuk debug FPS.

        while (gameThread != null && !Thread.currentThread().isInterrupted()) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint(); // Ini akan memanggil paintComponent secara aman di EDT
                delta--;
            }

            // Memberi sedikit jeda untuk mengurangi beban CPU,
            // namun game loop yang baik biasanya tidak mengandalkan sleep(fixed) seperti ini
            // jika presisi timing sangat krusial. Untuk game sederhana, ini cukup.
            try {
                // Waktu tidur yang sangat singkat, atau bisa dihilangkan jika timing FPS sudah akurat
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Penting untuk re-interrupt
                System.out.println("GamePanel run: Game thread diinterupsi saat sleep.");
                break; // Keluar dari loop jika diinterupsi
            }
        }
        System.out.println("GamePanel run: Game loop selesai.");
    }

    public void update() {
        if (keyH.escapePressed) {
            if (gameState.getGameState() == gameState.play) {
                gameState.setGameState(gameState.pause);
            } else if (gameState.getGameState() == gameState.pause) {
                gameState.setGameState(gameState.play);
            }
            keyH.escapePressed = false;
        }

        if (keyH.inventoryPressed) { // Anda belum memiliki implementasi inventory di GameStateUI sebelumnya
            if (gameState.getGameState() == gameState.play) {
                gameState.setGameState(gameState.inventory);
            } else if (gameState.getGameState() == gameState.inventory) {
                gameState.setGameState(gameState.play);
            }
            keyH.inventoryPressed = false;
        }


        if (gameState.getGameState() == gameState.play) {
            player.update(); // player adalah PlayerView
            // Logika interaksi Anda
            if (keyH.interactPressed) {
                int objIndex = cChecker.checkObject(player, obj, currentMap);
                if (objIndex != 999) {
                    if (obj[currentMap][objIndex] != null) {
                        obj[currentMap][objIndex].interact();
                    }
                }
                // ... (Logika teleportasi atau aksi lain jika tidak ada objek) ...
                keyH.interactPressed = false;
            }
        } else if (gameState.getGameState() == gameState.pause) {
            if (keyH.enterPressed) {
                if (gameStateUI.commandNum == 0) { // Continue
                    gameState.setGameState(gameState.play);
                } else if (gameStateUI.commandNum == 1) { // Exit Game
                    exitToMenu();
                }
                keyH.enterPressed = false;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Gambar Tile dan Objek Game
        if (gameState.getGameState() == gameState.play ||
            gameState.getGameState() == gameState.pause ||
            gameState.getGameState() == gameState.inventory) { // Pastikan map dan player digambar juga saat inventory
            tileM.draw(g2);
            for (InteractableObject interactableObjItem : obj[currentMap]) {
                if (interactableObjItem != null) {
                    interactableObjItem.draw(g2, this);
                }
            }
            player.draw(g2); // player adalah PlayerView
        }

        // GameStateUI akan menggambar semua elemen UI termasuk waktu, pause screen, inventory, dll.
        // berdasarkan state game saat ini.
        if (gameStateUI != null) {
            gameStateUI.draw(g2);
        } else {
            // Ini seharusnya tidak terjadi jika inisialisasi benar
            g2.setColor(Color.RED);
            g2.drawString("Error: GameStateUI is null!", 50, 50);
            System.err.println("GamePanel paintComponent: gameStateUI is null!");
        }

        // Tampilkan prompt interaksi jika dalam state play dan ada objek interaktif
        if (gameState.getGameState() == gameState.play) {
            int objIndex = cChecker.checkObject(player, obj, currentMap);
            if (objIndex != 999 && obj[currentMap][objIndex] != null) {
                g2.setColor(Color.WHITE);
                g2.setFont(customFont != null ? customFont : new Font("Arial", Font.PLAIN, 18));
                String text = "[F] Interact with " + obj[currentMap][objIndex].name;
                int x = getWidth() / 2 - g2.getFontMetrics().stringWidth(text) / 2;
                int y = getHeight() - 50;
                g2.drawString(text, x, y);
            }
        }

        g2.dispose();
    }

    // Metode teleportPlayer dan exitToMenu (sesuaikan implementasi exitToMenu)
    public void teleportPlayer(int mapIndex, int newWorldX, int newWorldY) {
        // music.stop(); // Hentikan musik map lama
        currentMap = mapIndex;
        player.worldX = newWorldX; // player adalah PlayerView
        player.worldY = newWorldY;

        // Kosongkan objek di map baru atau muat objek baru
        // for (int i = 0; i < obj[currentMap].length; i++) {
        //     obj[currentMap][i] = null;
        // }
        // aSetter.setInteractableObject(); // Panggil ulang untuk map baru

        String mapPath = "";
        if (currentMap == 0) mapPath = "/maps/map.txt";
        else if (currentMap == 1) mapPath = "/maps/beachmap.txt";
        // ... tambahkan path map lainnya ...

        if (!mapPath.isEmpty()) {
            tileM.loadMap(mapPath, currentMap);
        } else {
            System.err.println("Path peta tidak valid untuk mapIndex: " + currentMap);
        }
        // music.setFile(...); // Set musik untuk map baru
        // music.play();
        System.out.println("Player diteleportasi ke map " + currentMap + " di tile (" + player.worldX / tileSize + "," + player.worldY / tileSize + ")");
    }

    private void exitToMenu() {
        System.out.println("GamePanel exitToMenu: Aksi keluar ke menu utama.");
        stopGameThread(); // Hentikan game loop dan time manager
        // Logika untuk kembali ke MenuPanel atau menutup aplikasi
        // Contoh:
        // frame.getContentPane().removeAll();
        // MenuPanel menuPanel = new MenuPanel(frame); // Pastikan MenuPanel.java ada
        // frame.setContentPane(menuPanel);
        // frame.revalidate();
        // frame.repaint();
        // menuPanel.requestFocusInWindow(); // Agar MenuPanel bisa menerima input
        System.exit(0); // Cara paling sederhana untuk keluar sementara
    }
}