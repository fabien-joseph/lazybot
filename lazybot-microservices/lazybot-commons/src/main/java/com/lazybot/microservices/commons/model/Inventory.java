package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Inventory {
    private int id;
    private String type;
    private String title;
    private Item selectedItem;
    private List<Item> slots;

    public Inventory() {
    }

    public Inventory(int id, String type, String title, List<Item> slots, Item selectedItem) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.slots = slots;
        this.selectedItem = selectedItem;
    }
}
