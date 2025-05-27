package org.example.view.InteractableObject;

import java.io.IOException; // Lebih spesifik untuk ImageIO
import javax.imageio.ImageIO;
import org.example.controller.GameController;
// Tidak perlu import org.example.view.GamePanel;

public class ShippingBinObject extends InteractableObject {
    // HAPUS: GamePanel gp;

    public ShippingBinObject() { // Konstruktor tanpa parameter
        super("Shipping Bin"); // Panggil konstruktor superclass dengan nama objek
        this.collision = true; // Atur jika shipping bin solid atau tidak (biasanya solid)
        loadImage(); // Panggil metode untuk memuat gambar spesifik
    }

    @Override
    protected void loadImage() {
        try {
            // Hanya memuat gambar, tidak melakukan scaling di sini.
            // Scaling akan dilakukan oleh AssetSetter.
            this.image = ImageIO.read(getClass().getResourceAsStream("/InteractableObject/ShippingBin.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ShippingBin.png for ShippingBinObject");
        }
    }

    @Override
    public void interact(GameController controller) {
        // Implementasikan logika interaksi spesifik untuk shipping bin di sini.
        // Misalnya, membuka UI untuk menjual item.
        System.out.println("Interacting with the Shipping Bin");
        // Contoh: controller.openShippingBinUI();
    }
}