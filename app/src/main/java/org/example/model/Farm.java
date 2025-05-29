package org.example.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.example.model.Map.FarmMap; // Jika Anda menggunakan FarmMap secara spesifik
import org.example.model.Items.Food; // Diperlukan untuk instanceof jika task.getCookedDish() mengembalikan Items
import org.example.model.Items.Items; // Import Items class
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.InteractableObject.InteractableObject; // Jika masih relevan untuk array objects

public class Farm {
    private final Player playerModel;
    // Ukuran array objects disesuaikan dengan kebutuhan Anda (misal, maxMap = 6, maxObjectsPerMap = 100)
    private final InteractableObject[][] objects = new InteractableObject[6][100];
    private int currentMap = 0; // Indeks peta saat ini
    private List<CookingInProgress> activeCookings = new ArrayList<>();
    private final PlayerStats playerStats;
    private final GameClock gameClock; // Sumber kebenaran untuk waktu, hari, musim, cuaca
    private final FarmMap farmMap; // Asumsi Anda memiliki dan menggunakannya

    public Farm(String farmName, Player playerModel) {
        this.playerModel = playerModel;
        if (this.playerModel != null) {
            this.playerModel.setFarmname(farmName); // Pastikan playerModel tidak null
        } else {
            System.err.println("ERROR (Farm Constructor): playerModel is null.");
            // Pertimbangkan untuk melempar exception atau menangani kasus ini
        }
        this.playerStats = new PlayerStats(); // Pastikan PlayerStats() adalah konstruktor yang benar
        this.gameClock = new GameClock();   // GameClock akan menginisialisasi waktu/hari/musim/cuaca awal
        this.farmMap = new FarmMap();     // Pastikan FarmMap() adalah konstruktor yang benar
        // Inisialisasi lain jika ada
    }

    // --- Getters Utama ---
    public Player getPlayerModel() { return playerModel; }
    public GameClock getGameClock() { return gameClock; }
    public PlayerStats getPlayerStats() { return playerStats; }
    public FarmMap getFarmMap() { return farmMap; }
    public List<CookingInProgress> getActiveCookings() {
        return this.activeCookings; // Atau return new ArrayList<>(this.activeCookings);
    }

    // --- Getters untuk Info Waktu, Hari, Musim, Cuaca (Delegasi ke GameClock) ---
    public LocalTime getCurrentTime() {
        return (this.gameClock != null) ? this.gameClock.getCurrentTime() : LocalTime.of(6,0); // Default jika null
    }

    public int getCurrentDay() {
        return (this.gameClock != null) ? this.gameClock.getDay() : 1;
    }

    public Season getCurrentSeason() {
        return (this.gameClock != null) ? this.gameClock.getCurrentSeason() : Season.SPRING;
    }

    public Weather getCurrentWeather() {
        return (this.gameClock != null) ? this.gameClock.getTodayWeather() : Weather.SUNNY;
    }

    // --- Manajemen Peta dan Objek ---
    public int getCurrentMap() { return currentMap; }
    public void setCurrentMap(int mapIndex) {
        if (mapIndex >= 0 && mapIndex < this.objects.length) {
            this.currentMap = mapIndex;
        } else {
            System.err.println("ERROR (Farm.setCurrentMap): Invalid map index: " + mapIndex);
        }
    }

    public InteractableObject[] getObjectsForCurrentMap() {
        if (currentMap >= 0 && currentMap < this.objects.length) {
            return objects[currentMap];
        }
        return new InteractableObject[0]; // Kembalikan array kosong jika indeks tidak valid
    }

    // Anda mungkin memiliki metode untuk menambahkan objek ke peta tertentu
    public void addObjectToMap(int mapIndex, int objectSlot, InteractableObject object) {
        if (mapIndex >= 0 && mapIndex < objects.length &&
            objectSlot >= 0 && objectSlot < objects[mapIndex].length) {
            objects[mapIndex][objectSlot] = object;
        } else {
            System.err.println("ERROR (Farm.addObjectToMap): Invalid mapIndex or objectSlot.");
        }
    }

    public void clearObjects(int mapIndex) {
        if (mapIndex >= 0 && mapIndex < objects.length) {
            for (int i = 0; i < objects[mapIndex].length; i++) {
                objects[mapIndex][i] = null;
            }
        } else {
            System.err.println("ERROR (Farm.clearObjects): Invalid map index: " + mapIndex);
        }
    }

