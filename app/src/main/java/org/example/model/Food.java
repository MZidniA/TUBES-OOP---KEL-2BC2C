package org.example.model;

public class Food  extends Items{
    private int Recoverenergy;

    public Food(String name, int sellprice, int buyprice, int Recoverenergy) {
        super(name, sellprice, buyprice);
        this.Recoverenergy = Recoverenergy;
    }
    public int getRecoverenergy() {
        return Recoverenergy;
    }
    public void setRecoverenergy(int recoverenergy) {
        Recoverenergy = recoverenergy;
    }

    
}
