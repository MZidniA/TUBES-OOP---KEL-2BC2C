package org.example.model.Items;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.controller.UtilityTool;

public class Misc extends Items {
    private boolean isOutsideCategory;

    public Misc(String name, int sellprice, int buyprice) {
        super(name, sellprice, buyprice);
        this.isOutsideCategory = true;
    }

    public boolean isOutsideCategory() {
        return isOutsideCategory;
    }
    
    public void setOutsideCategory(boolean isOutsideCategory) {
        this.isOutsideCategory = isOutsideCategory;
    }

    public void loadImage() {
        String path = null;
        String lowerName = getName().toLowerCase().replace(" ", "");

        switch (lowerName) {
            case "coal":
                path = "/misc/Coal.png";
                break;
            case "firewood":
                path = "/misc/Firewood.png";
                break;
            case "proposalring":
                path = "/misc/ProposalRing.png";
                break;
            case "wood":
                path = "/misc/Wood.png";
                break;
            default:
                this.image = null;
                System.err.println("No image available for misc item: " + getName());
                return;
        }

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            if (this.image == null) {
                System.err.println("Failed to load image for " + getName() + " at path: " + path);
            } else {
                UtilityTool uTool = new UtilityTool();
                this.image = uTool.scaleImage(this.image, 32, 32);
            }
        } catch (IOException e) {
            System.err.println("Error loading image for " + getName() + " from path " + path + ": " + e.getMessage());
            this.image = null;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid image path for " + getName() + ": " + e.getMessage());
            this.image = null;
        }
    }
}
