package org.example.model.Items;

public class Crops extends Items implements EdibleItem {
    private int jumlahcropperpanen;
    private int energyRestored; 

    public Crops(String name, int sellprice, int buyprice, int jumlahcropperpanen, int energyRestored) {
        super(name, sellprice, buyprice);
        this.jumlahcropperpanen = jumlahcropperpanen;
        this.energyRestored = energyRestored;
    }

    public int getJumlahcropperpanen() {
        return jumlahcropperpanen;
    }

    public void setJumlahcropperpanen(int jumlahcropperpanen) {
        this.jumlahcropperpanen = jumlahcropperpanen;
    }

    @Override
    public int getEnergyRestored() {
        return energyRestored;
    }

    @Override
    public String getName() {
        return super.getName();
    }
}