package org.example.model.Items;
import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.UtilityTool;

public class Equipment extends Items {
    private int durability;

    public Equipment(String name, int sellprice, int buyprice, int durability) {
        super(name, sellprice, buyprice);
        this.durability = durability;
        loadImage(); // Panggil loadImage() di sini untuk memuat gambar saat objek dibuat
    }

    public void loadImage() {
        String path = null;
        switch (getName().toLowerCase()) {
            case "hoe":
                path = "/equipment/Hoe.png"; // Sesuaikan dengan path dan nama file Anda
                break;
            case "watering can":
                path = "/equipment/WateringCan.png";
                break;
            case "pickaxe":
                path = "/equipment/PickAxe.png"; // Perhatikan nama file "PickAxe.png"
                break;
            case "fishing rod":
                path = "/equipment/FishingRod.png";
                break;
            default:
                System.err.println("Equipment.loadImage(): No image path defined for " + getName());
                this.image = null;
                return;
        }

        try {
            if (path != null) {
                this.image = ImageIO.read(getClass().getResourceAsStream(path));
                if (this.image == null) {
                     System.err.println("Equipment.loadImage(): Failed to load image from path: " + path + " for " + getName());
                }
                // Scaling bisa dilakukan di sini jika semua equipment punya ukuran ikon yang sama,
                // atau serahkan pada PlayerView/GameStateUI saat menggambar jika ukurannya bervariasi
                // atau gambar aset sudah di-pre-scale.
                UtilityTool uTool = new UtilityTool();
                this.image = uTool.scaleImage(this.image, 32, 32); // Contoh scale ke 16x16
            }
        } catch (IOException e) {
            System.err.println("Error loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) { // Tangkap jika path resource tidak valid
            System.err.println("Error (likely invalid path) loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        }
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}