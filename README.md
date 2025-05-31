# 2BC2B
* Sarah Alwa Neguita Surbakti 18223023
* Ferro Arka Berlian 18223027
* Inggried Amelia Deswanty 18223035
* Muhammad Zidni Alkindi 18223071



# Spakbor Hill

Selamat datang di Spakbor Hill! Sebuah game simulasi pertanian dan kehidupan sederhana di mana Anda dapat membangun pertanian impian, berinteraksi dengan penduduk kota, menjelajahi berbagai lokasi, dan membangun kehidupan baru.


## Tentang Game

Di Spakbor Hill, Anda akan memulai petualangan sebagai petani baru yang mewarisi sebuah lahan di daerah pedesaan yang tenang. Tugas Anda adalah mengolah lahan tersebut, menanam berbagai jenis tanaman sesuai musim, merawatnya, dan menjual hasil panen untuk mendapatkan keuntungan. Selain bertani, Anda juga bisa memancing ikan di berbagai perairan, memasak makanan lezat dari hasil panen dan pancingan, serta membangun hubungan dengan para penduduk kota yang unik. 


## Fitur Utama

* **Bertani**: Cangkul tanah, tanam berbagai jenis benih, sirami tanaman Anda, dan panen hasilnya.
* **Siklus Waktu & Musim**: Rasakan perubahan waktu dari pagi hingga malam, serta pergantian musim (Spring, Summer, Fall, Winter) yang memengaruhi jenis tanaman yang bisa ditanam dan ikan yang bisa ditangkap.
* **Cuaca Dinamis**: Hadapi hari yang cerah atau hujan, yang dapat memengaruhi aktivitas bertani Anda.
* **Interaksi NPC**: Berkenalan dan berinteraksi dengan berbagai karakter (NPC) di kota. Anda bisa mengobrol, memberi hadiah, melamar, hingga menikah.
* **Eksplorasi**: Kunjungi berbagai lokasi seperti Pertanian Anda, Kota, Pantai, Sungai di Hutan, Danau Gunung, dan Rumah Anda.
* **Aktivitas Lain**:
    * **Memancing**: Tangkap berbagai jenis ikan di danau, sungai, atau laut.
    * **Memasak**: Buat berbagai hidangan menggunakan bahan-bahan yang Anda kumpulkan.
    * **Menonton TV**: Dapatkan ramalan cuaca untuk esok hari.
* **Manajemen Energi & Inventaris**: Perhatikan energi Anda saat bekerja dan kelola barang-barang Anda di inventaris.
* **Ekonomi**: Jual hasil panen, ikan, atau masakan melalui *Shipping Bin* untuk mendapatkan emas.
* **Tujuan Akhir Game**: Capai salah satu kondisi akhir game, seperti menikah atau mencapai target emas tertentu.

## Memulai

### Prasyarat

* Java Development Kit (JDK) versi 21 atau lebih tinggi.
* Git.
* Gradle (Wrapper akan mengunduhnya secara otomatis jika belum ada).

### 1. Clone Repository

Buka terminal atau command prompt Anda, lalu jalankan perintah berikut untuk meng-clone repository game ini:
```bash
git clone <TUBES-OOP---KEL-2BC2C>
cd <TUBES-OOP---KEL-2BC2C>
```

### 2. Compile dan Jalankan Game
Buka terminal di IDE kalian lalu ketik 
```bash
.\gradlew build
.\gradlew run
```


### 3. Kontrol Game
Gerak Pemain:
* W / Panah Atas: Bergerak ke atas
* S / Panah Bawah: Bergerak ke bawah
* A / Panah Kiri: Bergerak ke kiri
* D / Panah Kanan: Bergerak ke kanan
* Interaksi: F
* Info Pemain: G
I* nventory: I 
* Navigasi Menu/UI:
* W/A/S/D atau Panah Atas/Kiri/Bawah/Kanan: Navigasi pilihan di menu (Pause, Inventory, Cooking, Shipping Bin)
* Enter: Konfirmasi pilihan di menu
* Escape
  * Pause game saat bermain
  * Kembali/Tutup menu (Inventory, Cooking, Shipping Bin, Pause)
* Cheats (untuk Development/Testing):
  *Ctrl + Angka (0-8): Mengaktifkan berbagai cheat (detail lihat di KeyHandler.java dan CheatManager.java).