    public String getMapPathFor(int mapIndex) { // Ini akan digunakan oleh TileManager
        return switch (mapIndex) {
            case 0 -> "/maps/map.txt";       // Farm Map
            case 1 -> "/maps/beachmap.txt";  // Contoh Beach Map
            case 2 -> "/maps/rivermap.txt";  // Contoh River Map
            case 3 -> "/maps/townmap.txt";   // Contoh Town Map
            case 4 -> "/maps/housemap.txt";  // Contoh Player's House Map
            // Tambahkan case untuk peta lain jika ada
            default -> {
                System.err.println("WARNING (Farm.getMapPathFor): No path defined for map index " + mapIndex + ". Defaulting.");
                yield "/maps/map.txt"; // Default jika mapIndex tidak valid
            }
        };
    }

    // --- Logika untuk Cooking (Memasak Pasif) ---
    public void addActiveCooking(CookingInProgress cookingTask) {
        if (cookingTask != null) {
            this.activeCookings.add(cookingTask);
            System.out.println("LOG (Farm.addActiveCooking): New cooking task added - " +
                               cookingTask.getCookedDish().getName() + " x" + cookingTask.getQuantityProduced());
        } else {
            System.err.println("ERROR (Farm.addActiveCooking): Attempted to add a null cooking task.");
        }
    }

    /**
     * Memperbarui status semua masakan yang sedang berlangsung.
     * Metode ini harus dipanggil secara periodik oleh game loop (misalnya, setiap update utama atau setiap beberapa detik game).
     */
    public void updateCookingProgress() {
        if (activeCookings.isEmpty() || this.gameClock == null || this.playerModel == null || this.playerModel.getInventory() == null) {
            return; // Tidak ada yang diproses atau komponen penting null
        }

        LocalTime currentTime = this.gameClock.getCurrentTime();
        Iterator<CookingInProgress> iterator = activeCookings.iterator();

        while (iterator.hasNext()) {
            CookingInProgress task = iterator.next();
            if (task == null) { // Pemeriksaan keamanan
                iterator.remove();
                continue;
            }

            if (task.isClaimed()) {
                // Jika Anda ingin membersihkan task yang sudah diklaim dari daftar secara otomatis (bukan hanya saat klaim manual)
                // iterator.remove(); // Opsional: hapus jika tidak ingin menumpuk task yang sudah diklaim
                continue;
            }

            if (task.isReadyToClaim(currentTime)) {
                // OPSI SAAT INI: Otomatis tambahkan ke inventory pemain dan tandai sebagai diklaim.
                // Pastikan getCookedDish() mengembalikan objek Items (atau Food) yang benar
                Items dishToAdd = task.getCookedDish();
                int quantityProduced = task.getQuantityProduced();

                this.playerModel.getInventory().addInventory(dishToAdd, quantityProduced);
                task.setClaimed(true); // Tandai sudah diklaim (dan otomatis masuk inventory)

                System.out.println("LOG (Farm.updateCookingProgress): Dish '" + dishToAdd.getName() +
                                   "' x" + quantityProduced + " is cooked and automatically added to inventory for " +
                                   this.playerModel.getName() + "!");
                // UI bisa memberikan notifikasi berdasarkan ini.
                iterator.remove(); // Hapus dari daftar aktif setelah ditambahkan ke inventory
            }
        }
    }

    /**
     * Metode untuk pemain mengklaim masakan pertama yang sudah selesai dari kompor secara manual.
     * Dipanggil saat pemain berinteraksi dengan kompor dan ada masakan yang siap.
     * @return CookingInProgress yang berhasil diklaim, atau null jika tidak ada atau gagal.
     */
    public CookingInProgress claimFirstReadyDish() {
        if (activeCookings.isEmpty() || this.gameClock == null || this.playerModel == null || this.playerModel.getInventory() == null) {
            System.err.println("LOG (Farm.claimFirstReadyDish): Cannot claim dish - essential components are null or no active cookings.");
            return null;
        }

        LocalTime currentTime = this.gameClock.getCurrentTime();
        CookingInProgress claimedTask = null;
        Iterator<CookingInProgress> iterator = activeCookings.iterator();

        while (iterator.hasNext()) {
            CookingInProgress task = iterator.next();
            if (task != null && task.isReadyToClaim(currentTime) && !task.isClaimed()) {
                Items dishToClaim = task.getCookedDish();
                int quantityToClaim = task.getQuantityProduced();

                this.playerModel.getInventory().addInventory(dishToClaim, quantityToClaim);
                task.setClaimed(true);
                claimedTask = task;

                System.out.println("LOG (Farm.claimFirstReadyDish): Player " + this.playerModel.getName() + " claimed " +
                                   quantityToClaim + "x " + dishToClaim.getName() + ".");
                iterator.remove(); // Hapus dari daftar aktif setelah diklaim
                break; // Hanya klaim satu per interaksi untuk metode ini
            }
        }
        if (claimedTask == null) {
            System.out.println("LOG (Farm.claimFirstReadyDish): No dishes ready to be claimed at the moment.");
        }
        return claimedTask;
    }

    public InteractableObject[][] getAllObjects() {
        return this.objects;
    }
}