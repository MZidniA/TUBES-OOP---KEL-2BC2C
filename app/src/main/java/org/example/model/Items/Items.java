package org.example.model.Items;

import java.awt.image.BufferedImage;

public abstract class Items {
    private String name;
    private int sellprice;
    private int buyprice;
    protected BufferedImage image;

    public Items(String name, int sellprice, int buyprice) {
        this.name = name;
        this.sellprice = sellprice;
        this.buyprice = buyprice;
        loadImage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSellprice() {
        return sellprice;
    }

    public void setSellprice(int sellprice) {
        this.sellprice = sellprice;
    }

    public int getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(int buyprice) {
        this.buyprice = buyprice;
    }

    protected void loadImage() {
        this.image = null; // Default behavior, bisa dioverride di subclass
    }

    public BufferedImage getImage() {
        return image;
    }

    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Items)) return false;
        Items other = (Items) obj;
        return name.equals(other.name) && sellprice == other.sellprice && buyprice == other.buyprice;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    /**
     * Menentukan apakah item bisa diberikan ke NPC.
     * Misalnya, item seperti Hoe, Pickaxe, dll tidak bisa diberikan.
     */
    public boolean isGiftable() {
        String type = getType();
        return !(type.equalsIgnoreCase("Tool") || type.equalsIgnoreCase("Equipment"));
    }

    /**
     * Method tambahan untuk mendeteksi tipe item (berdasarkan instanceof).
     */
    public String getType() {
        if (this instanceof Food) return "Food";
        if (this instanceof Seeds) return "Seeds";
        if (this instanceof Fish) return "Fish";
        if (this instanceof Crops) return "Crops";
        if (this instanceof Equipment) return "Equipment";
        if (this instanceof Misc) return "Misc";
        if (this instanceof Furniture) return "Furniture";
        return "Unknown";
    }
}
