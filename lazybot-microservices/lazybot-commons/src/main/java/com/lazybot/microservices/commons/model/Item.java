package com.lazybot.microservices.commons.model;

public class Item {
    int type;
    int count;
    int metadata;
    String name;
    String displayName;
    int stackSize;
    int slot;

    public Item() {
    }

    public Item(int type, int count, int metadata, String name, String displayName, int stackSize, int slot) {
        this.type = type;
        this.count = count;
        this.metadata = metadata;
        this.name = name;
        this.displayName = displayName;
        this.stackSize = stackSize;
        this.slot = slot;
    }
}
