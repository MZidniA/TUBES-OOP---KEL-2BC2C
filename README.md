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

## Struktur Kode
```Text
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── org
│   │   │       └── example
│   │   │           ├── Main.java
│   │   │           ├── controller
│   │   │           │   ├── AssetSetter.java
│   │   │           │   ├── CheatManager.java
│   │   │           │   ├── CollisionChecker.java
│   │   │           │   ├── GameController.java
│   │   │           │   ├── GameState.java
│   │   │           │   ├── KeyHandler.java
│   │   │           │   ├── PlayerController.java
│   │   │           │   ├── StoreController.java
│   │   │           │   ├── TimeManager.java
│   │   │           │   ├── UtilityTool.java
│   │   │           │   └── action
│   │   │           │       ├── Action.java
│   │   │           │       ├── ChattingAction.java
│   │   │           │       ├── CookingAction.java
│   │   │           │       ├── EatingAction.java
│   │   │           │       ├── FishingAction.java
│   │   │           │       ├── GiftingAction.java
│   │   │           │       ├── HarvestingAction.java
│   │   │           │       ├── MarryingAction.java
│   │   │           │       ├── MovingAction.java
│   │   │           │       ├── PlantingAction.java
│   │   │           │       ├── ProposingAction.java
│   │   │           │       ├── RecoverLandAction.java
│   │   │           │       ├── SleepingAction.java
│   │   │           │       ├── TillingAction.java
│   │   │           │       ├── UpdateAndShowLocationAction.java
│   │   │           │       ├── WatchingAction.java
│   │   │           │       └── WateringAction.java
│   │   │           ├── model
│   │   │           │   ├── CookingInProgress.java
│   │   │           │   ├── Farm.java
│   │   │           │   ├── GameClock.java
│   │   │           │   ├── GameTime.java
│   │   │           │   ├── Inventory.java
│   │   │           │   ├── Player.java
│   │   │           │   ├── PlayerStats.java
│   │   │           │   ├── Recipe.java
│   │   │           │   ├── RecipeDatabase.java
│   │   │           │   ├── Sound.java
│   │   │           │   ├── Items
│   │   │           │   │   ├── Crops.java
│   │   │           │   │   ├── CropsFactory.java
│   │   │           │   │   ├── EdibleItem.java
│   │   │           │   │   ├── Equipment.java
│   │   │           │   │   ├── EquipmentFactory.java
│   │   │           │   │   ├── Fish.java
│   │   │           │   │   ├── FishFactory.java
│   │   │           │   │   ├── Food.java
│   │   │           │   │   ├── FoodFactory.java
│   │   │           │   │   ├── Furniture.java
│   │   │           │   │   ├── FurnitureFactory.java
│   │   │           │   │   ├── ItemDatabase.java
│   │   │           │   │   ├── Items.java
│   │   │           │   │   ├── Misc.java
│   │   │           │   │   ├── MiscFactory.java
│   │   │           │   │   ├── SeedFactory.java
│   │   │           │   │   └── Seeds.java
│   │   │           │   ├── Map
│   │   │           │   │   ├── FarmMap.java
│   │   │           │   │   ├── House.java
│   │   │           │   │   ├── Location.java
│   │   │           │   │   ├── Plantedland.java
│   │   │           │   │   ├── Pond.java
│   │   │           │   │   ├── ShippingBin.java
│   │   │           │   │   ├── Store.java
│   │   │           │   │   ├── Tile.java
│   │   │           │   │   ├── Tillableland.java
│   │   │           │   │   └── Tilledland.java
│   │   │           │   ├── NPC
│   │   │           │   │   ├── AbigailNPC.java
│   │   │           │   │   ├── CarolineNPC.java
│   │   │           │   │   ├── DascoNPC.java
│   │   │           │   │   ├── EmilyNPC.java
│   │   │           │   │   ├── MayorTadiNPC.java
│   │   │           │   │   ├── NPC.java
│   │   │           │   │   ├── NPCFactory.java
│   │   │           │   │   └── PerryNPC.java
│   │   │           │   └── enums
│   │   │           │       ├── FishType.java
│   │   │           │       ├── LocationType.java
│   │   │           │       ├── RelationshipStats.java
│   │   │           │       ├── Season.java
│   │   │           │       ├── SleepReason.java
│   │   │           │       ├── TileType.java
│   │   │           │       └── Weather.java
│   │   │           └── view
│   │   │               ├── ChattingDialogPanel.java
│   │   │               ├── ClassicMessageDialog.java
│   │   │               ├── EnergyWarningDialog.java
│   │   │               ├── FishingPanel.java
│   │   │               ├── FontMetrics.java
│   │   │               ├── GamePanel.java
│   │   │               ├── GameStateUI.java
│   │   │               ├── GiftingDialogPanel.java
│   │   │               ├── MenuPanel.java
│   │   │               ├── NPCInteractionPanel.java
│   │   │               ├── StorePanel.java
│   │   │               ├── TimeObserver.java
│   │   │               ├── TransitionPanel.java
│   │   │               ├── ViewPlayerInfoPanel.java
│   │   │               ├── InteractableObject
│   │   │               │   ├── AbigailHouse.java
│   │   │               │   ├── BedObject.java
│   │   │               │   ├── CarolineHouse.java
│   │   │               │   ├── DascoHouse.java
│   │   │               │   ├── DoorObject.java
│   │   │               │   ├── Emily.java
│   │   │               │   ├── EmilyStore.java
│   │   │               │   ├── InteractableObject.java
│   │   │               │   ├── MayorHouse.java
│   │   │               │   ├── MountainLakeObject.java
│   │   │               │   ├── OceanObject.java
│   │   │               │   ├── PerryHouse.java
│   │   │               │   ├── PlantedTileObject.java
│   │   │               │   ├── PondObject.java
│   │   │               │   ├── RiverObject.java
│   │   │               │   ├── ShippingBinObject.java
│   │   │               │   ├── StoreDoor.java
│   │   │               │   ├── StoveObject.java
│   │   │               │   ├── TVObject.java
│   │   │               │   └── UnplantedTileObject.java
│   │   │               ├── entitas
│   │   │               │   ├── Entity.java
│   │   │               │   └── PlayerView.java
│   │   │               └── tile
│   │   │                   ├── Tile.java
│   │   │                   └── TileManager.java
│   │   └── resources
│   │       ├── maps
│   │       │   ├── beachmap.txt
│   │       │   ├── housemap.txt
│   │       │   ├── map.txt
│   │       │   ├── rivermap.txt
│   │       │   ├── store.txt
│   │       │   └── townmap.txt
│   │       ├── font
│   │       │   ├── PressStart2P.ttf
│   │       │   └── slkscr.ttf
│   │       ├── menu
│   │       │   ├── menu.png
│   │       │   ├── FishingBG.png
│   │       │   ├── StoreBG.png
│   │       │   └── transition_background.png
│   │       ├── button
│   │       │   ├── button.png
│   │       │   └── message.png
│   │       ├── box
│   │       │   ├── box.png
│   │       │   └── (npc_name).png
│   │       ├── tiles
│   │       │   └── (nama_tile).png
│   │       ├── player
│   │       │   └── boy_(direction)_(frame).png
│   │       ├── crops
│   │       │   └── (nama_crop).png
│   │       ├── seeds
│   │       │   └── (nama_seed).png
│   │       ├── fish
│   │       │   └── (nama_ikan).png
│   │       ├── equipment
│   │       │   └── (nama_alat).png
│   │       ├── misc
│   │       │   └── (nama_misc).png
│   │       ├── food
│   │       │   └── (nama_makanan).png
│   │       ├── InteractableObject
│   │       │   └── (nama_objek).png
│   │       └── sound
│   │           └── SpringSDW.wav
│   └── test
│       └── java
│           └── org
│               └── example
│                   └── Test.java
├── .gitignore
├── pom.xml
└── README.md
```
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
