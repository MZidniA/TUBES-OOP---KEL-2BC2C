package org.example.model.Items;

public class Food extends Items implements EdibleItem {
    private int recoverenergy;
    private boolean fish; // Tambahkan field ini

    public Food(String name, int sellprice, int buyprice, int recoverenergy, boolean fish) {
        super(name, sellprice, buyprice);
        this.recoverenergy = recoverenergy;
        this.fish = fish; // Set dari konstruktor
    }

    // Konstruktor lama (jika ada) tetap, default fish = false
    public Food(String name, int sellprice, int buyprice, int recoverenergy) {
        this(name, sellprice, buyprice, recoverenergy, false);
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

    // IMPLEMENTASI isFish
    public boolean isFish() {
        return fish;
    }

    // Setter jika ingin mengubah status fish
    public void setFish(boolean fish) {
        this.fish = fish;
    }
}
