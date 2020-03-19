package com.lazybot.microservices.map.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Position {
    int x;
    int z;

    public Position() {
    }

    public Position(int x, int z) {
        this.x = x;
        this.z = z;
    }
}
