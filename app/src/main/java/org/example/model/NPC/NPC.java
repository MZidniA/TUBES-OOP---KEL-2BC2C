package org.example.model.NPC;
import java.util.List;


import org.example.model.Location;
import org.example.model.enums.RelationshipStats;

public abstract class NPC {
    private String name;
    private Location location;
    private List<String> lovedItems;
    private List<String> likedItems;
    private List<String> hatedItems;
    private int heartPoints;
    private RelationshipStats relationshipsStatus;

    public NPC(String name, Location location, List<String> lovedItems, List<String> likedItems, List<String> hatedItems, int heartPoints, RelationshipStats relationshipsStatus) {
        this.name = name;
        this.location = location;
        this.lovedItems = lovedItems;
        this.likedItems = likedItems;
        this.hatedItems = hatedItems;
        this.heartPoints = heartPoints;
        this.relationshipsStatus = relationshipsStatus;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getHeartPoints() {
        return heartPoints;
    }

    public List<String> getLovedItems() {
        return lovedItems;
    }

    public List<String> getLikedItems() {
        return likedItems;
    }

    public List<String> getHatedItems() {
        return hatedItems;
    }

    public RelationshipStats getRelationshipsStatus() {
        return relationshipsStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setHeartPoints(int heartPoints) {
        this.heartPoints = heartPoints;
    }

    public void setLovedItems(List<String> lovedItems) {
        for (String item : lovedItems) {
            if (!this.lovedItems.contains(item)) {
                this.lovedItems.add(item);
            }
        }
    }

    public void setLikedItems(List<String> likedItems) {
        for (String item : likedItems) {
            if (!this.likedItems.contains(item)) {
                this.likedItems.add(item);
            }
        }
    }

    public void setHatedItems(List<String> hatedItems) {
        for (String item : hatedItems) {
            if (!this.hatedItems.contains(item)) {
                this.hatedItems.add(item);
            }
        }
    }

    public void setRelationshipsStatus(RelationshipStats relationshipsStatus) {
        this.relationshipsStatus = relationshipsStatus;
    }

    public void addLovedItem(String item) {
        if (!this.lovedItems.contains(item)) {
            this.lovedItems.add(item);
        }
    }

    public void addLikedItem(String item) {
        if (!this.likedItems.contains(item)) {
            this.likedItems.add(item);
        }
    }

    public void addHatedItem(String item) {
        if (!this.hatedItems.contains(item)) {
            this.hatedItems.add(item);
        }
    }

    public boolean isItemLiked(String item) {
        return likedItems.contains(item) || lovedItems.contains(item);
    }
}