package com.lazybot.microservices.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The nbt tag of an {@link Item}. Actually unused
 */
@Getter
@Setter
@ToString
public class Nbt {
    private String type;
    private String name;

    public Nbt() {
    }

    public Nbt(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
