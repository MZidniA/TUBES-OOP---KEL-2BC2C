// Lokasi: src/main/java/org/example/controller/TimeManager.java
package org.example.controller;

import org.example.model.Farm; // Asumsi TimeManager memiliki akses ke Farm
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.TimeObserver; // Import interface observer

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeManager {
    private Thread timeThread;
    private boolean running;
    private Farm farm; // TimeManager perlu tahu Farm untuk memperbarui waktunya
    private final int REAL_SECOND_TO_GAME_MINUTE = 5; // 1 detik nyata = 5 menit game [cite: 223]

    // List untuk menyimpan observer
    private List<TimeObserver> observers;

    public TimeManager(Farm farm) { // Constructor menerima objek Farm
        this.farm = farm;
        this.observers = new ArrayList<>();
    }

    // Metode untuk mendaftarkan observer
    public void addObserver(TimeObserver observer) {
        observers.add(observer);
    }

    // Metode untuk memberi tahu semua observer
    private void notifyObservers() {
        // Panggil onTimeUpdate pada setiap observer dengan data waktu terbaru dari Farm
        for (TimeObserver observer : observers) {
            observer.onTimeUpdate(farm.getCurrentDay(), farm.getCurrentSeason(), farm.getCurrentWeather(), farm.getCurrentTime());
        }
    }

    public void startTimeSystem() {
        if (timeThread == null || !timeThread.isAlive()) {
            running = true;
            timeThread = new Thread(() -> {
                long lastTime = System.currentTimeMillis();

                while (running) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long elapsed = currentTimeMillis - lastTime;

                    // Menggunakan konstanta 1000ms untuk 1 detik nyata
                    if (elapsed >= 1000) { // Jika sudah 1 detik nyata berlalu [cite: 223]
                        updateGameTime(REAL_SECOND_TO_GAME_MINUTE); // Tambah waktu game [cite: 223]
                        lastTime = currentTimeMillis; // Reset lastTime

                        // Setelah waktu game diupdate, beri tahu UI untuk memperbarui
                        notifyObservers();
                    }

                    try {
                        // Agar tidak terlalu boros CPU, thread bisa tidur sebentar
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        e.printStackTrace();
                        running = false; // Hentikan thread jika terjadi interupsi
                    }
                }
            });
            timeThread.setName("GameTimeThread"); // Beri nama thread untuk debug [cite: 19]
            timeThread.start();
        }
    }

    public void stopTimeSystem() {
        running = false;
        if (timeThread != null) {
            timeThread.interrupt();
        }
    }

    // Metode ini yang akan memperbarui waktu di objek Farm
    private void updateGameTime(int minutesToAdd) {
        LocalTime newTime = farm.getCurrentTime().plusMinutes(minutesToAdd);
        farm.setCurrentTime(newTime);

        // Jika waktu melewati tengah malam (00:00), maka hari bertambah
        if (newTime.isBefore(farm.getCurrentTime())) { // Cek jika waktu "mundur" karena lewat tengah malam
             farm.setCurrentDay(farm.getCurrentDay() + 1);

            // Logika untuk perubahan musim (misal setiap 10 hari)
            if (farm.getCurrentDay() % 10 == 1) { // Hari pertama di musim baru (Hari 1, 11, 21, 31, dst) [cite: 228]
                Season nextSeason = null;
                switch (farm.getCurrentSeason()) {
                    case SPRING:
                        nextSeason = Season.SUMMER;
                        break;
                    case SUMMER:
                        nextSeason = Season.FALL;
                        break;
                    case FALL:
                        nextSeason = Season.WINTER;
                        break;
                    case WINTER:
                        nextSeason = Season.SPRING; // Kembali ke Spring
                        break;
                }
                farm.setCurrentSeason(nextSeason);
                // Reset hari untuk musim baru jika diperlukan (tergantung cara Anda menghitung total hari atau hari dalam musim)
                // Jika currentDay adalah total hari, biarkan saja.
                // Jika currentDay adalah hari dalam musim, maka reset ke 1.
                // Saat ini, currentDay adalah total hari yang dimainkan.
            }

            // Logika untuk perubahan cuaca (bisa random atau sesuai aturan spesifikasi)
            // Spesifikasi: "Dalam satu season, Rainy Day minimal terjadi 2 kali." [cite: 232]
            // Implementasi sederhana untuk demonstrasi: Ganti cuaca secara acak
            if (Math.random() < 0.3) { // Contoh: 30% kemungkinan hujan
                farm.setCurrentWeather(Weather.RAINY);
            } else {
                farm.setCurrentWeather(Weather.SUNNY);
            }
        }
    }
}