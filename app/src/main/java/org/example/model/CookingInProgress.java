package org.example.model; // Atau package yang sesuai

import org.example.model.Items.Food; // Pastikan Food diimport dengan benar
import java.time.LocalTime;
import java.time.temporal.ChronoUnit; // Untuk perhitungan sisa waktu yang lebih baik

public class CookingInProgress {
    private final Food cookedDish;          // Makanan yang dihasilkan (menggunakan final Food dari konstruktor)
    private final int quantityProduced;     // Jumlah makanan yang dihasilkan
    private final LocalTime startTime;        // Waktu game saat memasak dimulai
    private final int durationInGameHours;  // Durasi memasak dalam jam game
    private final LocalTime completionTime; // Waktu game saat masakan selesai (nama diganti dari completionGameTime)
    private boolean claimed;                // Status apakah makanan sudah diambil/diklaim

    /**
     * Konstruktor untuk CookingInProgress.
     * @param cookedDish Objek Food yang akan dihasilkan.
     * @param quantityProduced Jumlah dish yang akan dihasilkan.
     * @param startTime Waktu game (LocalTime) saat proses memasak dimulai.
     * @param durationInGameHours Durasi memasak dalam satuan jam game.
     */
    public CookingInProgress(Food cookedDish, int quantityProduced, LocalTime startTime, int durationInGameHours) {
        if (cookedDish == null) {
            throw new IllegalArgumentException("Cooked dish cannot be null.");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null.");
        }
        if (quantityProduced <= 0) {
            throw new IllegalArgumentException("Quantity produced must be positive.");
        }
        if (durationInGameHours <= 0) {
            throw new IllegalArgumentException("Cooking duration must be positive.");
        }

        this.cookedDish = cookedDish;
        this.quantityProduced = quantityProduced;
        this.startTime = startTime;
        this.durationInGameHours = durationInGameHours;
        this.completionTime = this.startTime.plusHours(this.durationInGameHours);
        this.claimed = false;
    }

    public Food getCookedDish() {
        return cookedDish;
    }

    public int getQuantityProduced() { // Implementasi yang benar
        return quantityProduced;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getDurationInGameHours() {
        return durationInGameHours;
    }

    public LocalTime getCompletionTime() { // Nama diganti agar lebih konsisten
        return completionTime;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    /**
     * Mengecek apakah masakan sudah selesai berdasarkan waktu saat ini.
     * @param currentTime Waktu game saat ini.
     * @return true jika masakan sudah selesai (atau sudah lewat waktu selesainya), false jika belum.
     */
    private boolean isCompleted(LocalTime currentTime) { // Dibuat private karena isReadyToClaim lebih relevan dari luar
        if (currentTime == null) return false;
        return !currentTime.isBefore(completionTime);
    }

    /**
     * Mengecek apakah masakan sudah selesai dan belum diklaim.
     * @param currentTime Waktu game saat ini.
     * @return true jika siap diklaim, false jika tidak.
     */
    public boolean isReadyToClaim(LocalTime currentTime) {
        return isCompleted(currentTime) && !isClaimed();
    }

    /**
     * Menghitung sisa waktu memasak dalam satuan menit game.
     * Berguna untuk ditampilkan di UI.
     * @param currentTime Waktu game saat ini.
     * @return Sisa waktu dalam menit game. Mengembalikan 0 jika sudah selesai atau sudah diklaim.
     */
    public long getRemainingGameMinutes(LocalTime currentTime) {
        if (claimed || isReadyToClaim(currentTime) || currentTime == null) {
            return 0;
        }

        // Jika waktu selesai adalah di "hari berikutnya" secara LocalTime (misal, mulai 23:30, selesai 00:30)
        // dan waktu saat ini masih di "hari ini" (misal 23:45).
        if (completionTime.isBefore(startTime) && !currentTime.isBefore(startTime)) {
            // Waktu tersisa di hari ini + waktu dari awal hari sampai selesai
            long minutesToEndOfDay = ChronoUnit.MINUTES.between(currentTime, LocalTime.MAX); // Menit hingga 23:59:59.999...
            long minutesFromStartOfDay = ChronoUnit.MINUTES.between(LocalTime.MIN, completionTime); // Menit dari 00:00
            return minutesToEndOfDay + minutesFromStartOfDay + 1; // +1 untuk inklusivitas menit
        } else if (completionTime.isAfter(currentTime)) { // Jika selesai di hari yang sama (LocalTime)
            return Math.max(0, ChronoUnit.MINUTES.between(currentTime, completionTime));
        }

        return 0; // Jika sudah lewat atau kondisi lain
    }
}