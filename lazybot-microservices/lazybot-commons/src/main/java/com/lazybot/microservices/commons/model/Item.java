package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An item
 */
@Getter
@Setter
@ToString
public class Item {
    private int type;
    private int count;
    private int metadata;
    private String name;
    private String displayName;
    private int stackSize;
    private int slot;

    public Item() {
    }

    public Item(int type, int count, int metadata) {
        this.type = type;
        this.count = count;
        this.metadata = metadata;
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
