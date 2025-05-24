package org.example.model.Items;


public class Food  extends Items{
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
}
