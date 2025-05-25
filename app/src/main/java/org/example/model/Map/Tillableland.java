package org.example.model.Map;

public class Tillableland extends Tile {
    public Tillableland(int x, int y) {
        super(x, y, true, '.'); 
    }

    public void interact() {
        System.out.println("Interacting with Tillableland at coordinates (" + getX() + ", " + getY() + ")");
    }
}