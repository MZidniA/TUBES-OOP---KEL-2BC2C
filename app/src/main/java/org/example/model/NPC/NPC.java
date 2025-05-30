package org.example.model.NPC;
import java.util.ArrayList;
import java.util.List;
import org.example.model.Items.Items;
import org.example.model.enums.LocationType;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.RelationshipStats;

public abstract class NPC {
    private String name;
    private LocationType locationtype;
    private List<Items> lovedItems;
    private List<Items> likedItems;
    private List<Items> hatedItems;
    private int heartPoints;
    private RelationshipStats relationshipsStatus;

    public NPC(String name, LocationType locationtype, List<Items> lovedItems, List<Items> likedItems, List<Items> hatedItems, int heartPoints, RelationshipStats relationshipsStatus) {
        this.name = name;
        this.locationtype = locationtype;
        this.lovedItems = ItemDatabase.validateItemList(lovedItems);
        this.likedItems = ItemDatabase.validateItemList(likedItems);
        this.hatedItems = ItemDatabase.validateItemList(hatedItems);
        this.heartPoints = heartPoints;
        this.relationshipsStatus = relationshipsStatus;
    }

    public String getName() {
        return name;
    }

    public LocationType getLocation() {
        return locationtype;
    }

    public int getHeartPoints() {
        return heartPoints;
    }

    public List<Items> getLovedItems() {
        return lovedItems;
    }

    public List<Items> getLikedItems() {
        return likedItems;
    }

    public List<Items> getHatedItems() {
        return hatedItems;
    }

    public List<Items> HatedItemsExceptLovednLiked(List<Items> loved, List<Items> liked) {
        List<Items> hated = new ArrayList<>();
        for (Items item : ItemDatabase.getAllItems().values()) {
            if (!loved.contains(item) && !liked.contains(item)) {
                hated.add(item);
            }
        }
        return hated;
    }

    public RelationshipStats getRelationshipsStatus() {
        return relationshipsStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(LocationType locationtype) {
        this.locationtype = locationtype;
    }

    public void setHeartPoints(int heartPoints) {
        this.heartPoints = heartPoints;
    }

    public void setLovedItems(List<Items> lovedItems) {
        for (Items item : lovedItems) {
            if (!this.lovedItems.contains(item)) {
                this.lovedItems.add(item);
            }
        }
    }

    public void setLikedItems(List<Items> likedItems) {
        for (Items item : likedItems) {
            if (!this.likedItems.contains(item)) {
                this.likedItems.add(item);
            }
        }
    }

    public void setHatedItems(List<Items> hatedItems) {
        for (Items item : hatedItems) {
            if (!this.hatedItems.contains(item)) {
                this.hatedItems.add(item);
            }
        }
    }

    public void setRelationshipsStatus(RelationshipStats relationshipsStatus) {
        this.relationshipsStatus = relationshipsStatus;
    }

    public void addLovedItem(Items item) {
        if (!this.lovedItems.contains(item)) {
            this.lovedItems.add(item);
        }
    }

    public void addLikedItem(Items item) {
        if (!this.likedItems.contains(item)) {
            this.likedItems.add(item);
        }
    }

    public void addHatedItem(Items item) {
        if (!this.hatedItems.contains(item)) {
            this.hatedItems.add(item);
        }
    }

    public boolean isItemLiked(Items item) {
        return likedItems.contains(item) || lovedItems.contains(item);
    }

    public RelationshipStats getRelationshipStatus() {
        return this.relationshipsStatus;
    }

}