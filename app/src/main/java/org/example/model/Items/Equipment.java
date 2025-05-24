package org.example.model.Items;

public class Equipment extends Items {
    private int durability;

    public Equipment(String name, int sellprice, int buyprice, int durability) {
        super(name, sellprice, buyprice);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}