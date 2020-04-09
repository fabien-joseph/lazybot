package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Inventory {
    int id;
    String type;
    String title;
    Item[] items;
    Item selectedItem;

    public Inventory() {
    }

    public Inventory(int id, String type, String title, Item[] items, Item selectedItem) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.items = items;
        this.selectedItem = selectedItem;
    }
}
