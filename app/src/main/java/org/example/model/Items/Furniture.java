// Lokasi: org.example.model.Items.Furniture.java
package org.example.model.Items;

import javax.imageio.ImageIO; // Import yang mungkin dibutuhkan
import java.io.IOException;   // Import yang mungkin dibutuhkan
import org.example.controller.UtilityTool; // Jika ingin scaling

public class Furniture extends Items {
    // Tambahkan atribut spesifik furniture jika ada (misal, ukuran, tipe, dll.)
    // private String dimensions; // contoh

    public Furniture(String name, int sellPrice, int buyPrice /*, String dimensions*/) {
        super(name, sellPrice, buyPrice);
        // this.dimensions = dimensions;
        loadImage(); // Panggil loadImage di konstruktor
    }

    @Override
    public void loadImage() {
        // String path = null;
        // UtilityTool uTool = new UtilityTool();
        // // Contoh: jika nama file furniture sama dengan nama itemnya (setelah normalisasi)
        // // String imageName = getName().replaceAll("\\s+", "") + ".png";
        // // path = "/furniture/" + imageName; // Asumsi ada folder /furniture/

        // // Karena tidak ada path spesifik, kita set null atau gambar placeholder
        // // Contoh switch jika Anda punya beberapa gambar furniture:
        // switch (getName().toLowerCase()) {
        //     case "single bed":
        //          path = "/furniture/SingleBed.png"; // Ganti dengan path aktual jika ada
        //          break;
        //     case "wooden chair":
        //          path = "/furniture/WoodenChair.png"; // Contoh
        //          break;
        //     // Tambahkan case untuk furniture lain
        //     default:
        //         System.out.println("Furniture.loadImage(): No specific image path for " + getName() + ". Image will be null.");
        //         this.image = null; // Tidak ada gambar spesifik
        //         return;
        // }

        // try {
        //     this.image = ImageIO.read(getClass().getResourceAsStream(path));
        //     if (this.image == null) {
        //         System.err.println("Furniture.loadImage(): Failed to load image from path: " + path + " for " + getName());
        //     } else {
        //         this.image = uTool.scaleImage(this.image, 32, 32); // Sesuaikan ukuran target
        //     }
        // } catch (IOException e) {
        //     System.err.println("Error (IOException) loading image for Furniture " + getName() + " from path " + path + ": " + e.getMessage());
        //     this.image = null;
        // } catch (IllegalArgumentException e) {
        //     System.err.println("Error (IllegalArgumentException - likely invalid path) loading image for Furniture " + getName() + " from path " + path + ": " + e.getMessage());
        //     this.image = null;
        // }
    }

    // Getter dan Setter untuk atribut spesifik furniture jika ada
    // public String getDimensions() { return dimensions; }
}