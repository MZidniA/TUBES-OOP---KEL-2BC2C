package org.example.model.NPC;

import java.time.LocalDate;
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
    private LocalDate lastGiftDate;

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
        this.lovedItems = lovedItems;
    }

    public void setLikedItems(List<Items> likedItems) {
        this.likedItems = likedItems;
    }

    public void setHatedItems(List<Items> hatedItems) {
        this.hatedItems = hatedItems;
    }

    public void setRelationshipsStatus(RelationshipStats relationshipsStatus) {
        this.relationshipsStatus = relationshipsStatus;
    }

    public boolean hasGiftedToday() {
        return LocalDate.now().equals(lastGiftDate);
    }

    public void receiveGift(Items item) {
        if (hasGiftedToday()) return;

        if (lovedItems.contains(item)) {
            heartPoints += 25;
        } else if (likedItems.contains(item)) {
            heartPoints += 20;
        } else if (hatedItems.contains(item)) {
            heartPoints -= 25;
        } else {
            // Neutral item: tidak ada perubahan
        }

        if (heartPoints > 100) heartPoints = 100;
        if (heartPoints < 0) heartPoints = 0;

        lastGiftDate = LocalDate.now();
    }

    public boolean isItemLiked(Items item) {
        return likedItems.contains(item) || lovedItems.contains(item);
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

    public void setLastGiftDateToday() {
        this.lastGiftDate = java.time.LocalDate.now();
    }

}
