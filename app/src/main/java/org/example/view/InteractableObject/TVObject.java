package org.example.view.InteractableObject; // Pastikan paketnya sesuai

import org.example.controller.GameController;
import org.example.controller.action.WatchingAction;
import org.example.model.Farm;
import org.example.model.Player;
import org.example.model.enums.Weather;
import org.example.view.GameStateUI;

import javax.imageio.ImageIO; // Untuk memuat gambar
import java.io.IOException; // Untuk menangani error IO

public class TVObject extends InteractableObject {

    public TVObject() { // Konstruktor tanpa parameter GameController
        super("TV"); // Nama objek diubah menjadi "TV" agar lebih singkat, atau "Televisi" juga boleh
        this.collision = true;
        loadImage();
    }

    @Override
    protected void loadImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/InteracableObject/tv.png")); // Path gambar TV
            if (image == null) {
                System.err.println("Gambar untuk TVObject tidak ditemukan di path: /InteracableObject/tv.png");
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar untuk TVObject: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void interact(GameController gc) {
        // ... (Logika interaksi tetap sama persis seperti sebelumnya, sudah bagus) ...
        if (gc == null) {
            System.err.println("TVObject Error: GameController null saat interaksi.");
            return;
        }

        Farm currentFarm = gc.getFarmModel();
        Player player = currentFarm.getPlayerModel();
        GameStateUI ui = gc.getGameStateUI();

        if (player == null || ui == null || currentFarm == null) {
            System.err.println("TVObject Error: Komponen penting (Player, UI, Farm) null.");
            return;
        }

        if (currentFarm.getCurrentMap() != 4) { // Asumsi map 4 adalah rumah
            ui.showTemporaryMessage("Kamu hanya bisa menonton TV di dalam rumah.");
            return;
        }

        if (player.getEnergy() < WatchingAction.ENERGY_COST) {
            ui.showTemporaryMessage("Energi tidak cukup untuk menonton TV.");
            return;
        }

        WatchingAction watchingAction = new WatchingAction();

        if (watchingAction.canExecute(currentFarm)) {
            watchingAction.execute(currentFarm); 

            Weather forecast = currentFarm.getGameClock().getWeatherForecast();
            String forecastMessage = "Ramalan cuaca besok: " + forecast.toString() + ".";
            ui.showTemporaryMessage(forecastMessage);
        } else {
            ui.showTemporaryMessage("Tidak bisa menonton TV saat ini.");
        }
    }
}   