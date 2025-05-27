package org.example.model.Items;

public class Food extends Items implements EdibleItem {
    private int recoverenergy;

    public Food(String name, int sellprice, int buyprice, int recoverenergy) {
        super(name, sellprice, buyprice);
        this.recoverenergy = recoverenergy;
    }

    public int getrecoverenergy() {
        return recoverenergy;
    }

    public void setrecoverenergy(int recoverenergy) {
        this.recoverenergy = recoverenergy;
    }

    @Override
    public int getEnergyRestored() {
        return recoverenergy;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public void initialize() {
        // Optional: implementasi jika diperlukan
    }
}
