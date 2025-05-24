package org.example.view;

import org.example.model.GameTime;

public class TimeView {
    public void displayAll(GameTime g) {
        System.out.printf("Waktu: %s | Hari: %d | Musim: %s | Cuaca: %s\n");
    }

    public void showWeatherAlert() {
        System.out.println("[INFO] Hujan turun hari ini. Semua lahan basah!");
    }

    public void showCheatMenu() {
        System.out.println("[CHEAT] Menu untuk set Season/Weather manual");
    }
}
