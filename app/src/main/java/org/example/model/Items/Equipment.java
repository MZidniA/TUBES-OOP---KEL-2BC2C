package org.example.model.Items;
import javax.imageio.ImageIO;
import java.io.IOException;
import org.example.controller.UtilityTool;

public class Equipment extends Items {
    private int durability;

    public Equipment(String name, int sellprice, int buyprice, int durability) {
        super(name, sellprice, buyprice);
        this.durability = durability;
        loadImage(); 
    }

    public void loadImage() {
        String path = null;
        switch (getName().toLowerCase()) {
            case "hoe":
                path = "/equipment/Hoe.png"; 
                break;
            case "watering can":
                path = "/equipment/WateringCan.png";
                break;
            case "pickaxe":
                path = "/equipment/PickAxe.png"; 
                break;
            case "fishing rod":
                path = "/equipment/FishingRod.png";
                break;
            default:
                this.image = null;
                return;
        }

        try {
            if (path != null) {
                this.image = ImageIO.read(getClass().getResourceAsStream(path));
                if (this.image == null) {
                     System.err.println("Gagal ngeload gambar " + path + " dari " + getName());
                }
                UtilityTool uTool = new UtilityTool();
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) { 
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