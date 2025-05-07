package org.example.model;

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
}
