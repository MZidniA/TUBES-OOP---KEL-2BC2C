package org.example.model;

public abstract class Items {
    private String name;
    private int sellprice;
    private int buyprice;


    public Items(String name, int sellprice, int buyprice) {
        this.name = name;
        this.sellprice = sellprice;
        this.buyprice = buyprice;
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
}
