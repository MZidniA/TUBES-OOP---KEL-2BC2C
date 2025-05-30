package org.example.model;

import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Iterator;

import org.example.model.Items.Items;
import org.example.model.Map.FarmMap;
import org.example.model.enums.Season;
import org.example.model.enums.Weather;
import org.example.view.InteractableObject.InteractableObject;
// Tidak perlu import PlayerView lagi di sini

public class Farm {
    private final Player playerModel;
    private int currentDay;
    // private final PlayerView playerView; // DIHAPUS: Farm tidak boleh tahu tentang PlayerView
    private final InteractableObject[][] objects = new InteractableObject[6][100]; // maxMap, maxObjects
    private int currentMap = 0;
    // Tambahkan list untuk menyimpan CookingInProgress jika belum ada
    private List<CookingInProgress> activeCookings = new java.util.ArrayList<>();
    private final PlayerStats playerStats;
    private final GameClock gameClock;
    private final FarmMap farmMap = new FarmMap(); // Tambahkan deklarasi dan inisialisasi FarmMap
    private Weather currentWeather; // Tambahkan untuk menyimpan cuaca saat ini
    private Season currentSeason; // Tambahkan untuk menyimpan musim saat ini   

    public Farm(String farmName, Player playerModel) { // Parameter hanya Player model
        this.playerModel = playerModel;
        this.playerModel.setFarmname(farmName);
        this.playerStats = new PlayerStats(); // Atau sesuai konstruktor PlayerStats yang tersedia
        this.gameClock = new GameClock(); // Atau sesuai konstruktor GameClock
        // Inisialisasi PlayerView TIDAK LAGI dilakukan di sini
        // this.playerView = new PlayerView(playerModel, ???); // DIHAPUS
        this.currentDay = 1; // Inisialisasi hari pertama
        this.currentWeather = Weather.SUNNY; // Inisialisasi cuaca awal
    }
    
    // Getters
    public Player getPlayerModel() { return playerModel; }
    // public PlayerView getPlayerView() { return playerView; } // DIHAPUS

    public int getCurrentMap() { return currentMap; }
    public InteractableObject[][] getAllObjects() { return objects; }
    public InteractableObject[] getObjectsForCurrentMap() { return objects[currentMap]; }

    // Setters
    public void setCurrentMap(int map) { this.currentMap = map; }

    public String getMapPathFor(int mapIndex) {
        return switch (mapIndex) {
            case 0 -> "/maps/map.txt";
            case 1 -> "/maps/beachmap.txt";
            case 2 -> "/maps/rivermap.txt";
            case 3 -> "/maps/townmap.txt";   // Disesuaikan dengan pemanggilan loadMap di TileManager
            case 4 -> "/maps/housemap.txt";
            // ... dst, pastikan semua peta terdaftar di sini jika digunakan
            default -> "/maps/map.txt"; // Default jika mapIndex tidak valid
        };
    }
    
    public void clearObjects(int mapIndex) {
        if (mapIndex >= 0 && mapIndex < objects.length) {
            for(int i = 0; i < objects[mapIndex].length; i++) {
                objects[mapIndex][i] = null;
            }
        }
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public GameClock getGameClock() {
        return gameClock;
    }

        // (Pastikan ada getter jika dibutuhkan di tempat lain)
    public List<CookingInProgress> getActiveCookings() {
        return activeCookings;
    }

    // Add this method to provide access to the FarmMap
    public FarmMap getFarmMap() {
        return this.farmMap;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public LocalTime getCurrentTime() {
        return gameClock.getCurrentTime();
    }

    public void setCurrentTime(LocalTime newTime) {
        gameClock.setCurrentTime(newTime);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentSeason(Season nextSeason) {
        this.currentSeason = nextSeason;
    }

    public void setCurrentWeather(Weather nextWeather) {
        this.currentWeather = nextWeather;
    }


    public InteractableObject getObjectAtTile(int mapIndex, int col, int row, int tileSize) {
        if (mapIndex < 0 || mapIndex >= objects.length) return null;
        for (InteractableObject obj : objects[mapIndex]) {
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == col && objRow == row) {
                    return obj;
                }
            }
        }
        return null;
    }

   
    public boolean removeObjectAtTile(int mapIndex, int col, int row, int tileSize) {
        if (mapIndex < 0 || mapIndex >= objects.length) return false;
        for (int i = 0; i < objects[mapIndex].length; i++) {
            InteractableObject obj = objects[mapIndex][i];
            if (obj != null) {
                int objCol = obj.worldX / tileSize;
                int objRow = obj.worldY / tileSize;
                if (objCol == col && objRow == row) {
                    objects[mapIndex][i] = null; 
                    return true;
                }
            }
        }
        return false;
    }
<<<<<<< Updated upstream
    

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
=======

    public void updateCookingProgress() {
        // Pemeriksaan awal untuk komponen penting
        if (activeCookings.isEmpty() || this.gameClock == null || this.playerModel == null || this.playerModel.getInventory() == null) {
            return; // Tidak ada yang perlu diproses atau komponen penting null
        }

        LocalTime currentTime = this.gameClock.getCurrentTime(); // Dapatkan waktu game saat ini

        // Gunakan Iterator untuk menghindari ConcurrentModificationException saat menghapus item dari list
>>>>>>> Stashed changes
        Iterator<CookingInProgress> iterator = activeCookings.iterator();

        while (iterator.hasNext()) {
            CookingInProgress task = iterator.next();
<<<<<<< Updated upstream
            if (task == null) { // Pemeriksaan keamanan
=======

            if (task == null) { // Keamanan jika ada task null di list
>>>>>>> Stashed changes
                iterator.remove();
                continue;
            }

<<<<<<< Updated upstream
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
=======
            // Kita hanya peduli pada task yang belum diklaim
            if (task.isClaimed()) {
                // Opsional: Anda bisa menghapus task yang sudah diklaim dari list di sini
                // jika Anda tidak ingin listnya terus bertambah. Tapi karena kita remove setelah klaim,
                // ini mungkin tidak perlu.
                // iterator.remove();
                continue;
            }

            // Cek apakah masakan sudah siap untuk diklaim (selesai dan belum diklaim)
            if (task.isCompleted(currentTime)) {
                Items cookedDish = task.getResultingDish(); // Dapatkan item hasil masakan
                int quantityProduced = task.getQuantity(); // Dapatkan jumlah yang dihasilkan

                if (cookedDish != null) {
                    // Tambahkan hasil masakan ke inventory pemain
                    this.playerModel.getInventory().addInventory(cookedDish, quantityProduced);
                    
                    // Tandai task sebagai sudah diklaim
                    task.setClaimed(true); 

                    System.out.println("LOG (Farm.updateCookingProgress): Dish '" + cookedDish.getName() +
                                       "' x" + quantityProduced + " is cooked and automatically added to inventory for " +
                                       this.playerModel.getName() + "!");
                    
                    // Hapus task dari daftar activeCookings karena sudah selesai dan diklaim
                    iterator.remove(); 
                } else {
                    System.err.println("ERROR (Farm.updateCookingProgress): Cooked dish is null for a completed task. Task removed.");
                    iterator.remove(); // Hapus task yang bermasalah
                }
            }
        }
    }
>>>>>>> Stashed changes
}
