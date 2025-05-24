package org.example.model.Items;

public class Crops extends Items {
    private int jumlahcropperpanen;


	public Crops( String name, int sellprice, int buyprice, int jumlahcropperpanen) {
		super(name, sellprice, buyprice);
        this.jumlahcropperpanen = jumlahcropperpanen;
	}
    public int getJumlahcropperpanen() {
        return jumlahcropperpanen;
    }
    public void setJumlahcropperpanen(int jumlahcropperpanen) {
        this.jumlahcropperpanen = jumlahcropperpanen;
    }
}